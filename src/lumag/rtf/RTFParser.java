package lumag.rtf;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class RTFParser {
	private enum State {
		TEXT,
		BACKSLASH,
		CONTROL,
		PARAMETER,
		BACKTICK,
		BACKTICK_X,
		BLOB
	}

	private IRTFContentHandler handler;
	
	private int level; 
	
	private State state = State.TEXT;
	
	private char[] codePageTable = CodePageTables.getCodePageTable(437);
	private StringBuilder builder = new StringBuilder();
	private String currentControl;

	private int tickedChar;

	private boolean shouldSkipGroupIfUnknown;
	private int skipLevel = Integer.MAX_VALUE;

	private long blobBytesLeft;

	private ByteArrayOutputStream blobBuffer;
	
	public void setCharacterSet(int cpNumber) {
		char[] table = CodePageTables.getCodePageTable(cpNumber);
		if (table != null) {
			codePageTable = table;
		} else {
			System.err.println("Unsupported codepage" + cpNumber);
		}
	}
	
	private void processControlWord(String control, boolean hasParameter, int parameter) {
		if (level >= skipLevel) {
			builder.setLength(0);
			return;
		}
		boolean handled = false;

		handled |= handler.control(control, hasParameter, hasParameter ? parameter: 1);

		if (!handled) {
			String ctrl = control;
			if (hasParameter) {
				ctrl = ctrl + " " + parameter;
			}
			if (shouldSkipGroupIfUnknown) {
				System.err.println("Ignoring: " + ctrl);
				skipLevel = level;
				shouldSkipGroupIfUnknown = false;
			} else {
				System.err.println("Ignored unsupported control: " + ctrl);
			}
		}

		state = State.TEXT;
	}

	public void process(byte[] bytes, int offset, int length) {
		for (int i = 0; i < length; i++) {
			byte b = bytes[offset + i];
			switch (state) {
			case TEXT:
				processStateText(b);
				break;
			case BACKSLASH:
				processStateBackslash(b);
				break;
			case CONTROL:
				processStateControl(b);
				break;
			case PARAMETER:
				processStateParameter(b);
				break;
			case BACKTICK:
				processStateBacktick(b);
				break;
			case BACKTICK_X:
				processStateBacktickX(b);
				break;
			case BLOB:
				processStateBlob(b);
				break;
			default:
				// really means something is broken
				throw new InternalError("unsupported rtf state?");
			}
		}
	}

	private void processStateText(byte b) {
		if (level < skipLevel) {
			processStateTextNormal(b);
		} else {
			processStateTextIgnoredGroup(b);
		}
	}

	private void processStateTextIgnoredGroup(byte b) {
		builder.setLength(0);
		if (b == '{') {
			level ++;
		} else if (b == '}') {
			if (level == skipLevel) {
				handler.endGroup();
				skipLevel = Integer.MAX_VALUE;
			}
			level --;
		} else if (b == '\\') {
			builder.setLength(0);
			state = State.BACKSLASH;
		} else {
			// ignore
		}
	}

	private void processStateTextNormal(byte b) {
		if (b == '{') {
			handler.string(builder.toString());
			builder.setLength(0);
			level ++;
			handler.startGroup();
		} else if (b == '}') {
			handler.string(builder.toString());
			builder.setLength(0);
			handler.endGroup();
			level --;
		} else if (b == '\\') {
			handler.string(builder.toString());
			builder.setLength(0);
			state = State.BACKSLASH;
		} else if (b == '\n' || b == '\r') {
			// ignore
		} else {
			putChar(b);
		}
	}

	private void processStateBackslash(byte b) {
		if (level >= skipLevel) {
			state = State.TEXT;
		} else if (Character.isLetter(b)) {
			state = State.CONTROL;
			putChar(b);
		} else if (b == '\'') {
			state = State.BACKTICK;
			tickedChar = 0;
		} else if (b == '*') {
			shouldSkipGroupIfUnknown = true;
			state = State.TEXT;
		} else {
			processControlWord(String.valueOf((char) b), false, 1);
		}
	}

	private void processStateControl(byte b) {
		if (Character.isLetter(b)) {
			putChar(b);
		} else if (Character.isDigit(b) || b == '-') {
			currentControl = builder.toString();
			builder.setLength(0);
			putChar(b);
			state = State.PARAMETER;
		} else {
			currentControl = builder.toString();
			builder.setLength(0);
			processControlWord(currentControl, false, 1);
			currentControl = null;
			if (!Character.isWhitespace(b)) {
				processStateText(b);
			}
		}
	}

	private void processStateParameter(byte b) {
		if (Character.isDigit(b)) {
			putChar(b);
		} else {
			if (currentControl.equals("bin")) {
				blobBytesLeft = Long.valueOf(builder.toString());
				builder.setLength(0);
				if (blobBytesLeft > Integer.MAX_VALUE) {
					blobBuffer = new ByteArrayOutputStream(Integer.MAX_VALUE);
				} else {
					blobBuffer = new ByteArrayOutputStream((int)blobBytesLeft);
				}
				state = State.BLOB;
			} else {
				int parameterValue = Integer.valueOf(builder.toString());
				builder.setLength(0);
				processControlWord(currentControl, true, parameterValue);
				currentControl = null;
				if (!Character.isWhitespace(b)) {
					processStateText(b);
				}
			}
		}
	}

	private void processStateBacktick(byte b) {
		if (Character.digit(b, 16) == -1) {
			state = State.TEXT;
		} else {
			tickedChar = Character.digit(b, 16);
			state = State.BACKTICK_X;
		}
	}

	private void processStateBacktickX(byte b) {
		if (Character.digit(b, 16) != -1) {
			tickedChar = (tickedChar << 4) + Character.digit(b, 16);
			putChar(tickedChar);
		}
		state = State.TEXT;
	}

	private void processStateBlob(byte b) {
		blobBuffer.write(b);
		blobBytesLeft --;
		if (blobBytesLeft == 0) {
			if (level < skipLevel) {
				handler.binaryBlob(blobBuffer.toByteArray());
			}
			blobBuffer = null;
			state = State.TEXT;
		}
	}

	private void putChar(int ch) {
		builder.append(codePageTable[ch & 0xff]);
	}
	
	public void setHandler(IRTFContentHandler handler) {
		this.handler = handler;
	}

	public static void main(String[] args) throws Exception {
		RTFParser parser = new RTFParser();
		parser.setHandler(new DefaultRTFHandler());
		InputStream stream = new BufferedInputStream(
				new FileInputStream(
						args.length>0?args[0]:
						"tests/test.rtf"));
		byte[] buffer = new byte[1024];
		while (true) {
			int len = stream.read(buffer);
			if (len < 0) {
				break;
			}
			parser.process(buffer, 0, len);
		}
		stream.close();
	}

	public int getLevel() {
		return level;
	}

}

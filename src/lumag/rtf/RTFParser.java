package lumag.rtf;

import java.io.BufferedInputStream;
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
	}

	private IRTFContentHandler handler;
	
	private int level; 
	
	private State state = State.TEXT;
	
	private char[] codePageTable = CodePageTables.getCodePageTable(437);
	private StringBuilder builder = new StringBuilder();

	private boolean parameterNegative;
	private int parameterValue;
	
	private byte tickedChar;

	private boolean canSkipGroupIfUnknown;
	private int skipLevel = Integer.MAX_VALUE;
	
	public void startGroup() {
		level ++;
		if (level >= skipLevel) {
			return;
		}
		handler.startGroup();
	}
	
	public void endGroup() {
		level --;
		if (level >= skipLevel) {
			return;
		}
		skipLevel = Integer.MAX_VALUE;
		handler.endGroup();
	}
	
	private void processText() {
		if (level >= skipLevel) {
			builder.setLength(0);
			return;
		}

		if (builder.length() == 0) {
			return;
		}

		handler.string(builder.toString());

		builder.setLength(0);
	}
	
	private void processControlWord() {
		if (level >= skipLevel) {
			builder.setLength(0);
			return;
		}
		String control = builder.toString();
		builder.setLength(0);
		boolean handled = false;
		handled |= handler.control(control);
		if (!handled) {
			if (canSkipGroupIfUnknown) {
				System.err.println("Ignoring: " + control);
				skipLevel = level;
				canSkipGroupIfUnknown = false;
			} else {
				System.err.println("Ignored unsupported control: " + control);
			}
		}
	}
	
	private void processControlWord(int parameter) {
		if (level >= skipLevel) {
			builder.setLength(0);
			return;
		}
		String control = builder.toString();
		builder.setLength(0);
		boolean handled = false;
		if (control.equals("rtf")) {
			System.out.println("RTF version " + parameter);
			handled = true;
		} else if (control.equals("ansicpg")) {
			char[] table = CodePageTables.getCodePageTable(parameter);
			if (table != null) {
				codePageTable = table;
			} else {
				System.err.println("Unsupported codepage" + parameter);
			}
		}
		handled |= handler.control(control, parameter);
		if (!handled) {
			if (canSkipGroupIfUnknown) {
				System.err.println("Ignoring: " + control + " " + parameter);
				skipLevel = level;
				canSkipGroupIfUnknown = false;
			} else {
				System.err.println("Ignored unsupported control: " + control + " " + parameter);
			}
		}
	}

	public void process(byte[] bytes, int offset, int length) {
		for (int i = 0; i < length; i++) {
			byte b = bytes[offset + i];
			switch (state) {
			case TEXT:
				if (b == '{') {
					processText();
					startGroup();
				} else if (b == '}') {
					processText();
					endGroup();
				} else if (b == '\\') {
					processText();
					state = State.BACKSLASH;
				} else {
					putChar(b);
				}
				break;
			case BACKSLASH:
				if (('a' <= b && b <= 'z') ||
					('A' <= b && b <= 'Z')) {
					state = State.CONTROL;
					putChar(b);
				} else if (b == '\'') {
					state = State.BACKTICK;
					tickedChar = 0;
				} else if (b == '*') {
					canSkipGroupIfUnknown = true;
					state = State.TEXT;
				} else {
					System.out.println("Unknown Control symbol: "  + (char)b);
					state = State.TEXT;
				}
				break;
			case CONTROL:
				if (b == '{') {
					processControlWord();
					startGroup();
					state = State.TEXT;
				} else if (b == '}') {
					processControlWord();
					endGroup();
					state = State.TEXT;
				} else if (('a' <= b && b <= 'z') ||
					('A' <= b && b <= 'Z')) {
					putChar(b);
				} else if (b == '\\') {
					processControlWord();
					state = State.BACKSLASH;
				} else if (b == ' ' || b == '\n' || b == '\r' || b == '\t') {
					processControlWord();
					state = State.TEXT;
				} else if (b == '-') {
					parameterNegative = true;
					parameterValue = 0;
					state = State.PARAMETER;
				} else if ('0' <= b && b <= '9') {
					parameterNegative = false;
					parameterValue = b - '0';
					state = State.PARAMETER;
				} else {
					processControlWord();
					state = State.TEXT;
					putChar(b);
				}
				break;
			case PARAMETER:
				if (b == '{') {
					processControlWord(parameterValue);
					startGroup();
					state = State.TEXT;
				} else if (b == '}') {
					processControlWord(parameterValue);
					endGroup();
					state = State.TEXT;
				} else if ('0' <= b && b <= '9') {
					parameterValue = parameterValue * 10 + (b - '0');
				} else if (b == '\\') {
					if (parameterNegative) {
						parameterValue = - parameterValue;
					}
					processControlWord(parameterValue);
					state = State.BACKSLASH;
				} else if (b == ' ' || b == '\n' || b == '\r' || b == '\t') {
					processControlWord(parameterValue);
					state = State.TEXT;
				} else {
					processControlWord(parameterValue);
					state = State.TEXT;
					putChar(b);
				}
				break;
			case BACKTICK:
				tickedChar = 0;
				if ('0' <= b && b <= '9') {
					tickedChar |= b - '0';
				} else if ('a' <= b && b <= 'z') {
					tickedChar |= b - 'a' + 10;
				} else if ('A' <= b && b <= 'Z') {
					tickedChar |= b - 'A' + 10;
				}
				state = State.BACKTICK_X;
				break;
			case BACKTICK_X:
				tickedChar <<= 4;
				if ('0' <= b && b <= '9') {
					tickedChar |= b - '0';
				} else if ('a' <= b && b <= 'z') {
					tickedChar |= b - 'a' + 10;
				} else if ('A' <= b && b <= 'Z') {
					tickedChar |= b - 'A' + 10;
				}
				putChar(tickedChar);
				state = State.TEXT;
				break;
			}
		}
	}

	private void putChar(byte b) {
		if (b >= 0) {
			builder.append((char) b);
		} else {
			builder.append(codePageTable[b&0x7f]);
		}
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

}

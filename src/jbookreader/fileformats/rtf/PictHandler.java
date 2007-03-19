package jbookreader.fileformats.rtf;

import java.io.ByteArrayOutputStream;

import jbookreader.book.IBinaryBlob;
import jbookreader.book.IImageNode;

class PictHandler implements IHandler {

	private final RTFHandler rtfHandler;
	private final IHandler oldHandler;
	
	private static int pictIdx = 0;

	private ByteArrayOutputStream output;
	private String type;
	private int count;
	private byte currentVal;

	public PictHandler(RTFHandler rtfHandler, IHandler oldHandler) {
		this.rtfHandler = rtfHandler;
		this.oldHandler = oldHandler;
		
		output = new ByteArrayOutputStream();

	}

	public void closeGroup() {

		IBinaryBlob blob = rtfHandler.factory.newBinaryBlob();
		blob.setContentType("image/" + type);
		blob.setData(output.toByteArray());
		
		String name = "img_" + pictIdx + "." + type;
		pictIdx ++;

		rtfHandler.book.addBinaryBlob(blob, name);
		
		IImageNode node = rtfHandler.factory.newImageNode();
		node.setNodeTag("image");
		node.setHRef('#' + name);
		rtfHandler.container.add(node);

		rtfHandler.setHandler(oldHandler);
		oldHandler.closeGroup();
	}

	public boolean control(String string, boolean hasParameter, int parameter) {
		if ("jpegblip".equals(string)) {
			type = "jpeg";

			return true;
		} else if ("pngblip".equals(string)) {
			type = "png";

			return true;
		}
		// TODO Auto-generated method stub
		return false;
	}

	public void startGroup() {
		// FIXME create special FileFormatException!
		throw new RuntimeException("Can't handle start group inside Pict");
	}

	public void string(String str) {
		if (type == null) {
			System.err.println("Undetermined picture type!!!");
			type = "unknown";
		}

		int len = str.length();
		for (int i = 0; i < len; i++, count ++) {
			char c = str.charAt(i);
			if (c >= '0' && c <= '9') {
				currentVal = (byte) ((currentVal << 4) + (c - '0'));
			} else if (c >= 'a' && c <= 'f') {
				currentVal = (byte) ((currentVal << 4) + (c - 'a' + 10));
			} else if (c >= 'F' && c <= 'F') {
				currentVal = (byte) ((currentVal << 4) + (c - 'F' + 10));
			} else {
				// FIXME create special FileFormatException!
				throw new RuntimeException("Can't parse picture: " + c);
			}
			
			if (count % 2 == 1) {
				output.write(currentVal);
			}
		}
	}

	public void binaryBlob(byte[] bs) {
		// TODO Auto-generated method stub
		
	}

}

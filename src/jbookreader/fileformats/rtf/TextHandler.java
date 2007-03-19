/**
 * 
 */
package jbookreader.fileformats.rtf;

import java.util.HashMap;
import java.util.Map;

import jbookreader.book.IContainerNode;
import jbookreader.book.ITextNode;

class TextHandler implements IHandler {

	private final RTFHandler rtfHandler;
	private Map<String, IControlHandler> controlHandlers =
		new HashMap<String, IControlHandler>();
	private int unicodeCharLength = 1;

	int skipChars;

	TextHandler(RTFHandler handler) {
		rtfHandler = handler;
		fillControlHandlers();
	}
	
	private void fillControlHandlers() {
		controlHandlers.put("rtf", new IControlHandler() {
			public void control(String string, boolean hasParameter, int parameter) {
				System.out.println("RTF version " + parameter);
			}
		});
		controlHandlers.put("ansicpg", new IControlHandler() {
			public void control(String string, boolean hasParameter, int parameter) {
				rtfHandler.parser.setCharacterSet(parameter);
			}
		});
		controlHandlers.put("par", new IControlHandler() {
			public void control(String string, boolean hasParameter, int parameter) {
				IContainerNode node = rtfHandler.factory.newContainerNode();
				node.setNodeTag("par");
				rtfHandler.container.getParentNode().add(node);
				rtfHandler.container = node;
			}
		});
		controlHandlers.put("\n", new IControlHandler() {
			public void control(String string, boolean hasParameter, int parameter) {
				IContainerNode node = rtfHandler.factory.newContainerNode();
				node.setNodeTag("par");
				rtfHandler.container.getParentNode().add(node);
				rtfHandler.container = node;
			}
		});
		controlHandlers.put("\r", new IControlHandler() {
			public void control(String string, boolean hasParameter, int parameter) {
				IContainerNode node = rtfHandler.factory.newContainerNode();
				node.setNodeTag("par");
				rtfHandler.container.getParentNode().add(node);
				rtfHandler.container = node;
			}
		});
		controlHandlers.put("uc", new IControlHandler() {
			public void control(String string, boolean hasParameter, int parameter) {
				unicodeCharLength = parameter;
			}
		});
		controlHandlers.put("u", new IControlHandler() {
			public void control(String string, boolean hasParameter, int parameter) {
				string(Character.valueOf((char) parameter).toString());
				skipChars = unicodeCharLength;
			}
		});
		controlHandlers.put("~", new IControlHandler() {
			public void control(String string, boolean hasParameter, int parameter) {
				// XXX: nbsp
				string("\u00A0");
			}
		});
		controlHandlers.put(" ", new IControlHandler() {
			public void control(String string, boolean hasParameter, int parameter) {
				// XXX: nbsp
				string("\u00A0");
			}
		});
		controlHandlers.put("_", new IControlHandler() {
			public void control(String string, boolean hasParameter, int parameter) {
				// XXX: nb dash;
				string("\u2011");
			}
		});
		controlHandlers.put("fonttbl", new IControlHandler() {
			public void control(String string, boolean hasParameter, int parameter) {
				rtfHandler.ignoredLevel = rtfHandler.parser.getLevel();
			}
		});
		controlHandlers.put("stylesheet", new IControlHandler() {
			public void control(String string, boolean hasParameter, int parameter) {
				rtfHandler.ignoredLevel = rtfHandler.parser.getLevel();
			}
		});
		controlHandlers.put("pict", new IControlHandler() {
			public void control(String string, boolean hasParameter, int parameter) {
				rtfHandler.setHandler(new PictHandler(rtfHandler, TextHandler.this));
			}
		});
	}

	public void closeGroup() {
		// TODO Auto-generated method stub
		
	}

	public boolean control(String string, boolean hasParameter, int parameter) {
		if (controlHandlers.containsKey(string)) {
			controlHandlers.get(string).control(string, hasParameter, parameter);
			return true;
		}

		return false;
	}

	public void string(String string) {
		String text = string;
		if (skipChars >= string.length()) {
			skipChars -= string.length();
		} else {
			if (skipChars != 0) {
				text = string.substring(skipChars);
				skipChars = 0;
			}
		}

		ITextNode node = rtfHandler.factory.newTextNode();
		node.setText(text);
		rtfHandler.container.add(node);

	}

	public void startGroup() {
		// TODO Auto-generated method stub
		
	}

	public void binaryBlob(byte[] bs) {
		if (skipChars != 0) {
			skipChars --;
		}
	}
	
}

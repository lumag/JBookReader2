package jbookreader.fileformats.impl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import jbookreader.book.IBook;
import jbookreader.book.IBookFactory;
import jbookreader.book.IContainerNode;
import jbookreader.book.IStylesheet;
import jbookreader.book.ITextNode;
import jbookreader.css.CSSParser;
import jbookreader.css.CSSStylesheet;
import jbookreader.fileformats.IErrorHandler;
import jbookreader.fileformats.IFileFormatDescriptor;
import lumag.rtf.IRTFContentHandler;
import lumag.rtf.RTFParser;

import org.xml.sax.SAXException;

class RichTextFormat implements IFileFormatDescriptor {

	private static class RTFHandler implements IRTFContentHandler {

		private final IBookFactory factory;
		private IBook book;
		private IContainerNode container;
		private final RTFParser parser;
		
		private int unicodeCharLength = 1;
		private int skipChars;

		// FIXME: correctly implement non-text destinations
		private int ignoredLevel = Integer.MAX_VALUE;
		
		public RTFHandler(IBookFactory factory, RTFParser parser) {
			this.factory = factory;
			this.parser = parser;
			this.book = factory.newBook();
			IContainerNode body = factory.newContainerNode();
			book.addBody(body, "");
			container = factory.newContainerNode();
			body.add(container);
		}

		public boolean control(String string, boolean hasParameter, int parameter) {
			if (parser.getLevel() >= ignoredLevel) {
				// assume we support all controls in ignored destinations
				return true;
			}

			if (string.equals("rtf")) {
				System.out.println("RTF version " + parameter);
			} else if (string.equals("ansicpg")) {
				parser.setCharacterSet(parameter);
			} else if (string.equals("par")
					|| string.equals("\n")
					|| string.equals("\r")) {
				// new paragraph
				IContainerNode node = factory.newContainerNode();
				container.getParentNode().add(node);
				container = node;
			} else if (string.equals("uc")) {
				unicodeCharLength = parameter;
			} else if (string.equals("u")) {
				string(Character.valueOf((char) parameter).toString());
				skipChars = unicodeCharLength;
			} else if (string.equals("~")
					|| string.equals(" ")) {
				// XXX: nbsp
				string("\u00A0");
			} else if (string.equals("_")) {
				// XXX: nb dash;
				string("\u2011");
			} else if (string.equals("fonttbl")
					|| string.equals("stylesheet")
					|| string.equals("pict")) {
				ignoredLevel = parser.getLevel();
			} else {
				return false;
			}
			return true;
		}

		public void endGroup() {
			if (parser.getLevel() < ignoredLevel) {
				ignoredLevel = Integer.MAX_VALUE;
			}
			// TODO Auto-generated method stub

		}

		public void startGroup() {
			// TODO Auto-generated method stub

		}

		public void string(String string) {
			if (parser.getLevel() >= ignoredLevel) {
				return;
			}
			
			if (skipChars >= string.length()) {
				skipChars -= string.length();
			} else {
				ITextNode node = factory.newTextNode();
				node.setText(string.substring(skipChars));
				container.add(node);
				skipChars = 0;
			}
		}

		public IBook getBook() {
			return book;
		}

		public void binaryBlob(byte[] bs) {
			skipChars--;
			// TODO Auto-generated method stub
			
		}

	}

	private final Collection<String> extensions;
	private IStylesheet stylesheet;
	
	public RichTextFormat() {
		extensions = new ArrayList<String>();
		extensions.add(".rtf");
	}

	public IBook parse(String uri, IErrorHandler handler, IBookFactory factory)
			throws SAXException, IOException {
		RTFParser parser = new RTFParser();
		RTFHandler rtfHandler = new RTFHandler(factory, parser);
		parser.setHandler(rtfHandler);
		InputStream stream = new BufferedInputStream(
				new FileInputStream(uri));
		byte[] buffer = new byte[1024];
		while (true) {
			int len = stream.read(buffer);
			if (len < 0) {
				break;
			}
			parser.process(buffer, 0, len);
		}
		stream.close();
		return rtfHandler.getBook();
	}

	public String getDescription() {
		return "Rich Text Format";
	}

	public Collection<String> getExtensions() {
		return extensions;
	}

	public IStylesheet getStylesheet() {
		if (stylesheet == null) {
			try {
				stylesheet = CSSParser.parse("resources/css/rtf.css");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (stylesheet == null) {
			return new CSSStylesheet();
		}
		return stylesheet;
	}

}

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
import jbookreader.book.ITextNode;
import jbookreader.fileformats.IErrorHandler;
import jbookreader.fileformats.IFileFormatDescriptor;
import lumag.rtf.IRTFContentHandler;
import lumag.rtf.RTFParser;

import org.xml.sax.SAXException;

class RichTextFormat implements IFileFormatDescriptor {

	private class RTFHandler implements IRTFContentHandler {

		private final IBookFactory factory;
		private IBook book;
		private IContainerNode container;

		public RTFHandler(IBookFactory factory) {
			this.factory = factory;
			this.book = factory.newBook();
			IContainerNode body = factory.newContainerNode();
			book.addBody(body, "");
			container = factory.newContainerNode();
			body.add(container);
		}

		public boolean control(String string) {
			if (string.equals("par")) {
				IContainerNode node = factory.newContainerNode();
				container.getParentNode().add(node);
				container = node;
				return true;
			}
			// TODO Auto-generated method stub
			return false;
		}

		public boolean control(String string, int parameter) {
			// TODO Auto-generated method stub
			return false;
		}

		public void endGroup() {
			// TODO Auto-generated method stub

		}

		public void startGroup() {
			// TODO Auto-generated method stub

		}

		public void string(String string) {
			ITextNode node = factory.newTextNode();
			node.setText(string);
			container.add(node);
		}

		public IBook getBook() {
			return book;
		}

	}

	private final Collection<String> extensions;
	
	public RichTextFormat() {
		extensions = new ArrayList<String>();
		extensions.add(".rtf");
	}

	public IBook parse(String uri, IErrorHandler handler, IBookFactory factory)
			throws SAXException, IOException {
		RTFParser parser = new RTFParser();
		RTFHandler rtfHandler = new RTFHandler(factory);
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

}

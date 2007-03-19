/**
 * 
 */
package jbookreader.fileformats.rtf;


import jbookreader.book.IBook;
import jbookreader.book.IBookFactory;
import jbookreader.book.IContainerNode;
import lumag.rtf.IRTFContentHandler;
import lumag.rtf.RTFParser;

class RTFHandler implements IRTFContentHandler {
	private IHandler handler;

	final IBookFactory factory;
	final IBook book;
	IContainerNode container;

	final RTFParser parser;

	// FIXME: correctly implement non-text destinations
	int ignoredLevel = Integer.MAX_VALUE;

	public RTFHandler(IBookFactory factory, RTFParser parser) {
		this.factory = factory;
		this.parser = parser;

		handler = new TextHandler(this);

		this.book = factory.newBook();
		IContainerNode body = factory.newContainerNode();
		body.setNodeTag("rtf");

		book.addBody(body, "");
		container = factory.newContainerNode();
		container.setNodeTag("par");
		body.add(container);
	}

	public boolean control(String string, boolean hasParameter, int parameter) {
		if (parser.getLevel() >= ignoredLevel) {
			// assume we support all controls in ignored destinations
			return true;
		}

		return handler.control(string, hasParameter, parameter);
	}

	public void endGroup() {
		if (parser.getLevel() <= ignoredLevel) {
			ignoredLevel = Integer.MAX_VALUE;
		}
		handler.closeGroup();
//		System.out.println("end " + parser.getLevel());
	}

	public void startGroup() {
		handler.startGroup();
//		System.out.println("start " + parser.getLevel());
	}

	public void string(String string) {
		if (parser.getLevel() >= ignoredLevel) {
			return;
		}

		if (string.length() == 0) {
			return;
		}

		handler.string(string);
	}

	public IBook getBook() {
		return book;
	}

	public void binaryBlob(byte[] bs) {
		handler.binaryBlob(bs);
	}

	public void setHandler(IHandler handler) {
		this.handler = handler;
	}

}

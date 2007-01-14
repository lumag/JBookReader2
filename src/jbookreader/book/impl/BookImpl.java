package jbookreader.book.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import jbookreader.book.IBinaryBlob;
import jbookreader.book.IBook;
import jbookreader.book.IContainerNode;
import jbookreader.book.IStylesheet;


class BookImpl implements IBook {
	private Map<String, IContainerNode> bodies = new LinkedHashMap<String, IContainerNode>();
	private Map<String, IBinaryBlob> blobs = new LinkedHashMap<String, IBinaryBlob>();
	private IStylesheet stylesheet;

	public void addBody(IContainerNode node, String name) {
		// FIXME: remove cast
		((AbstractNode) node).setBook(this);
		bodies.put(name, node);
	}

	public IContainerNode getFirstBody() {
		return bodies.entrySet().iterator().next().getValue();
	}

	public void addBinaryBlob(IBinaryBlob blob, String name) {
		blobs.put(name, blob);
	}

	public IBinaryBlob getBinaryBlob(String name) {
		return blobs.get(name);
	}

	public IStylesheet getStylesheet() {
		return stylesheet;
	}

	public void setStylesheet(IStylesheet stylesheet) {
		this.stylesheet = stylesheet;
	}
}

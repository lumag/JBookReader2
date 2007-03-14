package jbookreader.book.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import jbookreader.book.IBinaryBlob;
import jbookreader.book.IBook;
import jbookreader.book.IContainerNode;
import jbookreader.book.INode;
import jbookreader.style.IStylesheet;


class BookImpl implements IBook {
	private Map<String, ContainerNodeImpl> bodies = new LinkedHashMap<String, ContainerNodeImpl>();
	private Map<String, BinaryBlobImpl> blobs = new LinkedHashMap<String, BinaryBlobImpl>();
	private IStylesheet<INode> stylesheet;

	public void addBody(IContainerNode node, String name) {
		ContainerNodeImpl container = (ContainerNodeImpl) node;
		container.setBook(this);
		bodies.put(name, container);
	}

	public ContainerNodeImpl getFirstBody() {
		return bodies.entrySet().iterator().next().getValue();
	}

	public void addBinaryBlob(IBinaryBlob blob, String name) {
		blobs.put(name, (BinaryBlobImpl) blob);
	}

	public BinaryBlobImpl getBinaryBlob(String name) {
		return blobs.get(name);
	}

	public IStylesheet<INode> getStylesheet() {
		return stylesheet;
	}

	public void setStylesheet(IStylesheet<INode> stylesheet) {
		this.stylesheet = stylesheet;
	}

	public INode getNodeByRef(String ref) {
		return new NodeFinder(this, ref).getNode();
	}
}

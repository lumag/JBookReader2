package jbookreader.book.impl;

import jbookreader.book.IImageNode;
import jbookreader.book.INodeVisitor;

public class ImageNodeImpl extends TextNodeImpl implements IImageNode {
	private String hRef;

	@Override
	public boolean accept(INodeVisitor visitor) {
		return visitor.visitImageNode(this);
	}

	public String getHRef() {
		return hRef;
	}

	public void setHRef(String value) {
		hRef = value;
	}

}

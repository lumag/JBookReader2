package jbookreader.book.impl;

import jbookreader.book.IImageNode;
import jbookreader.book.INodeVisitor;

public class ImageNodeImpl extends AbstractNamedNode implements IImageNode {
	private String hRef;
	private String text;

	public boolean accept(INodeVisitor visitor) {
		return visitor.visitImageNode(this);
	}

	public String getHRef() {
		return hRef;
	}

	public void setHRef(String value) {
		hRef = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}

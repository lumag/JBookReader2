package jbookreader.book.impl;

import jbookreader.book.INodeVisitor;
import jbookreader.book.ITextNode;

class TextNodeImpl extends AbstractNode implements ITextNode {
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void accept(INodeVisitor visitor) {
		visitor.visitTextNode(this);
	}
	
	@Override
	public String toString() {
		return "#text";
	}

}

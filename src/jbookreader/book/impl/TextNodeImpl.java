package jbookreader.book.impl;

import jbookreader.book.INodeVisitor;
import jbookreader.book.ITextNode;

public class TextNodeImpl extends AbstractNode implements ITextNode {
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean accept(INodeVisitor visitor) {
		return visitor.visitTextNode(this);
	}

}

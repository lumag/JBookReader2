package jbookreader.book.impl;

import jbookreader.book.IBook;
import jbookreader.book.IContainerNode;
import jbookreader.book.INode;

abstract class AbstractNode implements INode {
	private IContainerNode parentNode;
	private IBook book;
	
	public IContainerNode getParentNode() {
		return parentNode;
	}
	
	void setParentNode(IContainerNode node) {
		parentNode = node;
	}

	public IBook getBook() {
		return book;
	}

	void setBook(IBook book) {
		this.book = book;
	}
}

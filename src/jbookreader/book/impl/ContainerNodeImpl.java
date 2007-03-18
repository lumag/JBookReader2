package jbookreader.book.impl;

import java.util.NoSuchElementException;

import jbookreader.book.IContainerNode;
import jbookreader.book.INode;
import jbookreader.book.INodeVisitor;
import lumag.util.ArrayOrderedSet;
import lumag.util.IOrderedContainer;


class ContainerNodeImpl extends AbstractNamedNode implements IContainerNode {
	private IOrderedContainer<INode> children = new ArrayOrderedSet<INode>();

	public boolean add(INode node) {
		if (node.getParentNode() != null) {
			throw new IllegalArgumentException("The node being added is already a part of some book");
		}
		// XXX: try to find a way to remove these casts
		((AbstractNode) node).setParentNode(this);
		((AbstractNode) node).setBook(this.getBook());
		return this.children.add(node);
	}

	public void accept(INodeVisitor visitor) {
		visitor.visitContainerNode(this);
	}

	public void visitChildren(INodeVisitor visitor) {
		for (INode node: children) {
			node.accept(visitor);
		}
		visitor.flush();
	}

	public boolean isEmpty() {
		return children.isEmpty();
	}

	public INode getNext(INode element) throws NoSuchElementException {
		return children.getNext(element);
	}

	public INode getPrevious(INode element) throws NoSuchElementException {
		return children.getPrevious(element);
	}

	public boolean hasNext(INode element) throws NoSuchElementException {
		return children.hasNext(element);
	}

	public boolean hasPrevious(INode element) throws NoSuchElementException {
		return children.hasPrevious(element);
	}

	public int getNumber(INode element) throws NoSuchElementException {
		return children.getNumber(element);
	}

	public void remove(INode node) throws UnsupportedOperationException {
		children.remove(node);
	}

}

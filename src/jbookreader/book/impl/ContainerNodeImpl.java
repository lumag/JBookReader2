package jbookreader.book.impl;

import java.util.NoSuchElementException;

import jbookreader.book.IContainerNode;
import jbookreader.book.INode;
import jbookreader.book.INodeVisitor;
import lumag.util.OrderedSet;


public class ContainerNodeImpl extends AbstractNode implements IContainerNode {
	private String nodeClass;
	private String nodeTag;

	private OrderedSet<INode> children = new OrderedSet<INode>();

	public String getNodeClass() {
		return nodeClass;
	}

	public String getNodeTag() {
		return nodeTag;
	}

	public void add(INode node) {
		if (node.getParentNode() != null) {
			throw new IllegalArgumentException("The node being added is already a part of some book");
		}
		// XXX: try to find a way to remove these casts
		((AbstractNode) node).setParentNode(this);
		((AbstractNode) node).setBook(this.getBook());
		this.children.add(node);
	}

	public void setNodeClass(String nodeClass) {
		this.nodeClass = nodeClass;
	}

	public void setNodeTag(String nodeTag) {
		this.nodeTag = nodeTag;
	}

	public boolean accept(INodeVisitor visitor) {
		return visitor.visitContainerNode(this);
	}

	public void visitChildren(INodeVisitor visitor) {
		for (INode node: children) {
			boolean stop = node.accept(visitor);
			if (stop) {
				return;
			}
		}
	}

	public boolean isEmpty() {
		return children.isEmpty();
	}

	public boolean contains(INode element) {
		return children.contains(element);
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

}

package jbookreader.book;

import lumag.util.IOrderedContainer;

public interface IContainerNode extends INode, IOrderedContainer<INode>, INamedNode {
	void visitChildren(INodeVisitor visitor);

	void add(INode node);
	boolean isEmpty();
}

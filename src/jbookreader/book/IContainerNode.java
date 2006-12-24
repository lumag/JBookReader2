package jbookreader.book;

import lumag.util.IOrderedContainer;

public interface IContainerNode extends INode, IOrderedContainer<INode> {
	String getNodeClass();
	void setNodeClass(String klass);
	String getNodeTag();
	void setNodeTag(String tag);
	
	void visitChildren(INodeVisitor visitor);

	void add(INode node);
	boolean isEmpty();
}

package jbookreader.book;

public interface INode {
	/**
	 * A wrapper around {@link INodeVisitor} interface.
	 * Each {@link INode} implementation should call corresponding
	 * method from {@link INodeVisitor} interface.
	 * @param visitor node traversing visitor
	 */
	void accept(INodeVisitor visitor);
	IContainerNode getParentNode();
	IBook getBook();
	
	String getNodeRef();
}
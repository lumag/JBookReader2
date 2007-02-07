package jbookreader.book;

/**
 * Node traversal interface.
 * Each method processes one type of nodes. Each visiting method should return true if
 * processing of nodes should be stopped.
 * @author Dmitry Baryshkov (dbaryshkov@gmail.com)
 *
 */
public interface INodeVisitor {
	void visitTextNode(ITextNode node);
	void visitContainerNode(IContainerNode node);
	void visitImageNode(IImageNode node);
	void flush();
}

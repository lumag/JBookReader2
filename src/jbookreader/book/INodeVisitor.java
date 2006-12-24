package jbookreader.book;

/**
 * Node traversal interface.
 * Each method processes one type of nodes. Each visiting method should return true if
 * processing of nodes should be stopped.
 * @author Dmitry Baryshkov (dbaryshkov@gmail.com)
 *
 */
public interface INodeVisitor {
	boolean visitTextNode(ITextNode node);
	boolean visitContainerNode(IContainerNode node);
	boolean visitImageNode(IImageNode node);
}

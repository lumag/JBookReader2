package jbookreader.book;

import java.util.NoSuchElementException;

public interface IContainerNode extends INamedNode {
	void visitChildren(INodeVisitor visitor);

	boolean add(INode node);
	boolean isEmpty();

	int getNumber(INode node) throws NoSuchElementException;

	void remove(INode node) throws UnsupportedOperationException;

}

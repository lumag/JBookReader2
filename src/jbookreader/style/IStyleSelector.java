package jbookreader.style;

import jbookreader.book.INode;

public interface IStyleSelector {
	boolean applies(INode node);
	long getWeight();
}

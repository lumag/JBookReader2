package jbookreader.book;

import jbookreader.style.IStylesheet;

public interface IBook {
	void addBody(IContainerNode node, String name);
	IContainerNode getFirstBody();

	void addBinaryBlob(IBinaryBlob blob, String name);
	IBinaryBlob getBinaryBlob(String name);

	IStylesheet getStylesheet();
	void setStylesheet(IStylesheet stylesheet);
}

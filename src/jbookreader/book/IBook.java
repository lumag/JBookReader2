package jbookreader.book;

public interface IBook {
	void addBody(IContainerNode node, String name);
	IContainerNode getFirstBody();

	void addBinaryBlob(IBinaryBlob blob, String name);
	IBinaryBlob getBinaryBlob(String name);
}

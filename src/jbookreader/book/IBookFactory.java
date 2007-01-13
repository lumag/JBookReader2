package jbookreader.book;


public interface IBookFactory {
	public IBook newBook();
	public ITextNode newTextNode();
	public IContainerNode newContainerNode();
	public IBinaryBlob newBinaryBlob();
	public IImageNode newImageNode();
}

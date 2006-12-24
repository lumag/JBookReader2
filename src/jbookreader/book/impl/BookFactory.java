package jbookreader.book.impl;

import jbookreader.book.IBinaryBlob;
import jbookreader.book.IBook;
import jbookreader.book.IBookFactory;
import jbookreader.book.IContainerNode;
import jbookreader.book.IImageNode;
import jbookreader.book.ITextNode;

public class BookFactory implements IBookFactory {
	public IBook newBook() {
		return new BookImpl();
	}
	
	public ITextNode newTextNode() {
		return new TextNodeImpl();
	}
	
	public IContainerNode newContainerNode() {
		return new ContainerNodeImpl();
	}

	public IBinaryBlob newBinaryBlob() {
		return new BinaryBlobImpl();
	}

	public IImageNode newImageNode() {
		return new ImageNodeImpl();
	}
}

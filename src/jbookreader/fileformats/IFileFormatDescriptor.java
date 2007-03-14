package jbookreader.fileformats;

import java.io.IOException;
import java.util.Collection;

import jbookreader.book.IBook;
import jbookreader.book.IBookFactory;
import jbookreader.book.INode;
import jbookreader.style.IStylesheet;

import org.xml.sax.SAXException;

public interface IFileFormatDescriptor {
	
	String getDescription();
	Collection<String> getExtensions();

	IBook parse(String uri, IErrorHandler handler, IBookFactory factory)
			throws SAXException, IOException;

	IStylesheet<INode> getStylesheet();

}
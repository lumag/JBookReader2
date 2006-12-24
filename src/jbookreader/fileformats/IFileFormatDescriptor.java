package jbookreader.fileformats;

import java.io.IOException;

import jbookreader.book.IBook;
import jbookreader.book.IBookFactory;

import org.xml.sax.SAXException;

public interface IFileFormatDescriptor {

	IBook parse(String uri, IErrorHandler handler, IBookFactory factory)
			throws SAXException, IOException;

}
package jbookreader.fileformats.impl;


import jbookreader.fileformats.IErrorHandler;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SAXParseErrorHandler implements ErrorHandler {
	private final IErrorHandler handler;

	public SAXParseErrorHandler(final IErrorHandler handler) {
		this.handler = handler;
	}

	private String generateMessage(SAXParseException exception) {
		return exception.getSystemId() + ":" + exception.getLineNumber() + ": " + exception.getLocalizedMessage();
	}
	public void error(SAXParseException exception) throws SAXException {
		if (handler.error(false, generateMessage(exception))) {
			throw exception;
		}
		exception.printStackTrace();
	}

	public void fatalError(SAXParseException exception) throws SAXException {
		handler.error(true, generateMessage(exception));
		throw exception;
	}

	public void warning(SAXParseException exception) throws SAXException {
		handler.warning(generateMessage(exception));
		exception.printStackTrace();
	}

}

package jbookreader.css;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;

class CSSErrorHandler implements ErrorHandler {

	public void error(CSSParseException exception) throws CSSException {
		exception.printStackTrace();
	}

	public void fatalError(CSSParseException exception) throws CSSException {
		exception.printStackTrace();
	}

	public void warning(CSSParseException exception) throws CSSException {
		exception.printStackTrace();
	}

}

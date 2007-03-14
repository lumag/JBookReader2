package jbookreader.css;

import java.io.IOException;
import java.io.Reader;

import jbookreader.book.INode;
import jbookreader.style.IStylesheet;
import lumag.util.ClassFactory;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.ConditionFactory;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Parser;
import org.w3c.css.sac.SelectorFactory;

public class CSSParser {
	public static IStylesheet<INode> parse(String name) throws CSSException, IOException {
		return parse(new InputSource(name));
	}

	public static IStylesheet<INode> parse(Reader reader) throws CSSException, IOException {
		return parse(new InputSource(reader));
		
	}
	
	private static IStylesheet<INode> parse(InputSource source) throws CSSException, IOException {
		Parser cssParser = ClassFactory.createClass(Parser.class,
				"sac.parser");
		CSSHandler handler = new CSSHandler();
		cssParser.setDocumentHandler(handler);
		cssParser.setSelectorFactory(
                ClassFactory.createClass(
                                SelectorFactory.class,
                                "jbookreader.factory.sac.selector"));
		cssParser.setConditionFactory(
                ClassFactory.createClass(
                                ConditionFactory.class,
                                "jbookreader.factory.sac.condition"));
		cssParser.setErrorHandler(new CSSErrorHandler());
		
		cssParser.parseStyleSheet(source);

		return handler.getStylesheet();
	}

	/**
	 * CSS 1
	 */
	public static long getWeight(int a, int b, int c) {
		return CSSParser.getWeight(0, a, b, c);
	}

	/**
	 * CSS 2
	 */
	public static long getWeight(int a, int b, int c, int d) {
		return a << 24 + b << 16 + c << 8 + d;
	}
}

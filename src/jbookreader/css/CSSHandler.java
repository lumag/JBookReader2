package jbookreader.css;

import java.util.EnumMap;
import java.util.Map;

import jbookreader.style.IStyleSelector;
import jbookreader.style.StyleAttribute;
import jbookreader.style.impl.CSSStylesheet;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.SelectorList;

class CSSHandler implements DocumentHandler {
	private CSSStylesheet stylesheet = new CSSStylesheet();
	// FIXME: string
	private Map<StyleAttribute, String> properties;

	public void comment(String text) throws CSSException {
//		System.out.println("/* " + text + "*/");
	}

	public void startDocument(InputSource source) throws CSSException {
//		System.out.println("/* start document */");
	}

	public void endDocument(InputSource source) throws CSSException {
//		System.out.println("/* end of document */");
	}

	public void startSelector(SelectorList selectors) throws CSSException {
		// do nothing
		properties = new EnumMap<StyleAttribute, String>(StyleAttribute.class);
	}

	public void endSelector(SelectorList selectors) throws CSSException {
		int len = selectors.getLength();
		for (int i = 0; i < len; i++) {
			stylesheet.add((IStyleSelector) selectors.item(i), properties);
		}
		properties = null;
	}

	public void property(String name, LexicalUnit value, boolean important)
	throws CSSException {
		StyleAttribute attr;

		if (name == null) {
			throw new CSSException("null property name");
		} else if ("display".equals(name)) {
			attr = StyleAttribute.DISPLAY; 
		} else {
//			throw new CSSException("property " + name + " not supported");
			System.err.println("property " + name + " not supported");
			return;
		}

		properties.put(attr, value.toString());
	}

	public void startPage(String name, String pseudo_page) throws CSSException {
//		System.out.println("start page");
		throw new CSSException("page not supported");
	}

	public void endPage(String name, String pseudo_page) throws CSSException {
//		System.out.println("end page");
		throw new CSSException("page not supported");
	}

	public void ignorableAtRule(String atRule) throws CSSException {
//		System.out.println("at rule " + atRule);
		throw new CSSException("ignorable at rule");
	}

	public void importStyle(String uri, SACMediaList media,
			String defaultNamespaceURI) throws CSSException {
//		System.out.println("import style " + uri + " for media " + media);
		throw new CSSException("imports not supported");
	}

	public void namespaceDeclaration(String prefix, String uri)
			throws CSSException {
//		System.out.println("namespace: " + prefix + " -> " + uri);
		throw new CSSException("namespaces not supported");
	}

	public void startFontFace() throws CSSException {
//		System.out.println("start font face");
		throw new CSSException("font faces not supported");
	}

	public void endFontFace() throws CSSException {
//		System.out.println("end font face");
		throw new CSSException("font faces not supported");
	}

	public void startMedia(SACMediaList media) throws CSSException {
//		System.out.println("start media " + media);
		throw new CSSException("media not supported");
	}

	public void endMedia(SACMediaList media) throws CSSException {
//		System.out.println("end media");
		throw new CSSException("media not supported");
	}

}

package jbookreader.css;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jbookreader.book.IStylesheet;
import jbookreader.style.ERuleValueType;
import jbookreader.style.IStyleRule;
import jbookreader.style.StyleAttribute;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.SelectorList;

class CSSHandler implements DocumentHandler {
	private CSSStylesheet stylesheet = new CSSStylesheet();
	private Map<IStyleSelector, List<IStyleRule>> ruleSets;
	private static Map<String, IRuleHandler> handlers = new HashMap<String, IRuleHandler>();
	
	static {
		handlers.put("display", new EnumHandler(StyleAttribute.DISPLAY));
		handlers.put("font-style", new EnumHandler(StyleAttribute.FONT_STYLE));
		handlers.put("font-family", new StringArrayHandler(StyleAttribute.FONT_FAMILY));
		handlers.put("text-align", new EnumHandler(StyleAttribute.TEXT_ALIGN));
		handlers.put("font-weight", new FontWeightHandler());
	}

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
		int len = selectors.getLength();
		ruleSets = new LinkedHashMap<IStyleSelector, List<IStyleRule>>();
		for (int i = 0; i < len; i++) {
			ruleSets.put((IStyleSelector) selectors.item(i), new ArrayList<IStyleRule>());
		}
	}

	public void endSelector(SelectorList selectors) throws CSSException {
		for (Entry<IStyleSelector, List<IStyleRule>> rules : ruleSets.entrySet()) {
			stylesheet.add(rules.getKey(), rules.getValue());
		}
		ruleSets = null;
	}

	public void property(String name, LexicalUnit value, boolean important)
	throws CSSException {
		try {
			IRuleHandler handler;
			if (name == null) {
				throw new CSSException("null property name");
			}
			handler = handlers.get(name);
			if (handler == null) {
				// throw new CSSException("property " + name + " not supported");
				System.err.println("property " + name + " not supported");
				return;
			}
			handler.handle(this, value);
		} catch (IllegalArgumentException e){
			System.err.println("Error during parsing property: \"" + name + ": " + value + "\"");
			System.err.println("Caused by: ");
			e.printStackTrace();
		}
	}

	public void addRule(StyleAttribute attr, ERuleValueType type, Object value) {
		for (Entry<IStyleSelector, List<IStyleRule>> rules : ruleSets.entrySet()) {
			rules.getValue().add(new StyleRuleImpl(attr, rules.getKey().getWeight(), type, value));
		}
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

	public IStylesheet getStylesheet() {
		return stylesheet;
	}
}

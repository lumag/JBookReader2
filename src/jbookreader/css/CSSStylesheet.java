package jbookreader.css;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import jbookreader.style.IStyleSelector;
import jbookreader.style.IStylesheet;
import jbookreader.style.StyleAttribute;

class CSSStylesheet implements IStylesheet {
	Map<IStyleSelector, Map<StyleAttribute, String>> rules =
		new LinkedHashMap<IStyleSelector, Map<StyleAttribute,String>>();

	public Map<StyleAttribute, String> getRules(IStyleSelector selector) {
		Map<StyleAttribute, String> properties = rules.get(selector);
		if (properties == null) {
			return null;
		}
		return Collections.unmodifiableMap(properties);
	}

	public Collection<IStyleSelector> getSelectors() {
		return Collections.unmodifiableCollection(rules.keySet());
	}

	public void add(IStyleSelector selector, Map<StyleAttribute, String> properties) {
		rules.put(selector, properties);
	}

}

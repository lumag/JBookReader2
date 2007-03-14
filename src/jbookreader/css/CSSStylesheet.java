package jbookreader.css;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jbookreader.book.INode;
import jbookreader.style.IStyleRule;
import jbookreader.style.IStylesheet;

class CSSStylesheet implements IStylesheet<INode> {
	Map<IStyleSelector, List<IStyleRule>> rules =
		new LinkedHashMap<IStyleSelector, List<IStyleRule>>();

	public List<IStyleRule> getApplicableRules(INode node) {
		List<IStyleRule> result = new ArrayList<IStyleRule>();
		for (Map.Entry<IStyleSelector, List<IStyleRule>> entry : rules.entrySet()) {
			if (entry.getKey().applies(node)) {
				result.addAll(entry.getValue());
			}
		}
		return result;
	}

	public void add(IStyleSelector selector, List<IStyleRule> properties) {
		if (!properties.isEmpty()) {
			rules.put(selector, properties);
		}
	}

}

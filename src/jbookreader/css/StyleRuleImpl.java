package jbookreader.css;

import jbookreader.style.IStyleRule;
import jbookreader.style.StyleAttribute;

class StyleRuleImpl implements IStyleRule {

	private final StyleAttribute attribute;
	private final String value;
	private final long weight;

	public StyleRuleImpl(StyleAttribute attribute, String value, long weight) {
		this.attribute = attribute;
		this.value = value;
		this.weight = weight;
	}

	public StyleAttribute getAttribute() {
		return attribute;
	}

	public String getValue() {
		return value;
	}

	public long getWeight() {
		return weight;
	}

}

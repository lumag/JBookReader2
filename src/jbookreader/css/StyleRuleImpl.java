package jbookreader.css;

import jbookreader.style.ERuleValueType;
import jbookreader.style.IStyleRule;
import jbookreader.style.StyleAttribute;

class StyleRuleImpl implements IStyleRule {

	private final StyleAttribute attribute;
	private final long weight;
	private final ERuleValueType type;
	private final Object value;

	public StyleRuleImpl(StyleAttribute attribute, long weight, ERuleValueType type, Object value) {
		this.attribute = attribute;
		this.weight = weight;
		this.type = type;
		this.value = value;
	}

	public StyleAttribute getAttribute() {
		return attribute;
	}

	public long getWeight() {
		return weight;
	}

	public ERuleValueType getValueType() {
		return type;
	}

	public <T> T getValue(Class<T> klass) throws ClassCastException {
		return klass.cast(value);
	}

}

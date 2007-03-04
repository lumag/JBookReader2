/**
 * 
 */
package jbookreader.formatengine.style.impl;

import jbookreader.formatengine.IStyleConfig;
import jbookreader.style.IStyleRule;
import jbookreader.style.StyleAttribute;

abstract class BasicStyleValueComputer implements IStyleValueComputer {

	public Object compute(StyleAttribute attribute, IStyleRule rule, StyleStackState oldState, IStyleConfig config) {
		switch (rule.getValueType()) {
		case INHERIT:
			return oldState.getAttributeValue(attribute);
		case STRING_ARRAY:
			return handleStringArray(attribute, rule, oldState, config);
		case ENUM:
			return handleEnumValue(attribute, rule, oldState, config);
		case INTEGER:
			return handleInteger(attribute, rule, oldState, config);
		case DIMENSION:
			return handleDimension(attribute, rule, oldState, config);
		default:
			throw new InternalError("Undandled rule value type: " + rule.getValueType());
		}
	}
	
	protected Object handleDimension(StyleAttribute attribute, IStyleRule rule, StyleStackState oldState, IStyleConfig config) {
		throw new IllegalArgumentException(
				"Bad " + attribute + " rule: " + rule);
	}

	protected Object handleStringArray(StyleAttribute attribute, IStyleRule rule, StyleStackState oldState, IStyleConfig config) {
		throw new IllegalArgumentException(
				"Bad " + attribute + " rule: " + rule);
	}
	
	protected Object handleEnumValue(StyleAttribute attribute, IStyleRule rule, StyleStackState oldState, IStyleConfig config) {
		throw new IllegalArgumentException(
				"Bad " + attribute + " rule: " + rule);
	}
	
	protected Object handleInteger(StyleAttribute attribute, IStyleRule rule, StyleStackState oldState, IStyleConfig config) {
		throw new IllegalArgumentException(
				"Bad " + attribute + " rule: " + rule);
	}
}

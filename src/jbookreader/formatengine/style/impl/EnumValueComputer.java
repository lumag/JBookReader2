/**
 * 
 */
package jbookreader.formatengine.style.impl;

import jbookreader.style.IStyleRule;
import jbookreader.style.StyleAttribute;

class EnumValueComputer extends BasicStyleValueComputer {

	@Override
	protected Object handleEnumValue(StyleAttribute attribute, IStyleRule rule, StyleStackState oldState) {
		return rule.getValue(attribute.getAttributeValueClass());
	}

}
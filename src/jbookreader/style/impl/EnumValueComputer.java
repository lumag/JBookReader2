/**
 * 
 */
package jbookreader.style.impl;

import jbookreader.style.IStyleConfig;
import jbookreader.style.IStyleRule;
import jbookreader.style.StyleAttribute;

class EnumValueComputer extends BasicStyleValueComputer {

	@Override
	protected Object handleEnumValue(StyleAttribute attribute, IStyleRule rule, StyleStackState oldState, IStyleConfig config) {
		return rule.getValue(attribute.getAttributeValueClass());
	}

}
/**
 * 
 */
package jbookreader.formatengine.style.impl;

import jbookreader.style.IStyleRule;
import jbookreader.style.StyleAttribute;

class StringArrayValueComputer extends BasicStyleValueComputer {

	@Override
	protected Object handleStringArray(StyleAttribute attribute, IStyleRule rule, StyleStackState oldState) {
		return rule.getValue(String[].class);
	}

}
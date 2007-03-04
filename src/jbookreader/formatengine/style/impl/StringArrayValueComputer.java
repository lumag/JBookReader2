/**
 * 
 */
package jbookreader.formatengine.style.impl;

import jbookreader.formatengine.IStyleConfig;
import jbookreader.style.IStyleRule;
import jbookreader.style.StyleAttribute;

class StringArrayValueComputer extends BasicStyleValueComputer {

	@Override
	protected Object handleStringArray(StyleAttribute attribute, IStyleRule rule, StyleStackState oldState, IStyleConfig config) {
		return rule.getValue(String[].class);
	}

}
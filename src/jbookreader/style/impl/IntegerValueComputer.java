package jbookreader.style.impl;

import jbookreader.style.IStyleConfig;
import jbookreader.style.IStyleRule;
import jbookreader.style.StyleAttribute;

class IntegerValueComputer extends BasicStyleValueComputer {

	@Override
	protected Object handleInteger(StyleAttribute attribute, IStyleRule rule, StyleStackState oldState, IStyleConfig config) {
		return rule.getValue(Integer.class);
	}

}

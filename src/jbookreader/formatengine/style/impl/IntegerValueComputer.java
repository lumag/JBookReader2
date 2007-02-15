package jbookreader.formatengine.style.impl;

import jbookreader.style.IStyleRule;
import jbookreader.style.StyleAttribute;

class IntegerValueComputer extends BasicStyleValueComputer {

	@Override
	protected Object handleInteger(StyleAttribute attribute, IStyleRule rule, StyleStackState oldState) {
		return rule.getValue(Integer.class);
	}

}

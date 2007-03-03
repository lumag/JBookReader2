package jbookreader.formatengine.style.impl;

import jbookreader.style.FontSize;
import jbookreader.style.IDimension;
import jbookreader.style.IStyleRule;
import jbookreader.style.StyleAttribute;

public class FontSizeValueComputer extends BasicStyleValueComputer implements
		IStyleValueComputer {

	@Override
	protected Object handleDimension(StyleAttribute attribute, IStyleRule rule, StyleStackState oldState) {
		return Math.round(oldState.getDimensionConvertor().convert(rule.getValue(IDimension.class)));
	}

	@Override
	protected Object handleEnumValue(StyleAttribute attribute, IStyleRule rule, StyleStackState oldState) {
		FontSize size = rule.getValue(FontSize.class);
		// FIXME: change base value to config?
		return Math.round(size.getScale() * 10);
	}
	
}

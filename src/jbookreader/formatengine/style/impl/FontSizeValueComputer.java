package jbookreader.formatengine.style.impl;

import jbookreader.formatengine.IStyleConfig;
import jbookreader.style.FontSize;
import jbookreader.style.IDimension;
import jbookreader.style.IStyleRule;
import jbookreader.style.StyleAttribute;

public class FontSizeValueComputer extends BasicStyleValueComputer implements
		IStyleValueComputer {

	@Override
	protected Object handleDimension(StyleAttribute attribute, IStyleRule rule, StyleStackState oldState, IStyleConfig config) {
		return Math.round(oldState.getDimensionConvertor().convert(rule.getValue(IDimension.class)));
	}

	@Override
	protected Object handleEnumValue(StyleAttribute attribute, IStyleRule rule, StyleStackState oldState, IStyleConfig config) {
		FontSize size = rule.getValue(FontSize.class);

		return Math.round(size.getScale() * config.getMediumFontSize());
	}
	
}

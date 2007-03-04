package jbookreader.formatengine.style.impl;

import jbookreader.formatengine.IStyleConfig;
import jbookreader.style.FontWeight;
import jbookreader.style.IStyleRule;
import jbookreader.style.StyleAttribute;

class FontWeightValueComputer extends IntegerValueComputer {

	@Override
	protected Object handleEnumValue(StyleAttribute attribute, IStyleRule rule, StyleStackState oldState, IStyleConfig config) {
		FontWeight weight = rule.getValue(FontWeight.class);
		int val;
		switch (weight) {
		case NORMAL:
			return 400;
		case BOLD:
			return 700;
		case LIGHTER:
			val = oldState.<Integer>getAttributeValue(attribute);
			if (val - 100 < 100) {
				return 100;
			}
			return val - 100;
		case BOLDER:
			val = oldState.<Integer>getAttributeValue(attribute);
			if (val + 100 > 900) {
				return 900;
			}
			return val + 100;
		default:
			throw new InternalError("Unhandled font-weight value: " + weight);
		}
	}

}

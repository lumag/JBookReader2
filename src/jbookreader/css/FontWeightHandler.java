package jbookreader.css;

import org.w3c.css.sac.LexicalUnit;

import jbookreader.style.ERuleValueType;
import jbookreader.style.FontWeight;
import jbookreader.style.StyleAttribute;

class FontWeightHandler extends BasicHandler implements IRuleHandler {
	FontWeightHandler() {
		super(StyleAttribute.FONT_WEIGHT, FontWeight.class);
	}

	@Override
	public void handle(CSSHandler handler, LexicalUnit value) {
		if (value.getLexicalUnitType() == LexicalUnit.SAC_INTEGER) {
			handler.addRule(StyleAttribute.FONT_WEIGHT, ERuleValueType.INTEGER, value.getIntegerValue());
		} else {
			super.handle(handler, value);
		}
	}

}

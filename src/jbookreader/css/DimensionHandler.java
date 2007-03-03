package jbookreader.css;

import org.w3c.css.sac.LexicalUnit;

import jbookreader.style.ERuleValueType;
import jbookreader.style.StyleAttribute;

class DimensionHandler extends BasicHandler {

	protected DimensionHandler(StyleAttribute attribute) {
		super(attribute);
	}

	protected DimensionHandler(StyleAttribute attribute,
			Class<? extends Enum<?>> enumClass) {
		super(attribute, enumClass);
	}

	@Override
	public void handle(CSSHandler handler, LexicalUnit value) {
		if (
				value.getLexicalUnitType() == LexicalUnit.SAC_EM ||
				value.getLexicalUnitType() == LexicalUnit.SAC_EX ||
				value.getLexicalUnitType() == LexicalUnit.SAC_PIXEL ||
				value.getLexicalUnitType() == LexicalUnit.SAC_INCH ||
				value.getLexicalUnitType() == LexicalUnit.SAC_CENTIMETER ||
				value.getLexicalUnitType() == LexicalUnit.SAC_POINT ||
				value.getLexicalUnitType() == LexicalUnit.SAC_PICA ||
				value.getLexicalUnitType() == LexicalUnit.SAC_DIMENSION) {
	
			handler.addRule(getAttribute(),
					ERuleValueType.DIMENSION,
					new Dimension(value.getFloatValue(), value.getDimensionUnitText()));
		} else {
			super.handle(handler, value);
		}
	}

}

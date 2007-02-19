package jbookreader.css;

import jbookreader.style.ERuleValueType;
import jbookreader.style.StyleAttribute;

import org.w3c.css.sac.LexicalUnit;

abstract class BasicHandler implements IRuleHandler {
	private final StyleAttribute attribute;

	public StyleAttribute getAttribute() {
		return attribute;
	}

	protected BasicHandler(final StyleAttribute attribute) {
		this.attribute = attribute;
	}

	public void handle(CSSHandler handler, LexicalUnit value) {
		if (value.getLexicalUnitType() == LexicalUnit.SAC_INHERIT &&
				value.getNextLexicalUnit() != null) {
			handler.addRule(attribute, ERuleValueType.INHERIT, null);
		}

		throw new IllegalArgumentException("Bad value for attribute '"
				+ attribute + "': "
				+ value.getLexicalUnitType() + " (" + value +")");
	}

}

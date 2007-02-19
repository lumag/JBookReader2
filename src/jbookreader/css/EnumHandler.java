/**
 * 
 */
package jbookreader.css;

import jbookreader.style.ERuleValueType;
import jbookreader.style.StyleAttribute;

import org.w3c.css.sac.LexicalUnit;

class EnumHandler extends BasicHandler {

	private final Class<? extends Enum<?>> klass;

	protected EnumHandler(final StyleAttribute attribute, final Class<? extends Enum<?>> klass) {
		super(attribute);
		this.klass = klass;
	}

	@SuppressWarnings("unchecked")
	public EnumHandler(final StyleAttribute attribute) {
		this(attribute, (Class<? extends Enum<?>>) attribute.getAttributeValueClass());
	}


	@Override
	@SuppressWarnings("unchecked")
	public void handle(CSSHandler handler, LexicalUnit value) {
		if (value.getLexicalUnitType() == LexicalUnit.SAC_IDENT &&
			value.getNextLexicalUnit() == null) {
			String name = value.getStringValue().toUpperCase();

			handler.addRule(getAttribute(), ERuleValueType.VALUE, Enum.valueOf(klass.asSubclass(Enum.class), name));
		} else {
			super.handle(handler, value);
		}
	}
	
}
/**
 * 
 */
package jbookreader.css;

import jbookreader.style.ERuleValueType;
import jbookreader.style.StyleAttribute;

import org.w3c.css.sac.LexicalUnit;

class EnumHandler implements IRuleHandler {

	private final StyleAttribute attribute;
	private final Class<? extends Enum<?>> klass;

	protected EnumHandler(final StyleAttribute attribute, final Class<? extends Enum<?>> klass) {
		this.attribute = attribute;
		this.klass = klass;
	}

	@SuppressWarnings("unchecked")
	public EnumHandler(final StyleAttribute attribute) {
		this(attribute, (Class<? extends Enum<?>>) attribute.getAttributeValueClass());
	}


	@SuppressWarnings("unchecked")
	public void handle(CSSHandler handler, LexicalUnit value) {
		if (value.getLexicalUnitType() == LexicalUnit.SAC_INHERIT) {
			handler.addRule(attribute, ERuleValueType.INHERIT, null);
		}
		if (value.getLexicalUnitType() != LexicalUnit.SAC_IDENT ||
			value.getNextLexicalUnit() != null) {
			throw new IllegalArgumentException("Bad value type: "
							+ value.getLexicalUnitType() + " (" + value +")");
		}
		
		String name = value.getStringValue().toUpperCase();

		handler.addRule(attribute, ERuleValueType.VALUE, Enum.valueOf(klass.asSubclass(Enum.class), name));
	}
	
}
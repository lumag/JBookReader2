package jbookreader.css;

import jbookreader.style.ERuleValueType;
import jbookreader.style.StyleAttribute;

import org.w3c.css.sac.LexicalUnit;

class BasicHandler implements IRuleHandler {
	private final StyleAttribute attribute;

	private final Class<? extends Enum<?>> enumClass;

	@SuppressWarnings("unchecked")
	protected BasicHandler(final StyleAttribute attribute) {
		this(attribute, 
				Enum.class.isAssignableFrom(attribute.getAttributeValueClass()) ?
					(Class<? extends Enum<?>>) attribute.getAttributeValueClass() :
					null
			);
	}

	protected BasicHandler(final StyleAttribute attribute, final Class<? extends Enum<?>> enumClass) {
		this.attribute = attribute;
		this.enumClass = enumClass;
	}

	public StyleAttribute getAttribute() {
		return attribute;
	}

	@SuppressWarnings("unchecked")
	public void handle(CSSHandler handler, LexicalUnit value) {
		if (value.getLexicalUnitType() == LexicalUnit.SAC_INHERIT &&
				value.getNextLexicalUnit() != null) {
			handler.addRule(attribute, ERuleValueType.INHERIT, null);
		} else if (enumClass != null &&
				value.getLexicalUnitType() == LexicalUnit.SAC_IDENT &&
					value.getNextLexicalUnit() == null) {
				String name = value.getStringValue().toUpperCase().replace('-', '_');

				handler.addRule(getAttribute(),
						ERuleValueType.VALUE,
						Enum.valueOf(enumClass.asSubclass(Enum.class), name));
		} else {
			throw new IllegalArgumentException("Bad value for attribute '"
				+ attribute + "': "
				+ value.getLexicalUnitType() + " (" + value +")");
		}
	}

}

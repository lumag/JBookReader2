package jbookreader.style;

import static jbookreader.style.ERuleValueType.*;

public enum StyleAttribute implements IStyleRule {
	DISPLAY(		Display.class,		false,	ENUM,	Display.INLINE),

//	WIDTH(			Integer.class,		false,	INTEGER,	-1),
	
	TEXT_ALIGN(		Alignment.class,	true,	ENUM,	Alignment.JUSTIFY),
	
	FONT_FAMILY(	String[].class, 	true,	STRING_ARRAY,	new String[]{"default"}),
	FONT_SIZE(		Integer.class,		true,	ENUM,	FontSize.MEDIUM),
	FONT_STYLE(		FontStyle.class,	true,	ENUM,	FontStyle.NORMAL),
	FONT_WEIGHT(	Integer.class,		true,	ENUM,	FontWeight.NORMAL);
	
	private final Class<?> attributeValueClass;
	private final boolean inherit;
	private final ERuleValueType initialValueType;
	private final Object initialValue;
	
	StyleAttribute(Class<?> klass, boolean inherit, ERuleValueType initialValueType, Object initialValue) {
		this.attributeValueClass = klass;
		this.inherit = inherit;
		this.initialValueType = initialValueType;
		this.initialValue = initialValue;
	}
	
	public StyleAttribute getAttribute() {
		return this;
	}

	public ERuleValueType getValueType() {
		return initialValueType;
	}

	public Class<?> getAttributeValueClass() {
		return attributeValueClass;
	}

	public boolean isInherit() {
		return inherit;
	}

	public <T> T getValue(Class<T> klass) throws ClassCastException {
		return klass.cast(initialValue);
	}

	public long getWeight() {
		return 0;
	}

}

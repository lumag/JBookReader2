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
	
	private final ERuleValueType attributeType;
	private final Class<?> attributeValueClass;
	private final Object initialValue;
	private final boolean inherit;
	
	StyleAttribute(Class<?> klass, boolean inherit, ERuleValueType attributeType, Object initialValue) {
		this.attributeType = attributeType;
		this.attributeValueClass = klass;
		this.initialValue = initialValue;
		this.inherit = inherit;
	}
	
	public StyleAttribute getAttribute() {
		return this;
	}

	public ERuleValueType getValueType() {
		return attributeType;
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

package jbookreader.style;

import static jbookreader.style.AttributeType.*;

public enum StyleAttribute {
	DISPLAY(		ENUM,		Display.class,		false,	Display.INLINE),
	
	TEXT_ALIGN(		ENUM,		Alignment.class,	true,	Alignment.JUSTIFY),
	
	FONT_FAMILY(	STRING_ARRAY, String[].class,	true,	new String[]{"default"}),
	// FIXME change to dimension
	FONT_SIZE(		INTEGER,	Integer.class,		true,	10),
	FONT_STYLE(		ENUM,		FontStyle.class,	true,	FontStyle.NORMAL),
	FONT_WEIGHT(	INTEGER,	Integer.class,		true,	400);
	
	private final AttributeType attributeType;
	private final Class<?> attributeValueClass;
	private final Object initialValue;
	private final boolean inherit;
	
	<T> StyleAttribute(AttributeType attributeType, Class<T> klass, boolean inherit, T initialValue) {
		this.attributeType = attributeType;
		this.attributeValueClass = klass;
		this.initialValue = initialValue;
		this.inherit = inherit;
	}

	public AttributeType getAttributeType() {
		return attributeType;
	}

	public Class<?> getAttributeValueClass() {
		return attributeValueClass;
	}

	public boolean isInherit() {
		return inherit;
	}

	public Object getInitialValue() {
		return initialValue;
	}

}

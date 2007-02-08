package jbookreader.formatengine.style.impl;

import java.util.Map;

import jbookreader.style.Alignment;
import jbookreader.style.Display;
import jbookreader.style.FontStyle;
import jbookreader.style.IStyleRule;
import jbookreader.style.StyleAttribute;

class StyleStackState {
//	String nodeTag;
//	String nodeClass;

	final Display display;
	final Alignment textAlignment;
	final String[] fontFamily;
	final int fontSize;
	final int fontWeight;
	final FontStyle fontStyle;
	
	StyleStackState() {
		display = Display.BLOCK;
		textAlignment = Alignment.JUSTIFY;
		fontFamily = new String[]{"Serif"};
		fontSize = 12;
		fontWeight = 400;
		fontStyle = FontStyle.NORMAL;
	}
	
	StyleStackState(StyleStackState oldState, Map<StyleAttribute, IStyleRule> rules) {
		display = setEnumValue(Display.class, StyleAttribute.DISPLAY, rules, oldState.display);
		textAlignment = setEnumValue(Alignment.class, StyleAttribute.TEXT_ALIGN, rules, oldState.textAlignment);
		fontFamily = setStringArrayValue(StyleAttribute.FONT_FAMILY, rules, oldState.fontFamily);
		fontSize = oldState.fontSize;
		fontWeight = oldState.fontWeight;
		fontStyle = setEnumValue(FontStyle.class, StyleAttribute.FONT_STYLE, rules, oldState.fontStyle);
	}
	
	private String[] setStringArrayValue(StyleAttribute attribute, Map<StyleAttribute, IStyleRule> rules, String[] defValue) {

		IStyleRule value = rules.get(attribute);

		// FIXME: change to throw!
		if (value == null) {
			return defValue;
		}
		switch (value.getValueType()) {
		case INHERIT:
			return defValue;
		case STRING_ARRAY:
			return value.getValue(String[].class);
		default:
			throw new IllegalArgumentException(
					"Bad " + attribute + " value: " + value);
		}
	}

	private <T> T setEnumValue(Class<? extends T> cl,
			StyleAttribute attribute,
			Map<StyleAttribute, IStyleRule> rules,
			T defValue) {

		IStyleRule value = rules.get(attribute);

		// FIXME: change to throw!
		if (value == null) {
			return defValue;
		}
		switch (value.getValueType()) {
		case INHERIT:
			return defValue;
		case VALUE:
			return value.getValue(cl);
		default:
			throw new IllegalArgumentException(
					"Bad " + attribute + " value: " + value);
		}
	}
}

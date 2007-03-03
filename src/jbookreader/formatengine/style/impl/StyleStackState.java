package jbookreader.formatengine.style.impl;

import java.util.EnumMap;
import java.util.Map;

import jbookreader.style.AttributeType;
import jbookreader.style.IDimensionConvertor;
import jbookreader.style.IStyleRule;
import jbookreader.style.StyleAttribute;

class StyleStackState {
	static Map<StyleAttribute, IStyleValueComputer> specials;
	static Map<AttributeType, IStyleValueComputer> generics; 
	static {
		specials = new EnumMap<StyleAttribute, IStyleValueComputer>(StyleAttribute.class);
		specials.put(StyleAttribute.FONT_WEIGHT, new FontWeightValueComputer());
		specials.put(StyleAttribute.FONT_SIZE, new FontSizeValueComputer());
		
		generics = new EnumMap<AttributeType, IStyleValueComputer>(AttributeType.class);
		generics.put(AttributeType.INTEGER, new IntegerValueComputer());
		generics.put(AttributeType.STRING_ARRAY, new StringArrayValueComputer());
		generics.put(AttributeType.ENUM, new EnumValueComputer());
	}
	
	private final Map<StyleAttribute, Object> attributes;
	private final IDimensionConvertor convertor = new DimensionConvertor();

	StyleStackState() {
		attributes = new EnumMap<StyleAttribute, Object>(StyleAttribute.class);
		
		for (StyleAttribute attribute : StyleAttribute.values()) {
			attributes.put(attribute, attribute.getInitialValue());
		}
	}
	
	StyleStackState(StyleStackState oldState, Map<StyleAttribute, IStyleRule> rules) {
		attributes = new EnumMap<StyleAttribute, Object>(StyleAttribute.class);
		for (StyleAttribute attrib: StyleAttribute.values()) {
			IStyleRule rule = rules.get(attrib);

			if (rule == null) {
				attributes.put(attrib, oldState.getDefaultValue(attrib));
				continue;
			}
			
			IStyleValueComputer computer = specials.get(attrib);
			if (computer == null) {
				computer = generics.get(attrib.getAttributeType());
			}
			
			if (computer == null) {
				throw new InternalError("Can't get value computer for attribute " + attrib);
			}

			attributes.put(attrib, computer.compute(attrib, rule, oldState));
		}
	}
	
	Object getDefaultValue(StyleAttribute attribute) {
		if (attribute.isInherit()) {
			return attributes.get(attribute);
		}
		return attribute.getInitialValue();
	}
	
	<T> T getAttributeValue(StyleAttribute attribute, Class<T> klass) {
		// XXX maybe add more checks about T vs attribute.getAttributeValueClass()
		return klass.cast(attributes.get(attribute));
	}
	
	IDimensionConvertor getDimensionConvertor() {
		return convertor;
	}
}

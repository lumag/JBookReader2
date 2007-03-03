package jbookreader.formatengine.style.impl;

import java.util.EnumMap;
import java.util.Map;

import jbookreader.style.ERuleValueType;
import jbookreader.style.IDimensionConvertor;
import jbookreader.style.IStyleRule;
import jbookreader.style.StyleAttribute;

class StyleStackState {
	private static Map<StyleAttribute, IStyleValueComputer> specials;
	private static Map<ERuleValueType, IStyleValueComputer> generics; 
	static {
		specials = new EnumMap<StyleAttribute, IStyleValueComputer>(StyleAttribute.class);
		specials.put(StyleAttribute.FONT_WEIGHT, new FontWeightValueComputer());
		specials.put(StyleAttribute.FONT_SIZE, new FontSizeValueComputer());
		
		generics = new EnumMap<ERuleValueType, IStyleValueComputer>(ERuleValueType.class);
		generics.put(ERuleValueType.INTEGER, new IntegerValueComputer());
		generics.put(ERuleValueType.STRING_ARRAY, new StringArrayValueComputer());
		generics.put(ERuleValueType.ENUM, new EnumValueComputer());
	}
	
	private static final IStyleRule inheritRule = new IStyleRule() {

		public StyleAttribute getAttribute() {
			throw new InternalError("Attribute requested for special inheritance rule");
		}

		public <T> T getValue(Class<T> klass) throws ClassCastException {
			return null;
		}

		public ERuleValueType getValueType() {
			return ERuleValueType.INHERIT;
		}

		public long getWeight() {
			return 0;
		}
		
	};
	
	private final Map<StyleAttribute, Object> attributes;
	private final IDimensionConvertor convertor = new DimensionConvertor();

	StyleStackState() {
		attributes = new EnumMap<StyleAttribute, Object>(StyleAttribute.class);
		
		for (StyleAttribute attribute : StyleAttribute.values()) {
			IStyleValueComputer computer = findComputer(attribute);
			attributes.put(attribute, computer.compute(attribute, attribute, null));
		}
	}
	
	StyleStackState(StyleStackState oldState, Map<StyleAttribute, IStyleRule> rules) {
		attributes = new EnumMap<StyleAttribute, Object>(StyleAttribute.class);
		for (StyleAttribute attribute: StyleAttribute.values()) {
			IStyleValueComputer computer = findComputer(attribute);

			IStyleRule rule = rules.get(attribute);

			if (rule == null) {
				if (attribute.isInherit()) {
					rule = inheritRule;
				} else {
					rule = attribute;
				}
			}

			attributes.put(attribute, computer.compute(attribute, rule, oldState));
		}
	}

	private IStyleValueComputer findComputer(StyleAttribute attrib) throws InternalError {
		IStyleValueComputer computer = specials.get(attrib);
		if (computer == null) {
			computer = generics.get(attrib.getValueType());
		}
		
		if (computer == null) {
			throw new InternalError("Can't get value computer for attribute " + attrib);
		}
		return computer;
	}
	
	@SuppressWarnings("unchecked")
	<T extends Object> T getAttributeValue(StyleAttribute attribute) {
		return (T) attribute.getAttributeValueClass().cast(attributes.get(attribute));
	}
	
	void setAttributeValue(StyleAttribute attribute, Object object) {
		attributes.put(attribute, attribute.getAttributeValueClass().cast(object));
	}

	IDimensionConvertor getDimensionConvertor() {
		return convertor;
	}

}

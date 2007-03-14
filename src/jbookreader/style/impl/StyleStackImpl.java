package jbookreader.style.impl;

import static jbookreader.style.StyleAttribute.DISPLAY;
import static jbookreader.style.StyleAttribute.FONT_FAMILY;
import static jbookreader.style.StyleAttribute.FONT_SIZE;
import static jbookreader.style.StyleAttribute.FONT_STYLE;
import static jbookreader.style.StyleAttribute.FONT_WEIGHT;
import static jbookreader.style.StyleAttribute.TEXT_ALIGN;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import jbookreader.style.Alignment;
import jbookreader.style.Display;
import jbookreader.style.FontStyle;
import jbookreader.style.IStyleConfig;
import jbookreader.style.IStyleRule;
import jbookreader.style.IStyleStack;
import jbookreader.style.IStylesheet;
import jbookreader.style.StyleAttribute;

public class StyleStackImpl<T> implements IStyleStack<T> {
	private List<IStylesheet<T>> stylesheets = new ArrayList<IStylesheet<T>>();
	private List<StyleStackState> stateStack = new ArrayList<StyleStackState>();
	private StyleStackState currentState;
	private IStyleConfig config;

	public StyleStackImpl() {
		// do nothing
	}
	
	public void setConfig(final IStyleConfig config) {
		if (currentState != null) {
			throw new IllegalStateException(
					"Can't change config in the middle of operation");
		}

		this.config = config;

		currentState = new StyleStackState(config);
		stateStack.add(currentState);
	}
	
	IStyleConfig getConfig() {
		return config;
	}

	public void addStylesheet(final IStylesheet<T> stylesheet) {
		stylesheets.add(stylesheet);
	}

	public void push(T context) {
		Map<StyleAttribute, IStyleRule> rules =
			new EnumMap<StyleAttribute, IStyleRule>(
					StyleAttribute.class);
		
		for (IStylesheet<T> stylesheet: stylesheets) {
			for (IStyleRule rule:
					stylesheet.getApplicableRules(context)) {
				StyleAttribute attrib = rule.getAttribute();
				long weight = rule.getWeight();
				if (!rules.containsKey(attrib) ||
					rules.get(attrib).getWeight() <= weight) {
					rules.put(attrib, rule);
				}
			}
		}
		
//		System.out.println(node);
//		for (Map.Entry<StyleAttribute, IStyleRule> rule : rules.entrySet()) {
//			System.out.println(rule.getKey() + ": (" + rule.getValue().getValueType() + ") "+ rule.getValue().getValue(Object.class));
//		}
		
		currentState = new StyleStackState(currentState, rules, config);
		
		stateStack.add(currentState);
	}

	public void pop() {
		stateStack.remove(currentState);
		if (stateStack.isEmpty()) {
			currentState = new StyleStackState(config);
			stateStack.add(currentState);
			System.err.println("style stack became empty!");
		} else {
			currentState = stateStack.get(stateStack.size() - 1);
		}
	}
	
	public Display getDisplay() {
		return currentState.getAttributeValue(DISPLAY);
	}
	
//	public int getWidth() {
//		return currentState.<Integer>getAttributeValue(WIDTH);
//	}
//
	public Alignment getTextAlign() {
		return currentState.getAttributeValue(TEXT_ALIGN);
	}

	public String[] getFontFamily() {
		return currentState.getAttributeValue(FONT_FAMILY);
	}

	public int getFontSize() {
		return currentState.<Integer>getAttributeValue(FONT_SIZE);
	}

	public int getFontWeight() {
		return currentState.<Integer>getAttributeValue(FONT_WEIGHT); 
	}

	public FontStyle getFontStyle() {
		return currentState.getAttributeValue(FONT_STYLE);
	}

}

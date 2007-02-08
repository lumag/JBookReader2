package jbookreader.formatengine.style.impl;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import jbookreader.book.INode;
import jbookreader.book.IStylesheet;
import jbookreader.formatengine.IStyleStack;
import jbookreader.style.Alignment;
import jbookreader.style.Display;
import jbookreader.style.FontStyle;
import jbookreader.style.IStyleRule;
import jbookreader.style.StyleAttribute;

public class StyleStackImpl implements IStyleStack {
	List<IStylesheet> stylesheets = new ArrayList<IStylesheet>();
	List<StyleStackState> stateStack = new ArrayList<StyleStackState>();
	StyleStackState currentState = new StyleStackState();

	public StyleStackImpl() {
		stateStack.add(currentState);
	}

	public void addStylesheet(IStylesheet stylesheet) {
		stylesheets.add(stylesheet);
	}

	public void push(INode node) {
		Map<StyleAttribute, IStyleRule> rules =
			new EnumMap<StyleAttribute, IStyleRule>(
					StyleAttribute.class);
		
		for (IStylesheet stylesheet: stylesheets) {
			for (IStyleRule rule:
					stylesheet.getApplicableRules(node)) {
				StyleAttribute attrib = rule.getAttribute();
				long weight = rule.getWeight();
				if (!rules.containsKey(attrib) ||
					rules.get(attrib).getWeight() <= weight) {
					rules.put(attrib, rule);
				}
			}
		}
		
		System.out.println(node);
		for (Map.Entry<StyleAttribute, IStyleRule> rule : rules.entrySet()) {
			System.out.println(rule.getKey() + ": (" + rule.getValue().getValueType() + ") "+ rule.getValue().getValue(Object.class));
		}
		
		currentState = new StyleStackState(currentState, rules);
		
		stateStack.add(currentState);
	}

	public void pop() {
		stateStack.remove(currentState);
		if (stateStack.isEmpty()) {
			currentState = new StyleStackState();
			stateStack.add(currentState);
			System.err.println("style stack became empty!");
		} else {
			currentState = stateStack.get(stateStack.size() - 1);
		}
	}

	public Display getDisplay() {
		return currentState.display;
	}

	public Alignment getTextAlign() {
		return currentState.textAlignment;
	}

	public String[] getFontFamily() {
		return currentState.fontFamily;
	}

	public int getFontSize() {
		return currentState.fontSize;
	}

	public int getFontWeight() {
		return currentState.fontWeight;
	}

	public FontStyle getFontStyle() {
		return currentState.fontStyle;
	}

}

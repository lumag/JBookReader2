package jbookreader.style.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jbookreader.book.INode;
import jbookreader.book.INodeVisitor;
import jbookreader.style.Alignment;
import jbookreader.style.Display;
import jbookreader.style.FontStyle;
import jbookreader.style.IStyleStack;
import jbookreader.style.IStylesheet;

public class StyleStackImpl implements IStyleStack {
	List<StyleStackState> stateStack = new ArrayList<StyleStackState>();
	StyleStackState currentState = new StyleStackState();
	// FIXME
	private final INodeVisitor visitor = new FB2StyleNodeVisitor(this); 
	
	public StyleStackImpl() {
		stateStack.add(currentState);
	}

	public void push(INode node) {
		currentState = currentState.clone();
		
		node.accept(visitor);
		
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

	public List<String> getFontFamily() {
		return Collections.unmodifiableList(currentState.fontFamily);
	}

	public String getFirstFontFamily() {
		return currentState.fontFamily.get(0);
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

	public void addStylesheet(IStylesheet stylesheet) {
		System.out.println("add " + stylesheet);
		// TODO Auto-generated method stub
		
	}

}

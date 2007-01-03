package jbookreader.style;

import java.util.List;

import jbookreader.book.INode;

public interface IStyleStack {
	void push(INode node);
	void pop();
	
	Display getDisplay();
	Alignment getTextAlign();
	
	List<String> getFontFamily();
	String getFirstFontFamily();
	int getFontSize();
}

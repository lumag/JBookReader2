package jbookreader.formatengine;

import java.util.List;

import jbookreader.book.INode;
import jbookreader.book.IStylesheet;
import jbookreader.style.Alignment;
import jbookreader.style.Display;
import jbookreader.style.FontStyle;

public interface IStyleStack {
	void addStylesheet(IStylesheet stylesheet);
	
	void push(INode node);
	void pop();
	
	Display getDisplay();
	Alignment getTextAlign();
	
	List<String> getFontFamily();
	String getFirstFontFamily();
	int getFontSize();
	FontStyle getFontStyle();
	int getFontWeight();
}

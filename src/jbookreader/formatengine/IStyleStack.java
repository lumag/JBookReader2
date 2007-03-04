package jbookreader.formatengine;

import jbookreader.book.INode;
import jbookreader.book.IStylesheet;
import jbookreader.style.Alignment;
import jbookreader.style.Display;
import jbookreader.style.FontStyle;

public interface IStyleStack {
	void setConfig(final IStyleConfig config);
	void addStylesheet(final IStylesheet stylesheet);
	
	void push(INode node);
	void pop();
	
	Display getDisplay();

//	int getWidth();

	Alignment getTextAlign();
	
	String[] getFontFamily();
	int getFontSize();
	FontStyle getFontStyle();
	int getFontWeight();
}

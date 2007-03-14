package jbookreader.style;


public interface IStyleStack<T> {
	void setConfig(final IStyleConfig config);
	void addStylesheet(final IStylesheet<T> stylesheet);
	
	void push(T node);
	void pop();
	
	Display getDisplay();

//	int getWidth();

	Alignment getTextAlign();
	
	String[] getFontFamily();
	int getFontSize();
	FontStyle getFontStyle();
	int getFontWeight();
}

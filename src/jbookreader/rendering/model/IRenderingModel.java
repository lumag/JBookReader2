package jbookreader.rendering.model;

import jbookreader.book.IBook;
import jbookreader.formatengine.ICompositor;
import jbookreader.formatengine.IFormatEngine;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.style.IStyleConfig;
import jbookreader.style.IStylesheet;

public interface IRenderingModel<T> {
	
	void clear();

	void setConfig(IStyleConfig config);

	void setCompositor(ICompositor<T> compositor);

	void setFormatEngine(IFormatEngine<T> engine);

	// FIXME: remove/replace this call to remove direct dependancy on the jbr.book
	void setBook(IBook book);

	void setDefaultStylesheet(IStylesheet<T> defaultStylesheet);

	void setFormatStylesheet(IStylesheet<T> formatStylesheet);

	float findNextHeight(float height, int direction);

	void render(IGraphicDriver<T> driver, int minHeight, int maxHeight, int offset);

	float getHeight(IGraphicDriver<T> driver);
	
	float getOffset(IGraphicDriver<T> driver, T context);
}

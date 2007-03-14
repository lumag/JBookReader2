package jbookreader.rendering.swing;

import jbookreader.book.IBook;
import jbookreader.formatengine.ICompositor;
import jbookreader.formatengine.IFormatEngine;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.style.IStylesheet;

public interface IRenderingModel<T> {

	void setCompositor(ICompositor<T> compositor);

	void setFormatEngine(IFormatEngine<T> engine);

	void setBook(IBook book);

	void setDefaultStylesheet(IStylesheet<T> defaultStylesheet);

	void setFormatStylesheet(IStylesheet<T> formatStylesheet);

	int findNextHeight(float height, int direction);

	void render(IGraphicDriver<T> driver, int minHeight, int maxHeight, int offset);

	float getHeight(IGraphicDriver<T> driver);
}

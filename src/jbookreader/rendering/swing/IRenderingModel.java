package jbookreader.rendering.swing;

import jbookreader.book.IBook;
import jbookreader.book.IStylesheet;
import jbookreader.formatengine.ICompositor;
import jbookreader.formatengine.IFormatEngine;
import jbookreader.rendering.IGraphicDriver;

public interface IRenderingModel<T> {

	void setCompositor(ICompositor<T> compositor);

	void setFormatEngine(IFormatEngine<T> engine);

	void setBook(IBook book);

	void setDefaultStylesheet(IStylesheet defaultStylesheet);

	void setFormatStylesheet(IStylesheet formatStylesheet);

	int findNextHeight(float height, int direction);

	void render(IGraphicDriver<T> driver, int minHeight, int maxHeight, int offset);

	float getHeight(IGraphicDriver<T> driver);
}

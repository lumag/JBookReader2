package jbookreader.rendering.swing;

import jbookreader.book.IBook;
import jbookreader.book.INode;
import jbookreader.book.IStylesheet;
import jbookreader.formatengine.ICompositor;
import jbookreader.formatengine.IFormatEngine;
import jbookreader.rendering.IGraphicDriver;

public interface IRenderingModel {

	void setCompositor(ICompositor<INode> compositor);

	void setFormatEngine(IFormatEngine<INode> engine);

	void setBook(IBook book);

	void setDefaultStylesheet(IStylesheet defaultStylesheet);

	void setFormatStylesheet(IStylesheet formatStylesheet);

	int findNextHeight(float height, int direction);

	void render(IGraphicDriver<INode> driver, int minHeight, int maxHeight, int offset);

	float getHeight(IGraphicDriver<INode> driver);
}

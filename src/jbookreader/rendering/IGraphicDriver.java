package jbookreader.rendering;

import java.io.IOException;
import java.io.InputStream;


public interface IGraphicDriver {
	// factory methods
	IDrawable renderString(String s, IFont font);
	IDrawable renderBox(int width, int height, int depth);
	IDrawable renderImage(String contentType, InputStream dataStream) throws IOException;

	// TODO font variations
	IFont getFont(String name, int size);
	
	// drawing methods
	void clear();

	void addHorizontalSpace(int amount);
	void addVerticalSpace(int amount);

	int getHorizontalPosition();
	int getVerticalPosition();

	int getPaperWidth();
	int getPaperHeight();
}

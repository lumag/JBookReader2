package jbookreader.rendering;

import java.io.IOException;
import java.io.InputStream;

import jbookreader.style.FontDescriptor;


public interface IGraphicDriver<T> {
	// factory methods
	IDrawable<T> renderString(String s, IFont font, T context);
	IDrawable<T> renderBox(int width, int height, int depth, T context);
	IDrawable<T> renderImage(String contentType, InputStream dataStream, T context) throws IOException;

	IFont getFont(FontDescriptor fd);
	
	// drawing methods
	void clear();

	void addHorizontalSpace(float amount);
	void addVerticalSpace(float amount);

	float getHorizontalPosition();
	float getVerticalPosition();

	int getPaperWidth();
	int getPaperHeight();
}

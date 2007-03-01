package jbookreader.rendering;

import java.io.IOException;
import java.io.InputStream;

import jbookreader.style.FontDescriptor;


public interface IGraphicDriver {
	// factory methods
	IDrawable renderString(String s, IFont font);
	IDrawable renderBox(int width, int height, int depth);
	IDrawable renderImage(String contentType, InputStream dataStream) throws IOException;

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

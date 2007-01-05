package jbookreader.rendering.text;

import java.io.InputStream;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IFont;
import jbookreader.rendering.IGraphicDriver;

public class TextRenderer implements IGraphicDriver {
	private static final int WIDTH = 80; 
	private int xPosition;
	private int yPosition;
	public void addHorizontalSpace(int amount) {
		for (int i = 0; i < amount; i++) {
			append(" ");
		}
//		xPosition += amount;
	}

	public void addVerticalSpace(int amount) {
		yPosition += amount;
	}

	public void clear() {
		xPosition = 0;
		yPosition = 0;
	}

	public int getPaperHeight() {
		return Integer.MAX_VALUE;
	}

	public int getHorizontalPosition() {
		return xPosition;
	}

	public int getVerticalPosition() {
		return yPosition;
	}

	public int getPaperWidth() {
		return WIDTH;
	}

	public IDrawable renderBox(int width, int height, int depth) {
		throw new UnsupportedOperationException("boxes aren't supported");
	}

	public IDrawable renderString(String s, IFont font) {
		return new StringBox(this, s);
	}
	
	public IDrawable renderImage(String contentType, InputStream dataStream) {
		throw new UnsupportedOperationException("image rendering isn't supported");
	}

	void append(String s) {
		System.out.print(s);
		xPosition += s.length();
	}

	public IFont getFont(String name, int size, boolean bold, boolean italic) {
		return new TextFont(name, size);
	}

}

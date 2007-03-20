package jbookreader.rendering.text;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IFont;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.style.FontDescriptor;

public class TextRenderer<T> implements IGraphicDriver<T> {
	private static final int WIDTH = 80; 
	private float xPosition;
	private int realXPosition;
	private float yPosition;
	private int realYPosition;
	private PrintStream outStream;
	
	public TextRenderer() {
		outStream = System.out;
	}
	
	public TextRenderer(String name) throws IOException {
		outStream = new PrintStream(name);
	}

	public void addHorizontalSpace(float amount) {
		xPosition += amount;
	}

	public void addVerticalSpace(float amount) {
		yPosition += amount;
	}

	public void clear() {
		outStream.flush();
		xPosition = 0;
		realXPosition = 0;
		yPosition = 0;
		realYPosition = 0;
	}

	public int getPaperHeight() {
		return Integer.MAX_VALUE;
	}

	public float getHorizontalPosition() {
		return xPosition;
	}

	public float getVerticalPosition() {
		return yPosition;
	}

	public int getPaperWidth() {
		return WIDTH;
	}

	public IDrawable<T> renderBox(int width, int height, int depth, T context) {
		throw new UnsupportedOperationException("boxes aren't supported");
	}

	public IDrawable<T> renderString(String s, IFont font, T context) {
		return new StringBox<T>(this, s, context);
	}
	
	public IDrawable<T> renderImage(String contentType, InputStream dataStream, T context) {
		throw new UnsupportedOperationException("image rendering isn't supported");
	}
	
	private void fixPosition() {
		int xp = Math.round(xPosition);
		int yp = Math.round(yPosition);
		while (realYPosition < yp) {
			outStream.append('\n');
			realYPosition += 1;
			realXPosition = 0;
		}
		while (realXPosition < xp) {
			outStream.append(' ');
			realXPosition += 1;
		}
	}

	void append(String s) {
		fixPosition();
		outStream.append(s);
		xPosition += s.length();
		realXPosition += s.length();
	}

	public IFont getFont(FontDescriptor fd) {
		return new TextFont(fd.getFamily(), fd.getSize());
	}

}

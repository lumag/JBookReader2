package jbookreader.rendering.text;

import java.io.IOException;
import java.io.InputStream;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IFont;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.style.FontDescriptor;

public class TextRenderer<T> implements IGraphicDriver<T> {
	private static final int WIDTH = 80; 
	private float xPosition;
	private int realXPosition;
	private int yPosition;
	private Appendable outStream = System.out;

	public void addHorizontalSpace(float amount) {
		xPosition += amount;
	}

	public void addVerticalSpace(float amount) {
		// FIXME: really change strings
		yPosition += amount;
	}

	public void clear() {
		xPosition = 0;
		realXPosition = 0;
		yPosition = 0;
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
	
	private void fixXPosition() throws IOException {
		int xp = Math.round(xPosition);
		while (realXPosition < xp) {
			outStream.append(' ');
			realXPosition += 1;
		}
	}

	void append(String s) {
		try {
			fixXPosition();
			outStream.append(s);
			xPosition += s.length();
			realXPosition += s.length();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public IFont getFont(FontDescriptor fd) {
		return new TextFont(fd.getFamily(), fd.getSize());
	}

}

/**
 * 
 */
package jbookreader.rendering.swing;

import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JComponent;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IFont;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.style.FontDescriptor;
import lumag.util.SimpleCache;

class JGraphicDriver<T> implements IGraphicDriver<T> {
	private final JBookComponent<T> component;

	JGraphicDriver(JBookComponent<T> component) {
		this.component = component;
	}

	// TODO: move to separate layer between IGraphicDriver and IFormatEngine
	private SimpleCache<FontDescriptor, IFont> fontsCache = new SimpleCache<FontDescriptor, IFont>(
			new SimpleCache.Getter<FontDescriptor, IFont>() {

				public IFont process(FontDescriptor fd) {
					return new AWTFontAdapter(fd, JGraphicDriver.this.component.getFontRC());
				}
			});

	private float horizontalPosition;

	private float verticalPosition;

	public void addHorizontalSpace(float amount) {
		horizontalPosition += amount;
	}

	public void addVerticalSpace(float amount) {
		verticalPosition += amount;
	}

	public void clear() {
		horizontalPosition = verticalPosition = 0;
		// FIXME: maybe add renderingModel.clear()
	}

	public float getHorizontalPosition() {
		return horizontalPosition;
	}

	public float getVerticalPosition() {
		return verticalPosition;
	}

	public int getPaperWidth() {
		return component.getPaperWidth();
	}

	public int getPaperHeight() {
		return component.getPaperHeight();
	}

	public IFont getFont(FontDescriptor fd) {
		//			return new AWTFontAdapter(fd, getFontRC());
		return fontsCache.get(fd);
	}

	public IDrawable<T> renderBox(int width, int height, int depth,
			T node) {
		throw new UnsupportedOperationException("boxes aren't supported");
	}

	public IDrawable<T> renderString(final String s, final IFont font,
			T node) {
		if (this.component.getFontRC() == null) {
			throw new IllegalStateException("renderString with null frc");
		}

		return new SimpleSwingString<T>(this, s, (AWTFontAdapter) font, node);
	}

	public IDrawable<T> renderImage(String contentType,
			InputStream dataStream, T node) throws IOException {
		// throw new UnsupportedOperationException("unsupported");
		return new AWTImageAdapter<T>(this, contentType, dataStream, node);
	}

	/*
	 * These methods are here just for simplification
	 */
	FontRenderContext getFontRC() {
		return component.getFontRC();
	}
	
	Graphics2D getPaperGraphics() {
		return component.getPaperGraphics();
	}

	public JComponent getComponent() {
		return component;
	}

}

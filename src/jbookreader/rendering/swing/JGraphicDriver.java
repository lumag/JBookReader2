/**
 * 
 */
package jbookreader.rendering.swing;

import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JComponent;

import jbookreader.book.INode;
import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IFont;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.style.FontDescriptor;
import lumag.util.SimpleCache;

class JGraphicDriver implements IGraphicDriver<INode> {
	private final JBookComponent component;

	JGraphicDriver(JBookComponent component) {
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

	public IDrawable<INode> renderBox(int width, int height, int depth,
			INode node) {
		throw new UnsupportedOperationException("boxes aren't supported");
	}

	public IDrawable<INode> renderString(final String s, final IFont font,
			INode node) {
		if (this.component.getFontRC() == null) {
			throw new IllegalStateException("renderString with null frc");
		}

		return new SimpleSwingString<INode>(this, s, (AWTFontAdapter) font, node);
	}

	public IDrawable<INode> renderImage(String contentType,
			InputStream dataStream, INode node) throws IOException {
		// throw new UnsupportedOperationException("unsupported");
		return new AWTImageAdapter<INode>(this, contentType, dataStream, node);
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

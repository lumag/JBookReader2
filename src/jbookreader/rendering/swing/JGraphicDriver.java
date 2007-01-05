package jbookreader.rendering.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.JComponent;
import javax.swing.Scrollable;

import jbookreader.book.IBook;
import jbookreader.formatengine.ICompositor;
import jbookreader.formatengine.IFormatEngine;
import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IFont;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.rendering.Position;
import jbookreader.style.impl.FB2StyleStackImpl;

@SuppressWarnings("serial")
public class JGraphicDriver extends JComponent implements IGraphicDriver, Scrollable {
	
	private static final int PIXEL_SCALE_FACTOR = 100;
	
	// FIXME: these should not be exposed. perhaps I should encapsulate draw(Shape) process.
	int horizontalPosition;
	int verticalPosition;

	private IFormatEngine formatEngine;
	private ICompositor compositor;
	private IBook book;
	private List<IDrawable> lines;

	private FontRenderContext fontRC;

	private Graphics2D paperGraphics;
	
	static int pixelToDimension(float px) {
		return Math.round(px * PIXEL_SCALE_FACTOR);
	}
	
	static float dimensionToPixel(int dim) {
		return (dim * 1.0f/PIXEL_SCALE_FACTOR);
	}

	public void setCompositor(ICompositor compositor) {
		this.compositor = compositor;
	}

	public void setFormatEngine(IFormatEngine engine) {
		this.formatEngine = engine;
	}

	public void setBook(IBook book) {
		this.book = book;
		lines = null;
		clear();
	}

	Graphics2D getPaperGraphics() {
		return paperGraphics;
	}

	FontRenderContext getFontRC() {
		return fontRC;
	}

	/*
	 * IGraphicDriver methods
	 */

	public void addHorizontalSpace(int amount) {
		horizontalPosition += amount;
	}

	public void addVerticalSpace(int amount) {
		verticalPosition += amount;
	}

	public void clear() {
		horizontalPosition = verticalPosition = 0;
	}

	public AWTFontAdapter getFont(String name, int size, boolean bold, boolean italic) {
		return new AWTFontAdapter(name, size, fontRC, bold, italic);
	}

	public int getHorizontalPosition() {
		return horizontalPosition;
	}

	public int getVerticalPosition() {
		return verticalPosition;
	}

	public IDrawable renderBox(int width, int height, int depth) {
		throw new UnsupportedOperationException("boxes aren't supported");
	}

	public IDrawable renderString(final String s, final IFont font) {
		if (fontRC == null) {
			throw new IllegalStateException("renderString with null frc");
		}

		return new SimpleSwingString(this, s, (AWTFontAdapter) font);
	}
	
	public IDrawable renderImage(String contentType, InputStream dataStream) throws IOException {
		//		throw new UnsupportedOperationException("unsupported");
		Iterator<ImageReader> readers = ImageIO.getImageReadersByMIMEType(contentType);
		if (!readers.hasNext()) {
			throw new UnsupportedOperationException("content type '" + contentType + "' isn't supported");
		}
		ImageReader reader = readers.next();
		ImageInputStream stream = ImageIO.createImageInputStream(dataStream);
		if (stream == null) {
			throw new UnsupportedOperationException("Can't create image input stream");
		}
		reader.setInput(stream, true);
		BufferedImage image = reader.read(0);
//		BufferedImage image = ImageIO.read(dataStream);
		return new AWTImageAdapter(this, image);
	}

	public int getPaperWidth() {
		Insets insets = getInsets();
		return pixelToDimension(getWidth() - insets.left - insets.right);
	}

	public int getPaperHeight() {
		Insets insets = getInsets();
		return pixelToDimension(getHeight() - insets.top - insets.bottom);
	}

	/*
	 * Swing methods
	 */
	@Override
	protected void paintComponent(Graphics g) {
		System.err.println("REPAINT " + getWidth() + "x" + getHeight());
		if (isOpaque()) { // paint background
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		
		if (book == null) {
			return;
		}
		
		Insets insets = getInsets();
		int w = getWidth() - insets.left - insets.right;
		int h = getHeight() - insets.top - insets.bottom;
		System.out.println(w + "x" + h);
	
		paperGraphics = (Graphics2D) g.create(insets.left, insets.top, w, h);
		paperGraphics.setBackground(getBackground());
		paperGraphics.setColor(Color.BLACK);

		// FIXME: this should be set from config
		paperGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		paperGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		fontRC = paperGraphics.getFontRenderContext();

		if (lines == null || getPaperWidth() != 
			lines.get(0).getWidth(Position.MIDDLE)
			) {
			// FIXME: move to separate thread!
			System.err.println("formatting");
			long before = System.nanoTime();
			lines = formatEngine.format(this, compositor, book.getFirstBody(),
					new FB2StyleStackImpl());
			long after = System.nanoTime();
			System.err.println("done " + (after - before)/1000000 + "ms");
			
			int height = 0;
			for (IDrawable dr: lines) {
				// FIXME: correct inter-line value!
				height += dr.getHeight() + dr.getDepth();
			}
			setPreferredSize(new Dimension(getWidth(), Math.round(
					dimensionToPixel(height) + insets.top + insets.bottom)));
			revalidate();
			repaint();
		} else {
			int hmin = 0;
			int hmax = getPaperHeight();
			Rectangle rectangle = paperGraphics.getClipBounds();
			if (rectangle != null) {
				hmin = pixelToDimension(rectangle.y);
				hmax = pixelToDimension(rectangle.y + rectangle.height);
			}
			System.out.print("rendering ");
			horizontalPosition = verticalPosition = 0;
			for (IDrawable dr: lines) {
				if (verticalPosition + dr.getHeight() > hmin) {
					dr.draw(Position.MIDDLE);
					horizontalPosition = 0;
				}
				// FIXME: correct inter-line value!
				verticalPosition += dr.getHeight() + dr.getDepth();
				if (verticalPosition > hmax) {
					break;
				}
			}
			System.out.println("done");
			
		}

		fontRC = null;
		paperGraphics.dispose();
		paperGraphics = null;
	}

	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	public boolean getScrollableTracksViewportWidth() {
		return true;
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		// FIXME: provide real value :)
		return 10;
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		// FIXME: provide real value :)
		return 100;
	}

}

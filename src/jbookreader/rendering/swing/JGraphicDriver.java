package jbookreader.rendering.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.Scrollable;

import jbookreader.book.IBook;
import jbookreader.formatengine.ICompositor;
import jbookreader.formatengine.IFormatEngine;
import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IFont;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.rendering.Position;

@SuppressWarnings("serial")
public class JGraphicDriver extends JComponent implements IGraphicDriver, Scrollable {
	
	private static final int PIXEL_SCALE_FACTOR = 100;

	// FIXME: these should not be exposed. perhaps I should encapsulate draw(Shape) process.
	int horizontalPosition;
	int verticalPosition;

	private IFormatEngine formatEngine;
	private ICompositor compositor;
	@SuppressWarnings("unused")
	private IBook book;
	private List<IDrawable> lines;

	private FontRenderContext fontRC;

	private Graphics2D paperGraphics;
	
	public JGraphicDriver() {
		setPreferredSize(new Dimension(256, 256));
	}
	
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

	public AWTFontAdapter getFont(String name, int size) {
		return new AWTFontAdapter(name, size, fontRC);
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

		return new SwingString(this, s, font);
	}
	
	public IDrawable renderImage(InputStream dataStream) throws IOException {
		BufferedImage image = ImageIO.read(dataStream);
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

		fontRC = paperGraphics.getFontRenderContext();

		if (lines == null || getPaperWidth() != 
			lines.get(0).getWidth(Position.MIDDLE_OF_LINE)
			) {
			// FIXME: move to separate thread!
			System.err.println("formatting");
			lines = formatEngine.format(this, compositor, book.getFirstBody());
			System.err.println("done");
			
			int height = 0;
			for (IDrawable dr: lines) {
				// FIXME: correct inter-line value!
				height += dr.getHeight() + dr.getDepth();
			}
			setPreferredSize(new Dimension(getWidth(), Math.round(
					dimensionToPixel(height + insets.top + insets.bottom))));
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
					dr.draw(Position.MIDDLE_OF_LINE);
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

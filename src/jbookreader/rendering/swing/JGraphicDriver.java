package jbookreader.rendering.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import jbookreader.book.IBook;
import jbookreader.book.INode;
import jbookreader.book.IStylesheet;
import jbookreader.formatengine.ICompositor;
import jbookreader.formatengine.IFormatEngine;
import jbookreader.formatengine.IStyleConfig;
import jbookreader.formatengine.IStyleStack;
import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IFont;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.rendering.Position;
import jbookreader.style.FontDescriptor;
import lumag.util.ClassFactory;
import lumag.util.SimpleCache;

@SuppressWarnings("serial")
public class JGraphicDriver extends JComponent implements IGraphicDriver<INode>, Scrollable {
	// TODO: move to separate layer between IGraphicDriver and IFormatEngine
	private SimpleCache<FontDescriptor, IFont> fontsCache = new SimpleCache<FontDescriptor, IFont>(
			new SimpleCache.Getter<FontDescriptor, IFont>(){

				public IFont process(FontDescriptor fd) {
					return new AWTFontAdapter(fd, JGraphicDriver.this.getFontRC());
				}
			});
	
	// FIXME: these should not be exposed. perhaps I should encapsulate draw(Shape) process.
	float horizontalPosition;
	float verticalPosition;

	private IFormatEngine<INode> formatEngine;
	private Compositor<INode> compositor;
	private IBook book;
	private List<IDrawable<INode>> lines;

	private FontRenderContext fontRC;

	private Graphics2D paperGraphics;

	private IStylesheet defaultStylesheet;
	private IStylesheet formatStylesheet;

	private IStyleConfig config = new JGraphicDriverConfig(this);
	
	public void setCompositor(ICompositor<INode> compositor) {
		this.compositor = new Compositor<INode>(compositor);
	}

	public void setFormatEngine(IFormatEngine<INode> engine) {
		this.formatEngine = engine;
	}

	public void setBook(IBook book) {
		this.book = book;
		lines = null;
		clear();
	}

	public void setDefaultStylesheet(IStylesheet defaultStylesheet) {
		this.defaultStylesheet = defaultStylesheet;
	}

	public void setFormatStylesheet(IStylesheet formatStylesheet) {
		this.formatStylesheet = formatStylesheet;
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

	public void addHorizontalSpace(float amount) {
		horizontalPosition += amount;
	}

	public void addVerticalSpace(float amount) {
		verticalPosition += amount;
	}

	public void clear() {
		horizontalPosition = verticalPosition = 0;
	}

	public IFont getFont(FontDescriptor fd) {
//		return new AWTFontAdapter(fd, getFontRC());
		return fontsCache.get(fd);
	}

	public float getHorizontalPosition() {
		return horizontalPosition;
	}

	public float getVerticalPosition() {
		return verticalPosition;
	}

	public IDrawable<INode> renderBox(int width, int height, int depth, INode node) {
		throw new UnsupportedOperationException("boxes aren't supported");
	}

	public IDrawable<INode> renderString(final String s, final IFont font, INode node) {
		if (fontRC == null) {
			throw new IllegalStateException("renderString with null frc");
		}

		return new SimpleSwingString<INode>(this, s, (AWTFontAdapter) font, node);
	}
	
	public IDrawable<INode> renderImage(String contentType, InputStream dataStream, INode node) throws IOException {
		// throw new UnsupportedOperationException("unsupported");
		return new AWTImageAdapter<INode>(this, contentType, dataStream, node);
	}

	public int getPaperWidth() {
		Insets insets = getInsets();
		return getWidth() - insets.left - insets.right;
	}

	public int getPaperHeight() {
		Insets insets = getInsets();
		return getHeight() - insets.top - insets.bottom;
	}

	/*
	 * Swing methods
	 */
    @Override
	protected void paintBorder(Graphics g) {
		Rectangle rectangle = getVisibleRect();
        Border border = getBorder();
        if (border != null) {
            border.paintBorder(this, g, rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
    }
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
		
		Rectangle visible = getVisibleRect();
		Insets insets = getInsets();
		int w = visible.width - insets.left - insets.right;
		int h = visible.height - insets.top - insets.bottom;
		int offset = visible.y;
		System.out.println(w + "x" + h + "@" + offset);
	
		paperGraphics = (Graphics2D) g.create(
				visible.x + insets.left,
				visible.y + insets.top, w, h);
		paperGraphics.setBackground(getBackground());
		paperGraphics.setColor(Color.BLACK);

		// FIXME: this should be set from config
		paperGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		paperGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		paperGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		fontRC = paperGraphics.getFontRenderContext();

		if (lines == null || lines.isEmpty()
				|| getPaperWidth() != lines.get(0).getWidth(Position.MIDDLE)
					) {

			reformatBook();

			float height = 0;
			for (IDrawable<?> dr: lines) {
				// FIXME: correct inter-line value!
				height += dr.getHeight() + dr.getDepth();
			}

			System.out.println(height + insets.top + insets.bottom);
			setPreferredSize(new Dimension(getWidth(), Math.round(
					height + insets.top + insets.bottom)));
			revalidate();
			repaint();
		} else {
			int hmin = 0;
			int hmax = getPaperHeight();
			Rectangle rectangle = paperGraphics.getClipBounds();
			if (rectangle != null) {
				hmin = rectangle.y;
				hmax = rectangle.y + rectangle.height;
			}
			System.out.print("rendering from " + hmin + " to " + hmax + "... ");
			horizontalPosition = 0;
			verticalPosition = - offset;
			long before = System.nanoTime();
			for (IDrawable<INode> dr: lines) {
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
			long after = System.nanoTime();
			System.err.println("done " + (after - before)/1000000 + "ms");
			
		}

		fontRC = null;
		paperGraphics.dispose();
		paperGraphics = null;
	}

	private void reformatBook() {
		// FIXME: move to separate thread!
		System.err.println("formatting");
		IStyleStack<INode> styleStack = createStyleStack();
		styleStack.setConfig(config);
		if (defaultStylesheet != null ) {
			styleStack.addStylesheet(defaultStylesheet);
		}
		if (formatStylesheet != null ) {
			styleStack.addStylesheet(formatStylesheet);
		}
		IStylesheet bookStylesheet = book.getStylesheet();
		if (bookStylesheet != null) {
			styleStack.addStylesheet(bookStylesheet);
		}
		compositor.clearTotal();
		long before = System.nanoTime();
		lines = formatEngine.format(this, compositor, book.getFirstBody(),
				styleStack);
		long after = System.nanoTime();
		long format = (after - before)/1000000;
		long compose = compositor.getTotal();
		System.err.println("done " + format + "ms (" + compose+ "ms for composition)");
		
	}

	@SuppressWarnings("unchecked")
	private IStyleStack<INode> createStyleStack() {
		return ClassFactory.createClass(IStyleStack.class, "jbookreader.stylestack");
	}

	private int findNextHeight(float height, int direction) {
		float h = height;
		if (direction > 0) {
			for (IDrawable<?> dr: lines) {
				// FIXME: correct inter-line value!
				h -= dr.getHeight() + dr.getDepth();
				float move = - h;
				if (move > 1) {
					return Math.round(move);
				}
			}
		} else {
			for (IDrawable<?> dr: lines) {
				// FIXME: correct inter-line value!
				if (h > dr.getHeight() + dr.getDepth() + 1) {
					h -= dr.getHeight() + dr.getDepth();
				} else {
					float move = h; 
					return Math.round(move);
				}
			}
		}
		return 0;
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
		if (lines == null || lines.isEmpty() || orientation == SwingConstants.HORIZONTAL) {
			return 0;
		}

		float height = visibleRect.y;
		return findNextHeight(height, direction);
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		final int pageSize = 100;
		if (lines == null || lines.isEmpty() || orientation == SwingConstants.HORIZONTAL) {
			return 0;
		}

		float height = visibleRect.y;
		if (direction > 0) {
			height += pageSize;
		} else {
			height -= pageSize;
		}
		return pageSize + findNextHeight(height, direction);
	}

}

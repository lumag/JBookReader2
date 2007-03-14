package jbookreader.rendering.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;

import javax.swing.JComponent;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class JBookComponent extends JComponent implements Scrollable {
	
	private JGraphicDriver driver = new JGraphicDriver(this);
	
	private IRenderingModel renderingModel;

	private FontRenderContext fontRC;

	private Graphics2D paperGraphics;

	Graphics2D getPaperGraphics() {
		return paperGraphics;
	}

	FontRenderContext getFontRC() {
		return fontRC;
	}
	
	int getPaperWidth() {
		Insets insets = getInsets();
		return getWidth() - insets.left - insets.right;
	}

	int getPaperHeight() {
		Insets insets = getInsets();
		return getHeight() - insets.top - insets.bottom;
	}

	public void setRenderingModel(IRenderingModel model) {
		this.renderingModel = model;
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

		float height = renderingModel.getHeight(this.driver);
		int prefHeight = Math.round(height + insets.top + insets.bottom);
		
		if (getPreferredSize().height != prefHeight) {
			setPreferredSize(new Dimension(getWidth(), prefHeight));
			revalidate();
			repaint();
		} else {
			int hmin = 0;
			int hmax = driver.getPaperHeight();
			Rectangle rectangle = paperGraphics.getClipBounds();
			if (rectangle != null) {
				hmin = rectangle.y;
				hmax = rectangle.y + rectangle.height;
			}

			renderingModel.render(driver, hmin, hmax, offset);
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
		if (orientation == SwingConstants.HORIZONTAL) {
			return 0;
		}

		float height = visibleRect.y;
		return renderingModel.findNextHeight(height, direction);
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		if (orientation == SwingConstants.HORIZONTAL) {
			return 0;
		}

		final int pageSize = 100;

		float height = visibleRect.y;
		if (direction > 0) {
			height += pageSize;
		} else {
			height -= pageSize;
		}
		return pageSize + renderingModel.findNextHeight(height, direction);
	}

}

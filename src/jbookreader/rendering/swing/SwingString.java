package jbookreader.rendering.swing;

import java.awt.Graphics2D;
import java.awt.font.TextLayout;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.Position;

final class SwingString implements IDrawable {
	private final JGraphicDriver driver;

	private final TextLayout layout;

	private final int depth;

	private final int height;

	private final int width;

	SwingString(JGraphicDriver driver, String s, AWTFontAdapter font) {
		this.driver = driver;
		layout = new TextLayout(s, font.getFont(), this.driver.getFontRC());

		depth = JGraphicDriver.pixelToDimension(layout.getDescent());
		height = JGraphicDriver.pixelToDimension(layout.getAscent());
		width = JGraphicDriver.pixelToDimension(layout.getAdvance());
	}

	public void draw(Position position) {
		Graphics2D g2d = this.driver.getPaperGraphics();
		if (g2d == null) {
			throw new IllegalStateException("draw with g2d = null");
		}
		layout.draw(g2d,
				JGraphicDriver.dimensionToPixel(this.driver.horizontalPosition),
				JGraphicDriver.dimensionToPixel(this.driver.verticalPosition + height));
		this.driver.horizontalPosition += width;
	}

	public int getDepth() {
		return depth;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth(Position position) {
		return width;
	}

}

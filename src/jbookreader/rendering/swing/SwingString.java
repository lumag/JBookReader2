package jbookreader.rendering.swing;

import java.awt.Graphics2D;
import java.awt.font.TextLayout;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.Position;

final class SwingString implements IDrawable {
	private final JGraphicDriver driver;

	private final TextLayout layout;

	private final float depth;

	private final float height;

	private final float width;

	SwingString(JGraphicDriver driver, String s, AWTFontAdapter font) {
		this.driver = driver;
		layout = new TextLayout(s, font.getFont(), this.driver.getFontRC());

		depth = layout.getDescent();
		height = layout.getAscent();
		width = layout.getAdvance();
	}

	public void draw(Position position) {
		Graphics2D g2d = this.driver.getPaperGraphics();
		if (g2d == null) {
			throw new IllegalStateException("draw with g2d = null");
		}
		layout.draw(g2d,
				this.driver.horizontalPosition,
				this.driver.verticalPosition + height);
		this.driver.horizontalPosition += width;
	}

	public float getDepth() {
		return depth;
	}

	public float getHeight() {
		return height;
	}

	public float getWidth(Position position) {
		return width;
	}

}

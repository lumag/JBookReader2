package jbookreader.rendering.swing;

import java.awt.Graphics2D;
import java.awt.font.TextLayout;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.Position;

final class SwingString<T> implements IDrawable<T> {
	private final JGraphicDriver<T> driver;

	private final TextLayout layout;

	private final float depth;

	private final float height;

	private final float width;

	private final T context;

	SwingString(final JGraphicDriver<T> driver, final String s, final AWTFontAdapter font, final T context) {
		this.driver = driver;
		this.context = context;

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
				this.driver.getHorizontalPosition(),
				this.driver.getVerticalPosition() + height);
		this.driver.addHorizontalSpace(width);
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

	public T getContext() {
		return context;
	}

}

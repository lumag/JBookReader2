package jbookreader.rendering.swing;

import java.awt.Graphics2D;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.Position;

final class SimpleSwingString implements IDrawable {

	private final JGraphicDriver driver;
	private final String string;
	private final AWTFontAdapter font;
	private float height;
	private float depth;
	private float width;

	SimpleSwingString(JGraphicDriver driver, String string, AWTFontAdapter font) {
		this.driver = driver;
		this.string = string;
		this.font = font;

		LineMetrics metrics = font.getFont().getLineMetrics(string, driver.getFontRC());
		Rectangle2D bounds = font.getFont().getStringBounds(string, driver.getFontRC());
//		System.out.println(bounds);
		height = metrics.getAscent();
		depth = metrics.getDescent();

		// There are some problems if we use bounds for height/depth:
		//     if the first char is some type of long dash \u2013, then we get incorrect
		//     measurements (the base line is shifted)
//		height = (float) bounds.getHeight();
//		depth = (float) (bounds.getHeight() + bounds.getY());

		width = (float) bounds.getWidth();
	}

	public void draw(Position position) {
		Graphics2D graphics = driver.getPaperGraphics();
		if (!graphics.getFont().equals(font.getFont())) {
			graphics.setFont(font.getFont());
		}
//		System.out.println(width + " x " + height + " + " + depth);
		graphics.drawString(string,
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

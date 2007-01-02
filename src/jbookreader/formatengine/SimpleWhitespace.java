package jbookreader.formatengine;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.rendering.Position;

public class SimpleWhitespace implements IDrawable {
	private final IGraphicDriver driver;
	private final int space;

	SimpleWhitespace(final IGraphicDriver driver, final int space) {
		this.driver = driver;
		this.space = space;
	}

	public void draw(Position position) {
		driver.addHorizontalSpace(space);
	}

	public int getDepth() {
		return 0;
	}

	public int getHeight() {
		return 0;
	}

	public int getWidth(Position position) {
		return space;
	}

	@Override
	public String toString() {
		return "Space: " + space;
	}
}

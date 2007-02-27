package jbookreader.formatengine.impl;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.rendering.Position;

class SimpleWhitespace implements IDrawable {
	private final IGraphicDriver driver;
	private final float space;

	SimpleWhitespace(final IGraphicDriver driver, final float space) {
		this.driver = driver;
		this.space = space;
	}

	public void draw(Position position) {
		driver.addHorizontalSpace(space);
	}

	public float getDepth() {
		return 0;
	}

	public float getHeight() {
		return 0;
	}

	public float getWidth(Position position) {
		return space;
	}

	@Override
	public String toString() {
		return "Space: " + space;
	}
}

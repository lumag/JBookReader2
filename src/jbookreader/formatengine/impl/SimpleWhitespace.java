package jbookreader.formatengine.impl;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.rendering.Position;

class SimpleWhitespace<T> implements IDrawable<T> {
	private final IGraphicDriver<?> driver;
	private final float space;
	private final T context;

	SimpleWhitespace(final IGraphicDriver<?> driver, final float space, final T context) {
		this.driver = driver;
		this.space = space;
		this.context = context;
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
	
	public T getContext() {
		return context;
	}

	@Override
	public String toString() {
		return "Space: " + space;
	}
}

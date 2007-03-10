package jbookreader.formatengine.impl;

import jbookreader.formatengine.IAdjustableDrawable;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.rendering.Position;

class Glue<T> implements IAdjustableDrawable<T> {
	private final float space;
	private final float stretch;
	private final float shrink;
	private final IGraphicDriver<?> driver;
	private float adjustment;
	private final T context;

	Glue(final IGraphicDriver<?> driver, final float space, final float stretch, final float shrink, final T context) {
		this.driver = driver;
		this.space = space;
		this.stretch = stretch;
		this.shrink = shrink;
		this.context = context;
	}
	public float getShrink(Position position) {
		if (position != Position.MIDDLE) {
			return 0;
		}
		return stretch;
	}
	public float getWidth(Position position) {
		if (position != Position.MIDDLE) {
			return 0;
		}
		return space + adjustment;
	}
	public float getStretch(Position position) {
		if (position != Position.MIDDLE) {
			return 0;
		}
		return stretch;
	}
	public float getDepth() {
		return 0;
	}
	public float getHeight() {
		return 0;
	}

	public void draw(Position position) {
		driver.addHorizontalSpace(getWidth(position));
	}
	
	@Override
	public String toString() {
		return "Glue: " + space + "+" + adjustment + ":" + stretch + ":" + shrink;
	}

	public void adjust(float adjust) {
		this.adjustment = adjust;
	}

	public T getContext() {
		return context;
	}

}

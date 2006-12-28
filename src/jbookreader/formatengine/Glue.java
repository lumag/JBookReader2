package jbookreader.formatengine;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.IGraphicDriver;
import jbookreader.rendering.Position;

class Glue implements IDrawable {
	private final int space;
	private final int stretch;
	private final int shrink;
	private final IGraphicDriver driver;
	private int adjustment;

	Glue(final IGraphicDriver driver, final int space, final int stretch, final int shrink) {
		this.driver = driver;
		this.space = space;
		this.stretch = stretch;
		this.shrink = shrink;
	}
	public int getShrink(Position position) {
		if (position != Position.MIDDLE_OF_LINE) {
			return 0;
		}
		return stretch;
	}
	public int getWidth(Position position) {
		if (position != Position.MIDDLE_OF_LINE) {
			return 0;
		}
		return space + adjustment;
	}
	public int getStretch(Position position) {
		if (position != Position.MIDDLE_OF_LINE) {
			return 0;
		}
		return stretch;
	}
	public int getDepth() {
		return 0;
	}
	public int getHeight() {
		return 0;
	}

	public void draw(Position position) {
		driver.addHorizontalSpace(getWidth(position));
	}
	
	@Override
	public String toString() {
		return "Glue: " + space + "+" + adjustment + ":" + stretch + ":" + shrink;
	}

	public void adjustWidth(int adjust) {
		this.adjustment = adjust;
	}
}

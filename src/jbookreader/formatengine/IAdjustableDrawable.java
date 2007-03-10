package jbookreader.formatengine;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.Position;

public interface IAdjustableDrawable<T> extends IDrawable<T> {

	float getShrink(Position position);

	float getStretch(Position position);

	void adjust(float adjust);

}
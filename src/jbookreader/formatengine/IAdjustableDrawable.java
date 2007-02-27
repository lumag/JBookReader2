package jbookreader.formatengine;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.Position;

public interface IAdjustableDrawable extends IDrawable {

	float getShrink(Position position);

	float getStretch(Position position);

	void adjust(float adjust);

}
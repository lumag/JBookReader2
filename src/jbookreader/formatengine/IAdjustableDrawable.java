package jbookreader.formatengine;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.Position;

interface IAdjustableDrawable extends IDrawable {

	int getShrink(Position position);

	int getStretch(Position position);

	void adjust(int adjust);

}
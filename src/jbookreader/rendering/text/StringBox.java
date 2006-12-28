/**
 * 
 */
package jbookreader.rendering.text;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.Position;

class StringBox implements IDrawable {
	/**
	 * 
	 */
	private final TextRenderer renderer;
	private final String string;

	StringBox(TextRenderer renderer, final String string) {
		this.renderer = renderer;
		this.string = string;
	}

	public void draw(Position position) {
		this.renderer.append(string);
	}

	public int getDepth() {
		return 0;
	}

	public int getHeight() {
		return 1;
	}

	public int getWidth(Position position) {
		return string.length();
	}

	public int getShrink(Position position) {
		return 0;
	}

	public int getStretch(Position position) {
		return 0;
	}
	
	public void adjustWidth(int adjust) {
		throw new UnsupportedOperationException("Width adjustment not supported");
	}

	@Override
	public String toString() {
		return "'" + string +"'";
	}
}
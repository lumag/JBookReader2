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

	public float getDepth() {
		return 0;
	}

	public float getHeight() {
		return 1;
	}

	public float getWidth(Position position) {
		return string.length();
	}

	@Override
	public String toString() {
		return "'" + string +"'";
	}
}
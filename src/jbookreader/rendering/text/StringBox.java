/**
 * 
 */
package jbookreader.rendering.text;

import jbookreader.rendering.IDrawable;
import jbookreader.rendering.Position;

class StringBox<T> implements IDrawable<T> {
	/**
	 * 
	 */
	private final TextRenderer<T> renderer;
	private final String string;
	private final T context;

	StringBox(final TextRenderer<T> renderer, final String string, final T context) {
		this.renderer = renderer;
		this.string = string;
		this.context = context;
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

	public T getContext() {
		return context;
	}

	@Override
	public String toString() {
		return "'" + string +"'";
	}
}
/**
 * 
 */
package jbookreader.rendering.text;

import jbookreader.rendering.IFont;

class TextFont implements IFont {
	private final String name;
	private final int size;

	public TextFont(String name, int size) {
		this.name = name;
		this.size = size;
	}

	public float getSpaceWidth() {
		return 1;
	}

	public String getFamily() {
		return name;
	}

	public int getSize() {
		return size;
	}

	public boolean isBold() {
		return false;
	}

	public boolean isItalic() {
		return false;
	}
}
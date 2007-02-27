package jbookreader.rendering.swing;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import jbookreader.rendering.IFont;

class AWTFontAdapter implements IFont {
	private Font font;
	private float spaceWidth;

	AWTFontAdapter(String name, int size, FontRenderContext frc, boolean bold, boolean italic) {
		int style = Font.PLAIN;
		if (bold) {
			style |= Font.BOLD;
		}
		if (italic) {
			style |= Font.ITALIC;
		}
		font = new Font(name, style, size);
		Rectangle2D r2d = font.createGlyphVector(frc, new char[]{' '}).getLogicalBounds();
		spaceWidth = (float) (r2d.getMaxX() - r2d.getMinX());
	}
	
	Font getFont() {
		return font;
	}

	public float getSpaceWidth() {
		return spaceWidth;
	}

	public String getFamily() {
		return font.getFamily();
	}

	public int getSize() {
		return font.getSize();
	}

	public boolean isBold() {
		return font.isBold();
	}

	public boolean isItalic() {
		return font.isItalic();
	}

}

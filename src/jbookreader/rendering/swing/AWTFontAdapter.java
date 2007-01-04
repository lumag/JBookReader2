package jbookreader.rendering.swing;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import jbookreader.rendering.IFont;

class AWTFontAdapter implements IFont {
	private Font font;
	private int spaceWidth;

	AWTFontAdapter(String name, int size, FontRenderContext frc) {
		font = new Font(name, Font.ITALIC, size);
		Rectangle2D r2d = font.createGlyphVector(frc, new char[]{' '}).getLogicalBounds();
		spaceWidth = JGraphicDriver.pixelToDimension((float) (r2d.getMaxX() - r2d.getMinX()));
	}
	
	Font getFont() {
		return font;
	}

	public int getSpaceWidth() {
		return spaceWidth;
	}

	public String getFamily() {
		return font.getFamily();
	}

	public int getSize() {
		return font.getSize();
	}

}

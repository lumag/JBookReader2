package jbookreader.rendering.swing;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import jbookreader.rendering.IFont;
import jbookreader.style.FontDescriptor;

class AWTFontAdapter implements IFont {
	private final Font font;
	private final float spaceWidth;

	AWTFontAdapter(FontDescriptor fd, FontRenderContext frc) {
		if (frc == null) {
			throw new NullPointerException("Null FRC passed");
		}
		int style = Font.PLAIN;
		if (fd.isBold()) {
			style |= Font.BOLD;
		}
		if (fd.isItalic()) {
			style |= Font.ITALIC;
		}
		font = new Font(fd.getFamily(), style, fd.getSize());
		System.out.println(font);
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

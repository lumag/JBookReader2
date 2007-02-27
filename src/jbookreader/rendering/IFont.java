package jbookreader.rendering;

public interface IFont {
	float getSpaceWidth();

	String getFamily();
	int getSize();
	boolean isItalic();
	boolean isBold();
}

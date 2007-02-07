package jbookreader.rendering;

public interface IFont {
	int getSpaceWidth();

	String getFamily();
	int getSize();
	boolean isItalic();
	boolean isBold();
}

package jbookreader.style;

public enum FontSize {
	XX_SMALL(3f/5),
	X_SMALL(3f/4),
	SMALL(8f/9),
	MEDIUM(1f),
	LARGE(6f/5),
	X_LARGE(3f/2),
	XX_LARGE(2f/1);
	
	private final float scale;

	FontSize(final float scale) {
		this.scale = scale; 
	}

	public float getScale() {
		return scale;
	}
}

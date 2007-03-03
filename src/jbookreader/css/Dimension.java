package jbookreader.css;

import jbookreader.style.IDimension;

class Dimension implements IDimension {

	private final float value;
	private final String unit;

	Dimension(final float value, final String unit) {
		this.value = value;
		this.unit = unit;
	}

	public String getUnit() {
		return unit;
	}

	public float getValue() {
		return value;
	}

	@Override
	public String toString() {
		return Float.toString(value) + unit;
	}

}

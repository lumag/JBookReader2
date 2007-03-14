package jbookreader.style.impl;

import java.util.HashMap;
import java.util.Map;

import jbookreader.style.IDimension;
import jbookreader.style.IDimensionConvertor;

class DimensionConvertor implements IDimensionConvertor {
	private static class SimpleConvertor implements IDimensionConvertor {
		private final float scale;

		protected SimpleConvertor(final float scale) {
			this.scale = scale;
		}

		public float convert(IDimension dimension) {
			return dimension.getValue() * scale;
		}
		
	}
	private Map<String, IDimensionConvertor> convertors = new HashMap<String, IDimensionConvertor>();
	
	public DimensionConvertor() {
		convertors.put("px", new SimpleConvertor(1));
	}

	public float convert(IDimension dimension) {
		IDimensionConvertor conv = convertors.get(dimension.getUnit());
		if (conv == null) {
			throw new IllegalArgumentException("Uknown unit passed (" + dimension.getUnit()+ "): " + dimension);
		}
		return conv.convert(dimension);
	}

}

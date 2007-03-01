package jbookreader.style;

public class FontDescriptor {
	private final String family;
	private final int size;
	private final boolean bold;
	private final boolean italic;
	
	public FontDescriptor(final String family, final int size, final boolean bold, final boolean italic) {
		this.family = family;
		this.size = size;
		this.bold = bold;
		this.italic = italic;
	}

	public String getFamily() {
		return family;
	}

	public int getSize() {
		return size;
	}

	public boolean isBold() {
		return bold;
	}

	public boolean isItalic() {
		return italic;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FontDescriptor) {
			FontDescriptor fd = (FontDescriptor) obj;
			return family.equals(fd.family) &&
					size == fd.size &&
					bold == fd.bold &&
					italic == fd.italic;
				
		}
		return false;
	}

	@Override
	public int hashCode() {
		return family.hashCode() ^
				(size << 3) ^
				((bold?1:0) << 10) ^
				((italic?1:0) << 15);
	}
}

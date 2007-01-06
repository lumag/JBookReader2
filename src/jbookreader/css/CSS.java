package jbookreader.css;

public class CSS {

	/**
	 * CSS 1
	 */
	public static long getWeight(int a, int b, int c) {
		return getWeight(0, a, b, c);
	}

	/**
	 * CSS 2
	 */
	public static long getWeight(int a, int b, int c, int d) {
		return a << 24 + b << 16 + c << 8 + d;
	}

}

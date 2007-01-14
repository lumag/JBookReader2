package jbookreader.style;


public interface IStyleRule {
	long getWeight();
	StyleAttribute getAttribute();
	// FIXME: change from string to specific class
	String getValue();
}

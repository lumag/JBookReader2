package jbookreader.style;


public interface IStyleRule {
	long getWeight();
	StyleAttribute getAttribute();
	ERuleValueType getValueType();
	<T> T getValue(Class<T> klass) throws ClassCastException;
}

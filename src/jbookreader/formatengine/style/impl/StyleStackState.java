package jbookreader.formatengine.style.impl;

import java.util.ArrayList;
import java.util.List;

import jbookreader.style.Alignment;
import jbookreader.style.Display;
import jbookreader.style.FontStyle;

class StyleStackState implements Cloneable {
	String nodeTag;
	String nodeClass;

	Display display;
	Alignment textAlignment;
	List<String> fontFamily;
	int fontSize;
	int fontWeight;
	FontStyle fontStyle;
	
	StyleStackState() {
		display = Display.BLOCK;
		textAlignment = Alignment.JUSTIFY;
		fontFamily = new ArrayList<String>();
		fontFamily.add("Serif");
		fontSize = 12;
		fontWeight = 400;
		fontStyle = FontStyle.NORMAL;
	}
	
	@Override
	public StyleStackState clone() {
		try {
			return (StyleStackState) super.clone();
		} catch (CloneNotSupportedException e) {
			// We support cloning!
			throw new InternalError(e.toString());
		}
	}
}

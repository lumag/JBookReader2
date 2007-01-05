package jbookreader.style.impl;

import java.util.ArrayList;
import java.util.List;

import jbookreader.style.Alignment;
import jbookreader.style.Display;

class StyleStackState implements Cloneable {
	String nodeTag;
	String nodeClass;

	Display display;
	Alignment textAlignment;
	List<String> fontFamily;
	int fontSize;
	
	StyleStackState() {
		display = Display.BLOCK;
		textAlignment = Alignment.JUSTIFY;
		fontFamily = new ArrayList<String>();
		fontFamily.add("serif");
		fontSize = 10;
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

package jbookreader.formatengine.style.impl;

import jbookreader.style.IStyleRule;
import jbookreader.style.StyleAttribute;

public interface IStyleValueComputer {
	Object compute(StyleAttribute attribute, IStyleRule rule, StyleStackState oldState);
}

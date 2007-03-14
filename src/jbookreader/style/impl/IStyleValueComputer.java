package jbookreader.style.impl;

import jbookreader.style.IStyleConfig;
import jbookreader.style.IStyleRule;
import jbookreader.style.StyleAttribute;

interface IStyleValueComputer {
	Object compute(StyleAttribute attribute, IStyleRule rule, StyleStackState oldState, IStyleConfig config);
}

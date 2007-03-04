package jbookreader.formatengine.style.impl;

import jbookreader.formatengine.IStyleConfig;
import jbookreader.style.IStyleRule;
import jbookreader.style.StyleAttribute;

interface IStyleValueComputer {
	Object compute(StyleAttribute attribute, IStyleRule rule, StyleStackState oldState, IStyleConfig config);
}

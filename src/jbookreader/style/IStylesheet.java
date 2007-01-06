package jbookreader.style;

import java.util.Collection;
import java.util.Map;

public interface IStylesheet {
	Collection<IStyleSelector> getSelectors();
	Map<StyleAttribute, String> getRules(IStyleSelector selector);
}

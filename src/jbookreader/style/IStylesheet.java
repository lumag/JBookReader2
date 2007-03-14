package jbookreader.style;

import java.util.List;

public interface IStylesheet<T> {
	List<IStyleRule> getApplicableRules(T context);
}

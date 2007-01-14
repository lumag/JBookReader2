package jbookreader.book;

import java.util.List;

import jbookreader.style.IStyleRule;


public interface IStylesheet {
	List<IStyleRule> getApplicableRules(INode node);
}

package jbookreader.css.selectors;

import jbookreader.book.INode;
import jbookreader.css.IStyleSelector;

import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.SimpleSelector;

public class ConditionalSelectorImpl implements ConditionalSelector,
		IStyleSelector {

	private final SimpleSelector selector;
	private final Condition condition;

	public ConditionalSelectorImpl(SimpleSelector selector, Condition condition) {
		this.selector = selector;
		this.condition = condition;
	}

	public Condition getCondition() {
		return condition;
	}

	public SimpleSelector getSimpleSelector() {
		return selector;
	}

	public short getSelectorType() {
		return SAC_CONDITIONAL_SELECTOR;
	}

	public boolean applies(INode node) {
		return ((IStyleSelector) condition).applies(node)
			&& ((IStyleSelector) selector).applies(node);
	}

	public long getWeight() {
		return ((IStyleSelector) condition).getWeight()
			 + ((IStyleSelector) selector).getWeight();
	}

}

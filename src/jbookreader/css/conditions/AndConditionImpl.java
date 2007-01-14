package jbookreader.css.conditions;

import jbookreader.book.INode;
import jbookreader.css.IStyleSelector;

import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;

class AndConditionImpl implements CombinatorCondition, IStyleSelector {

	private final Condition first;
	private final Condition second;

	AndConditionImpl(Condition first, Condition second) {
		this.first = first;
		this.second = second;
	}

	public Condition getFirstCondition() {
		return first;
	}

	public Condition getSecondCondition() {
		return second;
	}

	public short getConditionType() {
		return SAC_AND_CONDITION;
	}

	public boolean applies(INode node) {
		return ((IStyleSelector) first).applies(node)
			&& ((IStyleSelector) second).applies(node);
	}

	public long getWeight() {
		return ((IStyleSelector) first).getWeight()
			 + ((IStyleSelector) second).getWeight();
	}

}

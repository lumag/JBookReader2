package jbookreader.css.selectors;

import jbookreader.book.INode;
import jbookreader.css.IStyleSelector;

import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SimpleSelector;

public class DescendantSelectorImpl implements DescendantSelector,
		IStyleSelector {

	private final Selector parent;
	private final SimpleSelector descendant;

	public DescendantSelectorImpl(Selector parent, SimpleSelector descendant) {
		this.parent = parent;
		// TODO Auto-generated constructor stub
		this.descendant = descendant;
	}

	public Selector getAncestorSelector() {
		return parent;
	}

	public SimpleSelector getSimpleSelector() {
		return descendant;
	}

	public short getSelectorType() {
		return SAC_DESCENDANT_SELECTOR;
	}

	public boolean applies(INode node) {
		if (((IStyleSelector) descendant).applies(node)) {
			for (INode ancestorNode = node.getParentNode();
				ancestorNode != null;
				ancestorNode = ancestorNode.getParentNode()) {
				if (((IStyleSelector) parent).applies(ancestorNode)) {
					return true;
				}
			}
		}
		return false;
	}

	public long getWeight() {
		return ((IStyleSelector) descendant).getWeight()
			 + ((IStyleSelector) parent).getWeight(); 
	}

}

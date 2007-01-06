package jbookreader.css.selectors;

import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CharacterDataSelector;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DescendantSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.NegativeSelector;
import org.w3c.css.sac.ProcessingInstructionSelector;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorFactory;
import org.w3c.css.sac.SiblingSelector;
import org.w3c.css.sac.SimpleSelector;

public class CSSSelectorFactory implements SelectorFactory {

	public SimpleSelector createAnyNodeSelector() throws CSSException {
		throw new CSSException("selector unsupported");
	}

	public CharacterDataSelector createCDataSectionSelector(String data)
			throws CSSException {
		throw new CSSException("selector unsupported");
	}

	public DescendantSelector createChildSelector(Selector parent,
			SimpleSelector child) throws CSSException {
		throw new CSSException("selector unsupported");
	}

	public CharacterDataSelector createCommentSelector(String data)
			throws CSSException {
		throw new CSSException("selector unsupported");
	}

	public ConditionalSelector createConditionalSelector(
			SimpleSelector selector, Condition condition) throws CSSException {
		return new ConditionalSelectorImpl(selector, condition);
	}

	public DescendantSelector createDescendantSelector(Selector parent,
			SimpleSelector descendant) throws CSSException {
		return new DescendantSelectorImpl(parent, descendant);
	}

	public SiblingSelector createDirectAdjacentSelector(short nodeType,
			Selector child, SimpleSelector directAdjacent) throws CSSException {
		throw new CSSException("selector unsupported");
	}

	public ElementSelector createElementSelector(String namespaceURI,
			String tagName) throws CSSException {
		return new ElementSelectorImpl(namespaceURI, tagName);
	}

	public NegativeSelector createNegativeSelector(SimpleSelector selector)
			throws CSSException {
		throw new CSSException("selector unsupported");
	}

	public ProcessingInstructionSelector createProcessingInstructionSelector(
			String target, String data) throws CSSException {
		throw new CSSException("selector unsupported");
	}

	public ElementSelector createPseudoElementSelector(String namespaceURI,
			String pseudoName) throws CSSException {
		throw new CSSException("selector unsupported");
	}

	public SimpleSelector createRootNodeSelector() throws CSSException {
		throw new CSSException("selector unsupported");
	}

	public CharacterDataSelector createTextNodeSelector(String data)
			throws CSSException {
		throw new CSSException("selector unsupported");
	}

}

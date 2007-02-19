package jbookreader.css.selectors;

import jbookreader.book.INamedNode;
import jbookreader.book.INode;
import jbookreader.css.CSSParser;
import jbookreader.css.IStyleSelector;

import org.w3c.css.sac.ElementSelector;

public class ElementSelectorImpl implements ElementSelector,
		IStyleSelector {

	private final String namespaceURI;
	private final String name;

	public ElementSelectorImpl(String namespaceURI, String tagName) {
		this.namespaceURI = namespaceURI;
		this.name = tagName;
	}

	public String getLocalName() {
		return name;
	}

	public String getNamespaceURI() {
		return namespaceURI;
	}

	public short getSelectorType() {
		return SAC_ELEMENT_NODE_SELECTOR;
	}

	public boolean applies(INode node) {
		if (node instanceof INamedNode) {
			INamedNode namedNode = (INamedNode) node;
			// FIXME: namespace
			if (name == null) {
				// any node
				return true;
			}
			return name.equals(namedNode.getNodeTag());
		}
		return false;
	}

	public long getWeight() {
		return CSSParser.getWeight(0,0,0,1);
	}
}

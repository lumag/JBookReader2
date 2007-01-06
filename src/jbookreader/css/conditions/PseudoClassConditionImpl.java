package jbookreader.css.conditions;

import jbookreader.book.INode;
import jbookreader.css.CSS;
import jbookreader.style.IStyleSelector;

import org.w3c.css.sac.AttributeCondition;

class PseudoClassConditionImpl implements AttributeCondition,
		IStyleSelector {

	private final String namespaceURI;
	private final String value;

	PseudoClassConditionImpl(String namespaceURI, String value) {
		this.namespaceURI = namespaceURI;
		this.value = value;
	}

	public String getLocalName() {
		return null;
	}

	public String getNamespaceURI() {
		return namespaceURI;
	}

	public boolean getSpecified() {
		return true;
	}

	public String getValue() {
		return value;
	}

	public short getConditionType() {
		return SAC_PSEUDO_CLASS_CONDITION;
	}

	public boolean applies(INode node) {
		// TODO Auto-generated method stub
		return false;
	}

	public long getWeight() {
		return CSS.getWeight(0,0,1,0);
	}

}

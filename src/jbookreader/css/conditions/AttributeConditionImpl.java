package jbookreader.css.conditions;

import jbookreader.book.INode;
import jbookreader.css.CSS;
import jbookreader.style.IStyleSelector;

import org.w3c.css.sac.AttributeCondition;

class AttributeConditionImpl implements AttributeCondition,
		IStyleSelector {

	private final String localName;
	private final String namespaceURI;
	private final boolean specified;
	private final String value;

	AttributeConditionImpl(String localName, String namespaceURI, boolean specified, String value) {
		this.localName = localName;
		this.namespaceURI = namespaceURI;
		this.specified = specified;
		this.value = value;
	}

	public String getLocalName() {
		return localName;
	}

	public String getNamespaceURI() {
		return namespaceURI;
	}

	public boolean getSpecified() {
		return specified;
	}

	public String getValue() {
		return value;
	}

	public short getConditionType() {
		return SAC_ATTRIBUTE_CONDITION;
	}

	public boolean applies(INode node) {
		// TODO Auto-generated method stub
		return false;
	}

	public long getWeight() {
		return CSS.getWeight(0, 0, 1, 0);
	}

}

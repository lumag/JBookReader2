package jbookreader.css.conditions;

import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionFactory;
import org.w3c.css.sac.ContentCondition;
import org.w3c.css.sac.LangCondition;
import org.w3c.css.sac.NegativeCondition;
import org.w3c.css.sac.PositionalCondition;

public class CSSConditionFactory implements ConditionFactory {

	public CombinatorCondition createAndCondition(Condition first,
			Condition second) throws CSSException {
		return new AndConditionImpl(first, second);
	}

	public AttributeCondition createAttributeCondition(String localName,
			String namespaceURI, boolean specified, String value)
			throws CSSException {
		return new AttributeConditionImpl(localName, namespaceURI, specified, value);
	}

	public AttributeCondition createBeginHyphenAttributeCondition(
			String localName, String namespaceURI, boolean specified,
			String value) throws CSSException {
		throw new CSSException("condition unsupported");
	}

	public AttributeCondition createClassCondition(String namespaceURI,
			String value) throws CSSException {
		throw new CSSException("condition unsupported");
	}

	public ContentCondition createContentCondition(String data)
			throws CSSException {
		throw new CSSException("condition unsupported");
	}

	public AttributeCondition createIdCondition(String value)
			throws CSSException {
		throw new CSSException("condition unsupported");
	}

	public LangCondition createLangCondition(String lang) throws CSSException {
		throw new CSSException("condition unsupported");
	}

	public NegativeCondition createNegativeCondition(Condition condition)
			throws CSSException {
		throw new CSSException("condition unsupported");
	}

	public AttributeCondition createOneOfAttributeCondition(String localName,
			String namespaceURI, boolean specified, String value)
			throws CSSException {
		throw new CSSException("condition unsupported");
	}

	public Condition createOnlyChildCondition() throws CSSException {
		throw new CSSException("condition unsupported");
	}

	public Condition createOnlyTypeCondition() throws CSSException {
		throw new CSSException("condition unsupported");
	}

	public CombinatorCondition createOrCondition(Condition first,
			Condition second) throws CSSException {
		throw new CSSException("condition unsupported");
	}

	public PositionalCondition createPositionalCondition(int position,
			boolean typeNode, boolean type) throws CSSException {
		throw new CSSException("condition unsupported");
	}

	public AttributeCondition createPseudoClassCondition(String namespaceURI,
			String value) throws CSSException {
		return new PseudoClassConditionImpl(namespaceURI, value);
	}

}

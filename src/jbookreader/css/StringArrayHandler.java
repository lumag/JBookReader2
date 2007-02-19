package jbookreader.css;

import java.util.ArrayList;
import java.util.List;

import jbookreader.style.ERuleValueType;
import jbookreader.style.StyleAttribute;

import org.w3c.css.sac.LexicalUnit;

public class StringArrayHandler implements IRuleHandler {

	private final StyleAttribute attribute;

	public StringArrayHandler(final StyleAttribute attribute) {
		this.attribute = attribute;
	}

	public void handle(CSSHandler handler, LexicalUnit value) {
		if (value.getLexicalUnitType() == LexicalUnit.SAC_INHERIT) {
			handler.addRule(attribute, ERuleValueType.INHERIT, null);
		}
		List<String> strings = new ArrayList<String>();
		for (LexicalUnit lex = value; lex != null; lex = lex.getNextLexicalUnit()) {
//			System.err.println(lex.getLexicalUnitType() + " " + lex);
			switch (lex.getLexicalUnitType()) {
			case LexicalUnit.SAC_OPERATOR_COMMA:
				// ignore
				break;
			case LexicalUnit.SAC_STRING_VALUE:
			case LexicalUnit.SAC_IDENT:
				strings.add(lex.getStringValue());
				break;
			default:
				throw new IllegalArgumentException("Bad value type: "
						+ value.getLexicalUnitType() + " (" + value +")");
			}
		}
		handler.addRule(attribute, ERuleValueType.STRING_ARRAY,
				strings.toArray(new String[strings.size()]));
	}

}

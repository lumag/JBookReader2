/**
 * 
 */
package jbookreader.css;


import org.w3c.css.sac.LexicalUnit;

interface IRuleHandler {
	void handle(CSSHandler handler, LexicalUnit value);
}
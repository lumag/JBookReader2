/**
 * 
 */
package jbookreader.fileformats.rtf;

interface IControlHandler {
	void control(String string, boolean hasParameter, int parameter);
}

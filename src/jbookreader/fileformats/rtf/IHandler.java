/**
 * 
 */
package jbookreader.fileformats.rtf;

interface IHandler {
	boolean control(String string, boolean hasParameter, int parameter);
	void string(String str);
	void startGroup();
	void closeGroup();
	void binaryBlob(byte[] bs);
}

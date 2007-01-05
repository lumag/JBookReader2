package lumag.rtf;

public class DefaultRTFHandler implements IRTFContentHandler{

	public boolean control(String string, boolean parameterized, int parameter) {
//		System.out.println("Control word: " + string
//				+ " parameter: " + parameter);
		return false;
	}

	public void endGroup() { /* empty */ }

	public void startGroup() { /* empty */ }

	public void string(String string) {
//		System.out.println(string);
//		System.out.println("String: '" + string + "'");
	}

	public void binaryBlob(byte[] bs) {
//		System.out.println("Binary blob ignored");
	}

}

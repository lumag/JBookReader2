package lumag.rtf;

public interface IRTFContentHandler {

	void startGroup();

	void endGroup();

	void string(String string);

	boolean control(String string);

	boolean control(String string, int parameter);

}

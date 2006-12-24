package jbookreader.fileformats;

public interface IErrorHandler {
	/**
	 * This is a callback for parsing errors.
	 * 
	 * @param fatal whether the error is fatal
	 * @param message the message corresponding to the error
	 * @return <code>true</code> if the parsing should be stopped.
	 */
	boolean error(boolean fatal, String message);
	void warning(String message);
}

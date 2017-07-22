package de.htw.berlin.polysun4diac.exception;

/**
 * Exception indicating that an unsupported IEC 61499 data type ID was specified.
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 */
public class UnsupportedTypeIDException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1101530954018642170L;

	/** Default constructor */
	public UnsupportedTypeIDException() {
		this("Unsopported IEC 61499 type ID.");
	}
	
	/** String constructor for adding a custom message */
	public UnsupportedTypeIDException(String arg0) {
		super(arg0);
	}

}

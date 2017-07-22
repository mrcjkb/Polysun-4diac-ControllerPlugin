package de.htw.berlin.polysun4diac.exception;

/**
 * Exception indicating that an unsupported IEC 61499 data type was received from 4diac-RTE (FORTE).
 * To prevent this exception, make sure only supported data types are sent from a 4diac application to plugin controllers.
 * For a list of supported data types, see the ForteByteBuffer.
 * 
 * @see de.htw.berlin.polysun4diac.forte.comm.ForteDataBufferLayer
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 *
 */
public class UnsupportedForteDataTypeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8892806340698618896L;
	
	/** Constructor with a message String */
	public UnsupportedForteDataTypeException(String message) {
		super(message);
	}
}

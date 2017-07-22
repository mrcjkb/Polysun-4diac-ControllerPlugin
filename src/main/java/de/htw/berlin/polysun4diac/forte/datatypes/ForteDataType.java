package de.htw.berlin.polysun4diac.forte.datatypes;

import static de.htw.berlin.polysun4diac.forte.datatypes.ForteTypeIDs.*;

import de.htw.berlin.polysun4diac.exception.UnsupportedTypeIDException;

/** 
 * Enum for the supported IEC 61499 data types that can be sent to/received from FORTE. </p>
 * The data type equivalents are listed in the following table:
 * </p>
 * <table border="1">
 * <tr>
 *   <th> JAVA object/primitive </th> <th> IEC 61499 data type</th>
 * </tr>
 * <tr>
 *   <td> boolean </td> <td> BOOL </td>
 * </tr>
 * <tr>
 *   <td> int </td> <td> SINT, INT, DINT, USINT, UINT, UDINT </td>
 * </tr>
 * <tr>
 *   <td> long </td> <td> LINT, ULINT </td>
 * </tr>
 * <tr>
 *   <td> float </td> <td> REAL </td>
 * </tr>
 * <tr>
 *   <td> double </td> <td> LREAL </td>
 * </tr>
 * <tr>
 *   <td> DateAndTime*, LocalDateTime </td> <td> DATE_AND_TIME </td>
 * </tr>
 * <tr>
 *   <td> String </td> <td> STRING </td>
 * </tr>
 * </table>
 * 
 * *DateAndTime is part of this library.
 * </p>
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 */
public enum ForteDataType { 
	/** IEC 61499 equivalent to Java's <code>boolean</code> primitive type. */
	BOOL (TBOOLID),
	/** IEC 61499 equivalent to Java's <code>int</code> primitive type (unsigned short integer). */
	USINT (USINTID),
	/** IEC 61499 equivalent to Java's <code>int</code> primitive type (unsigned integer). */
	UINT (UINTID),
	/** IEC 61499 equivalent to Java's <code>int</code> primitive type (unsigned double integer). */
	UDINT (UDINTID),
	/** IEC 61499 equivalent to Java's <code>long</code> primitive type (unsigned long integer). */
	ULINT (ULINTID),
	/** IEC 61499 equivalent to Java's <code>int</code> primitive type (signed short integer). */
	SINT (SINTID),
	/** IEC 61499 equivalent to Java's <code>int</code> primitive type (signed integer). */
	INT (INTID),
	/** IEC 61499 equivalent to Java's <code>int</code> primitive type (signed double integer). */
	DINT (DINTID),
	/** IEC 61499 equivalent to Java's <code>long</code> primitive type (signed long integer). */
	LINT (LINTID),
	/** IEC 61499 equivalent to Java's <code>float</code> primitive type. */
	REAL (REALID),
	/** IEC 61499 equivalent to Java's <code>double</code> primitive type. */
	LREAL (LREALID),
	/** 
	 * IEC 61499 equivalent to this package's <code>DateAndTime</code> class. 
	 * @see DateAndTime
	 */
	DATE_AND_TIME (DTID),
	/**
	 * IEC 61499 equivalent to Java's <code>String</code> class.
	 * @see String 
	 */
	STRING (STRINGID),
	/** No data type represented. Used for specifying that communication services should only send/receive responses. */
	NONE(RESPONSEID);
	
	private final byte typeID;
	private int numBytes;
	private String javaTypeString;
	
	/**
	 * Constructs an enum with the FORTE type ID
	 * @param tID Type identifier used to represent IEC 61499 data types in FORTE
	 */
	ForteDataType(byte tID) {
		typeID = tID;
		switch (typeID) {
		case TBOOLID:
			numBytes = BOOLBN;
			javaTypeString = "boolean";
			break;
		case USINTID:
			numBytes = USINTBN;
			javaTypeString = "int";
			break;
		case UINTID:
			numBytes = UINTBN;
			javaTypeString = "int";
			break;
		case UDINTID:
			numBytes = UDINTBN;
			javaTypeString = "int";
			break;
		case ULINTID:
			numBytes = ULINTBN;
			javaTypeString = "long";
			break;
		case SINTID:
			numBytes = SINTBN;
			javaTypeString = "int";
			break;
		case INTID:
			numBytes = INTBN;
			javaTypeString = "int";
			break;
		case DINTID:
			numBytes = DINTBN;
			javaTypeString = "int";
			break;
		case LINTID:
			numBytes = LINTBN;
			javaTypeString = "long";
			break;
		case REALID:
			numBytes = REALBN;
			javaTypeString = "float";
			break;
		case LREALID:
			numBytes = LREALBN;
			javaTypeString = "double";
			break;
		case DTID:
			numBytes = DTBN;
			javaTypeString = "DateAndTime";
			break;
		case STRINGID:
			numBytes = STRINGBN;
			javaTypeString = "String";
			break;
		case RESPONSEID:
			numBytes = RESPONSEBN;
			javaTypeString = "FORTE Response";
			break;
		default:
			throw new UnsupportedTypeIDException();
		}
	}
	
	public byte getTypeID() {
		return typeID;
	}
	
	public int getNumBytes() {
		return numBytes;
	}
	
	/**
	 * @return A string representing the JAVA data type that this ForteDataType enum represents.
	 */
	public String getJavaTypeString() {
		return javaTypeString;
	}
}

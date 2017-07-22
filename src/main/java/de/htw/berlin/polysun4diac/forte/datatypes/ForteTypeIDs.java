package de.htw.berlin.polysun4diac.forte.datatypes;

/**
 * Class for holding the supported FORTE type IDs that represent the IEC 61499 data types.
 * They are used for the conversion of byte data arriving from FORTE to the data types specified by the type IDs.
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 */
public final class ForteTypeIDs {
	
	/** FORTE type ID of true BOOL data type */
	public static final byte TBOOLID = 64;
	/** FORTE type ID of false BOOL data type */
	public static final byte FBOOLID = 65;
	/** FORTE type ID of SINT data type */
	public static final byte SINTID = 66;
	/** FORTE type ID of INT data type */
	public static final byte INTID = 67;
	/** FORTE type ID of DINT data type */
	public static final byte DINTID = 68;
	/** FORTE type ID of LINT data type */
	public static final byte LINTID = 69;
	/** FORTE type ID of USINT data type */
	public static final byte USINTID = 70;
	/** FORTE type ID of UINT data type */
	public static final byte UINTID = 71;
	/** FORTE type ID of UDINT data type */
	public static final byte UDINTID = 72;
	/** FORTE type ID of ULINT data type */
	public static final byte ULINTID = 73;
	/** FORTE type ID of REAL data type */
	public static final byte REALID = 74;
	/** FORTE type ID of LREAL data type */
	public static final byte LREALID = 75;
	/** FORTE type ID of DATE_AND_TIME data type */
	public static final byte DTID = 79;
	/** FORTE type ID of STRING data type */
	public static final byte STRINGID = 80;
	/** FORTE ARRAY identifier */
	public static final byte ARRAYID = 118;
	/** FORTE response identifier (no input/output data) */
	public static final byte RESPONSEID = 5;
	/** Number of bytes to allocate for BOOL data type */
	public static final int BOOLBN = 1;
	/** Number of bytes to allocate for SINT data type */
	public static final int SINTBN = 2;
	/** Number of bytes to allocate for INT data type */
	public static final int INTBN = 3;
	/** Number of bytes to allocate for DINT data type */
	public static final int DINTBN = 5;
	/** Number of bytes to allocate for LINT data type */
	public static final int LINTBN = 9;
	/** Number of bytes to allocate for USINT data type */
	public static final int USINTBN = 2;
	/** Number of bytes to allocate for UINT data type */
	public static final int UINTBN = 3;
	/** Number of bytes to allocate for UDINT data type */
	public static final int UDINTBN = 5;
	/** Number of bytes to allocate for ULINT data type */
	public static final int ULINTBN = 9;
	/** Number of bytes to allocate for REAL data type */
	public static final int REALBN = 5;
	/** Number of bytes to allocate for LREAL data type */
	public static final int LREALBN = 9;
	/** Number of bytes to allocate for DATE_AND_TIME data type */
	public static final int DTBN = 9;
	/** Maximum number of bytes to allocate for STRING data type */
	public static final int STRINGBN = 65535;
	/** Number of bytes to allocate for response without sending data */
	public static final int RESPONSEBN = 1;
}

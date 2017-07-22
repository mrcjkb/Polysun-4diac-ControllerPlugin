package de.htw.berlin.polysun4diac.forte.comm;

import static de.htw.berlin.polysun4diac.forte.datatypes.ForteTypeIDs.*;
import static de.htw.berlin.polysun4diac.CommonFunctionsAndConstants.*;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
//import java.time.LocalDateTime;  // TODO: Re-add this feature when Polysun updates to Java 8 SE
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import de.htw.berlin.polysun4diac.exception.UnsupportedForteDataTypeException;
import de.htw.berlin.polysun4diac.forte.datatypes.DateAndTime;
import de.htw.berlin.polysun4diac.forte.datatypes.ForteDataType;

/**
 * Class for reading/writing and buffering 4diac-RTE (FORTE) byte data arriving from or to be sent to
 * a communication service interface function block (CSIFB). Although it is technically possible to use an instance of this class
 * both for handling received data and data to be sent, it is not recommended if the CSIFB's data inputs differ from it's
 * data outputs. In this case, use a CommFunctionBlockLayer, which has two "parallel" ForteDataBufferLayer objects below: One for the inputs and one of the outputs.
 * </p>
 * The class can perform the following conversions between JAVA objects and IEC 61499 data types:
 * 
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
 *   <td> String </td> <td> STRING** </td>
 * </tr>
 * </table>
 * </p>
 * *DateAndTime is part of this library.</p>
 * </p>
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see de.htw.berlin.polysun4diac.forte.datatypes.ForteDataType
 * @see de.htw.berlin.polysun4diac.forte.datatypes.DateAndTime
 */
public class ForteDataBufferLayer extends AbstractCommunicationLayer implements IForteSocket, Serializable {
	
	private static final long serialVersionUID = 5303988619865082081L;
	
	/** Array for shifting bytes */
	private static final int[] BYTEPOS = {0, 8, 16, 24, 32, 40, 48, 56};
	/** Default number of data values for {@link #mDataValues} */
	private static final int DEFNUMDATAVALUES = 1;
	/** For initializing Position */
	private static final int POSITION_INIT = -1;
	/** Amount at which to increment {@link #mPosition} */
	private static final int POSITION_INCREMENT = 1;
	/** Message for NuSuchElementException */
	private static final String NOMOREELEMENTSMSG = "No more elements to access.";
	
	/** Number of data values in {@link #mDataValues} */
	private int mNumDataValues;
	/** 
	 * Fixed-size List of data values to be sent/received </p>
	 * WARNING: The add() method throws an exception on this member.
	 * */
	private List<Object> mDataValues;
	/** Indicates whether type at position is an array or not */
	private boolean[] mArrayFlags;
	
	/** ByteBuffer member that holds the bytes */
	private ByteBuffer mBuffer;
	/** Indicates which IEC 61499 data type is at which position */
	private ForteDataType[] mTypes;
	/** Current position of stored JAVA data types */
	private int mPosition = POSITION_INIT;
	/** Reference time for receiving DateAndTime objects */
	private long mDTreference = ZERO_INIT;
	/** Flag to determine whether this object is initialized */
	private boolean mIsInitialized = false;
	
	@Override
	public boolean openConnection(CommLayerParams params) throws IOException {
		if (!isInitialized()) { // Object has not yet been initialized
			// --> This is the top layer with the same inputs as outputs.
			// Initialize with inputs defined in params
			initialize(params.getInputs(), params.getInputArrayLengths());
		}
		return super.openConnection(params);
	}
	
	/**
	 * Initializes this object's buffer that can hold multiple arrays of various data types
	 * @param dataTypes List containing the ForteDataType Enums.
	 * This could be the inputs of outputs set in a CommLayerParams object.
	 * @param arraySizes List containing the corresponding array sizes (1 for non-arrays)
	 * This could be the array sizes set in a CommLayerParams object.
	 */
	public void initialize(List<ForteDataType> dataTypes, List<Integer> arraySizes) {
		if (dataTypes.size() != arraySizes.size()) {
			throw new InputMismatchException("The sizes of the inputs do not match.");
		}
		mNumDataValues = dataTypes.size();
		mDataValues = initializeDataValues(mNumDataValues);
		mArrayFlags = new boolean[mNumDataValues];
		int numBytes = 0;
		int ct = 0;
		mTypes = new ForteDataType[mNumDataValues];
		Iterator<ForteDataType> dataIt = dataTypes.iterator();
		Iterator<Integer> arrSizeIt = arraySizes.iterator();
		while (dataIt.hasNext() && arrSizeIt.hasNext()) {
			int arrSize = arrSizeIt.next();
			ForteDataType type = dataIt.next();
			int bytes = type.getNumBytes();
			if (arrSize > 1) { // Array
				// 4 elements added for headers + array size (2 bytes) + number of bytes of array data
				numBytes += 4 + ((bytes - 1 < 1) ? 1 : bytes - 1) * arrSize; // Limit to [1, inf]
				mArrayFlags[ct] = true;
			} else { // Not an array
				numBytes += bytes;
				mArrayFlags[ct] = false;
			}
			mTypes[ct] = type;
			ct++;
		}
		initialize(numBytes);
		setInitialized();
	}
	
	@Override
	public boolean put(float value) {
		getBuffer().put(REALID);
		getBuffer().putFloat(value);
		return incrementPosition();
	}
	
	@Override
	public boolean put(float[] value) {
		putArrayHeader(REALID, value.length);
		for (float val : value) {
			getBuffer().putFloat(val);
		}
		return incrementPosition();
	}
	
	@Override
	public boolean put(double value) {
		getBuffer().put(LREALID);
		getBuffer().putDouble(value);
		return incrementPosition();
	}
	
	@Override
	public boolean put(double[] value) {
		putArrayHeader(LREALID, value.length);
		for (double val : value) {
			getBuffer().putDouble(val);
		}
		return incrementPosition();
	}
	
	@Override
	public boolean put(int value) {
		put(getTypeAtNextPosition().getTypeID());
		put32bitInteger(value, getTypeAtNextPosition().getNumBytes() - 1); // Exclude header byte
		return incrementPosition();
	}
	
	@Override
	public boolean put(int[] value) {
		putArrayHeader(getTypeAtNextPosition().getTypeID(), value.length);
		for (int val : value) {
			put32bitInteger(val, getTypeAtNextPosition().getNumBytes());
		}
		return incrementPosition();
	}
	
	@Override
	public boolean put(long value) {
		put(getTypeAtNextPosition().getTypeID());
		getBuffer().putLong(value);
		return incrementPosition();
	}
	
	@Override
	public boolean put(long[] value) {
		putArrayHeader(getTypeAtNextPosition().getTypeID(), value.length);
		for (long val : value) {
			getBuffer().putLong(val);
		}
		return incrementPosition();
	}
	
	@Override
	public boolean put(boolean value) {
		put(bool2forteID(value));
		return incrementPosition();
	}
	
	@Override
	public boolean put(boolean[] value) {
		putArrayHeader(value.length);
		for (boolean val : value) {
			put(val);
		}
		return incrementPosition();
	}
	
	@Override
	public boolean put(String value) {
		put(STRINGID);
		put(string2ForteBytes(value));
		return incrementPosition();
	}
	
	@Override
	public boolean put(String[] value) {
		putArrayHeader(STRINGID, value.length);
		for (String val : value) {
			put(string2ForteBytes(val));
		}
		return incrementPosition();
	}
	
	@Override
	public boolean put(DateAndTime value) {
		put(DTID);
		putDateAndTime(value);
		return incrementPosition();
	}
	
	@Override
	public boolean put(DateAndTime[] value) {
		putArrayHeader(DTID, value.length);
		for (DateAndTime val : value) {
			putDateAndTime(val);
		}
		return incrementPosition();
	}
	
	 // TODO: Re-add this feature when Polysun updates to Java 8 SE
//	@Override
//	public boolean put(LocalDateTime value) {
//		DateAndTime dt = new DateAndTime(value);
//		dt.setSimulationTimeS(0);
//		put(dt); // Convert to DateAndTime
//		return incrementPosition();
//	}
	 // TODO: Re-add this feature when Polysun updates to Java 8 SE
//	@Override
//	public boolean put(LocalDateTime[] value) {
//		putArrayHeader(DTID, value.length);
//		for (LocalDateTime val : value) {
//			DateAndTime dt = new DateAndTime(val);
//			putDateAndTime(dt);
//		}
//		return incrementPosition();
//	}
	
	@Override
	public byte[] array() {
		return getBuffer().array();
	}
	
	@Override
	public boolean getBool() {
		if (isBool()) {
			if (incrementPosition()) {
				return (boolean) getDataValues().get(getPosition());
			}
			throw new NoSuchElementException(NOMOREELEMENTSMSG);
		}
		throw new NoSuchElementException(typeIDtoBadAccessMessage(ForteDataType.BOOL));
	}
	
	@Override
	public long getLong() {
		if (isLong()) {
			if (incrementPosition()) {
				return (long) getDataValues().get(getPosition());
			}
			throw new NoSuchElementException(NOMOREELEMENTSMSG);
		}
		throw new NoSuchElementException(typeIDtoBadAccessMessage(ForteDataType.LINT));
	}
	
	@Override
	public int getInt() {
		if (isInt()) {
			if (incrementPosition()) {
				return (int) getDataValues().get(getPosition());
			}
			throw new NoSuchElementException(NOMOREELEMENTSMSG);
		}
		throw new NoSuchElementException(typeIDtoBadAccessMessage(ForteDataType.INT));
	}

	@Override
	public float getFloat() {
		if (isFloat()) {
			if (incrementPosition()) {
				return (float) getDataValues().get(getPosition());
			}
			throw new NoSuchElementException(NOMOREELEMENTSMSG);
		}
		throw new NoSuchElementException(typeIDtoBadAccessMessage(ForteDataType.REAL));
	}
	
	@Override
	public double getDouble() {
		if (isDouble()) {
			if (incrementPosition()) {
				return (double) getDataValues().get(getPosition());
			}
			throw new NoSuchElementException(NOMOREELEMENTSMSG);
		}
		throw new NoSuchElementException(typeIDtoBadAccessMessage(ForteDataType.LREAL));
	}
	
	@Override
	public DateAndTime getDateAndTime() {
		if (isDateAndTime()) {
			if (incrementPosition()) {
				return (DateAndTime) getDataValues().get(getPosition());
			}
			throw new NoSuchElementException(NOMOREELEMENTSMSG);
		}
		throw new NoSuchElementException(typeIDtoBadAccessMessage(ForteDataType.DATE_AND_TIME));
	}
	
	 // TODO: Re-add this feature when Polysun updates to Java 8 SE
//	@Override
//	public LocalDateTime getLocalDateTime() {
//		if (isLocalDateTime()) {
//			if (incrementPosition()) {
//				return ((DateAndTime) getDataValues().get(getPosition())).toLocalDateTime();
//			}
//			throw new NoSuchElementException(NOMOREELEMENTSMSG);
//		}
//		throw new NoSuchElementException(typeIDtoBadAccessMessage(ForteDataType.DATE_AND_TIME));
//	}
	
	@Override
	public String getString() {
		if (isString()) {
			if (incrementPosition()) {
				return (String) getDataValues().get(getPosition());
			}
			throw new NoSuchElementException(NOMOREELEMENTSMSG);
		}
		throw new NoSuchElementException(typeIDtoBadAccessMessage(ForteDataType.STRING));
	}
	
	@Override
	public boolean[] getBoolArray() {
		if (isBoolArray()) {
			if (incrementPosition()) {
				return (boolean[]) getDataValues().get(getPosition());
			}
			throw new NoSuchElementException(NOMOREELEMENTSMSG);
		}
		throw new NoSuchElementException(typeIDtoBadArrayAccessMessage(ForteDataType.BOOL));
	}
	
	@Override
	public long[] getLongArray() {
		if (isLongArray()) {
			if (incrementPosition()) {
				return (long[]) getDataValues().get(getPosition());
			}
			throw new NoSuchElementException(NOMOREELEMENTSMSG);
		}
		throw new NoSuchElementException(typeIDtoBadArrayAccessMessage(ForteDataType.LINT));
	}
	
	@Override
	public int[] getIntArray() {
		if (isIntArray()) {
			if (incrementPosition()) {
				return (int[]) getDataValues().get(getPosition());
			}
			throw new NoSuchElementException(NOMOREELEMENTSMSG);
		}
		throw new NoSuchElementException(typeIDtoBadArrayAccessMessage(ForteDataType.INT));
	}
	
	@Override
	public float[] getFloatArray() {
		if (isFloatArray()) {
			if (incrementPosition()) {
				return (float[]) getDataValues().get(getPosition());
			}
			throw new NoSuchElementException(NOMOREELEMENTSMSG);
		}
		throw new NoSuchElementException(typeIDtoBadArrayAccessMessage(ForteDataType.REAL));
	}
	
	@Override
	public double[] getDoubleArray() {
		if (isDoubleArray()) {
			if (incrementPosition()) {
				return (double[]) getDataValues().get(getPosition());
			}
			throw new NoSuchElementException(NOMOREELEMENTSMSG);
		}
		throw new NoSuchElementException(typeIDtoBadArrayAccessMessage(ForteDataType.LREAL));
	}
	
	@Override
	public DateAndTime[] getDateAndTimeArray() {
		if (isDateAndTimeArray()) {
			if (incrementPosition()) {
				return (DateAndTime[]) getDataValues().get(getPosition());
			}
			throw new NoSuchElementException(NOMOREELEMENTSMSG);
		}
		throw new NoSuchElementException(typeIDtoBadArrayAccessMessage(ForteDataType.DATE_AND_TIME));
	}
	
	 // TODO: Re-add this feature when Polysun updates to Java 8 SE
//	@Override
//	public LocalDateTime[] getLocalDateTimeArray() {
//		if (isLocalDateTimeArray()) {
//			if (incrementPosition()) {
//				LocalDateTime[] ldt = new LocalDateTime[getNumDataValues()];
//				int ct = 0;
//				for (Object value : getDataValues()) {
//					ldt[ct] = ((DateAndTime) value).toLocalDateTime();
//					ct++;
//				}
//				return ldt;
//			}
//			throw new NoSuchElementException(NOMOREELEMENTSMSG);
//		}
//		throw new NoSuchElementException(typeIDtoBadAccessMessage(ForteDataType.DATE_AND_TIME));
//	}
	
	@Override
	public String[] getStringArray() {
		if (isStringArray()) {
			if (incrementPosition()) {
				return (String[]) getDataValues().get(getPosition());
			}
			throw new NoSuchElementException(NOMOREELEMENTSMSG);
		}
		throw new NoSuchElementException(typeIDtoBadArrayAccessMessage(ForteDataType.STRING));
	}
	
	@Override
	public boolean isBool() {
		if (!isArrayAtPosition(getPosition() + 1)) {
			return chkBool();
		}
		return false;
	}
	
	@Override
	public boolean isLong() {
		if (!isArrayAtPosition(getPosition() + 1)) {
			return chkLong();
		}
		return false;
	}
	
	@Override
	public boolean isInt() {
		if (!isArrayAtPosition(getPosition() + 1)) {
			return chkInt();
		}
		return false;
	}
	
	@Override
	public boolean isFloat() {
		if (!isArrayAtPosition(getPosition() + 1)) {
			return chkFloat();
		}
		return false;
	}
	
	@Override
	public boolean isDouble() {
		if (!isArrayAtPosition(getPosition() + 1)) {
			return chkDouble();
		}
		return false;
	}
	
	@Override
	public boolean isDateAndTime() {
		if (!isArrayAtPosition(getPosition() + 1)) {
			return chkDateAndTime();
		}
		return false;
	}
	
	 // TODO: Re-add this feature when Polysun updates to Java 8 SE
//	@Override
//	public boolean isLocalDateTime() {
//		if (!isArrayAtPosition(getPosition() + 1)) {
//			return chkLocalDateTime();
//		}
//		return false;
//	}
	
	@Override
	public boolean isString() {
		if (!isArrayAtPosition(getPosition() + 1)) {
			return chkString();
		}
		return false;
	}
	
	@Override
	public boolean isBoolArray() {
		if (isArrayAtPosition(getPosition() + 1)) {
			return chkBool();
		}
		return false;
	}
	
	@Override
	public boolean isLongArray() {
		if (isArrayAtPosition(getPosition() + 1)) {
			return chkLong();
		}
		return false;
	}
	
	@Override
	public boolean isIntArray() {
		if (isArrayAtPosition(getPosition() + 1)) {
			return chkInt();
		}
		return false;
	}
	
	@Override
	public boolean isFloatArray() {
		if (isArrayAtPosition(getPosition() + 1)) {
			return chkFloat();
		}
		return false;
	}
	
	@Override
	public boolean isDoubleArray() {
		if (isArrayAtPosition(getPosition() + 1)) {
			return chkDouble();
		}
		return false;
	}
	
	@Override
	public boolean isDateAndTimeArray() {
		if (isArrayAtPosition(getPosition() + 1)) {
			return chkDateAndTime();
		}
		return false;
	}
	
	 // TODO: Re-add this feature when Polysun updates to Java 8 SE
//	@Override
//	public boolean isLocalDateTimeArray() {
//		if (isArrayAtPosition(getPosition() + 1)) {
//			return chkLocalDateTime();
//		}
//		return false;
//	}
	
	@Override
	public boolean isStringArray() {
		if (isArrayAtPosition(getPosition() + 1)) {
			return chkString();
		}
		return false;
	}
	
	@Override
	public void recvData() throws IOException {
		// Wait for data from stream, write to internal byte array and set maximum number of bytes allowed to be read
		// Clear buffer and pass received bytes to it
		rewind(); // Reset data position to -1
		// Convert byte buffer data into List entries
		while(incrementPosition()) {
			if (isArrayAtPosition(getPosition())) {
				readByte(); // Skip array header
				// Next two bytes are array size
				addArray(readLengthHeader());
			}
			else { // Not an array
				byte typeID = readByte();
				if (typeID == RESPONSEID) {
					reset();
					return; // FORTE response without data inputs
				}
				addValue(typeID);
			}
		}
		// Reset data position and clear byte buffer
		reset();
	}
	
	@Override
	public void sendData() throws IOException {
		if (isResponse()) {
			put(RESPONSEID);
		}
		sendData(array(), 0, getBuffer().position());
		reset();
	}
	
	@Override
	public void sendData(byte[] data, int off, int len) throws IOException {
		getBelow().sendData(data, off, len);
	}
	
	@Override
	public void sendRsp() throws IOException {
		byte[] rsp = {RESPONSEID};
		sendData(rsp);
	}
	
	@Override
	public void reset() {
		rewind();
		clearByteBuffer();
	}
	
	@Override
	public ForteDataType getTypeAtPosition(int position) {
		position = (position < 0) ? 0 : position; // Lower limit
		position = (position > getNumDataValues() - 1) ? getNumDataValues() - 1 : position; // Upper limit
		return getForteType()[position];
	}
	
	@Override
	public ForteDataType getTypeAtNextPosition() {
		return getTypeAtPosition(getPosition() + 1);
	}
	
	@Override
	public boolean isArrayAtPosition(int position) {
		position = (position < 0) ? 0 : position; // Lower limit
		position = (position > getNumDataValues() - 1) ? getNumDataValues() - 1 : position; // Upper limit
		return mArrayFlags[position];
	}
	
	@Override
	public void rewind() {
		setPosition(POSITION_INIT);
	}
	
	@Override
	public Iterator<Object> iterator() {
		return getDataValues().iterator();
	}
	
	@Override
	public boolean isConnected() {
		return getConnectionState();
	}
	
	/**
	 * Clears the internal byte buffer and sets its position to 0.
	 */
	private void clearByteBuffer() {
		getBuffer().clear();
	}
	
	/** 
	 * Reads the next byte data sequences determined according to the FORTE typeID and the specified array length.
	 * The bytes are translated to the corresponding JAVA types, which are stored in an internal buffer {@link #mDataValues}.
	 * @param arrayLength length of the array.
	 * @throws UnsupportedForteDataTypeException 
	 * @throws IOException 
	 */
	private void addArray(int arrayLength) throws UnsupportedForteDataTypeException, IOException {
		byte[] manualRead; // Bytes to be manually read by methods implemented in this class (int & long types)
		int manualReadLengh = POSITION_INIT; // Length of manualRead array initialized to -1 so that adding BN constants results in number of bytes excluding header
		boolean manualReadLengthSet = false;
		switch (readByte()) { // typeID
		case TBOOLID:
		case FBOOLID:
			boolean[] boolArray = new boolean[arrayLength];
			for (int i = 0; i < arrayLength; i++) {
				boolArray[i] = readByte() == TBOOLID;
			}
			getDataValues().set(getPosition(), boolArray);
			return;
		case SINTID: // Fall through IEC 61499 data types that are interpreted as int
		case USINTID:
			manualReadLengh += SINTBN;
			manualReadLengthSet = true;
		case INTID:
		case UINTID:
			if (!manualReadLengthSet) {
				manualReadLengh += INTBN;
				manualReadLengthSet = true;
			}
		case DINTID:
		case UDINTID:
			if (!manualReadLengthSet) {
				manualReadLengh += DINTBN;
			}
			// Transfer bytes from buffer to manualRead and then to array
			manualRead = new byte[manualReadLengh];
			int[] intArray = new int[arrayLength];
			for (int i = 0; i < arrayLength; i++) {
				for (int j = 0; j < manualReadLengh; j++) {
					manualRead[j] = readByte();
				}
				intArray[i] = bytes2limitedShortInteger(manualRead);
			}
			getDataValues().set(getPosition(), intArray);
			return;
		case LINTID: // Fall through IEC 61499 long integer types
		case ULINTID:
			long[] longArray = new long[arrayLength];
			for (int i = 0; i < arrayLength; i++) {
				longArray[i] = readLong();
			}
			getDataValues().set(getPosition(), longArray);
			return;
		case REALID:
			float[] floatArray = new float[arrayLength];
			for (int i = 0; i < arrayLength; i++) {
				floatArray[i] = readFloat();
			}
			getDataValues().set(getPosition(), floatArray);
			return;
		case LREALID:
			double[] doubleArray = new double[arrayLength];
			for (int i = 0; i < arrayLength; i++) {
				doubleArray[i] = readDouble();
			}
			getDataValues().set(getPosition(), doubleArray);
			return;
		case DTID:
			DateAndTime[] dt = new DateAndTime[arrayLength];
			for (int i = 0; i < arrayLength; i++) {
				dt[i] = readDateAndTime();
			}
			getDataValues().set(getPosition(), dt);
			return;
		case STRINGID:
			String[] s = new String[arrayLength];
			for (int i = 0; i < arrayLength; i++) {
				s[i] = readString();
			}
			getDataValues().set(getPosition(), s);
			return;
		default:
			throw new UnsupportedForteDataTypeException("Unsupported FORTE data type received.");
		}
	}
	
	/**
	 * Reads the next byte data sequence determined according to the FORTE typeID and translates it to the corresponding JAVA type.
	 * The data is stored in an internal buffer {@link #mDataValues}.
	 * @param typeID FORTE type ID used to identify FORTE data type.
	 * @throws UnsupportedForteDataTypeException
	 * @throws IOException 
	 */
	private void addValue(byte typeID) throws UnsupportedForteDataTypeException, IOException {
		byte[] manualRead; // Bytes to be manually read by methods implemented in this class (int & long types)
		int manualReadLengh = POSITION_INIT; // Length of manualRead array initialized to -1 so that adding BN constants results in number of bytes excluding header
		boolean manualReadLengthSet = false;
		switch (typeID) {
		case TBOOLID:
		case FBOOLID:
			getDataValues().set(getPosition(), typeID == TBOOLID);
			return;
		case SINTID: // Fall through IEC 61499 data types that are interpreted as int
		case USINTID:
			manualReadLengh += SINTBN;
			manualReadLengthSet = true;
		case INTID:
		case UINTID: 
			if (!manualReadLengthSet) {
				manualReadLengh += INTBN;
				manualReadLengthSet = true;
			}
		case DINTID:
		case UDINTID:
			if (!manualReadLengthSet) {
				manualReadLengh += DINTBN;
			}
			// Transfer bytes from buffer to manualRead and then to array
			manualRead = new byte[manualReadLengh];
			for (int j = 0; j < manualReadLengh; j++) {
				manualRead[j] = readByte();
			}
			getDataValues().set(getPosition(), bytes2limitedShortInteger(manualRead));
			return;
		case LINTID: // Fall through IEC 61499 long integer types
		case ULINTID:
			getDataValues().set(getPosition(), readLong());
			return;
		case REALID:
			getDataValues().set(getPosition(), readFloat());
			return;
		case LREALID:
			getDataValues().set(getPosition(), readDouble());
			return;
		case DTID:
			// Initialize with reference value and set time according to FORTE value
			getDataValues().set(getPosition(), readDateAndTime());
			return;
		case STRINGID:
			getDataValues().set(getPosition(), readString());
			return;
		default:
			throw new UnsupportedForteDataTypeException("Unsupported FORTE data type received.");
		}
	}
	
	/**
	 * @return true if there are no data inputs and this object is only there to send responses.
	 */
	 private boolean isResponse() {
		 return ForteDataType.NONE.equals(getTypeAtNextPosition());
	 }
	
	/**
	 * Reads the bytes of an IEC 61499 DATE_AND_TIME
	 * @return a corresponding DateAndTime object with it's SimulationStarteTime set to the reference value
	 * @throws IOException 
	 */
	private DateAndTime readDateAndTime() throws IOException {
		long simulationStartms = getDateAndTimeReference();
		DateAndTime dt = new DateAndTime(simulationStartms); 
		dt.setSimulationTimeS((int) ((readLong() - simulationStartms) / KILOTOSI));
		return dt;
	}
	
	/**
	 * Reads the bytes of an IEC 61499 STRING
	 * @return a corresponding String object
	 * @throws IOException 
	 */
	private String readString() throws IOException {
		byte[] b = new byte[readLengthHeader()];
		for (int i = 0; i < b.length; i++) {
			b[i] = readByte();
		}
		return new String(b);
	}
	
	/**
	 * Reads two bytes indicating the length of a String or an array and converts them to an integer
	 * @return length of a String or array
	 * @throws IOException 
	 */
	private int readLengthHeader() throws IOException {
		byte[] lengthHeader = new byte[LENGTHHEADERNUM];
		for (int i = 0; i < LENGTHHEADERNUM; i++) {
			lengthHeader[i] = readByte();
		}
		return bytes2limitedShortInteger(lengthHeader);
	}
	
	
	/**
	 * Generates an error message for attempted access of a wrong data type.
	 * @param typeID
	 * @return error message
	 */
	private String typeIDtoBadAccessMessage(ForteDataType type) {
		String isType = " value ";
		if (isArrayAtPosition(getPosition())) {
			isType = " array ";
		}
		return "Attemted to access "
				+ type.getJavaTypeString()
				+ " value where " 
				+ getTypeAtPosition(getPosition()).getJavaTypeString()
				+ isType + "is stored";
	}
	
	/**
	 * Generates an error message for attempted access of a wrong array data type.
	 * @param typeID
	 * @return error message
	 */
	private String typeIDtoBadArrayAccessMessage(ForteDataType type) {
		String isType = " value ";
		if (isArrayAtPosition(getPosition())) {
			isType = " array ";
		}
		return "Attemted to access "
				+ type.getJavaTypeString()
				+ " array where " 
				+ getTypeAtPosition(getPosition()).getJavaTypeString()
				+ isType + " is stored";
	}
	
	/** 
	 * @return The next byte from the .
	 * @throws IOException 
	 */
	public byte readByte() throws IOException {
		return getBelow().readByte();
	}
	
	/**
	 * Adds a DateAndTime to buffer without any headers
	 * @param value
	 */
	private void putDateAndTime(DateAndTime value) {
		getBuffer().putLong(value.getForteTime());
	}
	
	/**
	 * Adds byte value to buffer
	 * @param value
	 */
	private void put(byte value) {
		getBuffer().put(value);
	}
	
	/**
	 * Adds byte array to buffer
	 * @param value
	 */
	private void put(byte[] value) {
		for (byte b : value) {
			put(b);
		}
	}
	
	/**
	 * Adds a FORTE array header to the buffer
	 * @param typeID
	 * @param arraySize
	 */
	private void putArrayHeader(byte typeID, int arraySize) {
		putArrayHeader(arraySize);
		put(typeID);
	}
	
	/**
	 * Adds a FORTE array header to the buffer without adding a typeID (e.g., necessary for BOOL arrays)
	 * @param arraySize
	 */
	private void putArrayHeader(int arraySize) {
		put(ARRAYID);
		put(limitedShortInteger2Bytes(arraySize, 2)); // Maximum of 2 bytes permitted for array size in FORTE header
	}
	
	/**
	 * Converts an integer and a typeID to a FORTE byte array with a certain number of bytes.
	 * @param typeID
	 * @param value
	 * @param maxNumBytes
	 */
	private void put32bitInteger(int value, int maxNumBytes) {
		byte[] out = limitedShortInteger2Bytes(value, maxNumBytes);
		put(out);
	}
	
	/**
	 * Converts short integer to byte[] array
	 * @param value
	 * @param maxNumBytes
	 * @return
	 */
	private static byte[] limitedShortInteger2Bytes(int value, int maxNumBytes) {
		int ct = ZERO_INIT;
		byte[] res = new byte[maxNumBytes];
		// Note: It appears that only 4 byte integers are supported in Java. So the loop executes a maximum of 4 times.
		for (int i = maxNumBytes - 1; i >= 0 & ct < 4; i--) {
			res[i] = (byte) (value >> BYTEPOS[ct]);
			ct++;
		}
		return res;
	}
	
	/**
	 * Converts byte[] array to short integer
	 * @param value
	 * @return
	 */
	private static int bytes2limitedShortInteger(byte[] value) {
		int res = ZERO_INIT;
		int ct = ZERO_INIT;
		// Maximum number of bytes to read for integer is 4
		for (int i = ((value.length > 4) ? 4 : value.length) - 1; i > 0; i--) {
			res |= (value[i] & 0xFF) << BYTEPOS[ct];
			ct++;
		}
		res |= value[0] << BYTEPOS[ct];
		return res;
	}
	
	/**
	 * Converts a boolean value to a FORTE BOOL type ID.
	 * @param value JAVA boolean
	 * @return FORTE BOOL typeID
	 */
	private byte bool2forteID(boolean value) {
		return (value ? (byte) TBOOLID : (byte) FBOOLID); // 64 for true, 65 for false
	}
	
	/**
	 * Initializes the ForteByteBuffer's internal properties
	 * @param size
	 */
	private void initialize(int size) {
		setBuffer(ByteBuffer.allocate(size));
	}
	
	/**
	 * Initializes {@link #mDataValues} to a fixed-sized List.
	 * @param numDataValues
	 * @return
	 */
	private List<Object> initializeDataValues(int numDataValues) {
		return Arrays.asList(new Object[(numDataValues < DEFNUMDATAVALUES) ? DEFNUMDATAVALUES : numDataValues]);
	}
	
	@Override
	public int capacity() {
		return getBuffer().capacity();
	}
	
	@Override
	public boolean incrementPosition() {
		int previous = getPosition();
		setPosition(getPosition() + POSITION_INCREMENT);
		return (getPosition() > previous) ? true : false;
	}
	
	/**
	 * @return true if value at next position is of type boolean or boolean[].
	 * If the position is at the end of the data buffer, the current position is checked.
	 */
	private boolean chkBool() {
		return ForteDataType.BOOL.equals(getTypeAtNextPosition());
	}
	
	/**
	 * @return true if value at next position is of type long or long[].
	 * If the position is at the end of the data buffer, the current position is checked.
	 */
	private boolean chkLong() {
		ForteDataType type = getTypeAtNextPosition();
		return ForteDataType.LINT.equals(type)
				|| ForteDataType.ULINT.equals(type);
	}
	
	/**
	 * @return true if value at next position is of type int or int[].
	 * If the position is at the end of the data buffer, the current position is checked.
	 */
	private boolean chkInt() {
		ForteDataType type = getTypeAtNextPosition();
		return ForteDataType.SINT.equals(type)
				|| ForteDataType.USINT.equals(type)
				|| ForteDataType.INT.equals(type)
				|| ForteDataType.UINT.equals(type)
				|| ForteDataType.DINT.equals(type)
				|| ForteDataType.UDINT.equals(type);
	}
	
	/**
	 * @return true if value at next position is of type float or float[].
	 * If the position is at the end of the data buffer, the current position is checked.
	 */
	private boolean chkFloat() {
		return ForteDataType.REAL.equals(getTypeAtNextPosition());
	}
	
	/**
	 * @return true if value at next position is of type double or double[].
	 * If the position is at the end of the data buffer, the current position is checked.
	 */
	private boolean chkDouble() {
		return ForteDataType.LREAL.equals(getTypeAtNextPosition());
	}
	
	/**
	 * @return true if value at next position is of type DateAndTime or DateAndTime[].
	 * If the position is at the end of the data buffer, the current position is checked.
	 */
	private boolean chkDateAndTime() {
		return ForteDataType.DATE_AND_TIME.equals(getTypeAtNextPosition());
	}
	
	// TODO: Re-add this feature when Polysun updates to Java 8 SE
//	/**
//	 * @return true if value at next position is of type LocalDateTime or localDateTime[].
//	 * If the position is at the end of the data buffer, the current position is checked.
//	 */
//	private boolean chkLocalDateTime() {
//		return chkDateAndTime();
//	}
	
	/**
	 * @return true if value at next position is of type String or String[].
	 * If the position is at the end of the data buffer, the current position is checked.
	 */
	private boolean chkString() {
		return ForteDataType.STRING.equals(getTypeAtNextPosition());
	}
	
	/** Returns {@link mDataValues} */
	private List<Object> getDataValues() {
		return mDataValues;
	}
	
	@Override
	public int getNumDataValues() {
		return mNumDataValues;
	}
	
	/** Returns {@link #mTypes} */
	private ForteDataType[] getForteType() {
		return mTypes;
	}
	
	@Override
	public void setPosition(int position) {
		// Limit to number of data values and 0
		position = (position > getNumDataValues() - 1) ? getNumDataValues() - 1 : position;
		mPosition = (position < POSITION_INIT) ? POSITION_INIT : position;
	}
	
	@Override
	public void setDateAndTimeReference(DateAndTime dt) {
		mDTreference = dt.getForteSimulationStart();
	}
	
	@Override
	public void disconnect() throws IOException {
		closeConnection();
	}
	
	/** Returns {@link #mDTreference} */
	private long getDateAndTimeReference() {
		return mDTreference;
	}
	
	/** 
	 * Returns {@link #mPosition}
	 * If {@link #mPosition} is in the initialized position of -1, this method returns 0. 
	 */
	public int getPosition() {
		return mPosition;
	}
	
	/** Sets the ByteBuffer member that holds the bytes */
	private void setBuffer(ByteBuffer b) {
		mBuffer = b;
	}
	
	/** Returns the ByteBuffer member that holds the bytes */
	private ByteBuffer getBuffer() {
		return mBuffer;
	}
	
	/**
	 * @return flag to determine whether this object is initialized (true if initialized)
	 */
	private boolean isInitialized() {
		return mIsInitialized;
	}
	
	/** Sets a flag to indicate that this object is initialized */
	private void setInitialized() {
		mIsInitialized = true;
	}
	
	/**
	 * Converts a String to bytes expected by FORTE
	 * @param s String object
	 * @return The bytes representing an equivalent STRING in FORTE (excluding the type ID)
	 */
	private static byte[] string2ForteBytes(String s) {
		byte[] stringAsBytes = s.getBytes();
		// Add the length of the string to the header
		byte[] lengthHeader = limitedShortInteger2Bytes(stringAsBytes.length, LENGTHHEADERNUM);
		byte[] out = new byte[stringAsBytes.length + LENGTHHEADERNUM];
		int ct = 0;
		for (byte l : lengthHeader) {
			out[ct] = l;
			ct++;
		}
		for (byte b : stringAsBytes) {
			out[ct] = b;
			ct++;
		}
		return out;
	}
}
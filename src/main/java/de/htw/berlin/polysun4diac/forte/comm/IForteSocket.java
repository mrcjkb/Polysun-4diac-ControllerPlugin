package de.htw.berlin.polysun4diac.forte.comm;

import java.io.IOException;
//import java.time.LocalDateTime;  // TODO: Re-add this feature when Polysun updates to Java 8 SE
import java.util.Iterator;
import java.util.NoSuchElementException;

import de.htw.berlin.polysun4diac.exception.UnsupportedForteDataTypeException;
import de.htw.berlin.polysun4diac.forte.datatypes.DateAndTime;
import de.htw.berlin.polysun4diac.forte.datatypes.ForteDataType;


/**
 * Interface for communication between a Java application and IEC 61499 communication service interface function blocks (CSIFBs) running on 4diac-RTE (FORTE).
 * </p>
 * An IForteSocket can perform the following conversions between JAVA objects and IEC 61499 data types:
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
 * @see com.velasolaris.plugin.controller.spi.IPluginController
 */
public interface IForteSocket {
	
	/**
	 * This method is intended to allow array-backed buffers to be passed to native code more efficiently.
	 * Modifications to this buffer's content will cause the returned array's content to be modified, and vice versa. 
	 * @return the array that backs this buffer  (optional operation). 
	 */
	public byte[] array();
	
	/** Returns the buffer's capacity */
	public int capacity();
	
	/**
	 * Increments the position by one. </p>
	 * Use {@link #isBool()} to determine whether the correct data type is at the next position.
	 * @return boolean value at the next position.
	 * @throws NoSuchElementException 
	 */
	public boolean getBool();
	
	/**
	 * Increments the position by one. </p>
	 * Use {@link #isBoolArray()} to determine whether the correct data type is at the next position.
	 * @return boolean[] value at the next position.
	 * @throws NoSuchElementException 
	 */
	public boolean[] getBoolArray();
	
	/**
	 * Increments the position by one. </p>
	 * Use {@link #isDateAndTime()} to determine whether the correct data type is at the next position.
	 * @return DateAndTime value at the next position.
	 * @throws NoSuchElementException
	 */
	public DateAndTime getDateAndTime();
	
	/**
	 * Increments the position by one. </p>
	 * Use {@link #isDateAndTimeArray()} to determine whether the correct data type is at the next position.
	 * @return DateAndTime[] value at the next position.
	 * @throws NoSuchElementException
	 */
	public DateAndTime[] getDateAndTimeArray();
	
	/**
	 * Increments the position by one. </p>
	 * Use {@link #isDouble()} to determine whether the correct data type is at the next position.
	 * @return double value at the next position.
	 * @throws NoSuchElementException
	 */
	public double getDouble();
	
	/**
	 * Increments the position by one. </p>
	 * Use {@link #isDoubleArray()} to determine whether the correct data type is at the next position.
	 * @return double[] value at the next position.
	 * @throws NoSuchElementException
	 */
	public double[] getDoubleArray();
	
	/**
	 * Increments the position by one. </p>
	 * Use {@link #isFloat()} to determine whether the correct data type is at the next position.
	 * @return float value at the next position.
	 * @throws NoSuchElementException
	 */
	public float getFloat();
	
	/**
	 * Increments the position by one. </p>
	 * Use {@link #isFloatArray()} to determine whether the correct data type is at the next position.
	 * @return float[] value at the next position.
	 * @throws NoSuchElementException
	 */
	public float[] getFloatArray();
	
	/**
	 * Increments the position by one. </p>
	 * Use {@link #isInt()} to determine whether the correct data type is at the next position.
	 * @return int value at the next position.
	 * @throws NoSuchElementException
	 */
	public int getInt();
	
	/**
	 * Increments the position by one. </p>
	 * Use {@link #isIntArray()} to determine whether the correct data type is at the next position.
	 * @return int[] value at the next position.
	 * @throws NoSuchElementException
	 */
	public int[] getIntArray();
	
	 // TODO: Re-add this feature when Polysun updates to Java 8 SE
//	/**
//	 * Increments the position by one. </p>
//	 * Use {@link #isLocalDateTime()} to determine whether the correct data type is at the next position.
//	 * @return LocalDateTime value at the next position.
//	 * @throws NoSuchElementException
//	 */
//	public LocalDateTime getLocalDateTime();
	
	 // TODO: Re-add this feature when Polysun updates to Java 8 SE
//	/**
//	 * Increments the position by one. </p>
//	 * Use {@link #isLocalDateTimeArray()} to determine whether the correct data type is at the next position.
//	 * @return LocalDateTime[] value at the next position.
//	 * @throws NoSuchElementException
//	 */
//	public LocalDateTime[] getLocalDateTimeArray();
	
	/**
	 * Increments the position by one. </p>
	 * Use {@link #isLong()} to determine whether the correct data type is at the next position.
	 * @return long value at the next position.
	 * @throws NoSuchElementException
	 */
	public long getLong();
	
	/**
	 * Increments the position by one. </p>
	 * Use {@link #isLongArray()} to determine whether the correct data type is at the next position.
	 * @return long[] value at the next position.
	 * @throws NoSuchElementException
	 */
	public long[] getLongArray();
	
	/**
	 * @return The number of data values that can be stored within this buffer.
	 */
	public int getNumDataValues();
	
	/** 
	 * Returns this data buffer's position.
	 * If this method returns -1, {@link #incrementPosition()} must be called before extracting data.
	 */
	public int getPosition();
	
	/**
	 * Increments the position by one. </p>
	 * Use {@link #isString()} to determine whether the correct data type is at the next position.
	 * @return String value at the next position.
	 * @throws NoSuchElementException
	 */
	public String getString();
	
	/**
	 * Increments the position by one. </p>
	 * Use {@link #isStringArray()} to determine whether the correct data type is at the next position.
	 * @return String[] value at the next position.
	 * @throws NoSuchElementException
	 */
	public String[] getStringArray();
	
	/**
	 * Returns Intended for enabling the dynamic retrieval of data from the buffer.
	 * Throws an exception if position is out of bounds.
	 * @return a ForteDataType enumeration representing the FORTE data type at the next position.
	 */
	public ForteDataType getTypeAtNextPosition();
	
	/**
	 * Intended for enabling the dynamic retrieval of data from the buffer.
	 * Throws an exception if position is out of bounds.
	 * @param position
	 * @return a ForteDataType enumeration representing the FORTE data type at the specified position.
	 */
	public ForteDataType getTypeAtPosition(int position);
	
	/**
	 * Increments this buffer's position by 1.
	 * @return true if Position was successfully incremented, false otherwise
	 */
	public boolean incrementPosition();
	
	/**
	 * Intended for enabling the dynamic retrieval of data from the buffer.
	 * Throws an exception if position is out of bounds.
	 * @param position
	 * @return true if the data at the specified position is an array and false otherwise.
	 */
	public boolean isArrayAtPosition(int position);
	
	/**
	 * @return true if value at next position is of type boolean.
	 * If the position is at the end of the data buffer, the current position is checked.
	 */
	public boolean isBool();
	
	/**
	 * @return true if value at next position is of type boolean[].
	 * If the position is at the end of the data buffer, the current position is checked.
	 */
	public boolean isBoolArray();
	
	/**
	 * @return true if value at next position is of type DateAndTime.
	 * If the position is at the end of the data buffer, the current position is checked.
	 */
	public boolean isDateAndTime();
	
	/**
	 * @return true if value at next position is of type DateAndTime[].
	 * If the position is at the end of the data buffer, the current position is checked.
	 */
	public boolean isDateAndTimeArray();
	
	/**
	 * @return true if value at next position is of type double.
	 * If the position is at the end of the data buffer, the current position is checked.
	 */
	public boolean isDouble();
	
	/**
	 * @return true if value at next position is of type double[].
	 * If the position is at the end of the data buffer, the current position is checked.
	 */
	public boolean isDoubleArray();
	
	/**
	 * @return true if value at next position is of type float.
	 * If the position is at the end of the data buffer, the current position is checked.
	 */
	public boolean isFloat();
	
	/**
	 * @return true if value at next position is of type float[].
	 * If the position is at the end of the data buffer, the current position is checked.
	 */
	public boolean isFloatArray();
	
	/**
	 * @return true if value at next position is of type int.
	 * If the position is at the end of the data buffer, the current position is checked.
	 */
	public boolean isInt();
	
	/**
	 * @return true if value at next position is of type int[].
	 * If the position is at the end of the data buffer, the current position is checked.
	 */
	public boolean isIntArray();
	
	 // TODO: Re-add this feature when Polysun updates to Java 8 SE
//	/**
//	 * @return true if value at next position is of type LocalDateTime.
//	 * If the position is at the end of the data buffer, the current position is checked.
//	 */
//	public boolean isLocalDateTime();
	 // TODO: Re-add this feature when Polysun updates to Java 8 SE
//	/**
//	 * @return true if value at next position is of type LocalDateTime[].
//	 * If the position is at the end of the data buffer, the current position is checked.
//	 */
//	public boolean isLocalDateTimeArray();
	
	/**
	 * @return true if value at next position is of type long.
	 * If the position is at the end of the data buffer, the current position is checked.
	 */
	public boolean isLong();
	
	/**
	 * @return true if value at next position is of type long[].
	 * If the position is at the end of the data buffer, the current position is checked.
	 */
	public boolean isLongArray();
	
	/**
	 * @return true if value at next position is of type String.
	 * If the position is at the end of the data buffer, the current position is checked.
	 */
	public boolean isString();
	
	/**
	 * @return true if value at next position is of type String[].
	 * If the position is at the end of the data buffer, the current position is checked.
	 */
	public boolean isStringArray();
	
	/**
	 * Use this method with caution. It is recommended to use the class's type-safe built-in iteration methods instead.
	 * @see {@link #isDouble()}, {@link #getDouble()} {@link #isDoubleArray()}, {@link #getDoubleArray()},
	 * {@link #getBool()}, {@link #getInt()} {@link #getLong()}, {@link #getFloat()}, {@link #getString()}, 
	 * {@link #getDateAndTime()}, {@link #getLocalDateTime()},
	 * {@link #getBoolArray()}, {@link #getIntArray()} {@link #getLongArray()}, {@link #getFloatArray()}, {@link #getStringArray()}, 
	 * {@link #getDateAndTimeArray()}, {@link #getLocalDateTimeArray()}, etc.
	 * @return Iterator over the elements in this buffer in proper sequence.
	 */
	public Iterator<Object> iterator();
	
	/**
	 * Adds a boolean to be sent to FORTE as an IEC 61499 BOOLEAN to buffer
	 * @param value
	 * @return true if more data can be added, false if not
	 */
	public boolean put(boolean value);
	
	/**
	 * Adds a boolean[] array to be sent to FORTE as an IEC 61499 BOOLEAN array to buffer
	 * @param value
	 * @return true if more data can be added, false if not
	 */
	public boolean put(boolean[] value);
	
	/**
	 * Adds a DateAndTime object to be sent to FORTE as IEC 61499 DATE_AND_TIME data to buffer
	 * @param value
	 * @return true if more data can be added, false if not
	 */
	public boolean put(DateAndTime value);
	
	/**
	 * Adds a DateAndTime[] array to be sent to FORTE as an IEC 61499 DATE_AND_TIME array to buffer
	 * @param value
	 * @return true if more data can be added, false if not
	 */
	public boolean put(DateAndTime[] value);
	
	/**
	 * Adds a double to be sent to FORTE as IEC 61499 LREAL to buffer
	 * @param value
	 * @return true if more data can be added, false if not
	 */
	public boolean put(double value);
	
	/**
	 * Adds a double[] array to be sent to FORTE as an IEC 61499 LREAL array to buffer
	 * @param value
	 * @return true if more data can be added, false if not
	 */
	public boolean put(double[] value);
	
	/**
	 * Adds a float to be sent to FORTE as IEC 61499 REAL to buffer
	 * @param value
	 * @return true if more data can be added, false if not
	 */
	public boolean put(float value);
	
	/**
	 * Adds a float[] array to be sent to FORTE as an IEC 61499 REAL array to buffer
	 * @param value
	 * @return true if more data can be added, false if not
	 */
	public boolean put(float[] value);
	
	/**
	 * Adds a 32 bit int to be sent to FORTE as one of the IEC 61499 32 bit integer types to buffer
	 * @param value Value to be converted to byte data
	 * @return true if more data can be added, false if not
	 */
	public boolean put(int value);
	
	/**
	 * Adds a 32 bit int[] array to be sent to FORTE as an IEC 61499 32 bit integer array to buffer
	 * @param value
	 * @return true if more data can be added, false if not
	 */
	public boolean put(int[] value);
	
	 // TODO: Re-add this feature when Polysun updates to Java 8 SE
//	/**
//	 * Adds a LocalDateTime object to be sent to FORTE as a IEC 61499 DATE_AND_TIME value to buffer
//	 * @param value
//	 */
//	public boolean put(LocalDateTime value);
	
	 // TODO: Re-add this feature when Polysun updates to Java 8 SE
//	/**
//	 * Adds a LocalDateTime[] array to be sent to FORTE as an IEC 61499 DATE_AND_TIME array to buffer
//	 * @param value
//	 * @return true if more data can be added, false if not
//	 */
//	public boolean put(LocalDateTime[] value);
	
	/**
	 * Adds a 64 bit long integer to be sent to FORTE as an IEC 61499 integer to buffer
	 * @param value
	 * @return true if more data can be added, false if not
	 */
	public boolean put(long value);
	
	/**
	 * Adds a 64 bit long[] integer array to be sent to FORTE as an IEC 61499 integer array to buffer
	 * @param value
	 * @return true if more data can be added, false if not
	 */
	public boolean put(long[] value);
	
	/**
	 * Adds a String to be sent to FORTE as an IEC 61499 STRING to buffer
	 * @param value
	 * @return true if more data can be added, false if not
	 */
	public boolean put(String value);
	
	/**
	 * Adds a String[] array to be sent to FORTE as an IEC 61499 STRING array to buffer
	 * @param value
	 * @return true if more data can be added, false if not
	 */
	public boolean put(String[] value);
	
	/**
	 * Translates byte data representing FORTE data types buffered within the internal DataInputStream to Java data types and stores the Java data types in a buffer.
	 * {@link #reset()} is called after completing the write operation.
	 * Before calling this method, a data input layer must be set using {@link #setInputLayer(AbstractCommunicationLayer)}.
	 * @param iStream DataInputStrem connected to a FORTE CLIENT or SERVER function block
	 * @throws IOException
	 * @throws UnsupportedForteDataTypeException
	 */
	public void recvData() throws IOException, UnsupportedForteDataTypeException;
	
	/**
	 * Resets the position to its initialized state and clears the internal byte buffer. 
	 */
	public void reset();
	
	/**
	 * Resets the position to its initialized state.
	 */
	public void rewind();
	
	/**
	 * Sets a DateAndTime object as reference for any new DateAndTime objects received.
	 */
	public void setDateAndTimeReference(DateAndTime dt);
	
	/** 
	 * Sets the position. The position is limited to [-1, {@link #getNumDataValues()}].
	 * -1 is the initialized state.
	 * @param position
	 */
	public void setPosition(int position);
	
	/**
	 * Writes the data stored within this instance's byte buffer to a DataOutputStream.
	 * {@link #reset()} is called after completing the write operation.
	 * Before calling this method, the data output layer must be set using {@link #setOutputLayer(AbstractCommunicationLayer)}.
	 * @throws IOException
	 */
	public void sendData() throws IOException;
	
	/** Sends a response (a single byte with the value of 5 */
	public void sendRsp() throws IOException;
	
	/**
	 * Closes the connection.
	 */
	public void disconnect() throws IOException;
	
	/**
	 * @return true if the socket is connected, false otherwise.
	 */
	public boolean isConnected();
}

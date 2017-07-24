package de.htw.berlin.polysun4diac.forte.comm;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import de.htw.berlin.polysun4diac.forte.datatypes.DateAndTime;
import de.htw.berlin.polysun4diac.forte.datatypes.ForteDataType;

/**
 * Abstract class that combines the ICommunicationLayer and IForteSocket interfaces into one interface.
 * It also adds an abstract {@link #initialise(List, List)} method used to initialise the data types.
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see IForteSocket
 * @see ICommunicationLayer
 *
 */
public abstract class AbstractDataBufferLayer extends AbstractCommunicationLayer implements IForteSocket {

	private static final long serialVersionUID = 6919139571742000393L;

	@Override
	public abstract byte[] array();

	@Override
	public abstract int capacity();

	@Override
	public abstract boolean getBool();

	@Override
	public abstract boolean[] getBoolArray();

	@Override
	public abstract DateAndTime getDateAndTime();

	@Override
	public abstract DateAndTime[] getDateAndTimeArray();

	@Override
	public abstract double getDouble();

	@Override
	public abstract double[] getDoubleArray();

	@Override
	public abstract float getFloat();

	@Override
	public abstract float[] getFloatArray();

	@Override
	public abstract int getInt();

	@Override
	public abstract int[] getIntArray();

	@Override
	public abstract long getLong();

	@Override
	public abstract long[] getLongArray();

	@Override
	public abstract int getNumDataValues();

	@Override
	public abstract int getPosition();

	@Override
	public abstract String getString();

	@Override
	public abstract String[] getStringArray();

	@Override
	public abstract ForteDataType getTypeAtNextPosition();

	@Override
	public abstract ForteDataType getTypeAtPosition(int position);

	@Override
	public abstract boolean incrementPosition();

	@Override
	public abstract boolean isArrayAtPosition(int position);

	@Override
	public abstract boolean isBool();

	@Override
	public abstract boolean isBoolArray();

	@Override
	public abstract boolean isDateAndTime();

	@Override
	public abstract boolean isDateAndTimeArray();

	@Override
	public abstract boolean isDouble();
	@Override
	public abstract boolean isDoubleArray();

	@Override
	public abstract boolean isFloat();

	@Override
	public abstract boolean isFloatArray();

	@Override
	public abstract boolean isInt();

	@Override
	public abstract boolean isIntArray();

	@Override
	public abstract boolean isLong();

	@Override
	public abstract boolean isLongArray();

	@Override
	public abstract boolean isString();

	@Override
	public abstract boolean isStringArray();

	@Override
	public abstract Iterator<Object> iterator();

	@Override
	public abstract boolean put(boolean value);

	@Override
	public abstract boolean put(boolean[] value);

	@Override
	public abstract boolean put(DateAndTime value);

	@Override
	public abstract boolean put(DateAndTime[] value);

	@Override
	public abstract boolean put(double value);

	@Override
	public abstract boolean put(double[] value);

	@Override
	public abstract boolean put(float value);
	@Override
	public abstract boolean put(float[] value);

	@Override
	public abstract boolean put(int value);

	@Override
	public abstract boolean put(int[] value);

	@Override
	public abstract boolean put(long value);

	@Override
	public abstract boolean put(long[] value);

	@Override
	public abstract boolean put(String value);

	@Override
	public abstract boolean put(String[] value);

	@Override
	public abstract void reset();

	@Override
	public abstract void rewind();

	@Override
	public abstract void setDateAndTimeReference(DateAndTime dt);

	@Override
	public abstract void setPosition(int position);

	@Override
	public abstract void sendData() throws IOException;

	@Override
	public abstract void sendRsp() throws IOException;

	@Override
	public abstract void disconnect() throws IOException;

	@Override
	public abstract boolean isConnected();
	
	/**
	 * Initializes this object's buffer that can hold multiple arrays of various data types
	 * @param dataTypes <code>List</code> containing the data type enumerations.
	 * This could be the inputs of outputs set in a CommLayerParams object.
	 * @param arraySizes <code>List</code> containing the corresponding array sizes (1 for non-arrays)
	 * This could be the array sizes set in a CommLayerParams object.
	 */
	public abstract void initialise(List<Enum<?>> dataTypes, List<Integer> arraySizes);
}

package de.htw.berlin.polysun4diac.forte.comm;


import java.io.IOException;
//import java.time.LocalDateTime;  // TODO: Re-add this feature when Polysun updates to Java 8 SE
import java.util.Iterator;

import de.htw.berlin.polysun4diac.forte.datatypes.DateAndTime;
import de.htw.berlin.polysun4diac.forte.datatypes.ForteDataType;

/**
 * Top level OSI layer for handling communication with IEC 61499 communication service interface function blocks (CSIFBs)
 * running on 4diac-RTE (FORTE). This layer is intended for use only if the number of function block inputs differs
 * from the number of function block outputs, since the ForteDataBufferLayer by itself can only communicate with
 * function blocks whose number of inputs do not differ from their number of outputs.
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 */
public class CommFunctionBlockLayer extends AbstractCommunicationLayer implements IForteSocket {

	private static final long serialVersionUID = 1031792403895195015L;
	
	/** Output layer for handling data received from FORTE */
	AbstractDataBufferLayer mOutputLayer;
	/** Input layer for handling data that is sent to FORTE */
	AbstractDataBufferLayer mInputLayer;
	
	/**
	 * This method is intended to allow array-backed buffers to be passed to native code more efficiently.
	 * Modifications to this buffer's content will cause the returned array's content to be modified, and vice versa. 
	 * @return the array that backs this buffer for data to be sent (optional operation). 
	 */
	public byte[] array() {
		return getInputLayer().array();
	}

	/** Returns the input byte buffer's capacity */
	public int capacity() {
		return getInputLayer().capacity();
	}

	@Override
	public boolean getBool() {
		return getOutputLayer().getBool();
	}

	@Override
	public boolean[] getBoolArray() {
		return getOutputLayer().getBoolArray();
	}

	@Override
	public DateAndTime getDateAndTime() {
		return getOutputLayer().getDateAndTime();
	}

	@Override
	public DateAndTime[] getDateAndTimeArray() {
		return getOutputLayer().getDateAndTimeArray();
	}

	@Override
	public double getDouble() {
		return getOutputLayer().getDouble();
	}

	@Override
	public double[] getDoubleArray() {
		return getOutputLayer().getDoubleArray();
	}

	@Override
	public float getFloat() {
		return getOutputLayer().getFloat();
	}

	@Override
	public float[] getFloatArray() {
		return getOutputLayer().getFloatArray();
	}

	@Override
	public int getInt() {
		return getOutputLayer().getInt();
	}

	@Override
	public int[] getIntArray() {
		return getOutputLayer().getIntArray();
	}

	 // TODO: Re-add this feature when Polysun updates to Java 8 SE
//	@Override
//	public LocalDateTime getLocalDateTime() {
//		return getOutputLayer().getLocalDateTime();
//	}
	 // TODO: Re-add this feature when Polysun updates to Java 8 SE
//	@Override
//	public LocalDateTime[] getLocalDateTimeArray() {
//		return getOutputLayer().getLocalDateTimeArray();
//	}

	@Override
	public long getLong() {
		return getOutputLayer().getLong();
	}

	@Override
	public long[] getLongArray() {
		return getOutputLayer().getLongArray();
	}

	@Override
	public int getNumDataValues() {
		return getOutputLayer().getNumDataValues();
	}

	@Override
	public int getPosition() {
		return getOutputLayer().getPosition();
	}

	@Override
	public String getString() {
		return getOutputLayer().getString();
	}

	@Override
	public String[] getStringArray() {
		return getOutputLayer().getStringArray();
	}

	@Override
	public ForteDataType getTypeAtNextPosition() {
		return getOutputLayer().getTypeAtNextPosition();
	}

	@Override
	public ForteDataType getTypeAtPosition(int position) {
		return getOutputLayer().getTypeAtPosition(position);
	}

	@Override
	public boolean incrementPosition() {
		return getOutputLayer().incrementPosition();
	}

	@Override
	public boolean isArrayAtPosition(int position) {
		return getOutputLayer().isArrayAtPosition(position);
	}

	@Override
	public boolean isBool() {
		return getOutputLayer().isBool();
	}

	@Override
	public boolean isBoolArray() {
		return getOutputLayer().isBoolArray();
	}

	@Override
	public boolean isDateAndTime() {
		return getOutputLayer().isDateAndTime();
	}

	@Override
	public boolean isDateAndTimeArray() {
		return getOutputLayer().isDateAndTimeArray();
	}

	@Override
	public boolean isDouble() {
		return getOutputLayer().isDouble();
	}

	@Override
	public boolean isDoubleArray() {
		return getOutputLayer().isDoubleArray();
	}

	@Override
	public boolean isFloat() {
		return getOutputLayer().isFloat();
	}

	@Override
	public boolean isFloatArray() {
		return getOutputLayer().isFloatArray();
	}
	
	@Override
	public boolean isInt() {
		return getOutputLayer().isInt();
	}
	
	@Override
	public boolean isIntArray() {
		return getOutputLayer().isIntArray();
	}
	
	 // TODO: Re-add this feature when Polysun updates to Java 8 SE
//	@Override
//	public boolean isLocalDateTime() {
//		return getOutputLayer().isLocalDateTime();
//	}
	
	 // TODO: Re-add this feature when Polysun updates to Java 8 SE
//	@Override
//	public boolean isLocalDateTimeArray() {
//		return getOutputLayer().isLocalDateTimeArray();
//	}
	
	@Override
	public boolean isLong() {
		return getOutputLayer().isLong();
	}
	
	@Override
	public boolean isLongArray() {
		return getOutputLayer().isLongArray();
	}

	@Override
	public boolean isString() {
		return getOutputLayer().isString();
	}

	@Override
	public boolean isStringArray() {
		return getOutputLayer().isStringArray();
	}

	@Override
	public Iterator<Object> iterator() {
		return getOutputLayer().iterator();
	}

	@Override
	public boolean put(boolean value) {
		return getInputLayer().put(value);
	}

	@Override
	public boolean put(boolean[] value) {
		return getInputLayer().put(value);
	}

	@Override
	public boolean put(DateAndTime value) {
		return getInputLayer().put(value);
	}

	@Override
	public boolean put(DateAndTime[] value) {
		return getInputLayer().put(value);
	}

	@Override
	public boolean put(double value) {
		return getInputLayer().put(value);
	}

	@Override
	public boolean put(double[] value) {
		return getInputLayer().put(value);
	}

	@Override
	public boolean put(float value) {
		return getInputLayer().put(value);
	}

	@Override
	public boolean put(float[] value) {
		return getInputLayer().put(value);
	}

	@Override
	public boolean put(int value) {
		return getInputLayer().put(value);
	}

	@Override
	public boolean put(int[] value) {
		return getInputLayer().put(value);
	}

	 // TODO: Re-add this feature when Polysun updates to Java 8 SE
//	@Override
//	public boolean put(LocalDateTime value) {
//		return getInputLayer().put(value);
//	}
	 // TODO: Re-add this feature when Polysun updates to Java 8 SE
//	@Override
//	public boolean put(LocalDateTime[] value) {
//		return getInputLayer().put(value);
//	}

	@Override
	public boolean put(long value) {
		return getInputLayer().put(value);
	}

	@Override
	public boolean put(long[] value) {
		return getInputLayer().put(value);
	}

	@Override
	public boolean put(String value) {
		return getInputLayer().put(value);
	}

	@Override
	public boolean put(String[] value) {
		return getInputLayer().put(value);
	}

	@Override
	public void reset() {
		getInputLayer().reset();
		getOutputLayer().reset();
	}

	@Override
	public void rewind() {
		getInputLayer().rewind();
		getOutputLayer().rewind();
	}

	@Override
	public void setDateAndTimeReference(DateAndTime dt) {
		getOutputLayer().setDateAndTimeReference(dt);
		getInputLayer().setDateAndTimeReference(dt);
	}

	@Override
	public void setPosition(int position) {
		getOutputLayer().setPosition(position);
	}

	@Override
	public void sendData() throws IOException {
		getInputLayer().sendData();
	}
	
	@Override
	public void sendRsp() throws IOException {
		getInputLayer().sendRsp();
	}

	@Override
	public void disconnect() throws IOException {
		closeConnection();
	}
	
	@Override
	public boolean openConnection(CommLayerParams params) throws IOException {
		if (getInputLayer() != null && getOutputLayer() != null) {
			// Delegate connection opening to OSI layer below the input/output layers.
			setConnectionState(getBelow().openConnection(params));
			setIntermediateLayerConnectionstate();
			// Initialize intermediate layers
			getInputLayer().initialize(params.getInputs(), params.getInputArrayLengths());
			getOutputLayer().initialize(params.getOutputs(), params.getOutputArrayLengths());
		}
		return getConnectionState();
	}
	
	@Override
	public boolean closeConnection() throws IOException {
		if (getInputLayer() != null && getOutputLayer() != null) {
			// Delegate connection closing to OSI layer below the input/output layers.
			setConnectionState(getBelow().closeConnection());
			setIntermediateLayerConnectionstate();
		}
		return getConnectionState();
	}
	
	@Override
	public void sendData(byte[] data, int off, int len) throws IOException {
		getInputLayer().sendData();
	}
	
	@Override
	public void recvData() throws IOException {
		getOutputLayer().recvData();
	}
	
	@Override
	public byte readByte() throws IOException {
		return getOutputLayer().readByte();
	}
	
	@Override
	public long readLong() throws IOException {
		return getOutputLayer().readLong();
	}
	
	@Override
	public double readDouble() throws IOException {
		return getOutputLayer().readDouble();
	}
	
	@Override
	public float readFloat() throws IOException {
		return getOutputLayer().readFloat();
	}
	
	@Override
	public boolean isConnected() {
		return getConnectionState();
	}
	
	/**
	 * Updates the connection state of the input/output layers 
	 * just in case they are ever called.
	 */
	private void setIntermediateLayerConnectionstate() {
		getInputLayer().setConnectionState(getConnectionState());
		getOutputLayer().setConnectionState(getConnectionState());
	}
	
	/**
	 * @return the communication layer below the two ForteDataBufferLayers below this layer.
	 */
	public ICommunicationLayer getBelow() {
		return getInputLayer().getBelow();
	}
	
	@Override
	public void setBelow(ICommunicationLayer below) {
		setBelow(below, new ForteDataBufferLayer(), new ForteDataBufferLayer());
	}
	
	/**
	 * Sets the OSI layers below this layer.
	 * @param below OSI layer for handling further communication below the "output" and "input" params.
	 * @param output ForteDataBufferLayer for handling data that is received from FORTE.
	 * @param input ForteDataBufferLayer for handling data that is sent to FORTE.
	 */
	private void setBelow(ICommunicationLayer below, AbstractDataBufferLayer output, AbstractDataBufferLayer input) {
		mOutputLayer = output;
		mInputLayer = input;
		mOutputLayer.setBelow(below);;
		mInputLayer.setBelow(below);
	}
	
	/** returns {@link #mOutputLayer} for handling data that is received from FORTE. */
	private AbstractDataBufferLayer getOutputLayer() {
		return mOutputLayer;
	}
	
	/** returns {@link #mInputLayer} for handling data that is sent to FORTE. */
	private AbstractDataBufferLayer getInputLayer() {
		return mInputLayer;
	}
}

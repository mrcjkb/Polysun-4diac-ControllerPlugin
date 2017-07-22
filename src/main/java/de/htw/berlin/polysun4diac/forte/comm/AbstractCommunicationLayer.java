package de.htw.berlin.polysun4diac.forte.comm;

import java.io.IOException;
import java.io.Serializable;

/**
 * Abstract class providing the default implementation of an OSI layer.
 * Loosely based on the OSI layer interface implemented in FORTE.
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see <a href="https://www.eclipse.org/4diac/documentation/html/development/forte_communicationArchitecture.html">FORTE communication architecture</a>
 */
public abstract class AbstractCommunicationLayer implements Serializable, ICommunicationLayer {
	
	private static final long serialVersionUID = 1887877550487681139L;
	
	/**
	 * Communication layer above this layer
	 */
	private ICommunicationLayer mTopLayer;
	/**
	 * Communication layer below this layer
	 */
	private ICommunicationLayer mBottomLayer;
	/**
	 * Flag indicating whether this layer is connected (true) or not (false)
	 */
	private boolean mConnectionState = false;
	/**
	 * Byte array holding the raw data retrieved by calling {@link #recvData()}
	 */
	private byte[] mRawBytes;

	@Override
	public boolean openConnection(CommLayerParams params) throws IOException {
		if (getBelow() != null) {
			setConnectionState(getBelow().openConnection(params));
		}
		return getConnectionState();
	}
	
	@Override
	public boolean closeConnection() throws IOException {
		if (getBelow() != null) {
			setConnectionState(getBelow().closeConnection());
		}
		return getConnectionState();
	}
	
	@Override
	public void sendData(byte[] data, int off, int len) throws IOException  {
		getBelow().sendData(data, off, len);
	}
	
	@Override
	public void sendData(byte[] data) throws IOException {
		sendData(data, 0, data.length);
	}
	
	@Override
	public void recvData() throws IOException {
		getBelow().recvData();
	}
	
	/**
	 * @return the communication layer above this layer
	 */
	public ICommunicationLayer getAbove() {
		return mTopLayer;
	}
	
	@Override
	public ICommunicationLayer getBelow() {
		return mBottomLayer;
	}
	
	@Override
	public void setAbove(ICommunicationLayer layer) {
		mTopLayer = layer;
	}
	
	@Override
	public void setBelow(ICommunicationLayer layer) {
		mBottomLayer = layer;
		getBelow().setAbove(this);
	}
	
	@Override
	public byte readByte() throws IOException {
		return getBelow().readByte();
	}
	
	@Override
	public long readLong() throws IOException {
		return getBelow().readLong();
	}
	
	@Override
	public double readDouble() throws IOException {
		return getBelow().readDouble();
	}
	
	@Override
	public float readFloat() throws IOException {
		return getBelow().readFloat();
	}
	
	@Override
	public boolean getConnectionState() {
		return mConnectionState;
	}
	
	@Override
	public byte[] getRawBytes() {
		return mRawBytes;
	}
	
	/**
	 * Initializes the raw bytes to a certain size
	 */
	protected void setRawBytes(byte[] b) {
		mRawBytes = b;
	}
	
	/**
	 * Sets the raw bytes to a certain size
	 */
	protected void setRawBytes(byte[] b, int length) {
		for (int i = 0; i < length; i++) {
			mRawBytes[i] = b[i];
		}
	}
	
	/**
	 * Sets the raw bytes to a certain size beginning at offset off
	 */
	protected void setRawBytes(byte[] b, int off, int length) {
		for (int i = off; i < length; i++) {
			mRawBytes[i] = b[i];
		}
	}
	
	/**
	 * Sets the connection state
	 */
	protected void setConnectionState(boolean state) {
		mConnectionState = state;
	}

}

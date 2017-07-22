package de.htw.berlin.polysun4diac.forte.comm;

import java.io.IOException;

/**
 * Provides the public interface for an OSI layer.
 * Loosely based on the OSI layer interface implemented in FORTE.
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see <a href="https://www.eclipse.org/4diac/documentation/html/development/forte_communicationArchitecture.html">FORTE communication architecture</a>
 */
public interface ICommunicationLayer {
	/**
	 * Opens the connection and sets up the communication stack.
	 * If necessary, additional layers below this layer are created.
	 * @param params CommLayerParams object holding the connection info
	 * @return true if the connection was opened successfully, false otherwise
	 */
	public boolean openConnection(CommLayerParams params) throws IOException;
	
	/**
	 * Closes this layer and calls closeConnection() on the layer below.
	 * @return true if the connection was closed successfully, false otherwise
	 */
	public boolean closeConnection() throws IOException;
	
	/**
	 * Performs the necessary actions for sending data.
	 * @param data Byte array containing the data
	 * @param Offset (start of write operation)
	 * @param len Length of the data to be sent
	 */
	public void sendData(byte[] data, int off, int len) throws IOException;
	
	/**
	 * Performs the necessary actions for sending data.
	 * Equivalent to calling sendData(data, 0, data.length);
	 * @param data Byte array containing the data
	 */
	public void sendData(byte[] data) throws IOException;
	
	/**
	 * Performs the necessary actions for receiving data.
	 */
	public void recvData() throws IOException;
	
	/**
	 * @return the communication layer above this layer
	 */
	public ICommunicationLayer getAbove();
	
	/**
	 * @return the communication layer below this layer
	 */
	public ICommunicationLayer getBelow();
	
	/**
	 * Sets the communication layer above this layer.
	 */
	public void setAbove(ICommunicationLayer layer);
	
	/**
	 * Sets the communication layer below this layer.
	 */
	public void setBelow(ICommunicationLayer layer);
	
	/**
	 * Reads a byte from the communication layer below or the connected device if this layer is the bottom layer.
	 * @throws IOException
	 */
	public byte readByte() throws IOException;
	
	/**
	 * Reads 4 bytes from the communication layer below or the connected device if this layer is the bottom layer.
	 * @return the long value represented by the 4 bytes.
	 * @throws IOException
	 */
	public long readLong() throws IOException;
	
	/**
	 * Reads 8 bytes from the communication layer below or the connected device if this layer is the bottom layer.
	 * @return the double value represented by the 8 bytes.
	 * @throws IOException
	 */
	public double readDouble() throws IOException;
	
	/**
	 * Reads 4 bytes from the communication layer below or the connected device if this layer is the bottom layer.
	 * @return the float value represented by the 4 bytes.
	 * @throws IOException
	 */
	public float readFloat() throws IOException;
	
	/**
	 * @return true if the layer is connected, false otherwise.
	 */
	public boolean getConnectionState();
	
	/**
	 * @return a byte array holding the raw data retrieved by calling {@link #recvData()}
	 */
	public byte[] getRawBytes();
}

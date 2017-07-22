package de.htw.berlin.polysun4diac.forte.comm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Abstract bottom OSI layer for handling TCP/IP communication.
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see <a href="https://www.eclipse.org/4diac/documentation/html/development/forte_communicationArchitecture.html">FORTE communication architecture</a>
 */
public abstract class TCPcommunicationLayer extends AbstractCommunicationLayer {

	private static final long serialVersionUID = 4329823111612210389L;
	
	/** Client/Server socket */
	private Socket mSocket = new Socket();
	/** DataInputStream for reading data from the socket */
	private DataInputStream mIStream = null;
	/** Stream for sending data to the server */
	private DataOutputStream mOStream = null;
	
	@Override
	public boolean closeConnection() throws IOException {
		getInputStream().close();
		getOutputStream().close();
		getSocket().close();
		return getConnectionState();
	}
	
	/** @return {@link #mIStream} */
	protected DataInputStream getInputStream() {
		return mIStream;
	}
	
	/** Sets {@link #mIStream}. */
	protected void setInputStream(DataInputStream stream) {
		mIStream = stream;
	}
	
	/**
	 * Reads 
	 * @param buffer
	 * @throws IOException
	 */
	public void recvData(byte[] buffer) throws IOException {
		setRawBytes(buffer);
		getInputStream().read(getRawBytes());
	}
	
	@Override
	public void recvData() throws IOException {
		throw new IOException("recvData not supported for TCP communication. Use recvData(byete[] buffer) instead.");
	}
	
	@Override
	public byte readByte() throws IOException {
		return getInputStream().readByte();
	}
	
	@Override
	public long readLong() throws IOException {
		return getInputStream().readLong();
	}
	
	@Override
	public double readDouble() throws IOException {
		return getInputStream().readDouble();
	}
	
	@Override
	public float readFloat() throws IOException {
		return getInputStream().readFloat();
	}
	
	@Override
	public boolean getConnectionState() {
		return getSocket().isConnected();
	}
	
	@Override
	public void sendData(byte[] data, int off, int len) throws IOException {
		getOutputStream().write(data, off, len);
	}
	
	/**
	 * @return the internal client Socket
	 */
	protected Socket getSocket() {
		return mSocket;
	}
	
	/**
	 * Sets the socket object
	 */
	protected void setSocket(Socket s) {
		mSocket = s;
	}
	
	/**
	 * Sets {@link #mOStream}.
	 */
	protected void setOutputStream(DataOutputStream stream) {
		mOStream = stream;
	}
	
	/**
	 * Sets {@link #mIStream}.
	 */
	protected DataOutputStream getOutputStream() {
		return mOStream;
	}
}

package de.htw.berlin.polysun4diac.forte.comm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;

/**
 * Bottom OSI layer for handling UDP/IP communication of a subscriber.
 * Intended for communication with FORTE PUBLISHER function blocks.
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see <a href="https://www.eclipse.org/4diac/documentation/html/development/forte_communicationArchitecture.html">FORTE communication architecture</a>
 */
public class UDPsubscriberCommLayer extends UDPcommunicationLayer {

	private static final long serialVersionUID = 7986726965971088809L;
	/** Error message for attempted send operations */
	private static final String SENDERRMSG = "Subscribers cannot send data.";
	/** Maximum number of bytes to receive */
	private static final int MAXBYTES = 1472;
	
	/** UDP socket object */
	private MulticastSocket mSocket = null;
	/** IP address to listen on */
	private InetAddress mAddress;
	/** Number of bytes to receive */
	private int mBufferSize = MAXBYTES;
	/** ByteBuffer for storing received data for later use */
	private ByteBuffer mByteBuffer;
	
	@Override
	public boolean openConnection(CommLayerParams params) throws IOException {
		makeSocket(params.getPort());
		setAddress(params.getAddress());
		getSocket().joinGroup(params, null);
		setConnectionState(true);
		return getConnectionState();
	}
	
	@Override
	public boolean closeConnection() throws IOException {
		getSocket().leaveGroup(getAddress());
		return super.closeConnection();
	}
	
	@Override
	public void recvData() throws IOException {
		if (getRawBytes() != null) {
			recvData(getRawBytes());
		} else {
			initBuffer();
		}
		// Clear internal ByteBuffer and populate it with received bytes
		getBuffer().clear();
		getBuffer().put(getRawBytes());
		getBuffer().rewind();
	}
	
	@Override
	public void sendData(byte[] data, int off, int len) throws IOException  {
		throw new IOException(SENDERRMSG);
	}
	
	@Override
	public byte readByte() throws IOException {
		recvDataIfIsLastPosition();
		return getBuffer().get();
	}
	
	@Override
	public long readLong() throws IOException {
		recvDataIfIsLastPosition();
		return getBuffer().getLong();
	}
	
	@Override
	public double readDouble() throws IOException {
		recvDataIfIsLastPosition();
		return getBuffer().getDouble();
	}
	
	@Override
	public float readFloat() throws IOException {
		recvDataIfIsLastPosition();
		return getBuffer().getFloat();
	}
	
	@Override
	protected MulticastSocket getSocket() {
		return mSocket;
	}

	@Override
	protected void makeSocket(int port) throws IOException {
		mSocket = new MulticastSocket(port);
	}
	
	/** 
	 * Performs the buffer initialization.
	 * @throws IOException
	 */
	private void initBuffer() throws IOException {
		byte[] buf = new byte[getBufferSize()];
		DatagramPacket packet = recvData(buf);
		setBufferSize(packet.getLength());
		setRawBytes(new byte[getBufferSize()]);
		setRawBytes(buf, getBufferSize());
		mByteBuffer = ByteBuffer.allocate(getBufferSize());
	}
	
	/** 
	 * Waits for a packet from a publisher and writes the byte data to a buffer
	 * @param buf the buffer to write to
	 * @return The received packet
	 * @throws IOException 
	 * */
	private DatagramPacket recvData(byte[] buf) throws IOException {
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		getSocket().receive(packet);
		return packet;
	}
	
	/** Sets the IP address to listen on */
	private void setAddress(InetAddress addr) {
		mAddress = addr;
	}
	
	/** @return the IP address this object listens on */
	private InetAddress getAddress() {
		return mAddress;
	}
	
	/** Sets the buffer size */ 
	private void setBufferSize(int size) {
		mBufferSize = size;
	}
	
	/** @return {@link #mBufferSize} */
	private int getBufferSize() {
		return mBufferSize;
	}
	
	/** @return {@link mByteBuffer} */
	private ByteBuffer getBuffer() {
		return mByteBuffer;
	}
	
	/**
	 * calls recvData() if the current position is the last possible position of the buffer or if the buffer has not yet been initialized
	 * @throws IOException 
	 */
	private void recvDataIfIsLastPosition() throws IOException {
		if (getBuffer() == null || getBuffer().position() >= getBuffer().capacity()) {
			recvData();
		}
	}
}

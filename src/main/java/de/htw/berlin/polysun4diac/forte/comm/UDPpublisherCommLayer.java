package de.htw.berlin.polysun4diac.forte.comm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;

/**
 * Bottom OSI layer for handling UDP/IP communication of a publisher.
 * Intended for communication with FORTE SUBSCRIBER function blocks.
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see <a href="https://www.eclipse.org/4diac/documentation/html/development/forte_communicationArchitecture.html">FORTE communication architecture</a>
 */
public class UDPpublisherCommLayer extends UDPcommunicationLayer {

	private static final long serialVersionUID = -6047738437353930216L;
	/** Error message for attempted read/receipt operations */
	private static final String RECVERRMSG = "Publishers cannot receive data.";
	
	/** Parameters for sending packet data */
	InetSocketAddress mParams = null;
	/** UDP socket object */
	MulticastSocket mSocket = null;
	
	@Override
	public boolean openConnection(CommLayerParams params) throws IOException {
		makeSocket(params.getPort());
		setParams(params);
		setConnectionState(true);
		return getConnectionState();
	}
	
	@Override
	public void sendData(byte[] data, int off, int len) throws IOException  {
		if (getRawBytes() == null) { // Initialize
			setRawBytes(new byte[len]);
		}
		setRawBytes(data, off, len);
		DatagramPacket packet = new DatagramPacket(getRawBytes(), len, getParams());
		getSocket().send(packet);
	}
	
	@Override
	public void recvData() throws IOException {
		throw new IOException(RECVERRMSG);
	}
	
	@Override
	public boolean getConnectionState() {
		return super.getConnectionState() && !getParams().equals(null);
	}
	
	@Override
	public byte readByte() throws IOException {
		throw new IOException(RECVERRMSG);
	}
	
	@Override
	public long readLong() throws IOException {
		throw new IOException(RECVERRMSG);
	}
	
	@Override
	public double readDouble() throws IOException {
		throw new IOException(RECVERRMSG);
	}
	
	@Override
	public float readFloat() throws IOException {
		throw new IOException(RECVERRMSG);
	}
	
	@Override
	protected MulticastSocket getSocket() {
		return mSocket;
	}
	
	@Override
	protected void makeSocket(int port) throws IOException {
		mSocket = new MulticastSocket(port);
	}
	
	/** @return parameters for sending packet data */
	private SocketAddress getParams() {
		return mParams;
	}
	
	/** Sets the parameters for sending packet data */
	private void setParams(InetSocketAddress params) {
		mParams = params;
	}
}

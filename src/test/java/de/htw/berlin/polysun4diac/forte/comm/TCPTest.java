package de.htw.berlin.polysun4diac.forte.comm;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test cases for TCP communication using the TCPclientCommLayer and the TCPserverCommLayer.
 * The tests are carried out by echoing each other in a multi-threaded environment.
 * The assertion tests are carried out in the Echo subclass and the readDataTest() method.
 * 
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 *
 */
public class TCPTest {
	
	/** String to be sent in simple byte test */
	private static final String TESTSTRING = "TCP layer working correctly.";
	/** Data to be sent in buffer test */
	private static final byte B = 1;
	/** Data to be sent in buffer test */
	private static final long L = 2;
	/** Data to be sent in buffer test */
	private static final double D = 3.5;
	/** Data to be sent in buffer test */
	private static final float F = 4.5f;
	/** Total number of bytes to allocate for buffer test */
	private static final int TOT_BYTES = 21;
	/** IP address **/
	private static final String IP = "localhost";
	
	private static final float TEST_TOLERANCE = 0.0f;
	
	CommLayerParams params;
	CommLayerParams params2;
	Echo<TCPserverCommLayer> esrv;
	TCPclientCommLayer tcpClient;
	
	@Before
	public void setUp() throws Exception {
		params = new CommLayerParams(InetAddress.getByName(IP), 61501);
		esrv = new Echo<TCPserverCommLayer>(new TCPserverCommLayer());
		esrv.start();
		tcpClient = new TCPclientCommLayer();
		Thread.sleep(200); // Give echo time to open connection
		tcpClient.openConnection(params);
	}
	
	@After
	public void tearDown() throws Exception {
		tcpClient.closeConnection();
	}
	
	@Test
	public void echoTest() throws Exception {
		ByteBuffer bb = populateByteBuffer(B, L, D, F);
		tcpClient.sendData(bb.array());
		readDataTest(tcpClient);
		tcpClient.sendData(TESTSTRING.getBytes());
	}
	
	private ByteBuffer populateByteBuffer(byte b, long l, double d, float f) {
		ByteBuffer bb = ByteBuffer.allocate(TOT_BYTES);
		bb.put(b);
		bb.putLong(l);
		bb.putDouble(d);
		bb.putFloat(f);
		return bb;
	}
	
	private <T extends AbstractCommunicationLayer> ByteBuffer readDataTest(T socket) throws IOException {
		byte b = socket.readByte();
		assertEquals("Byte", B, b);
		long l = socket.readLong();
		assertEquals("Long", L, l);
		double d = socket.readDouble();
		assertEquals("Double", D, d, TEST_TOLERANCE);
		float f = socket.readFloat();
		assertEquals("Float", F, f, TEST_TOLERANCE);
		ByteBuffer bb = populateByteBuffer(b, l, d, f);
		return bb;
	}
	
	/**
	 * Wrapper for the TCPserverCommLayer & TCPclientCommLayer classes. Runs on a second thread
	 * and waits for data to arrive. Assertion tests are performed on each set of data.
	 */
	public class Echo<T extends TCPcommunicationLayer> extends Thread {
		private T mSocket;
		
		public Echo(T socket) throws IOException {
			mSocket = socket;
		}
		
		public void run() {
			try {
				mSocket.openConnection(params);
				ByteBuffer bb = readDataTest(mSocket);
				mSocket.sendData(bb.array());
				mSocket.recvData(new byte[TESTSTRING.length()]);
				assertEquals("Raw bytes",  TESTSTRING, new String(mSocket.getRawBytes()));
			} catch (IOException e) {
				fail("IOException");
			}
			String s = new String(mSocket.getRawBytes());
			assertEquals("Normal receival", TESTSTRING, s);
			try {
				mSocket.closeConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

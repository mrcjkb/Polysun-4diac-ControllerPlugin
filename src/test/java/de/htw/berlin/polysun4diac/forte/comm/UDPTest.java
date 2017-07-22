package de.htw.berlin.polysun4diac.forte.comm;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * JUnit test cases for UDP communication using the UDPsubscriberCommLayer and the UDPpublisherCommLayer.
 * The tests are carried out by echoing each other in a multi-threaded environment.
 * The assertion tests are carried out in the EchoPublisher & EchoSubscriber subclasses.
 * 
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 *
 */
public class UDPTest {

	/** Number of tests to be carried out */
	private static final int NUM_TESTS = 4;
	/** String to be sent in simple byte test */
	private static final String TESTSTRING = "UDP layer working correctly.";
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
	private static final String IP = "239.192.0.2";
	
	private static final float TEST_TOLERANCE = 0.0f;
	
	CommLayerParams params1;
	CommLayerParams params2;
	EchoPublisher pub1;
	EchoPublisher pub2;
	EchoSubscriber1 sub1;
	EchoSubscriber2 sub2;
	
	@Before
	public void setUp() throws Exception {
		params1 = new CommLayerParams(InetAddress.getByName(IP), 61500);
		params2 = new CommLayerParams(InetAddress.getByName(IP), 61501);
		params1.setServiceType(ForteServiceType.SUBSCRIBER);
		params2.setServiceType(ForteServiceType.SUBSCRIBER);
		pub1 = new EchoPublisher(params1);
		pub2 = new EchoPublisher(params2);
		sub1 = new EchoSubscriber1(params1);
		sub1.start();
		sub2 = new EchoSubscriber2(params2);
		sub2.start();
	}
	
	@After
	public void tearDown() throws Exception {
		pub1.closeConnection();
	}
	
	@Test
	public void simpleByteEchoTest() throws Exception {
		for (int i = 0; i < NUM_TESTS; i++) {
			pub1.sendEcho(TESTSTRING);
		}
	}
	
	@Test
	public void subscriberBufferTest() throws Exception {
		ByteBuffer bb = ByteBuffer.allocate(TOT_BYTES);
		bb.put(B);
		bb.putLong(L);
		bb.putDouble(D);
		bb.putFloat(F);
		pub2.sendEcho(bb.array());
	}

	/**
	 * Wrapper for the UDPsubscriberCommLayer class. Runs on a second thread and waits for
	 * data to be sent to it. Performs an assertion test on the received data.
	 */
	private class EchoSubscriber1 extends Thread {
		private UDPsubscriberCommLayer mSubscriber;
		
		public EchoSubscriber1(CommLayerParams parameters) throws IOException {
			mSubscriber = new UDPsubscriberCommLayer();
			mSubscriber.openConnection(parameters);
		}
		
		public void run() {
			for (int i = 0; i < NUM_TESTS; i++) {
				try {
					mSubscriber.recvData();
					String received = new String(mSubscriber.getRawBytes());
					assertEquals("String echo", TESTSTRING, received);
				} catch (IOException e) {
					e.printStackTrace();
					fail("IOException");
				}
			}
			try {
				mSubscriber.closeConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Wrapper for the UDPsubscriberCommLayer class. Runs on a second thread and waits for
	 * data to be sent to it. Performs assertion tests on the received data.
	 */
	private class EchoSubscriber2 extends Thread {
		private UDPsubscriberCommLayer mSubscriber;
		
		public EchoSubscriber2(CommLayerParams parameters) throws IOException {
			mSubscriber = new UDPsubscriberCommLayer();
			mSubscriber.openConnection(parameters);
		}
		
		public void run() {
			try {
				assertEquals("Byte", B, mSubscriber.readByte());
				assertEquals("Long", L, mSubscriber.readLong());
				assertEquals("Double", D, mSubscriber.readDouble(), TEST_TOLERANCE);
				assertEquals("Float", F, mSubscriber.readFloat(), TEST_TOLERANCE);
			} catch (IOException e) {
				e.printStackTrace();
				fail("IOException");
			}
			try {
				mSubscriber.closeConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Wrapper for the UDPpublisherCommLayer class intended to simplify the interface for testing.
	 */
	private class EchoPublisher {
		private UDPpublisherCommLayer mPublisher;
		
		public EchoPublisher(CommLayerParams parameters) throws IOException {
			mPublisher = new UDPpublisherCommLayer();
			mPublisher.openConnection(parameters);
		}
		
		public void sendEcho(String msg) throws IOException {
			byte[] b = msg.getBytes();
			mPublisher.sendData(b);
		}
		
		public void sendEcho(byte[] b) throws IOException {
			mPublisher.sendData(b);
		}
		
		public void closeConnection() throws IOException {
			mPublisher.closeConnection();
		}
	}
}

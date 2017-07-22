package de.htw.berlin.polysun4diac.forte.comm;

import java.net.InetAddress;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test cases for the IP communication layer.
 * 
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 *
 */
public class IPCommLayerTest {
	
	/** Multicast IP address */
	private static final String UDPIP = "239.192.0.2";
	/** IP address */
	private static final String TCPIP = "localhost";
	/** Port */
	private static final int PORT = 61500;
	
	IPcommunicationLayer ipLayer;
	CommLayerParams params;
	
	@Before
	public void setUp() throws Exception {
		ipLayer = new IPcommunicationLayer();
	}
	
	@Test
	public void testCreateClient() throws Exception {
		params = new CommLayerParams(InetAddress.getByName(TCPIP), PORT);
	}
	
	@Test
	public void testCreateServer() throws Exception {
		params = new CommLayerParams(InetAddress.getByName(TCPIP), PORT);
		params.setServiceType(ForteServiceType.SERVER);
	}
	
	@Test
	public void testCreateSubscriber() throws Exception {
		params = new CommLayerParams(InetAddress.getByName(UDPIP), PORT);
		params.setServiceType(ForteServiceType.SUBSCRIBER);
	}
	
	@Test
	public void testCreatePublisher() throws Exception {
		params = new CommLayerParams(InetAddress.getByName(UDPIP), PORT);
		params.setServiceType(ForteServiceType.PUBLISHER);
	}
}

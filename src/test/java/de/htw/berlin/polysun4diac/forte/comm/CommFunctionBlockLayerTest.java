package de.htw.berlin.polysun4diac.forte.comm;

import static org.junit.Assert.*;

import java.net.InetAddress;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.htw.berlin.polysun4diac.forte.datatypes.DateAndTime;
import de.htw.berlin.polysun4diac.forte.datatypes.ForteDataType;

/**
 * JUnit test cases for ComFunctionBlockLayer class.
 * NOTE: In order to run these tests, the 4diac application "PolysunPluginTestsApp" must be running on FORTE.
 * To do so, import the "PolysunPluginTests.sys" file in the directory /.../Polysun4diac PluginController/
 * and import it into 4diac-IDE. Map the application to EMB_RES,
 * open EMB_RES in System Configuration --> FORTE_PC --> EMB_RES1 and connect the COLD and WARM start events to the SEVER_1.INIT event input. 
 * Then deploy the application.
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 */
public class CommFunctionBlockLayerTest {
	
	/** Testing tolerance for doubles and floats */
	private static final float TEST_TOLERANCE = 0.0f;
	/** Double value to test sending and receiving */
	private static final double DVALUE = 5;
	/** Float value to test sending and receiving */
	private static final float FVALUE = 5.5f;
	/** Integer value to test sending and receiving */
	private static final int IVALUE = 5;
	/** Double array to test sending and receiving */
	private static double[] DARRAY = {0, 0};
	/** Number of test cycles to perform */
	private static int NUM_CYCLES = 2;
	/** IP address **/
	private static final String IP = "localhost";
	
	IForteSocket multiInputArrayOutputSocket;
	IForteSocket noOutputSocket;
	CommLayerParams params1;
	CommLayerParams params2;
	
	/** DateAmdTime value to test sending and receiving */
	DateAndTime dtValue = new DateAndTime(2017);
	
	 @Before
	public void setUp() throws Exception {
		params1 = new CommLayerParams(InetAddress.getByName(IP), 61503);
		params1.addInput(ForteDataType.REAL);
		params1.addInput(ForteDataType.SINT);
		params1.addInput(ForteDataType.UINT);
		params1.addInput(ForteDataType.DATE_AND_TIME);
		params1.addOutput(ForteDataType.LREAL, DARRAY.length);
		multiInputArrayOutputSocket = params1.makeIPSocket();
		multiInputArrayOutputSocket.setDateAndTimeReference(dtValue);
		params2 = new CommLayerParams(InetAddress.getByName(IP), 61504);
		params2.addInput(ForteDataType.LREAL);
		noOutputSocket = params2.makeIPSocket();
		dtValue.setSimulationTimeS(0); // Initialize DateAndTime
	}
	
	 @After
	public void tearDown() throws Exception {
		multiInputArrayOutputSocket.disconnect();
		noOutputSocket.disconnect();
	}
	
	@Test
	public void testDifferingInputOutput() throws Exception {
		for (int i = 0; i < NUM_CYCLES; i++) {
			multiInputArrayOutputSocket.put(FVALUE);
			multiInputArrayOutputSocket.put(IVALUE);
			multiInputArrayOutputSocket.put(IVALUE);
			multiInputArrayOutputSocket.put(dtValue);
			multiInputArrayOutputSocket.sendData();
			multiInputArrayOutputSocket.recvData();
			assertTrue(multiInputArrayOutputSocket.isDoubleArray());
			double[] rcv = multiInputArrayOutputSocket.getDoubleArray();
			int ct = 0;
			for (double r : rcv) {
				assertEquals("Double value in array", DARRAY[ct], r, TEST_TOLERANCE);
				ct++;
			}
		}
	}
	
	@Test
	public void testNoOutput() throws Exception {
		noOutputSocket.put(DVALUE);
		noOutputSocket.sendData();
		noOutputSocket.recvData();
	}
}
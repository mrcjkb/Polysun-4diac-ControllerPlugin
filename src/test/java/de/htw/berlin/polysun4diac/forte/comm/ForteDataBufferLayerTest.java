package de.htw.berlin.polysun4diac.forte.comm;

import static de.htw.berlin.polysun4diac.forte.datatypes.ForteTypeIDs.*;
import static org.junit.Assert.*;

import java.net.InetAddress;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.htw.berlin.polysun4diac.forte.comm.ForteDataBufferLayer;
import de.htw.berlin.polysun4diac.forte.datatypes.DateAndTime;
import de.htw.berlin.polysun4diac.forte.datatypes.ForteDataType;

/**
 * JUnit test cases for ForteDataBufferLayer class.
 * NOTE: In order to run these tests, the 4diac application "PolysunPluginTestsApp" must be running on FORTE.
 * To do so, import the "PolysunPluginTests.sys" file in the directory /.../Polysun4diac PluginController/
 * and import it into 4diac-IDE. Map the application to EMB_RES,
 * open EMB_RES in System Configuration --> FORTE_PC --> EMB_RES and connect the COLD and WARM start events to the SEVER_1.INIT event input. 
 * Then deploy the application.
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 */
public class ForteDataBufferLayerTest {
	
	/** Testing tolerance for doubles and floats */
	private static final float TEST_TOLERANCE = 0.0f;
	/** Double value to test sending and receiving */
	private static final double DVALUE = 5;
	/** Float value to test sending and receiving */
	private static final float FVALUE = 5.5f;
	/** Integer value to test sending and receiving */
	private static final int IVALUE = 5;
	/** String value to test sending and receiving */
	private static final String SVALUE = "five";
	/** Double value in its byte form */
	private static final byte[] DBYTE = {64, 20};
	/** Double array to test sending and receiving */
	private static double[] DARRAY = {1, 2, 3, 4, 5};
	/** Number of test cycles to perform */
	private static int NUM_CYCLES = 2;
	/** IP address **/
	private static final String IP = "localhost";
	
	IForteSocket singleInputSocket;
	IForteSocket inputOutputSocket;
	IForteSocket arraySocket;
	CommLayerParams params1;
	CommLayerParams params2;
	CommLayerParams params3;
	CommLayerParams params4;
	
	/** DateAmdTime value to test sending and receiving */
	DateAndTime dtValue = new DateAndTime(2017);
	
	@Ignore @Before
	public void setUp() throws Exception {
		// Set up params (default service type is CLIENT)
		params1 = new CommLayerParams(InetAddress.getByName(IP), 61500);
		params1.addInputOutput(ForteDataType.LREAL);
		params2 = new CommLayerParams(InetAddress.getByName(IP), 61501);
		params2.addInputOutput(ForteDataType.REAL);
		params2.addInputOutput(ForteDataType.INT);
		params2.addInputOutput(ForteDataType.UDINT);
		params2.addInputOutput(ForteDataType.STRING);
		params2.addInputOutput(ForteDataType.DATE_AND_TIME);
		params3 = new CommLayerParams(InetAddress.getByName(IP), 61502);
		params3.addInputOutput(ForteDataType.LREAL, DARRAY.length);
		singleInputSocket = params1.makeIPSocket();
		inputOutputSocket = params2.makeIPSocket();
		inputOutputSocket.setDateAndTimeReference(dtValue);
		arraySocket = new ForteDataBufferLayer();
		arraySocket = params3.makeIPSocket();
		dtValue.setSimulationTimeS(0); // Initialize DateAndTime
	}
	
	@Ignore @After
	public void tearDown() throws Exception {
		singleInputSocket.disconnect();
		inputOutputSocket.disconnect();
		arraySocket.disconnect();
	}
	
	@Test 
	public void testByteBuffer() {
		singleInputSocket.put(DVALUE);
		byte[] output = singleInputSocket.array();
		assertEquals("LREAL type identifier", LREALID, output[0]);
		assertEquals("First LREAL data byte", DBYTE[0], output[1]);
		assertEquals("Second LREAL data byte", DBYTE[1], output[2]);
		assertEquals("Byte array length", LREALBN, output.length);
	}

	@Test
	public void testSendReceiveDouble() throws Exception {
		for (int i = 0; i < NUM_CYCLES; i++) {
			singleInputSocket.put(DVALUE);
			singleInputSocket.sendData();
			singleInputSocket.recvData();
			assertTrue(singleInputSocket.isDouble());
			assertEquals("Response from FORTE", DVALUE, singleInputSocket.getDouble(), TEST_TOLERANCE);
		}
	}
	
	@Test
	public void testMultiInputOutput() throws Exception {
		for (int i = 0; i < NUM_CYCLES; i++) {
			inputOutputSocket.put(FVALUE);
			inputOutputSocket.put(IVALUE);
			inputOutputSocket.put(IVALUE);
			inputOutputSocket.put(SVALUE);
			inputOutputSocket.put(dtValue);
			inputOutputSocket.sendData();
			inputOutputSocket.recvData();
			assertTrue(inputOutputSocket.isFloat());
			assertEquals("Response from FORTE", FVALUE, inputOutputSocket.getFloat(), TEST_TOLERANCE);
			assertTrue(inputOutputSocket.isInt());
			assertEquals("Response from FORTE", IVALUE, inputOutputSocket.getInt());
			assertTrue(inputOutputSocket.isInt());
			assertEquals("Response from FORTE", IVALUE, inputOutputSocket.getInt());
			assertTrue(inputOutputSocket.isString());
			assertEquals("Response from FORTE", SVALUE, inputOutputSocket.getString());
			assertTrue(inputOutputSocket.isDateAndTime());
			DateAndTime dt = inputOutputSocket.getDateAndTime();
			assertEquals("Response from FORTE", dtValue.getSimulationTimeS(), dt.getSimulationTimeS());
		}
	}
	
	@Test
	public void testArray() throws Exception {
		for (int i = 0; i < NUM_CYCLES; i++) {
			arraySocket.put(DARRAY);
			arraySocket.sendData();
			arraySocket.recvData();
			assertTrue(arraySocket.isDoubleArray());
			double[] rcv = arraySocket.getDoubleArray();
			int ct = 0;
			for (double r : rcv) {
				assertEquals("Double value in array", DARRAY[ct], r, TEST_TOLERANCE);
				ct++;
			}
		}
	}
}
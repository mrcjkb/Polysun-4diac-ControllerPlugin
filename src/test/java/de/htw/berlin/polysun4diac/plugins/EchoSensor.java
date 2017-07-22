package de.htw.berlin.polysun4diac.plugins;

import static de.htw.berlin.polysun4diac.CommonFunctionsAndConstants.DEF_PORT_NUMBER;
import static de.htw.berlin.polysun4diac.CommonFunctionsAndConstants.DEF_TCP_ADDRESS;
import static org.junit.Assert.fail;

import java.io.IOException;

import de.htw.berlin.polysun4diac.forte.comm.CommLayerParams;
import de.htw.berlin.polysun4diac.forte.comm.ForteServiceType;
import de.htw.berlin.polysun4diac.forte.datatypes.ForteDataType;

/**
 * Wrapper for an IForteSocket. Runs on a second thread
 * and sends data. Intended for use in JUnit test cases.
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 */
public class EchoSensor extends IForteSocketEcho {

	private float[] mSensors;
	
	public EchoSensor(float[] sensors) {
		mSensors = sensors;
		mParams = new CommLayerParams(DEF_TCP_ADDRESS, DEF_PORT_NUMBER);
		mParams.setServiceType(ForteServiceType.SERVER);
		for (int i = 0; i < sensors.length; i++) {
			mParams.addInput(ForteDataType.LREAL);
		}
		mData = new double[sensors.length];
	}

	@Override
	public void run() {
		try {
			if (!isConnected()) {
				mSocket = mParams.makeIPSocket();
				setConnected(true);
			}
			for (int i = 0; i < mSensors.length; i++) {
				mSocket.put((double) mSensors[i]);
			}
			mSocket.sendData();
		} catch (IOException e) {
			e.printStackTrace();
			disconnect();
			fail("IOException");
		}
	}
}

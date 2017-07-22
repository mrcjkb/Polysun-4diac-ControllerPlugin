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
 * and waits for data to arrive. Intended for use in JUnit test cases.
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 */
public class EchoActor extends IForteSocketEcho {
	
	public EchoActor(int numOutputs) {
		mParams = new CommLayerParams(DEF_TCP_ADDRESS, DEF_PORT_NUMBER);
		mParams.setServiceType(ForteServiceType.SERVER);
		for (int i = 0; i < numOutputs; i++) {
			mParams.addOutput(ForteDataType.LREAL);
		}
		mData = new double[numOutputs];
	}
	
	@Override
	public void run() {
		try {
			if (!isConnected()) {
				mSocket = mParams.makeIPSocket();
				setConnected(true);
			}
			mSocket.recvData();
			for (int i = 0; i < mData.length; i++) {
				if (!mSocket.isDouble()) {
					System.out.println("Wrong data type received.");
				}
				mData[i] = mSocket.getDouble();
			}
			mSocket.sendRsp(); // sends a response
		} catch (IOException e) {
			e.printStackTrace();
			disconnect();
			fail("IOException");
		}
	}
}
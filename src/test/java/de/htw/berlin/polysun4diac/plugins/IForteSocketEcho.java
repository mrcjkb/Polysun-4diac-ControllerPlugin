package de.htw.berlin.polysun4diac.plugins;

import static org.junit.Assert.fail;

import java.io.IOException;

import de.htw.berlin.polysun4diac.forte.comm.CommLayerParams;
import de.htw.berlin.polysun4diac.forte.comm.IForteSocket;

/**
 * Abstract echo class wrapper for IForteSocket JUnit testing
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 */
public abstract class IForteSocketEcho extends Thread {

	protected IForteSocket mSocket;
	protected CommLayerParams mParams;
	protected double[] mData;
	protected boolean mIsConnected = false;

	/** Returns the data received from the sensor. */
	public double[] getReceivedData() {
		return mData;
	}
	
	/** Disconnects the echo actor. */
	public void disconnect() {
		try {
			mSocket.disconnect();
			setConnected(false);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Failed to disconnect echo actor.");
		}
	}
	
	/**
	 * @return <code>true</code> if the socket echo is connected.
	 */
	public boolean isConnected() {
		return mIsConnected;
	}
	
	/**
	 * @param tf <code>true</code> if the socket echo is connected.
	 */
	protected void setConnected(boolean tf) {
		mIsConnected = tf;
	}
}

package de.htw.berlin.polysun4diac.plugins;

import java.io.IOException;

import com.velasolaris.plugin.controller.spi.PluginControllerException;

import de.htw.berlin.polysun4diac.forte.comm.CommLayerParams;
import de.htw.berlin.polysun4diac.forte.comm.IForteSocket;

/**
 * Abstract class for Polysun PluginControllers that communicate with 4diac IEC 61499 applications running on 4diac-RTE (FORTE).
 * Subclasses of this class should only control a single component (i.e. a battery). In case more than one components are used, it is recommended
 * to set up the socket as a TCP client.
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see de.htw.berlin.polysun4diac.forte.comm.CommLayerParams
 * @see de.htw.berlin.polysun4diac.forte.comm.IForteSocket
 * @see com.velasolaris.plugin.controller.spi.IPluginController
 */
public abstract class AbstractSingleComponentController extends Abstract4diacPluginController {
	
	/** Socket for communicating with FORTE */
	private IForteSocket mSocket;
	
	/**
	 * Default constructor.
	 */
	public AbstractSingleComponentController() throws PluginControllerException {
		super();
	}

	@Override
	public void disconnect() {
		try {
			getSocket().disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected boolean isConnected() {
		if (getSocket() == null) {
			return false;
		}
		return getSocket().isConnected();
	}
	
	/**
	 * Calls the CommLayerParams's makeIPSocket() method and sets the internal IForteSocket to communicate with FORTE
	 * @param params used to create the IForteSocket.
	 * @throws PluginControllerException
	 */
	protected void makeIPSocket(CommLayerParams params) throws PluginControllerException {
		try {
			setSocket(params.makeIPSocket());
		} catch (IOException e) {
			e.printStackTrace();
			throw new PluginControllerException("Unable to connect to FORTE.", e);
		}
	}
	
	/** @return the socket for communicating with FORTE */
	protected IForteSocket getSocket() {
		return mSocket;
	}
	
	/** Sets the socket for communicating with FORTE */
	protected void setSocket(IForteSocket socket) {
		mSocket = socket;
	}
}

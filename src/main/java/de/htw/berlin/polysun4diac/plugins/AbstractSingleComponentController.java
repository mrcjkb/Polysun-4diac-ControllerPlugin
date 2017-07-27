package de.htw.berlin.polysun4diac.plugins;

import java.io.IOException;
import java.util.List;

import com.velasolaris.plugin.controller.spi.PluginControllerException;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Property;

import de.htw.berlin.polysun4diac.exception.UnsupportedForteDataTypeException;
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
	
	/** Key for the option to wait for a response from FORTE or not */
	protected static final String WAITFORRSP_KEY = "Wait for response";
	/** Integer indicating not to wait for a response from FORTE */
	protected static final int DONTWAITFORRSP = 0;
	
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
			// Ignore. Probably already disconnected.
		}
	}
	
	@Override
	protected boolean isConnected() {
		if (getSocket() == null) {
			return false;
		}
		return getSocket().isConnected();
	}
	
	@Override
	protected List<Property> initialisePropertyList() {
		List<Property> properties = super.initialisePropertyList();
		properties.add(new Property(WAITFORRSP_KEY, new String[] { "no" , "yes" }, DONTWAITFORRSP, "If yes is selected, the simulation is paused until a response (RSP) event is received from FORTE."));
		return properties;
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
			throw new PluginControllerException(getName() + ": Unable to connect to FORTE.", e);
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
	
	/**
	 * Attempts to call the socket's recvData() method.
	 * @throws PluginControllerException
	 */
	protected void recvData() throws PluginControllerException {
		try { // Wait for input from FORTE
			getSocket().recvData();
		} catch (UnsupportedForteDataTypeException e) {
			e.printStackTrace();
			throw new PluginControllerException(getName() + ": Unsupported FORTE data type.", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new PluginControllerException(getName() + ": Error receiving response from FORTE CSIFB.", e);
		}
	}
	
	/**
	 * Attempts to call the socket's sendData() method.
	 * @throws PluginControllerException
	 */
	protected void sendData() throws PluginControllerException {
		try {
			getSocket().sendData();
		} catch (IOException e) {
			throw new PluginControllerException(getName() + ": Error sending data to Forte.");
		}
	}
}

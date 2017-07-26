package de.htw.berlin.polysun4diac.plugins;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.velasolaris.plugin.controller.spi.PluginControllerException;
import com.velasolaris.plugin.controller.spi.PolysunSettings;

import de.htw.berlin.polysun4diac.exception.UnsupportedForteDataTypeException;

/**
 * Defines the default behaviour of a FORTE actor plugin controller for receiving control signals from IEC 61499 applications running on 4diac-RTE (FORTE).
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 */
public abstract class AbstractActorController extends AbstractSingleComponentController {

	/** Index of the SUBSCRIBER service type in the {@link #SERVICETYPE_KEY} property. */
	protected static final int SUBSCRIBER_IDX = 2;
	
	public AbstractActorController() throws PluginControllerException {
		super();
		setSendTimestamp(false); // Disable time stamp option, since actor plugins only receive data from FORTE.
	}

	@Override
	public int[] control(int simulationTime, boolean status, float[] sensors, float[] controlSignals, float[] logValues,
			boolean preRun, Map<String, Object> parameters) throws PluginControllerException {
		if (!status) {
			return null;
		}
		try { // Wait for input from FORTE
			getSocket().recvData();
		} catch (UnsupportedForteDataTypeException e) {
			e.printStackTrace();
			throw new PluginControllerException("Unsupported FORTE data type.", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new PluginControllerException("Error receiving response from FORTE CSIFB.", e);
		}
		populateControlSignals(controlSignals);
		return null;
	}
	
	@Override
	public List<String> getPropertiesToHide(PolysunSettings propertyValues, Map<String, Object> parameters) {
		List<String> propertiesToHide = super.getPropertiesToHide(propertyValues, parameters);
		propertiesToHide.add(WAITFORRSP_KEY); // Actors don't have to wait for a response
		return propertiesToHide;
	}
	
	/**
	 * Receives the data from FORTE and populates the control signals with the received data.
	 * @param controlSignals The control signals set by this plugin controller (Output parameter).
	 * @throws PluginControllerException
	 */
	abstract protected void populateControlSignals(float[] controlSignals) throws PluginControllerException;
}

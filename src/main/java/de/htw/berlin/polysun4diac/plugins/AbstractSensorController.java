package de.htw.berlin.polysun4diac.plugins;


import java.io.IOException;
import java.util.Map;

import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration;
import com.velasolaris.plugin.controller.spi.PluginControllerException;

/**
 * Defines the default behaviour of a FORTE sensor plugin controller for sending data to IEC 61499 applications running on 4diac-RTE (FORTE).
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 */
public abstract class AbstractSensorController extends AbstractSingleComponentController {
	
	public AbstractSensorController() throws PluginControllerException {
		super();
	}
	
	@Override
	public PluginControllerConfiguration getConfiguration(Map<String, Object> parameters)
			throws PluginControllerException {
		return null;
	}

	@Override
	public int[] control(int simulationTime, boolean status, float[] sensors, float[] controlSignals, float[] logValues,
			boolean preRun, Map<String, Object> parameters) throws PluginControllerException {
		try {
			if (!status) {
				return null;
			}
			// Buffer inputs
			putSensors(sensors);
			try { // Send buffer to FORTE
				if (sendTimestamp()) {
					getForteTimestamp().setSimulationTimeS(simulationTime);
					getSocket().put(getForteTimestamp());
				}
				getSocket().sendData();
			} catch (IOException e) {
				throw new PluginControllerException(getName() + ": Error sending Polysun data to FORTE.", e);
			}
			// Wait for response from FORTE if specified so by user.
			if (getProp(WAITFORRSP_KEY).getInt() != DONTWAITFORRSP) {
				recvData();
			}
			return null;
		} catch (PluginControllerException e) {
			// To avoid leaving open connections, disconnect() is called before throwing any exception.
			disconnect();
			throw e;
		}
	}
	
	/** 
	 * Method for buffering the sensor data 
	 * @param sensors The values of the sensors configured by the user (Input parameter).
	 */
	abstract protected void putSensors(float[] sensors);
}
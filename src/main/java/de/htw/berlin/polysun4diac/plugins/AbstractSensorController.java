package de.htw.berlin.polysun4diac.plugins;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration;
import com.velasolaris.plugin.controller.spi.PluginControllerException;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Property;

import de.htw.berlin.polysun4diac.exception.UnsupportedForteDataTypeException;

/**
 * Defines the default behaviour of a FORTE sensor plugin controller for sending data to IEC 61499 applications running on 4diac-RTE (FORTE).
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 */
public abstract class AbstractSensorController extends AbstractSingleComponentController {
	
	/** Key for the option to wait for a response from FORTE or not */
	private static final String WAITFORRSP_KEY = "Wait for response";
	/** Integer indicating not to wait for a response from FORTE */
	private static final int DONTWAITFORRSP = 0;
	
	public AbstractSensorController() throws PluginControllerException {
		super();
	}
	
	@Override
	public PluginControllerConfiguration getConfiguration(Map<String, Object> parameters)
			throws PluginControllerException {
		return null;
	}

	@Override
	protected List<Property> initialisePropertyList() {
		List<Property> properties = super.initialisePropertyList();
		properties.add(new Property(WAITFORRSP_KEY, new String[] { "no" , "yes" }, DONTWAITFORRSP, "If yes is selected, the simulation is paused until a response (RSP) event is received from FORTE."));
		return properties;
	}
	
	@Override
	public int[] control(int simulationTime, boolean status, float[] sensors, float[] controlSignals, float[] logValues,
			boolean preRun, Map<String, Object> parameters) throws PluginControllerException {
		try {
			int[] nextMinute = new int[] {simulationTime + 60}; // Register next minute
			if (!status) {
				return nextMinute;
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
				e.printStackTrace();
				throw new PluginControllerException("Error sending battery SoC and throughput to FORTE.", e);
			}
			// Wait for response from FORTE if specified so by user.
			if (getProp(WAITFORRSP_KEY).getInt() != DONTWAITFORRSP) {
				try { // Wait for response from FORTE before returning
					getSocket().recvData();
				} catch (UnsupportedForteDataTypeException e) {
					e.printStackTrace();
					throw new PluginControllerException("Unsupported FORTE data type.", e);
				} catch (IOException e) {
					e.printStackTrace();
					throw new PluginControllerException("Error receiving response from FORTE battery CSIFB.", e);
				} 
			}
			return nextMinute;
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
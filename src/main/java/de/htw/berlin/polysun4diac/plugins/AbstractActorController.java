package de.htw.berlin.polysun4diac.plugins;

import java.io.IOException;
import java.util.Map;

import com.velasolaris.plugin.controller.spi.PluginControllerException;

import de.htw.berlin.polysun4diac.exception.UnsupportedForteDataTypeException;

/**
 * Defines the default behaviour of a FORTE actor plugin controller for receiving control signals from IEC 61499 applications running on 4diac-RTE (FORTE).
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 */
public abstract class AbstractActorController extends AbstractSingleComponentController {

	public AbstractActorController() throws PluginControllerException {
		super();
		setSendTimestamp(false); // Disable time stamp option, since actor plugins only receive data from FORTE.
	}

	@Override
	public int[] control(int simulationTime, boolean status, float[] sensors, float[] controlSignals, float[] logValues,
			boolean preRun, Map<String, Object> parameters) throws PluginControllerException {
		int[] nextMinute = new int[] {simulationTime + 60}; // Register next minute
		if (!status) {
			return nextMinute;
		}
		try { // Wait for input from FORTE
			getSocket().recvData();
		} catch (UnsupportedForteDataTypeException e) {
			e.printStackTrace();
			throw new PluginControllerException("Unsupported FORTE data type.", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new PluginControllerException("Error receiving response from FORTE battery CSIFB.", e);
		}
		populateControlSignals(controlSignals);
		return nextMinute;
	}
	
	/**
	 * Receives the data from FORTE and populates the control signals with the received data.
	 * @param controlSignals The control signals set by this plugin controller (Output parameter).
	 * @throws PluginControllerException
	 */
	abstract protected void populateControlSignals(float[] controlSignals) throws PluginControllerException;
}

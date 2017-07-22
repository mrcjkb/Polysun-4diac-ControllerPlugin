package de.htw.berlin.polysun4diac.plugins;
// MFIXME Debug this
import static de.htw.berlin.polysun4diac.CommonFunctionsAndConstants.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.ControlSignal;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Property;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Sensor;
import com.velasolaris.plugin.controller.spi.PolysunSettings.PropertyValue;

import de.htw.berlin.polysun4diac.exception.UnsupportedForteDataTypeException;
import de.htw.berlin.polysun4diac.forte.comm.CommLayerParams;
import de.htw.berlin.polysun4diac.forte.comm.ForteServiceType;
import de.htw.berlin.polysun4diac.forte.datatypes.ForteDataType;

import com.velasolaris.plugin.controller.spi.PluginControllerException;
import com.velasolaris.plugin.controller.spi.PolysunSettings;

/**
 * Plugin controller for sending data to and receiving data from IEC 61499 applications running on 4diac-RTE (FORTE).
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see de.htw.berlin.polysun4diac.forte.comm.IForteSocket
 * @see de.htw.berlin.polysun4diac.forte.comm.CommLayerParams
 * @see com.velasolaris.plugin.controller.spi.IPluginController
 */
public class GenericForteController extends AbstractSingleComponentController {

	/** Index of the SUBSCRIBER service type in the {@link #SERVICETYPE_KEY} property. */
	private static final int SUBSCRIBER_IDX = 2;
	/** Index of the PUBLISHER service type in the {@link #SERVICETYPE_KEY} property. */
	private static final int PUBLISHER_IDX = 3;
	
	public GenericForteController() throws PluginControllerException {
		super();
		setSendTimestamp(true);
	}

	@Override
	public String getName() {
		return "Generic 4diac Controller";
	}

	@Override
	public String getDescription() {
		return "Plugin for communicating with a CLIENT, SERVER, PUBLISH or SUBSCRIBE function block running on 4diac-RTE (FORTE). "
				+ "The digital signals sent are of type REAL.";
	}
	
	@Override
	public String getVersion() {
		return "1.0 (BETA)";
	}
	
	@Override
	public PluginControllerConfiguration getConfiguration(Map<String, Object> parameters)
			throws PluginControllerException {
		return new PluginControllerConfiguration(initialisePropertyList(), null, null, null, 0, MAX_NUM_GENERIC_SENSORS, MAX_NUM_GENERIC_SIGNALS, getPluginIconResource(), null);
	}
	
	@Override
	protected List<Property> initialisePropertyList() {
		List<Property> properties = super.initialisePropertyList();
		properties.add(new Property(SERVICETYPE_KEY, new String[] { "CLIENT" , "SERVER", "SUBSCRIBER", "PUBLISHER" }, CLIENT_IDX, SERVICETYPE_TOOLTIP));
		return properties;
	}
	
	@Override
	public void initialiseSimulation(Map<String, Object> parameters) throws PluginControllerException {
		if (getProp(SERVICETYPE_KEY).getInt() == PUBLISHER_IDX) {
			List<ControlSignal> controlSignals = getControlSignals();
			for (ControlSignal c : controlSignals) {
				if (c.isUsed()) {
					throw new PluginControllerException("A PUBLISHER cannot receive data. Please change the service type or remove all control signals.");
				}
			}
		} else if (getProp(SERVICETYPE_KEY).getInt() == SUBSCRIBER_IDX) {
			List<Sensor> sensors = getSensors();
			for (Sensor s : sensors) {
				if (s.isUsed()) {
					throw new PluginControllerException("A SUBSCRIBER cannot send data. Please change the service type or remove all sensors.");
				}
			}
			setSendTimestamp(false);
		}
		super.initialiseSimulation(parameters);
	}
	
	@Override
	public List<String> getPropertiesToHide(PolysunSettings propertyValues, Map<String, Object> parameters) {
		List<String> propertiesToHide = super.getPropertiesToHide(propertyValues, parameters);
		if (getProp(SERVICETYPE_KEY).getInt() == SUBSCRIBER_IDX) {
			// Subscribers cannot send time stamps
			PropertyValue property = propertyValues.getPropertyValue(TIMESTAMPSETTING_KEY);
			if (property != null && property.getInt() == DISABLE_TIMESTAMP) {
				propertiesToHide.add(SIMULATIONSTART_KEY);
			}
		}
		if (isAnyControlSignalUsed() || getProp(SERVICETYPE_KEY).getInt() == SUBSCRIBER_IDX
				|| getProp(SERVICETYPE_KEY).getInt() == PUBLISHER_IDX) {
			propertiesToHide.add(WAITFORRSP_KEY);
		}
		return propertiesToHide;
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
			for (float s : sensors) {
				getSocket().put(s);
			}
			try { // Send buffer to FORTE
				if (sendTimestamp()) {
					getForteTimestamp().setSimulationTimeS(simulationTime);
					getSocket().put(getForteTimestamp());
				}
				getSocket().sendData();
			} catch (IOException e) {
				throw new PluginControllerException("Error sending Polysun data to FORTE.", e);
			}
			if (controlSignals.length > 0) {
				try { // Wait for input from FORTE
					getSocket().recvData();
				} catch (UnsupportedForteDataTypeException e) {
					e.printStackTrace();
					throw new PluginControllerException("Unsupported FORTE data type.", e);
				} catch (IOException e) {
					e.printStackTrace();
					throw new PluginControllerException("Error receiving response from FORTE CSIFB.", e);
				}
				for (int i = 0; i < controlSignals.length; i++) {
					if (!getSocket().isFloat()) {
						throw new PluginControllerException("The FORTE CSIFB should only send REAL data.");
					}
					controlSignals[i] = getSocket().getFloat();
				}
			} else if (getProp(WAITFORRSP_KEY).getInt() != DONTWAITFORRSP) {
				try { // Wait for response from FORTE before returning
					getSocket().recvData();
				} catch (UnsupportedForteDataTypeException e) {
					e.printStackTrace();
					throw new PluginControllerException("Unsupported FORTE data type.", e);
				} catch (IOException e) {
					e.printStackTrace();
					throw new PluginControllerException("Error receiving response from FORTE CSIFB.", e);
				} 
			}
			return nextMinute;
		} catch (PluginControllerException e) {
			// To avoid leaving open connections, disconnect() is called before throwing any exception.
			disconnect();
			throw e;
		}
	}
	
	@Override
	protected void initialiseConnection(String address, int port) throws PluginControllerException {
		CommLayerParams params = new CommLayerParams(address, port);
		switch (getProp(SERVICETYPE_KEY).getInt()) { // Default is CLIENT
		case SERVER_IDX:
			params.setServiceType(ForteServiceType.SERVER);
			break;
		case SUBSCRIBER_IDX:
			params.setServiceType(ForteServiceType.SUBSCRIBER);
			break;
		case PUBLISHER_IDX:
			params.setServiceType(ForteServiceType.PUBLISHER);
			break;
		}
		List<ControlSignal> controlSignals = getControlSignals();
		for (ControlSignal c : controlSignals) {
			if (c.isUsed()) {
				params.addOutput(ForteDataType.REAL);
			}
		}
		List<Sensor> sensors = getSensors();
		for (Sensor s : sensors) {
			if (s.isUsed()) {
				params.addInput(ForteDataType.REAL);
			}
		}
		if (sendTimestamp()) {
			params.addInput(ForteDataType.DATE_AND_TIME);
		}
		makeIPSocket(params);
	}

}
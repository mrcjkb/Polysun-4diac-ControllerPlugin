package de.htw.berlin.polysun4diac.plugins;

import static de.htw.berlin.polysun4diac.CommonFunctionsAndConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration;
import com.velasolaris.plugin.controller.spi.PluginControllerException;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Sensor;

import de.htw.berlin.polysun4diac.forte.comm.CommLayerParams;
import de.htw.berlin.polysun4diac.forte.datatypes.ForteDataType;

/**
 * Plugin controller for sending the battery's SoC and power throughput to 4diac-RTE (FORTE).
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see de.htw.berlin.polysun4diac.forte.comm.IForteSocket
 * @see de.htw.berlin.polysun4diac.forte.comm.CommLayerParams
 * @see com.velasolaris.plugin.controller.spi.IPluginController
 */
public class BatterySensorController extends AbstractSensorController {
	
	private static final String SENSOR1 = "State of charge";
	private static final String SENSOR2 = "Battery transfer";
	
	public BatterySensorController() throws PluginControllerException {
		super();
	}

	@Override
	public String getName() {
		return "Battery Sensor";
	}
	
	@Override
	public String getDescription() {
		return "Sensor for sending the battery's state of charge and last transferred power (positive = charge, negative = discharge) to 4diac-RTE (FORTE).";
	}

	@Override
	public PluginControllerConfiguration getConfiguration(Map<String, Object> parameters) throws PluginControllerException {
		List<Sensor> sensors = new ArrayList<>();
		sensors.add(new Sensor(SENSOR1, "", true, false, "The battery's SoC.")); // Sensor 1
		sensors.add(new Sensor(SENSOR2, "W", true, false, "The power used to charge (positive) / discharge (negative) the battery with.")); // Sensor 2
		return new PluginControllerConfiguration(initialisePropertyList(), sensors, null, null, 0, 0, 0, getPluginIconResource(), null);
	}
	
	@Override
	protected void initialiseConnection(String host, int port) throws PluginControllerException {
		// Default service type of CommLayerParams is client.
		CommLayerParams params = new CommLayerParams(host, port);
		int numUsedSensors = ZERO_INIT;
		// Inputs to send to FORTE
		if (getSensor(SENSOR1).isUsed()) {
			params.addInput(ForteDataType.LREAL); // Input 1: SoC
			numUsedSensors++;
		}
		if (getSensor(SENSOR2).isUsed()) {
			params.addInput(ForteDataType.LREAL); // Input 2: Charging power
			numUsedSensors++;
		}
		if (numUsedSensors == ZERO_INIT) {
			throw new PluginControllerException(getName() + ": At least one sensor must be used.");
		}
		if (sendTimestamp()) {
			params.addInput(ForteDataType.DATE_AND_TIME);
		}
		makeIPSocket(params); // Create the socket and connect to FORTE
	}
	
	@Override
	protected void putSensors(float[] sensors) {
		getSocket().put((double) getSensor(SENSOR1, sensors));
		// Currently, Polysun sends positive power as the discharging power and negative power as the charging power for the battery transfer.
		// This plugin reverses the sign to be in accordance with the sign of the BatteryActorController's output.
		getSocket().put((double) getSensor(SENSOR2, sensors) * (-1)); 
	}
}

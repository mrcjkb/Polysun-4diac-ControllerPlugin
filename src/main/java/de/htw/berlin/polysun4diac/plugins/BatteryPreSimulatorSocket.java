package de.htw.berlin.polysun4diac.plugins;

/*
 * MTODO Write JUnit tests
 * MTODO Add Optimization interval as param --> + isOptimizationTime() method for control()
 */
import static de.htw.berlin.polysun4diac.CommonFunctionsAndConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Property;

import de.htw.berlin.polysun4diac.forte.comm.CommLayerParams;
import de.htw.berlin.polysun4diac.forte.datatypes.ForteDataType;

import com.velasolaris.plugin.controller.spi.PluginControllerException;
import com.velasolaris.plugin.controller.spi.PolysunSettings;

/**
 * Plugin controller for pre-simulating the battery. Communicates with a PolysunBatteryModel function block running on 4diac-RTE (FORTE).
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see de.htw.berlin.polysun4diac.forte.comm.IForteSocket
 * @see de.htw.berlin.polysun4diac.forte.comm.CommLayerParams
 * @see com.velasolaris.plugin.controller.spi.IPluginController
 */
public class BatteryPreSimulatorSocket extends AbstractSingleComponentController {

	/** Key for the IPreSimulatable battery component */
	private static final String PRESIMULATABLE_BATTERY_KEY = "BATTERY";
	/** Key for the forecast horizon property */
	private static final String FORECAST_HORIZON_KEY = "Forecast horizon";
	/** Default forecast horizon in h */
	private static final float DEF_FORECAST_HORIZON = 15.0f;
	/** Minimum forecast horizon in h */
	private static final float MIN_FORECAST_HORIZON = 0.001f;
	/** Maximum forecast horizon in h */
	private static final float MAX_FORECAST_HORIZON = 24.0f;
	/** Number of arguments to the preSimulate() method */
	private static final int NUM_PRESIMULATION_ARGS = 3;

	/** Arguments for pre-simulation */
	private List<Object> mPreSimulationArgs = new ArrayList<Object>(NUM_PRESIMULATION_ARGS);

	public BatteryPreSimulatorSocket() throws PluginControllerException {
		super();
		setSendTimestamp(false); // Disable time stamp option
		for (int i = 0; i < NUM_PRESIMULATION_ARGS; i++) {
			mPreSimulationArgs.add(new Object[0]);
		}
	}

	@Override
	public String getName() {
		return "Battery Presimulator Socket";
	}

	@Override
	public String getVersion() {
		return "1.0 - prerelease";
	}

	@Override
	public String getDescription() {
		return "Pre-simulates the battery and communicates with a PolysunBatteryModel function block on 4diac-RTE.";
	}

	@Override
	public PluginControllerConfiguration getConfiguration(Map<String, Object> parameters)
			throws PluginControllerException {
		return new PluginControllerConfiguration(initialisePropertyList(), null, null, null, 0, 0, 0, getPluginIconResource(), null);
	}

	@Override
	public int[] control(int simulationTime, boolean status, float[] sensors, float[] controlSignals, float[] logValues,
			boolean preRun, Map<String, Object> parameters) throws PluginControllerException {
		try {
			if (isPreSimulatableComponentAvailable(PRESIMULATABLE_BATTERY_KEY)) {
				// Run in a loop until FORTE signals to stop.
				boolean run = true;
				while (run) {
					recvData();
					if (!getSocket().isBool()) {
						throw new PluginControllerException(getName() + ": Expected BOOL data from FORTE.");
					}
					run = getSocket().getBool();
					if (!getSocket().isDouble()) {
						throw new PluginControllerException(getName() + ": Expected LREAL data from FORTE.");
					}
					getPreSimulationArgs().set(0, simulationTime);
					double chargingEnergykWh = getSocket().getDouble();
					// Convert energy in kWh to power in kW.
					getPreSimulationArgs().set(1, chargingEnergykWh / (double) getProp(FORECAST_HORIZON_KEY).getFloat());
					// Convert forecast horizon from hours to seconds.
					getPreSimulationArgs().set(2, (int) (getProp(FORECAST_HORIZON_KEY).getFloat() * (float) SECONDS_PER_HOUR));
					// Tell Polysun to pre-simulate battery.
					List<Object> output = getPreSimulatableComponent(PRESIMULATABLE_BATTERY_KEY).preSimulate(getPreSimulationArgs());
					// Send the result to FORTE.
					getSocket().put(run);
					getSocket().put((double) output.get(0));
					sendData();
				}
			} else {
				throw new PluginControllerException(getName() + ": No battery component found.");
			}
		} catch (PluginControllerException e) {
			disconnect();
			throw e;
		}
		return null;
	}

	@Override
	public List<String> getPropertiesToHide(PolysunSettings propertyValues, Map<String, Object> parameters) {
		List<String> propertiesToHide = super.getPropertiesToHide(propertyValues, parameters);
		propertiesToHide.add(WAITFORRSP_KEY);
		return propertiesToHide;
	}

	@Override
	protected List<Property> initialisePropertyList() {
		List<Property> properties = super.initialisePropertyList();
		properties.add(new Property(FORECAST_HORIZON_KEY, DEF_FORECAST_HORIZON, MIN_FORECAST_HORIZON, MAX_FORECAST_HORIZON, "h", "The forecast horizon over which the battery is pre-simulated."));
		return properties;
	}

	@Override
	protected void initialiseConnection(String address, int port) throws PluginControllerException {
		CommLayerParams params = new CommLayerParams(address, port);
		// Output: Indicator for breaking out of loop.
		// Input: true if SIMIN event is to be sent in FORTE, false otherwise.
		params.addInputOutput(ForteDataType.BOOL);
		// Output: Power for battery charge/discharge pre-simulation.
		// Input: Battery capacity after pre-simulation.
		params.addInputOutput(ForteDataType.LREAL); 
		makeIPSocket(params); // Create the socket and connect to FORTE
	}

	/**
	 * @return {@link #mPreSimulationArgs}
	 */
	private List<Object> getPreSimulationArgs() {
		return mPreSimulationArgs;
	}
}

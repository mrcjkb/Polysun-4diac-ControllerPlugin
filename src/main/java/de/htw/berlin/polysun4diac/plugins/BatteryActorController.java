package de.htw.berlin.polysun4diac.plugins;

import static de.htw.berlin.polysun4diac.CommonFunctionsAndConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration;
import com.velasolaris.plugin.controller.spi.PluginControllerException;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.ControlSignal;

import de.htw.berlin.polysun4diac.forte.comm.CommLayerParams;
import de.htw.berlin.polysun4diac.forte.datatypes.ForteDataType;

/**
 * Plugin controller for receiving the battery's set power from 4diac-RTE (FORTE).
 * The actor takes an LREAL as the first control signal for the set power and a BOOL for the battery's control mode.
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see de.htw.berlin.polysun4diac.forte.comm.IForteSocket
 * @see de.htw.berlin.polysun4diac.forte.comm.CommLayerParams
 * @see com.velasolaris.plugin.controller.spi.IPluginController
 */
public class BatteryActorController extends AbstractActorController {
	
	private static final String CSIGNAL1 = "Set charging power";
	private static final String CSIGNAL2 = "Control mode";
	
	public BatteryActorController() throws PluginControllerException {
		super();
	}
	
	@Override
	public String getName() {
		return "Battery Actor";
	}
	
	@Override
	public String getDescription() {
		return "Actor for receiving the battery's set power and control mode from 4diac-RTE (FORTE).";
	}
	
	@Override
	public PluginControllerConfiguration getConfiguration(Map<String, Object> parameters) {
		List<ControlSignal> controlSignals = new ArrayList<>();
		controlSignals.add(new ControlSignal(CSIGNAL1, "W", true, true, "The power with which to charge (positive) or discharge (negative) the battery (LREAL in FORTE)."));
		controlSignals.add(new ControlSignal(CSIGNAL2, "", false, true, "The control mode of the battery (BOOL). FALSE for automatic control by Polysun, TRUE for control by 4diac application (BOOL in FORTE)."));
		return new PluginControllerConfiguration(initialisePropertyList(), null, controlSignals, null, 0, 0, 0, getPluginIconResource(), null);
	}

	@Override
	protected void initialiseConnection(String address, int port) throws PluginControllerException {
		// Default service type of CommLayerParams is client.
		CommLayerParams params = new CommLayerParams(address, port);
		params.addOutput(ForteDataType.LREAL); // Output: Set power for battery charge/discharge
		params.addOutput(ForteDataType.BOOL); // Output: Control mode
		makeIPSocket(params); // Create the socket and connect to FORTE
	}

	@Override
	protected void populateControlSignals(float[] controlSignals) throws PluginControllerException {
		if (getSocket().isDouble()) {
			controlSignals[getCSIdx(CSIGNAL1)] = (float) getSocket().getDouble();
		} else {
			throw new PluginControllerException(getName() + ": The battery actor function block should send LREAL data as a " + CSIGNAL1 + " control signal.");
		}
		if (getSocket().isBool()) {
			controlSignals[getCSIdx(CSIGNAL2)] = getSocket().getBool() ? 1.0f : 0.0f;
		} else {
			throw new PluginControllerException(getName() + ": The battery actor function block should send BOOL data as a " + CSIGNAL2 + " control signal.");
		}
	}
} 

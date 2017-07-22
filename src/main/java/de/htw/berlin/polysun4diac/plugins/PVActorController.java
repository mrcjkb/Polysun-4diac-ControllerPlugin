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

public class PVActorController extends AbstractActorController {

	private static final String CSIGNAL1 = "Derating factor";

	public PVActorController() throws PluginControllerException {
		super();
	}

	@Override
	public String getName() {
		return "Photovoltaics Actor";
	}

	@Override
	public String getDescription() {
		return "Actor for receiving the PV deration factor from 4diac-RTE (FORTE).";
	}

	@Override
	public PluginControllerConfiguration getConfiguration(Map<String, Object> parameters)
			throws PluginControllerException {
		List<ControlSignal> controlSignals = new ArrayList<>();
		controlSignals.add(new ControlSignal(CSIGNAL1, "", true, true, "The derating factor used for curtailment of the PV generator."));
		return new PluginControllerConfiguration(initialisePropertyList(), null, controlSignals, null, 0, 0, 0, getPluginIconResource(), null);
	}

	@Override
	protected void initialiseConnection(String address, int port) throws PluginControllerException {
		// Default service type of CommLayerParams is client.
		CommLayerParams params = new CommLayerParams(address, port);
		params.addOutput(ForteDataType.LREAL); // Output: Set power for battery charge/discharge
		makeIPSocket(params); // Create the socket and connect to FORTE
	}

	@Override
	protected void populateControlSignals(float[] controlSignals) throws PluginControllerException {
		if (getSocket().isDouble()) {
			controlSignals[getCSIdx(CSIGNAL1)] = (float) getSocket().getDouble();
		} else {
			throw new PluginControllerException("The battery actor function block should send LREAL data as a " + CSIGNAL1 + " control signal.");
		}
	}
}

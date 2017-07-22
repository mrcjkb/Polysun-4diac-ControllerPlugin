package de.htw.berlin.polysun4diac.plugins;

import static de.htw.berlin.polysun4diac.CommonFunctionsAndConstants.*;

import java.util.List;
import java.util.Map;

import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration;
import com.velasolaris.plugin.controller.spi.PluginControllerException;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.ControlSignal;

import de.htw.berlin.polysun4diac.forte.comm.CommLayerParams;
import de.htw.berlin.polysun4diac.forte.datatypes.ForteDataType;

/**
 * Plugin controller for receiving data from IEC 61499 applications running on 4diac-RTE (FORTE).
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see de.htw.berlin.polysun4diac.forte.comm.IForteSocket
 * @see de.htw.berlin.polysun4diac.forte.comm.CommLayerParams
 * @see com.velasolaris.plugin.controller.spi.IPluginController
 *
 */
public class GenericActorController extends AbstractActorController {
	
	public GenericActorController() throws PluginControllerException {
		super();
	}

	@Override
	public String getName() {
		return "Generic Actor";
	}
	
	@Override
	public String getDescription() {
		return "Actor for receiving a component's set values from 4diac-RTE (FORTE). The digital signals received must be of type REAL.";
	}

	@Override
	public PluginControllerConfiguration getConfiguration(Map<String, Object> parameters)
			throws PluginControllerException {
		return new PluginControllerConfiguration(initialisePropertyList(), null, null, null, 0, 0, MAX_NUM_GENERIC_SIGNALS, getPluginIconResource(), null);
	}
	
	@Override
	protected void populateControlSignals(float[] controlSignals) throws PluginControllerException {
		for (int i = 0; i < controlSignals.length; i++) {
			if (!getSocket().isFloat()) {
				throw new PluginControllerException("The Generic Actor function block should only send REAL data.");
			}
			controlSignals[i] = getSocket().getFloat();
		}
	}

	@Override
	protected void initialiseConnection(String address, int port) throws PluginControllerException {
		CommLayerParams params = new CommLayerParams(address, port);
		List<ControlSignal> controlSignals = getControlSignals();
		for (ControlSignal c : controlSignals) {
			if (c.isUsed()) {
				params.addOutput(ForteDataType.REAL);
			}
		}
		makeIPSocket(params);
	}
}
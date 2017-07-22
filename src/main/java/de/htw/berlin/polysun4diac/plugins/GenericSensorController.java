package de.htw.berlin.polysun4diac.plugins;

import static de.htw.berlin.polysun4diac.CommonFunctionsAndConstants.*;

import java.util.List;
import java.util.Map;

import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration;
import com.velasolaris.plugin.controller.spi.PluginControllerException;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Sensor;

import de.htw.berlin.polysun4diac.forte.comm.CommLayerParams;
import de.htw.berlin.polysun4diac.forte.datatypes.ForteDataType;

/**
 * Plugin controller for sending data to IEC 61499 applications running on 4diac-RTE (FORTE).
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see de.htw.berlin.polysun4diac.forte.comm.IForteSocket
 * @see de.htw.berlin.polysun4diac.forte.comm.CommLayerParams
 * @see com.velasolaris.plugin.controller.spi.IPluginController
 */
public class GenericSensorController extends AbstractSensorController {
	
	public GenericSensorController() throws PluginControllerException {
		super();
	}

	@Override
	public String getName() {
		return "Generic Sensor";
	}

	@Override
	public String getDescription() {
		return "Sensor for sending a data to 4diac-RTE (FORTE). The digital signals sent are of type REAL.";
	}
	
	@Override
	public PluginControllerConfiguration getConfiguration(Map<String, Object> parameters)
			throws PluginControllerException {
		return new PluginControllerConfiguration(initialisePropertyList(), null, null, null, 0, MAX_NUM_GENERIC_SENSORS, 0, getPluginIconResource(), null);
	}
	
	@Override
	protected void putSensors(float[] sensors) {
		for (float s : sensors) {
			getSocket().put(s);
		}
	}
	
	@Override
	protected void initialiseConnection(String address, int port) throws PluginControllerException {
		CommLayerParams params = new CommLayerParams(address, port);
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

package de.htw.berlin.polysun4diac.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.htw.berlin.polysun4diac.plugins.BatteryActorController;
import de.htw.berlin.polysun4diac.plugins.BatterySensorController;
import com.velasolaris.plugin.controller.spi.AbstractControllerPlugin;
import com.velasolaris.plugin.controller.spi.IPluginController;

/**
 * Controller plugin for Polysun. Adds the actors and sensors necessary for communicating with 4diac-RTE (FORTE).
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 *
 */
public class ForteActorSensorPlugin extends AbstractControllerPlugin {

	@Override
	public List<Class<? extends IPluginController>> getControllers(Map<String, Object> parameters) {
		List<Class<? extends IPluginController>> controllers = new ArrayList<>();
		controllers.add(BatteryActorController.class);
		controllers.add(BatterySensorController.class);
		controllers.add(PVSensorController.class);
		controllers.add(PVActorController.class);
		controllers.add(LoadSensorController.class);
		controllers.add(SGReadyHeatPumpController.class);
		controllers.add(GenericActorController.class);
		controllers.add(GenericSensorController.class);
		controllers.add(GenericForteController.class);
		controllers.add(BatteryPreSimulatorSocket.class);
		return controllers;
	}

	@Override
	public String getCreator() {
		return "Marc Jakobi, HTW Berlin";
	}

	@Override
	public String getDescription() {
		return "Actors and sensors for connecting Polysun components with IEC 61499 control applications running on 4diac-RTE (FORTE).";
	}
	
	@Override
	public String getVersion() {
		return "1.0";
	}
}

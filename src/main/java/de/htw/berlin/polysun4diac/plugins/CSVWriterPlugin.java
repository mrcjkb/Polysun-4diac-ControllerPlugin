package de.htw.berlin.polysun4diac.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.htw.berlin.io.csv.plugins.CSVWriterController;
import com.velasolaris.plugin.controller.spi.AbstractControllerPlugin;
import com.velasolaris.plugin.controller.spi.IPluginController;

/**
 * Controller plugin for Polysun. Adds the actors and sensors necessary for communicating with 4diac-RTE (FORTE).
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 *
 */
public class CSVWriterPlugin extends AbstractControllerPlugin {

	@Override
	public List<Class<? extends IPluginController>> getControllers(Map<String, Object> parameters) {
		List<Class<? extends IPluginController>> controllers = new ArrayList<>();
		controllers.add(CSVWriterController.class);
		return controllers;
	}

	@Override
	public String getCreator() {
		return "Marc Jakobi, HTW Berlin";
	}

	@Override
	public String getDescription() {
		return "A plugin controller for writing Polysun control inputs to a CSV file.";
	}
	
	@Override
	public String getVersion() {
		return "1.0";
	}
}

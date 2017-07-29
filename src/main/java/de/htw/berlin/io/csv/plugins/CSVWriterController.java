package de.htw.berlin.io.csv.plugins;

import java.util.Map;

import com.velasolaris.plugin.controller.spi.AbstractPluginController;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration;
import com.velasolaris.plugin.controller.spi.PluginControllerException;

public class CSVWriterController extends AbstractPluginController {

	public CSVWriterController() {
		super();
	}

	@Override
	public String getName() {
		return "CSV Writer";
	}

	@Override
	public String getVersion() {
		return "1.0 - alpha";
	}
	
	@Override
	public PluginControllerConfiguration getConfiguration(Map<String, Object> parameters)
			throws PluginControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] control(int simulationTime, boolean status, float[] sensors, float[] controlSignals, float[] logValues,
			boolean preRun, Map<String, Object> parameters) throws PluginControllerException {
		// TODO Auto-generated method stub
		return null;
	}

}

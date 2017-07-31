package de.htw.berlin.io.csv.plugins;

import static de.htw.berlin.polysun4diac.CommonFunctionsAndConstants.getPluginIconResource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.velasolaris.plugin.controller.spi.AbstractPluginController;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration;
import com.velasolaris.plugin.controller.spi.PluginControllerException;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Property;

public class CSVWriterController extends AbstractPluginController {

	private static final String PATH_KEY = "File path and name:";
	private static final int MAX_NUM_GENERIC_SENSORS = 30;
	
	private BufferedWriter mBuffer = null;
	private Writer mWriter = null;
	
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
		List<Property> properties = new ArrayList<>();
		String path = System.getProperty("user.home") + "/Desktop/output.csv";
		properties.add(new Property(PATH_KEY, path, "The host name (e.g., the IP address) of the function block this plugin connects to."));
		return new PluginControllerConfiguration(properties, null, null, null, 0, MAX_NUM_GENERIC_SENSORS, 0, getPluginIconResource(), null);
	}

	@Override
	public int[] control(int simulationTime, boolean status, float[] sensors, float[] controlSignals, float[] logValues,
			boolean preRun, Map<String, Object> parameters) throws PluginControllerException {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}

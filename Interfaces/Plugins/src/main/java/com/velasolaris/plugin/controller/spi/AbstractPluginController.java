package com.velasolaris.plugin.controller.spi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.ControlSignal;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Log;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Sensor;
import com.velasolaris.plugin.controller.spi.PolysunSettings.PropertyValue;

/**
 * Abstract plugin controller containing convenience code. Concrete plugin
 * controllers should inherit from this class instead of directly from the
 * ControllerPlugin interface.
 *
 * @author rkurmann
 * @since Polysun 9.1
 *
 */
public abstract class AbstractPluginController implements IPluginController {

	/** Static instance of the Logger for this class */
	protected static Logger sLog = Logger.getLogger(AbstractPluginController.class.getName());

	/**
	 * Field for the latest configured values from Polysun.
	 */
	private PolysunSettings polysunSettings;

	/**
	 * Used/set sensors in Polysun configuration, i.e. sensor linked to a
	 * component. In the order of declaration.
	 */
	protected boolean[] sensorsUsed;
	/**
	 * Used/set control signals in Polysun configuration, i.e. control signal
	 * linked to a component. In the order of declaration.
	 */
	protected boolean[] controlSignalsUsed;
	/**
	 * Property values array. In the order of declaration. In the order of
	 * declaration.
	 */
	protected PropertyValue[] properties;
	/**
	 * Property values as int array. Values may be converted. In the order of
	 * declaration.
	 */
	protected int[] propertiesInt;
	/**
	 * Property values as float array. Values may be converted. In the order of
	 * declaration.
	 */
	protected float[] propertiesFloat;
	/**
	 * Property values as string array. Values may be converted. In the order of
	 * declaration.
	 */
	protected String[] propertiesString;
	/**
	 * Property names as string array. Values may be converted. In the order of
	 * declaration.
	 */
	protected String[] propertiesName;
	/** Sensor names as string array. In the order of declaration. */
	protected String[] sensorsName;
	/** Controls signal names as string array. In the order of declaration. */
	protected String[] controlSignalsName;

	/** Number of remote function calls. */
	protected long nRemoteFunctionCalls;

	/** Total time used for remote function call [ns] */
	protected long sumRemoteFunction;

	/** Start time of a (remote) function call measurement [ns] (not an absolute value) */
	protected long startMeasureFunctionCall;

	/** Path of the plugin data. Used e.g. for scripts. */
	protected String pluginDataPath;


	/**
	 * Returns the technical ID of the plugin controller, i.e. the class name of
	 * the plugin controller.
	 *
	 * @return Technical id (String representation of the class name)
	 */
	public String getId() {
		return getClass().getName();
	}

	@Override
	public void build(PolysunSettings polysunSettings, Map<String, Object> parameters)
			throws PluginControllerException {
		setPolysunSettings(polysunSettings);
	}

	/**
	 * Sets configuration such as pluginDataPath.
	 *
	 * @param parameters Generic parameters
	 */
	protected void setConfiguration(Map<String, Object> parameters) {
		pluginDataPath = "" + parameters.get("Plugin.DataPath");
	}


	/**
	 * Returns the control signal setting for a control signal name.
	 *
	 * @param controlSignalName
	 *            the name of the control signal
	 * @return the control signal setting, or <code>null</code> if not found
	 */
	public ControlSignal getControlSignal(String controlSignalName) {
		if (polysunSettings == null) {
			return null;
		}
		return polysunSettings.getControlSignal(controlSignalName);
	}

	/**
	 * Returns the array index for the control signal name.
	 *
	 * @param controlSignalName
	 *            the name of the signal name
	 * @return array index, or -1 if not found
	 * @see #getCSIdx(String)
	 */
	public int getControlSignalIndex(String controlSignalName) {
		if (polysunSettings == null) {
			return INVALID_INDEX;
		}
		return polysunSettings.getControlSignalIndex(controlSignalName);
	}

	/**
	 * Returns all control signals (configured and generic).
	 *
	 * @return List of control signals in the order as shown in the GUI
	 *         (configured control signals and genric control signals). The same
	 *         order is also used in the control signal float array of
	 *         {@link IPluginController#control(int, boolean, float[], float[], float[], boolean, Map)}.
	 * @see com.velasolaris.plugin.controller.spi.PolysunSettings#getControlSignals()
	 */
	public List<ControlSignal> getControlSignals() {
		return polysunSettings.getControlSignals();
	}

	@Override
	public List<String> getControlSignalsToHide(PolysunSettings polysunSettings, Map<String, Object> parameters) {
		setPolysunSettings(polysunSettings);
		return new ArrayList<>();
	}

	/**
	 * Returns an boolean array for control signals where <code>true</code> at i
	 * if this control signal is used in Polysun.
	 *
	 * @return boolean array indicating if a control signal is set in Polysun
	 *         configuration
	 */
	public boolean[] getControlSignalsUsed() {
		return polysunSettings.getControlSignalsUsed();
	}

	@Override
	public String getCreator() {
		return null;
	}

	/**
	 * Returns the array index for the control signal name. Convenience method
	 * with a shorter method name.
	 *
	 * @param controlSignalName
	 *            the name of the signal name
	 * @return array index, or -1 if not found
	 * @see #getControlSignalIndex(String)
	 */
	public int getCSIdx(String controlSignalName) {
		return getControlSignalIndex(controlSignalName);
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getDocumentation() {
		return null;
	}

	/**
	 * Returns all logs.
	 *
	 * @return List of logs in the order as declared.
	 * @see com.velasolaris.plugin.controller.spi.PolysunSettings#getLogs()
	 */
	public List<Log> getLogs() {
		return polysunSettings.getLogs();
	}

	/**
	 * Returns the property value for a given name Convenience method with a
	 * shorter method name.
	 *
	 * @param propertyName
	 *            name of the property
	 * @return the property value or <code>null</code> if not found
	 */
	public PropertyValue getProp(String propertyName) {
		return getProperty(propertyName);
	}

	@Override
	public List<String> getPropertiesToHide(PolysunSettings polysunSettings, Map<String, Object> parameters) {
		setPolysunSettings(polysunSettings);
		return new ArrayList<>();
	}

	/**
	 * Returns the property value for a given name
	 *
	 * @param propertyName
	 *            name of the property
	 * @return the property value or <code>null</code> if not found
	 * @see #getProp(String)
	 */
	public PropertyValue getProperty(String propertyName) {
		if (polysunSettings == null) {
			return null;
		}
		return polysunSettings.getPropertyValue(propertyName);
	}

	/**
	 * Gets the property value for a property name as set by the user in the
	 * plugin controller GUI.
	 *
	 * @param index
	 *            index of the property as name given in
	 *            {@linkplain PluginControllerConfiguration}.
	 * @return property value or <code>null</code> if not found
	 */
	public PropertyValue getPropertyValue(int index) {
		return this.polysunSettings.getPropertyValue(index);
	}

	/**
	 * Gets the property value for a property name as set by the user in the
	 * plugin controller GUI.
	 *
	 * @param propertyName
	 *            name of the property as name given in
	 *            {@linkplain PluginControllerConfiguration}. Generic properties
	 *            are named with {@link PolysunSettings#GENERIC_PROPERTY_PREFIX} + number where
	 *            the number starts with 1, e.g. PluginControllerProperty1.
	 * @return property value or <code>null</code> if not found
	 */
	public PropertyValue getPropertyValue(String propertyName) {
		return polysunSettings.getPropertyValue(propertyName);
	}

	/**
	 * Gets the index of a property in the property array method.
	 *
	 * @param propertyName
	 *            name of the property as name given in
	 *            {@linkplain PluginControllerConfiguration}. Generic properties
	 *            are named with {@link PolysunSettings#GENERIC_PROPERTY_PREFIX} + number where
	 *            the number starts with 1, e.g. PluginControllerProperty1.
	 * @return the index in the float array, or -1 if not found
	 */
	public int getPropertyIndex(String propertyName) {
		return polysunSettings.getPropertyValueIndex(propertyName);
	}

	/**
	 * Returns the sensor setting for a sensor name.
	 *
	 * @param sensorName
	 *            the name of the sensor
	 * @return the sensor setting, or <code>null</code> if not found
	 */
	public Sensor getSensor(String sensorName) {
		if (polysunSettings == null) {
			return null;
		}
		return polysunSettings.getSensor(sensorName);
	}

	/**
	 * Returns the value of a sensor given by name.
	 *
	 * @param sensorName
	 *            the name of the sensor
	 * @param sensors
	 *            the sensor array
	 * @return the value for the sensor given by name
	 * @see #getSnIdx(String)
	 */
	public float getSensor(String sensorName, float[] sensors) {
		return sensors[getSensorIndex(sensorName)];
	}

	/**
	 * Returns the array index for the sensor name.
	 *
	 * @param sensorName
	 *            the name of the sensor
	 * @return array index, or -1 if not found
	 */
	public int getSensorIndex(String sensorName) {
		if (polysunSettings == null) {
			return INVALID_INDEX;
		}
		return polysunSettings.getSensorIndex(sensorName);
	}

	/**
	 * Returns the all sensors (configured and generic).
	 *
	 * @return List of sensors in the order as shown in the GUI (configured
	 *         sensors and generic sensors). The same order is also used in the
	 *         control signal float array of
	 *         {@link IPluginController#control(int, boolean, float[], float[], float[], boolean, Map)}.
	 * @see com.velasolaris.plugin.controller.spi.PolysunSettings#getSensors()
	 */
	public List<Sensor> getSensors() {
		return polysunSettings.getSensors();
	}

	@Override
	public List<String> getSensorsToHide(PolysunSettings polysunSettings, Map<String, Object> parameters) {
		setPolysunSettings(polysunSettings);
		return new ArrayList<>();
	}

	/**
	 * Returns the array index for the sensor name. Convenience method with a
	 * shorter method name.
	 *
	 * @param sensorName
	 *            the name of the sensor
	 * @return array index, or -1 if not found
	 * @see #getSensorIndex(String)
	 */
	public int getSnIdx(String sensorName) {
		return getSensorIndex(sensorName);
	}

	@Override
	public String getVersion() {
		return null;
	}

	@Override
	public int getFixedTimestep(Map<String, Object> parameters) {
		return PluginControllerConfiguration.DEFAULT_TIMESTEP;
	}

	@Override
	public void initialiseSimulation(Map<String, Object> parameters) throws PluginControllerException {
		sumRemoteFunction = 0;
		nRemoteFunctionCalls = 0;
	}

	@Override
	public boolean isEnabled(Map<String, Object> parameters) {
		return true;
	}

	/**
	 * Sets the control signal given by name with the given value to the control
	 * signal array.
	 *
	 * @param controlSignalName
	 *            the name of the control siganl
	 * @param value
	 *            the value to set
	 * @param controlSignals
	 *            the array to change
	 */
	public void setControlSignal(String controlSignalName, float value, float[] controlSignals) {
		controlSignals[getSensorIndex(controlSignalName)] = value;
	}

	/**
	 * Sets the fields from the polysun settings.
	 *
	 * @param polysunSettings
	 *            the values of polysun
	 */
	protected void setPolysunSettings(PolysunSettings polysunSettings) {
		this.polysunSettings = polysunSettings;
		properties = polysunSettings.getPropertyValuesArray();
		sensorsUsed = polysunSettings.getSensorUsed();
		controlSignalsUsed = polysunSettings.getControlSignalsUsed();
		propertiesInt = new int[properties.length];
		propertiesFloat = new float[properties.length];
		propertiesString = new String[properties.length];
		propertiesName = new String[properties.length];
		for (int i = 0; i < properties.length; i++) {
			propertiesInt[i] = properties[i].getInt();
			propertiesFloat[i] = properties[i].getFloat();
			propertiesString[i] = properties[i].getString();
			propertiesName[i] = properties[i].getName();
		}
		sensorsName = new String[getSensors().size()];
		for (int i = 0; i < sensorsName.length; i++) {
			sensorsName[i] = getSensors().get(i).getName();
		}
		controlSignalsName = new String[getControlSignals().size()];
		for (int i = 0; i < controlSignalsName.length; i++) {
			controlSignalsName[i] = getControlSignals().get(i).getName();
		}
	}

	@Override
	public void terminateSimulation(Map<String, Object> parameters) {
	}

	/**
	 * Export a resource embedded into a Jar file to the local file path.
	 *
	 * Ref.
	 * http://stackoverflow.com/questions/10308221/how-to-copy-file-inside-jar-to-outside-the-jar
	 *
	 * @param resourceName
	 *            ie.: "/control.m"
	 * @param destFolder
	 *            the destination folder
	 * @param onlyFile
	 *            should the only the file without resource path be exported?
	 * @return The path to the exported resource
	 * @throws FileNotFoundException if file not found
	 * @throws IOException for I/O problems
	 */
	public String exportResource(String resourceName, String destFolder, boolean onlyFile) throws FileNotFoundException, IOException {
		File resourceFile = new File(resourceName);
		// Note that each / is a directory down in the "jar tree" been the jar
		// the root of the tree
		try (InputStream stream = getClass().getResourceAsStream(resourceName);
				OutputStream resStreamOut = new FileOutputStream(
						destFolder + File.separator + (onlyFile ? resourceFile.getName() : resourceName));) {
			if (stream == null) {
				throw new FileNotFoundException("Cannot get resource \"" + resourceName + "\" from Jar file.");
			}

			int readBytes;
			byte[] buffer = new byte[4096];
			// jarFolder = new
			// File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\',
			// '/');
			while ((readBytes = stream.read(buffer)) > 0) {
				resStreamOut.write(buffer, 0, readBytes);
			}
		}

		return destFolder + File.separator + resourceName;
	}

	@Override
	public void closeResources() {
		// Do nothing
	}

	/**
	 * Start performance measuring (remote) function calls.
	 *
	 * @see AbstractPluginController#logPerfMeasure(String, Map)
	 */
	protected void startMeasureFunctionCall() {
		startMeasureFunctionCall = System.nanoTime();
	}

	/**
	 * End performance measuring (remote) function calls.
	 *
	 * @see AbstractPluginController#logPerfMeasure(String, Map)
	 */
	protected void stopMeasureFunctionCall() {
		sumRemoteFunction += System.nanoTime() - startMeasureFunctionCall;
		nRemoteFunctionCalls++;
	}

	/**
	 * Prints performance statistics about the (remote) function call.
	 *
	 * @param prefix
	 *            A prefix to write before the log
	 * @param parameters
	 *            General parameters for a Polysun print message.
	 * @return the performance measure message, it can be used in sub classes
	 * @throws Exception
	 *             For any problems
	 *
	 * @see #startMeasureFunctionCall()
	 * @see #stopMeasureFunctionCall()
	 */
	protected String logPerfMeasure(String prefix, Map<String, Object> parameters) throws Exception {
		String msg = prefix + "Total function call time: " + (sumRemoteFunction / 1000000000) + "s" + "\n" + prefix
				+ "Avg. remote function call: " + (sumRemoteFunction / 10000 / nRemoteFunctionCalls) / 100f + "ms";
		sLog.fine(msg);
		if (parameters != null) {
			parameters.put("Plugin.PrintMessage", msg);
		}
		return msg;
	}

	/**
	 * Replaces placeholders in a string path, which are [pluginDataPath], [userHome], [projectDir], [polysunDir].
	 * @param path the path where placeholders should be replaced
	 * @param parameters Generic parameters
	 * @return the path where placeholder have been replaced or <code>null</code> if path was <code>null</code>
	 */
	protected String replacePathPlaceholders(String path, Map<String, Object> parameters) {
		if (path != null) {
			return path
					.replaceAll("\\[pluginDataPath\\]", Matcher.quoteReplacement(pluginDataPath))
					.replaceAll("\\[userHome\\]", Matcher.quoteReplacement(System.getProperty("user.home")))
					.replaceAll("\\[projectDir\\]", Matcher.quoteReplacement((String) parameters.get("Polysun.ProjectDir")))
					.replaceAll("\\[polysunDir\\]", Matcher.quoteReplacement((String) parameters.get("Polysun.InstallPathAbsolute")))
					.replaceAll("[\\/]", Matcher.quoteReplacement(File.separator));
		}
		return null;
	}

}

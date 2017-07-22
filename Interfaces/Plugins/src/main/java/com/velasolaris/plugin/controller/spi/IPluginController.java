package com.velasolaris.plugin.controller.spi;

import java.util.List;
import java.util.Map;

/**
 * Interface for controllers that can be added as plugin to Polysun.
 * I.e. this interface defines a controller.
 * 
 * This is the Service Provider Interface (SPI).
 * 
 * Objects will be created by the default constructor. Thus, a constructor without any parameters must be available.
 * 
 * A plugin controller has uses the following concepts:
 * <ul>
 * <li>Sensors, input values "measured" in Polysun
 * <li>Control signals, the output of the control algorithm
 * <li>Logs, intermediate values to log and show in Polysun
 * </ul>.
 * 
 * There are configured and generic sensors and control signals.
 * Generic sensors and control signals behave as in ProgrammableController of Polysun.
 * 
 * Available values in the general parameters set by Polysun:
 * <ul>
 * <li>Plugin.DataPath, String: Proposed path for config data, within the Polysun user data folders
 * <li>Polysun.UserLevel, Integer: Level of the current license, 2 = Professional, 3 = Designer, 4 = Developer
 * <li>Polysun.Version, Long: The current version number, as Long
 * <li>Polysun.DataPath, String: Path to the data folder of Polysun.
 * <li>Polysun.DataPathAbsolute, String: Absolute path to the data folder of Polysun.
 * <li>Polysun.InstallPath, String: Path to the Polysun executable
 * </ul>
 * 
 * Available values in the general parameters read by Polysun:
 * <ul>
 * <li>Plugin.PrintMessage, String or List&lt;String&gt;: Message(s) written to the Polysun MessageArea
 * </ul>
 * 
 * @see PluginControllerConfiguration
 * 
 * @author rkurmann
 * @since Polysun 9.1
 *
 */
public interface IPluginController {
	
	static final int INVALID_INDEX = -1;
	
	/**
	 * The human readable name of the plugin controller. E.g. "Flowrate".
	 * This method is called if the name of the plugin controller is required.
	 * 
	 * @return Short name, must not <code>null</code>
	 */
	String getName();
	
	/**
	 * The creator of this plugin controller. E.g. "Vela Solaris".
	 * This method is called if the creator of the plugin controller is required.
	 * 
	 * @return Plain text or <code>null</code> if there is creator.
	 */
	String getCreator();
	
	/**
	 * The version of this plugin controller. E.g. "1.0".
	 * This method is called if the version of the plugin controller is required.
	 * 
	 * @return Plain text (semantic versioning suggested) or <code>null</code> if there is no version
	 */
	String getVersion();
	
	/**
	 * A short description of this plugin controller.
	 * E.g. "Controls the status (on/off) of one or two different components and the flowrate of a given pump based on two flowrate sensors."
	 * This method is called if a description of the plugin controller is shown.
	 * 
	 * @return Plain text or <code>null</code> if there is no description
	 */
	String getDescription();
	
	/**
	 * Documentation of this plugin controller.
	 * This method is called if a documentation of the plugin controller is shown.
	 * 
	 * @return Plain text or HTML or <code>null</code> if there is no description
	 */
	String getDocumentation();
	
	/**
	 * Is this plugin controller enabled?
	 * 
	 * @param parameters Generic parameters
	 * @return <code>true</code> if enabled, otherwise <code>false</code>
	 */
	boolean isEnabled(Map<String, Object> parameters);
	
	/**
	 * Configuration of this plugin controllers. E.g. the properties in the controller setting GUI, the sensors, the log values and the signals to control.
	 * This method is called when the plugin controller in Polysun is created and every time when the variant is reloaded.
	 * 
	 * @param parameters Generic parameters
	 * @return Plugin controller configuration. <code>null</code> sets a default configuration.
	 * @throws PluginControllerException if there is a problem creating the plugin controller configuration.
	 */
	PluginControllerConfiguration getConfiguration(Map<String, Object> parameters) throws PluginControllerException;
	
	/**
	 * Method to pass Polysun properties to this plugin controller.
	 * E.g. plugin controller GUI settings.
	 * This method is called when Polysun plugin controller in Polysun is built, e.g. a plugin controller property has been changed by the user.
	 * 
	 * The state of the current configuration could be kept for later use during the simulation.
	 * 
	 * @param polysunSettings The properties and settings of Polysun
	 * @param parameters Generic parameters
	 * @throws PluginControllerException if there is a problem creating the plugin controller configuration.
	 */
	void build(PolysunSettings polysunSettings, Map<String, Object> parameters) throws PluginControllerException;
	
	/**
	 * Determines the GUI properties which must be hidden in the current configuration.
	 * E.g. if the flowrate is not fixed, the Fixflowrate property must be hidden.
	 * 
	 * @param polysunSettings The configuration Polysun, user settings of properties
	 * @param parameters Generic parameters
	 * @return The list property names that must be hidden in the given configuration.
	 */
	List<String> getPropertiesToHide(PolysunSettings polysunSettings, Map<String, Object> parameters);
	
	
	/**
	 * Determines the sensors which must be hidden in the current configuration.
	 * E.g. if the flowrate is fixed, the flowrate sensor must be hidden.
	 * 
	 * @param polysunSettings The configuration Polysun, user settings of properties
	 * @param parameters Generic parameters
	 * @return The list sensor names that must be hidden in the given configuration.
	 */
	List<String> getSensorsToHide(PolysunSettings polysunSettings, Map<String, Object> parameters);
	
	/**
	 * Determines the controlSignals which must be hidden in the current configuration.
	 * 
	 * @param polysunSettings The configuration Polysun, user settings of properties
	 * @param parameters Generic parameters
	 * @return The list control signal names that must be hidden in the given configuration.
	 */
	List<String> getControlSignalsToHide(PolysunSettings polysunSettings, Map<String, Object> parameters);
	
	/**
	 * Fixed timestep. For each timepoint which is a multiple of this
	 * fixedTimestep, the simulation does a timestep. The Polysun solver can do
	 * more timesteps if necessary. Example, for fixedTimestep of 180s, the
	 * simulation solver does a simulation at least at 0s, 180s, 360s, 480s,
	 * 720s, ...
	 *
	 * @param parameters General parameters
	 * @return The fixed timestep, or a value less than 1 for the default timestep (240s
	 *         during day and 720s during the night)
	 */
	int getFixedTimestep(Map<String, Object> parameters);

	/**
	 * This method is called once before the simulation starts.
	 * 
	 * @param parameters Generic parameters
	 * @throws PluginControllerException if there is a problem
	 */
	void initialiseSimulation(Map<String, Object> parameters) throws PluginControllerException;
	
	/**
	 * Does the control work. This method is called each timestep by Polysun if signals must be controlled according to the sensor values.
	 * 
	 * @param simulationTime The simulation time in [s] beginning from the 1. January 00:00 (no leap year). 
	 * @param status The status of this controller according to user settings,
	 * 		<code>true</code> means enabled,
	 * 		<code>false</code> disabled.
	 * 		The status originates from the timer setting of the controller dialog. The user can enable
	 * 		or disable the controller for certain hours, days or month.
	 * 		This value should be respected by the controller implementation, otherwise it
	 * 		could lead to an unexpected user experience.
	 * @param sensors The values of the sensors configured by the user (Input parameter).
	 * @param controlSignals The control signals set by this plugin controller (Output parameter). 
	 * @param logValues The values to log in Polysun, e.g intermediate results. This value can be ignored.
	 * These values are shown in the Simulation Analysis or in the Log and Parameterizing output. 
	 * @param preRun Is this the real simulation or a pre run phase? This value can be ignored.
	 * @param parameters Generic parameters
	 * @return Timepoints [s].
	 * 		Registers these timepoints in the future, where the simulation have to do a timestep.
	 * 		It doesn't matter, if the same timepoint will be registered several times.
	 * 		Timepoint in the array is in seconds from the 1. Jan. 00:00, or <code>null</code> if no additional timesteps are required.
	 * 		These timesteps can be used for time based controlling strategies.
	 * @throws PluginControllerException if there is a problem. The simulation will be aborted.
	 */
	int[] control(int simulationTime, boolean status, float[] sensors, float[] controlSignals, float[] logValues, boolean preRun, Map<String, Object> parameters) throws PluginControllerException;
	
	/**
	 * This method is called once after the simulation.
	 * 
	 * @param parameters Generic parameters
	 */
	void terminateSimulation(Map<String, Object> parameters);
	
	/**
	 * Closes resources and cleans up.
	 * This method is called if resources should be freed.
	 * 
	 * Examples: Close files, disconnect connections
	 */
	void closeResources();
	
}

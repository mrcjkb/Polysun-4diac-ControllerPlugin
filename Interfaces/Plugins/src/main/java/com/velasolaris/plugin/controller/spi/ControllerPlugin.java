package com.velasolaris.plugin.controller.spi;

import java.util.List;
import java.util.Map;

/**
 * This interface defines a controller plugin in Polysun. I.e. this interface defines a plugin.
 * 
 * Implementing classes are automatically found from the classpath at startup of Polysun.
 * 
 * The controller plugin returns a list of plugin controllers that are available through
 * this plugin.
 * 
 * Configuration for automatic detection:
 * 
 * <ol>
 * <li>Create for each controller plugin a 
 * <code>META-INF/services/com.velasolaris.plugin.controller.spi.ControllerPlugin</code>
 * where 
 * the content of the file is the implementation of the controller plugin, e.g.
 * <code>com.velasolaris.plugin.controller.flowrate.FlowrateControllerPlugin</code>.
 * <li>Add JAR containing the source code and the
 * <code>META-INF/services/com.velasolaris.plugin.controller.spi.ControllerPlugin</code>
 * to the classpath.
 * </ol>
 * 	
 * @author rkurmann
 * @since Polysun 9.1
 *
 */
public interface ControllerPlugin {
	
	/** Plugin controller interface version 1 */
	static final int PLUGIN_CONTROLLER_INTERFACE_VERSION = 1;
	
	/**
	 * The human readable name of the controller plugin controller. E.g. "FlowrateControllerPlugin".
	 * This method is called if the name of the plugin controller is required.
	 * 
	 * @return Short name, must not <code>null</code>
	 */
	String getName();

	/**
	 * The controllers contained in this controller plugin.
	 * 
	 * @param parameters Generic parameters
	 * @return List of classes of type IPluginController
	 */
	List<Class<? extends IPluginController>> getControllers(Map<String, Object> parameters);
	
	/**
	 * The creator of this controller plugin. E.g. "Vela Solaris".
	 * This method is called if the creator is needed.
	 * 
	 * @return Plain text or <code>null</code> if there is creator.
	 */
	String getCreator();
	
	/**
	 * The version of this controller plugin. E.g. "1.0".
	 * This method is called if the version is required.
	 * 
	 * @return Plain text (semantic versioning suggested) or <code>null</code> if there is no version
	 */
	String getVersion();
	
	/**
	 * A short description of this controller plugin.
	 * This method is called if a description of the controller plugin is shown.
	 * 
	 * @return Plain text or <code>null</code> if there is no description
	 */
	String getDescription();
	
	/**
	 * Is this controller plugin enabled?
	 * 
	 * @param parameters Generic parameters
	 * @return <code>true</code> if enabled, otherwise <code>false</code>
	 */
	boolean isEnabled(Map<String, Object> parameters);
	
	/**
	 * Returns the supported plugin controller interface version.
	 * 
	 * Incrementing integer for each incompatible interface change.
	 * 
	 * @param parameters Generic parameters
	 * @return 1 for Polysun 9.1 interface version
	 */
	int getSupportedInterfaceVersion(Map<String, Object> parameters);

}

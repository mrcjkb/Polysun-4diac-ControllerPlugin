package com.velasolaris.plugin.controller.spi;

import java.util.Map;

/**
 * Abstract ControllerPlugin class.
 * Concrete controller plugins should inherit from this class
 * instead of directly from the ControllerPlugin interface.
 * 
 * @author rkurmann
 * @since Polysun 9.1
 *
 */
public abstract class AbstractControllerPlugin implements ControllerPlugin {

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public String getVersion() {
		return null;
	}

	@Override
	public boolean isEnabled(Map<String, Object> aParameters) {
		return true;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [" + getControllers(null) + "]";
	}

	@Override
	public int getSupportedInterfaceVersion(Map<String, Object> parameters) {
		return PLUGIN_CONTROLLER_INTERFACE_VERSION;
	};
	

}

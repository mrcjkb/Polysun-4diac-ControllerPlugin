package com.velasolaris.plugin.controller.spi;

/**
 * Helper class for plugin controllers (Singleton).
 * 
 * @author rkurmann
 * @since Polysun 9.1
 *
 */
public class PluginControllerHelper {

	private static PluginControllerHelper service;
	
	private PluginControllerHelper() {}
	
	public static synchronized PluginControllerHelper getInstance() {
		if (service == null) {
			service = new PluginControllerHelper();
		}
		return service;
	}	
	
}

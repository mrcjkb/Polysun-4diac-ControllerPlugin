package com.velasolaris.plugin.controller;

import com.velasolaris.plugin.controller.spi.PluginControllerException;

/**
 * Thrown when a plugin controller could not be found (anymore).
 * 
 * @author rkurmann
 * @since Polysun 9.1
 *
 */
public class PluginControllerNotFoundException extends PluginControllerException {

	private static final long serialVersionUID = 1L;

	public PluginControllerNotFoundException() {
	}

	public PluginControllerNotFoundException(String aMessage) {
		super(aMessage);
	}

	public PluginControllerNotFoundException(Throwable aCause) {
		super(aCause);
	}

	public PluginControllerNotFoundException(String aMessage, Throwable aCause) {
		super(aMessage, aCause);
	}

	public PluginControllerNotFoundException(String aMessage, Throwable aCause, boolean aEnableSuppression,
			boolean aWritableStackTrace) {
		super(aMessage, aCause, aEnableSuppression, aWritableStackTrace);
	}

}

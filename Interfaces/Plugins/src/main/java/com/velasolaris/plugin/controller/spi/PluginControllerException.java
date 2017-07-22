package com.velasolaris.plugin.controller.spi;

/**
 * Exception signaling a problem with a plugin controller.
 * See chained exception for the root cause.
 * 
 * @author rkurmann
 * @since Polysun 9.1
 *
 */
public class PluginControllerException extends Exception {

	private static final long serialVersionUID = 1L;

	public PluginControllerException() {
	}

	public PluginControllerException(String aMessage) {
		super(aMessage);
	}

	public PluginControllerException(Throwable aCause) {
		super(aCause);
	}

	public PluginControllerException(String aMessage, Throwable aCause) {
		super(aMessage, aCause);
	}

	public PluginControllerException(String aMessage, Throwable aCause, boolean aEnableSuppression,
			boolean aWritableStackTrace) {
		super(aMessage, aCause, aEnableSuppression, aWritableStackTrace);
	}

}

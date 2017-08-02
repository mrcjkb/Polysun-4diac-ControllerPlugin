package de.htw.berlin.polysun4diac;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JOptionPane;

public class CommonFunctionsAndConstants {

	/** Conversion factor for kilo units to SI. */
	public static final int KILOTOSI = 1000;
	/** For initializing data to zero. */
	public static final int ZERO_INIT = 0;
	/**
	 * Number of bytes used to indicate a FORTE STRING's length in it's byte
	 * header.
	 */
	public static final int LENGTHHEADERNUM = 2;
	/** Path to the default plugin controller image. */
	public static final String DEF_IMGPATH = "plugin/images/controller_plugin.png";
	/** Path to the plugin controller image. */
	public static final String IMGPATH = "plugin/images/controller_4diac.png";
	/** Default client address for communication with 4diac function blocks. */
	public static final String DEF_TCP_ADDRESS = "localhost";
	/** Smallest port number allowed (0 would cause the system to assign a free port randomly, which is not desirable here. */
	public static final int MIN_PORT_NUMBER = 1;
	/** Largest port number allowed. */
	public static final int MAX_PORT_NUMBER = 65535;
	/** Default port number for communication with 4diac function blocks. */
	public static final int DEF_PORT_NUMBER = 61499;
	/** Sleep time in ms for Threads in JUnit tests. */
	public static final int THREAD_SLEEP_TIME = 1000;
	/** Maximum number of generic control signals */
	public static final int MAX_NUM_GENERIC_SENSORS = 5;
	/** Maximum number of generic control signals */
	public static final int MAX_NUM_GENERIC_SIGNALS = 5;
	/** One hour in seconds */
	public static final int SECONDS_PER_HOUR = 3600;
	/** Number of seconds per year */
	public static final int NUM_SECONDS_PER_YEAR = 31536000;

	/**
	 * Attempts to load the custom 4diac plugin icon.
	 * @return A <code>String</code> representing the relative path to the icon. If loading the custom 4diac plugin icon fails, a <code>String</code>
	 * representing the relative path to the default plugin controller icon is returned. 
	 */
	public static String getPluginIconResource() {
		if (ClassLoader.getSystemResource(IMGPATH) == null) {
			return DEF_IMGPATH;
		}
		return IMGPATH;
	}
	
	/**
	 * @deprecated
	 * This method is deprecated and currently does not appear to work as intended. It was left here in case it may be useful for later use.
	 * Attempts to add a resource to the System CLASSPATH
	 * @param u <code>URL</code> to the resource
	 * @return a <code>String</code>  with the relative path to the resource (<code>null</code> if adding the resource failed)
	 */
	public static String addPath(URL u) {
		URL urltest = ClassLoader.getSystemResource(u.toString());
		if (urltest == null) {
			URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		    Class<URLClassLoader> urlClass = URLClassLoader.class;
		    // Use reflection to call URLClassLoader's protected addURL() method.
		    Method method;
			try {
				method = urlClass.getDeclaredMethod("addURL", new Class[]{URL.class});
				method.setAccessible(true);
			    method.invoke(urlClassLoader, new Object[]{u});
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Failed to add resource to CLASSPATH", "InfoBox: ", JOptionPane.INFORMATION_MESSAGE);
				// Ignore for now.
			}
		}
		// Convert URL to String representing relative path 
		// e.g., C:/Users/.../Polysun/plugins/ForteActorSensorPlugin.jar!/plugins/images/controller_4diac.png
		String resource = u.toString().substring(u.toString().indexOf("/"));
		System.out.println(resource);
		urltest = ClassLoader.getSystemResource(resource);
		if (urltest == null) {
			return null;
		}
		return resource;
	}
}

package de.htw.berlin.polysun4diac.plugins;

import static de.htw.berlin.polysun4diac.CommonFunctionsAndConstants.DEF_PORT_NUMBER;
import static de.htw.berlin.polysun4diac.CommonFunctionsAndConstants.DEF_TCP_ADDRESS;
import static de.htw.berlin.polysun4diac.CommonFunctionsAndConstants.MAX_PORT_NUMBER;
import static de.htw.berlin.polysun4diac.CommonFunctionsAndConstants.MIN_PORT_NUMBER;
import static de.htw.berlin.polysun4diac.forte.datatypes.DateAndTime.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.velasolaris.plugin.controller.spi.AbstractPluginController;
import com.velasolaris.plugin.controller.spi.PluginControllerException;
import com.velasolaris.plugin.controller.spi.PolysunSettings;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.ControlSignal;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Property;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Sensor;
import com.velasolaris.plugin.controller.spi.PolysunSettings.PropertyValue;

import de.htw.berlin.polysun4diac.forte.datatypes.DateAndTime;

/** 
 * Abstract class for Polysun PluginControllers that communicate with 4diac IEC 61499 applications running on 4diac-RTE (FORTE).
 * If more than one controllers are used, it is recommended to set up the socket as a TCP client.
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see de.htw.berlin.polysun4diac.forte.comm.CommLayerParams
 * @see de.htw.berlin.polysun4diac.forte.comm.IForteSocket
 * @see com.velasolaris.plugin.controller.spi.IPluginController
 */
public abstract class Abstract4diacPluginController extends AbstractPluginController {

	/** Key for the host parameter. */
	protected static final String HOST_KEY = "Host name";
	/** Key for the port parameter. */
	protected static final String PORT_KEY = "Port number";
	/** Key for the timestamp option. */
	protected static final String TIMESTAMPSETTING_KEY = "Send time stamp";
	/** Key for the simulation start time specification. */
	protected static final String SIMULATIONSTART_KEY = "Beginning of simulation";
	/** Key for the property specifying the IEC 61499 service type. */
	protected static final String SERVICETYPE_KEY = "Communication service type";
	/** Tool tip for {@link #SERVICETYPE_KEY} */
	protected static final String SERVICETYPE_TOOLTIP = "The communication service interface function block that this plugin represents.";
	/** Index of the CLIENT service type in the {@link #SERVICETYPE_KEY} property. */
	protected static final int CLIENT_IDX = 0;
	/** Index of the SERVER service type in the {@link #SERVICETYPE_KEY} property. */
	protected static final int SERVER_IDX = 1;
	/** Index for disabled sending of time stamp. */
	protected static final int DISABLE_TIMESTAMP = 0;
	
	/**
	 * Initialised to true by default.
	 * Flag for whether or not to send the time stamp to FORTE. 
	 */
	private boolean mSendTimestamp = true;
	/** Used for converting the simulation time to a format FORTE understands. */
	private DateAndTime mForteTimestamp;
	
	@Override
	public String getCreator() {
		return "Marc Jakobi, HTW Berlin";
	}
	
	@Override
	public String getVersion() {
		return "3.0";
	}
	
	@Override
	public void initialiseSimulation(Map<String, Object> parameters) throws PluginControllerException {
		super.initialiseSimulation(parameters);
		if (isConnected()) {
			// Disconnect if a previous connection was left open due to an exception that could not be caught by the PluginController
			disconnect();
		}
		if (sendTimestamp()) {
			setSendTimestamp(getProperty(TIMESTAMPSETTING_KEY).getInt() == 1);
			try {
				// Polysun not yet compatible with Java 8 SE, so the following is commented out for now
//				setForteTimestamp(new DateAndTime(LocalDateTime.parse(getProperty(SIMULATIONSTART_KEY).getString())));
				setForteTimestamp(new DateAndTime(getProperty(SIMULATIONSTART_KEY).getString()));
			} catch (ParseException e) {
				throw new PluginControllerException(getName() + ": The date and time format entered into the plugin controller was not regognised. "
						+ "Please use the format: " + DATEFORMATSTR, e);
			}
		}
		initialiseConnection(getProperty(HOST_KEY).getString(), getProperty(PORT_KEY).getInt());
	}

	@Override
	public void terminateSimulation(Map<String, Object> parameters) {
		disconnect();
	}
	
	@Override
	public List<String> getPropertiesToHide(PolysunSettings propertyValues, Map<String, Object> parameters) {
		List<String> propertiesToHide = super.getPropertiesToHide(propertyValues, parameters);
		if (sendTimestamp()) {
			// Show simulation start box only if sending time stamp is enabled.
			PropertyValue property = propertyValues.getPropertyValue(TIMESTAMPSETTING_KEY);
			if (property != null && property.getInt() == DISABLE_TIMESTAMP) {
				propertiesToHide.add(SIMULATIONSTART_KEY);
			}
		}
		return propertiesToHide;
	}
	
	/**
	 * Initialises the Property list for {@link #getConfiguration(Map)}
	 * @return Property list with the host name and port number input boxes for the GUI.
	 * By default, it contains a String property for the host name, an int property for the port number,
	 * a String[] propterty giving the option to send a time stamp and a String property allowing to set the beginning of the simulation time.
	 */
	protected List<Property> initialisePropertyList() {
		List<Property> properties = new ArrayList<>();
		// Host name and port number
		properties.add(new Property(HOST_KEY, DEF_TCP_ADDRESS, "The host name (e.g., the IP address) of the function block this plugin connects to."));
		properties.add(new Property(PORT_KEY, DEF_PORT_NUMBER, MIN_PORT_NUMBER, MAX_PORT_NUMBER, true, "The port number of the function block this plugin connects to. A valid port value is between 1 and 65535.")); // Port number
		if (sendTimestamp()) { // Call setSendTimestamp(false); in the constructor to disable the next two properties.
			properties.add(new Property(TIMESTAMPSETTING_KEY, new String[] { "no" , "yes" }, DISABLE_TIMESTAMP, "Send a time stamp representing the simulation time to FORTE."));
			int thisYear = Calendar.getInstance().get(Calendar.YEAR);
			DateAndTime dt = new DateAndTime(thisYear, REFMONTH, REFDAY, DEFHOUR, REFMIN, REFSEC, REFMS);
			dt.setSimulationTimeS(0);
			String defaultStart = dt.toString();
			// Polysun not jet compatible with Java 8 SE, so the following is commented out for now
//			int thisYear = LocalDateTime.now().getYear(); 
//			String defaultStart = LocalDateTime.of(thisYear, REFMONTH, REFDAY, DEFHOUR, REFMIN, REFSEC).toString();
			properties.add(new Property(SIMULATIONSTART_KEY, defaultStart, "The date and time at the beginning of the simulation. Please use the format: " + DATEFORMATSTR));
		}
		return properties;
	}
	
	/**
	 * Method for initializing a connection with FORTE.
	 * This method sets up the connection parameters and attempts to connect to a communication service interface function block (CSIFB) on a 4diac application.
	 * @param address The IP address
	 * @param port The port number
	 * @throws PluginControllerException
	 */
	protected abstract void initialiseConnection(String address, int port) throws PluginControllerException;
	
	/**
	 * Method for disconnecting the communication sockets.
	 * This method terminates all connections with FORTE. It is called at the end of the simulation and should be called every time an exception is caught.
	 * To prevent open connections when an exception occurs, wrap the entire control function in a try, catch block as follows:
	 * <pre>
	 * {@code
	 * try {
	 *	 // implement control() method here
	 * } catch (PluginControllerException e) {
	 * 	disconnect();
	 * 	throw e; // rethrow exception
	 * }
	 * </pre>
	 */
	protected abstract void disconnect();
	
	/**
	 * @return <code>true</code> if this plugin is connected to 4diac, <code>false</code> otherwise.
	 */
	protected abstract boolean isConnected();
	
	/**
	 * @return <code>true</code> if any control signals are used, <code>false</code> otherwise.
	 */
	protected boolean isAnyControlSignalUsed() {
		List<ControlSignal> controlSignals = getControlSignals();
		for (ControlSignal c : controlSignals) {
			if (c.isUsed()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return <code>true</code> if any sensors are used, <code>false</code> otherwise.
	 */
	protected boolean isAnySensorUsed() {
		List<Sensor> sensors = getSensors();
		for (Sensor s : sensors) {
			if (s.isUsed()) {
				return true;
			}
		}
		return false;
	}
	
	/** 
	 * Sets a flag indicating whether to send the time stamp to FORTE.
	 * @param flag send time stamp to FORTE (true); don't send time stamp to FORTE (false)
	 */
	protected void setSendTimestamp(boolean flag) {
		mSendTimestamp = flag;
	}
	
	/**
	 * @return true if time stamp should be sent to FORTE, false otherwise.
	 * This method returns true unless the feature is disabled by calling @{link {@link #setSendTimestamp(boolean)} with false as an argument.
	 */
	protected boolean sendTimestamp() {
		return mSendTimestamp;
	}
	
	/**
	 * Initialises the time stamp that is sent to Forte.
	 */
	protected void setForteTimestamp(DateAndTime t) {
		mForteTimestamp = t;
	}
	
	/**
	 * @return the Forte time stamp used to send the simulation time to FORTE.
	 */
	protected DateAndTime getForteTimestamp() {
		return mForteTimestamp;
	}
}

package de.htw.berlin.polysun4diac.plugins;

import static de.htw.berlin.polysun4diac.CommonFunctionsAndConstants.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration;
import com.velasolaris.plugin.controller.spi.PluginControllerException;
import com.velasolaris.plugin.controller.spi.PolysunSettings;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.ControlSignal;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Property;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Sensor;
import com.velasolaris.plugin.controller.spi.PolysunSettings.PropertyValue;

import de.htw.berlin.polysun4diac.exception.UnsupportedForteDataTypeException;
import de.htw.berlin.polysun4diac.forte.comm.CommLayerParams;
import de.htw.berlin.polysun4diac.forte.datatypes.ForteDataType;

/**
 * Plugin controller for receiving the heat pump's control mode from 4diac-RTE (FORTE).
 * The actor takes an LREAL as the control signal.
 * This controller acts as an adapter between the SG ready control modes and Polysun's heat pump control modes.
 * 
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see de.htw.berlin.polysun4diac.forte.comm.IForteSocket
 * @see de.htw.berlin.polysun4diac.forte.comm.CommLayerParams
 * @see com.velasolaris.plugin.controller.spi.IPluginController
 */
public class SGReadyHeatPumpController extends AbstractSingleComponentController {
	
	private static final String SENSOR1 = "Temperature of buffer storage.";
	private static final String CSIGNAL1 = "Heat pump ON/OFF";
	private static final String CSIGNAL2 = "Control mode";
	private static final String CSIGNAL3 = "Internal electric heating element ON/OFF";
	/** Default temperature thresholds at which the heat pump switches from SG Ready modes 3/4 to 2. */
	private static final float[] DEF_TEMP_THRESHOLDS = new float[] {55, 70};
	/** The upper and lower limits for {@link #DEF_TEMP_THRESHOLDS} */
	private static final float[] TEMP_THRESHOLD_LIMITS = new float[] {0, 200};
	/** SG Ready control mode representing heat pump off */
	private static final int OFF_OPERATION = 1;
	/** SG Ready control mode representing normal operation */
	private static final int NORMAL_OPERATION = 2;
	/** SG Ready control mode representing amplified operation */
	private static final int AMPLIFIED_OPERATION = 3;
	/** SG Ready control mode representing on (max power) operation */
	private static final int ONMAX_OPERATION = 4;
	/** Key for the property specifying the temperature threshold at which the heat pump switches from SG Ready mode 3 to 2. */
	private static final String TEMP_THRESHOLD3_KEY = "Temperature threshold for SG Ready mode 3";
	/** Key for the property specifying the temperature threshold at which the heat pump switches from SG Ready mode 4 to 2. */
	private static final String TEMP_THRESHOLD4_KEY = "Temperature threshold for SG Ready mode 4";
	/** Key for the property enabling/disabling the heating element for SG ready mode 3. */
	private static final String HEATING_ELEMENT3_KEY = "SG Ready mode 3 with heating element";
	/** Key for the property enabling/disabling the heating element for SG ready mode 4. */
	private static final String HEATING_ELEMENT4_KEY = "SG Ready mode 4 with heating element";
	/** Key for the property specifying the temperature hysteresis in K below which the heat pump switches back from forced normal operation to control mode 3 or 4 */
	private static final String TEMP_HYSTERESIS_KEY = "Temperature hysteresis";
	/** Default value for {@link #TEMP_HYSTERESIS_KEY} */
	private static final float DEF_TEMP_HYSTERESIS = 5;
	/** Integer representing the setting with a heating element for SG Ready modes 3 and 4. */
	private static final int HAS_HEATING_ELEMENT = 1;
	/** Integer representing the setting without a heating element for SG Ready modes 3 and 4. */
	private static final int HAS_NO_HEATING_ELEMENT = 0;
	/** Number of SG Ready relays for input of the control mode */
	private static final int NUM_RELAYS = 2;
	/** Time limit in s for SG Ready control mode 1 {@link #OFF_OPERATION} */
	private static final int OFF_TIME_LIM_S = 7200; // 2 hours
	/** Time hysteresis: Time after being forced to switch from {@link #OFF_OPERATION} to {@link #NORMAL_OPERATION} at which the heat pump is allowed back into {@link #OFF_OPERATION}. */
	private static final int OFF_TIME_HYSTERESIS_S = 900; // 15 min
	/** Initial value for {@link #mOffOperationBeginTimeS} */
	private static final int INIT_OFFOPERATIONBEGINTIME_S = - (OFF_TIME_LIM_S + OFF_TIME_HYSTERESIS_S + 1);
	
	/** 
	 * Polysun control mode equivalent to the SG Ready control mode 1: OFF </p>
	 * Default Polysun setting for this mode: </p>
	 *  - heat pump: off (max. 2 hours) </p>
	 * 	- control mode: doesn't matter </p>
	 *  - auxiliary heater: off
	 */
	private float[] mSGReadyMode1 = {0.0f, 1.0f, 0.0f};
	/** 
	 * Polysun control mode equivalent to the SG Ready control mode 2: NORMAL OPERATION </p>
	 * Default Polysun setting for this mode: </p>
	 *  - heat pump: on </p>
	 * 	- control mode: 1 (normal operation) </p>
	 *  - auxiliary heater: on
	 */
	private float[] mSGReadyMode2 = {1.0f, 1.0f, 0.0f};
	/** 
	 * Polysun control mode equivalent to the SG Ready control mode 3: AMPLIFIED.
	 * This mode is enabled until the temperature in the buffer tank exceeds a certain temperature, then it switches to {@link #mSGReadyMode1}.</p>
	 * Default Polysun setting for this mode:</p>
	 *  - heat pump: on </p>
	 * 	- control mode: 0 (maximum power)</p>
	 *  - auxiliary heater: off
	 */
	private float[] mSGReadyMode3 = {1.0f, 0.0f, 0.0f};
	/** 
	 * Polysun control mode equivalent to the SG Ready control mode 4: ON + AUXILIARY HEATER. This mode runs all the time. </p>
	 * Default Polysun setting for this mode:</p>
	 *  - heat pump: on </p>
	 * 	- control mode: 0 (maximum power)</p>
	 *  - auxiliary heater: on
	 */
	private float[] mSGReadyMode4 = {1.0f, 0.0f, 1.0f};
	/**
	 * Temperature threshold in °C at which the heat pump switches from SG Ready mode 3 to 2.
	 */
	private float mTempThreshold3;
	/**
	 * Temperature threshold in °C at which the heat pump switches from SG Ready mode 4 to 2.
	 */
	private float mTempThreshold4;
	/** 
	 * Temperature hysteresis in K for automatic control modes. If the temperature falls below the threshold - the hysteresis, the normal control mode is no longer forced.
	 * @see {@link #mTempThreshold3} {@link #mTempThreshold4}
	 */
	private float mTempHysteresis;
	/**
	 * Time at which the {@link #OFF_OPERATION} (SG Ready control mode 1) began (simulation time in s)
	 * @see {@link #control(int, boolean, float[], float[], float[], boolean, Map)
	 */
	private int mOffOperationBeginTimeS = INIT_OFFOPERATIONBEGINTIME_S;
	/** Flag indicating that normal operation has been forced even though control mode 3 or 4 was set. This is set to false when the temperature falls below the hysteresis. */
	private boolean mForcedNormalOperation = false;
	/** Flag indicating that normal operation has been forced even though control mode 1 was set. */
	private boolean mForcedOnOperation = false;
	
	public SGReadyHeatPumpController() throws PluginControllerException {
		super();
		setSendTimestamp(false); // Disable time stamp option, since this plugin cannot send any data to FORTE.
	}
	
	@Override
	public String getName() {
		return "SG Ready Heat Pump Adapter";
	}
	
	@Override
	public String getDescription() {
		return "Actor for receiving the heat pump's SG Ready control mode from 4diac-RTE (FORTE). "
				+ "This controller acts as an adapter between the SG ready control modes and Polysun's heat pump control modes. A modulating heat pump is required.";
	}
	
	@Override
	public PluginControllerConfiguration getConfiguration(Map<String, Object> parameters) {
		List<ControlSignal> controlSignals = new ArrayList<>();
		controlSignals.add(new ControlSignal(CSIGNAL1, "", false, true, "The heat pump's on/off switch."));
		controlSignals.add(new ControlSignal(CSIGNAL2, "", false, true, "The Polysun control mode of the heat pump. A modulating heat pump is required in order to be able set this control signal."));
		controlSignals.add(new ControlSignal(CSIGNAL3, "", false, false, "The internal electric heating element's on/off switch."));
		List<Sensor> sensors = new ArrayList<>();
		sensors.add(new Sensor(SENSOR1, "°C", true, false, "The AC power output of the PV field."));
		return new PluginControllerConfiguration(initialisePropertyList(), sensors, controlSignals, null, 0, 0, 0, getPluginIconResource(), null);
	}

	@Override
	protected List<Property> initialisePropertyList() {
		List<Property> properties = super.initialisePropertyList();
		properties.add(new Property(TEMP_THRESHOLD3_KEY, DEF_TEMP_THRESHOLDS[0], TEMP_THRESHOLD_LIMITS[0], TEMP_THRESHOLD_LIMITS[1], "°C", "The temperature threshold at which the heat pump switches from SG Ready mode 3 to 2."));
		properties.add(new Property(HEATING_ELEMENT3_KEY, new String[] { "no" , "yes" }, HAS_NO_HEATING_ELEMENT, "Does the SG Ready control mode 3 use a heating element?"));
		properties.add(new Property(TEMP_THRESHOLD4_KEY, DEF_TEMP_THRESHOLDS[1], TEMP_THRESHOLD_LIMITS[0], TEMP_THRESHOLD_LIMITS[1], "°C", "The temperature threshold at which the heat pump switches from SG Ready mode 4 to 2."));
		properties.add(new Property(HEATING_ELEMENT4_KEY, new String[] { "no" , "yes" }, HAS_HEATING_ELEMENT, "Does the SG Ready control mode 4 use a heating element?"));
		properties.add(new Property(TEMP_HYSTERESIS_KEY, DEF_TEMP_HYSTERESIS, TEMP_THRESHOLD_LIMITS[0], TEMP_THRESHOLD_LIMITS[1], "K", "Temperature hysteresis in K for automatic SG Ready control modes 3 and 4. If the temperature falls below the threshold minus the hysteresis, the normal control mode is no longer forced."));
		return properties;
	}
	
	@Override
	public List<String> getControlSignalsToHide(PolysunSettings propertyValues, Map<String, Object> parameters) {
		List<String> controlSignalsToHide = super.getControlSignalsToHide(propertyValues, parameters);
		// Show heating element sensor only if one of the SG Ready control modes uses an auxiliary heater.
		PropertyValue property3 = propertyValues.getPropertyValue(HEATING_ELEMENT3_KEY);
		PropertyValue property4 = propertyValues.getPropertyValue(HEATING_ELEMENT4_KEY);
		if (property3 != null && property4 != null
				&& property3.getInt() == HAS_NO_HEATING_ELEMENT && property4.getInt() == HAS_NO_HEATING_ELEMENT) {
			controlSignalsToHide.add(CSIGNAL3);
		}
		return controlSignalsToHide;
	}
	
	@Override
	public void initialiseSimulation(Map<String, Object> parameters) throws PluginControllerException {
		super.initialiseSimulation(parameters);
		if (getProperty(HEATING_ELEMENT3_KEY).getInt() == HAS_NO_HEATING_ELEMENT) {
			mSGReadyMode3[2] = 0.0f; // Disable auxiliary heater for mode 3.
		} else {
			mSGReadyMode3[2] = 1.0f; // Enable auxiliary heater for mode 3.
		}
		if (getProperty(HEATING_ELEMENT4_KEY).getInt() == HAS_NO_HEATING_ELEMENT) {
			mSGReadyMode4[2] = 0.0f; // Disable auxiliary heater for mode 4.
		} else {
			mSGReadyMode4[2] = 1.0f; // Enable auxiliary heater for mode 4.
		}
		setTempThresholds(getProperty(TEMP_THRESHOLD3_KEY).getFloat(), getProperty(TEMP_THRESHOLD4_KEY).getFloat());
		setTempHysteresis(getProperty(TEMP_HYSTERESIS_KEY).getFloat());
	}
	
	@Override
	protected void initialiseConnection(String address, int port) throws PluginControllerException {
		// Default service type of CommLayerParams is client.
		CommLayerParams params = new CommLayerParams(address, port);
		params.addOutput(ForteDataType.BOOL); // Output: SG ready relay 1
		params.addOutput(ForteDataType.BOOL); // Output: SG ready relay 2
		makeIPSocket(params); // Create the socket and connect to FORTE.
	}

	@Override
	public int[] control(int simulationTime, boolean status, float[] sensors, float[] controlSignals, float[] logValues,
			boolean preRun, Map<String, Object> parameters) throws PluginControllerException {
		if (!status) {
			return null;
		}
		try { // Wait for input from FORTE
			getSocket().recvData();
		} catch (UnsupportedForteDataTypeException e) {
			e.printStackTrace();
			throw new PluginControllerException("Unsupported FORTE data type.", e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new PluginControllerException("Error receiving response from FORTE battery CSIFB.", e);
		}
		// Read control signals from buffer
		boolean[] sgReadySignals = new boolean[2];
		for (int i = 0; i < NUM_RELAYS; i++) {
			if (getSocket().isBool()) {
				sgReadySignals[i] = getSocket().getBool();
			} else {
				throw new PluginControllerException("The battery actor function block should send BOOL data as a " + CSIGNAL1 + " control signal.");
			}
		}
		// Allow OFF operation again if cool down time for disallowing OFF operation has been exceeded.
		if (isForcedOnOperation() && simulationTime - getOffOperationBeginTimeS() >= OFF_TIME_LIM_S + OFF_TIME_HYSTERESIS_S) {
			setForcedOnOperation(false);
			setOffOperationBeginTimeS(INIT_OFFOPERATIONBEGINTIME_S);
		}
		// Analyse control signals and implement automatic control of the SG Ready control modes.
		int sgReadyIntSignal = sgready2Int(sgReadySignals);
		switch (sgReadyIntSignal) {
		case OFF_OPERATION:
			sgReadyIntSignal = controlOffOperation(simulationTime);
			break;
		case AMPLIFIED_OPERATION:
			sgReadyIntSignal = controlAutoControlledOperation(getSensor(SENSOR1, sensors), AutoControlledControlMode.AMPLIFIED);
			break;
		case ONMAX_OPERATION:
			sgReadyIntSignal = controlAutoControlledOperation(getSensor(SENSOR1, sensors), AutoControlledControlMode.ONMAX);
			break;
		}
		// Pass the corresponding Polysun control signals.
		float[] polysunSignals = sgready2polysun(sgReadyIntSignal);
		controlSignals[getCSIdx(CSIGNAL1)] = polysunSignals[0];
		controlSignals[getCSIdx(CSIGNAL2)] = polysunSignals[1];
		controlSignals[getCSIdx(CSIGNAL3)] = polysunSignals[2];
		return null;
	}
	
	/**
	 * Simulates the heat pump control of the SG Ready operation mode 1. If the {@link #OFF_OPERATION} lasts for longer than 2 hours, {@link #NORMAL_OPERATION} is forced.
	 * @param simulationTime The simulation time in [s] beginning from the 1. January 00:00 (no leap year) taken from the {@link #control(int, boolean, float[], float[], float[], boolean, Map)} method 
	 * @return an integer representing the SG Ready control mode that is used to determine what to write to the Polysun control signals.
	 */
	private int controlOffOperation(int simulationTime) {
		if (getOffOperationBeginTimeS() == INIT_OFFOPERATIONBEGINTIME_S) {
			// Initialisation of OFF operation mode.
			setOffOperationBeginTimeS(simulationTime);
			setForcedOnOperation(false);
		}
		if (isForcedOnOperation() || simulationTime - getOffOperationBeginTimeS() > OFF_TIME_LIM_S) { // 2 h exceeded?
			setForcedOnOperation(true);
			return NORMAL_OPERATION;
		} else {
			return OFF_OPERATION;
		}
	}
	
	/**
	 * Simulates the heat pump control of the SG Ready operation modes 3 & 4.
	 * @param currentTemp the current temperature of the buffer storage tank in °C
	 * @param controlMode enumeration representing the mode that is being controlled
	 * @return an integer representing the SG Ready control mode that is used to determine what to write to the Polysun control signals.
	 */
	private int controlAutoControlledOperation(float currentTemp, AutoControlledControlMode controlMode) {
		float threshold = getTempThresholdControlMode(controlMode);
		int sgReadyIntSignal = controlMode.getSignal();
		if (currentTemp >= threshold || isForcedNormalOperation() && currentTemp >= threshold - getTempHysteresis()) {
			sgReadyIntSignal = NORMAL_OPERATION;
			setForcedNormalOperation(true);
		} else {
			setForcedNormalOperation(false);
		}
		return sgReadyIntSignal;
	}
	
	/**
	 * Converts an SG Ready signal to an equivalent set of Polysun control signals.
	 * @param sgReadySignals first and second SG Ready control signals
	 * @return the heat pump on/off, control mode signal and the auxiliary heater on/off signal used to implement the SG Ready setting.
	 */
	private float[] sgready2polysun(int sgReadySignal) {
		switch (sgReadySignal) {
		case 1:
			return mSGReadyMode1;
		case 2:
			return mSGReadyMode2;
		case 3:
			return mSGReadyMode3;
		default:
			return mSGReadyMode4;
		}
	}
	
	/**
	 * Converts a boolean SG Ready signal to an equivalent integer.
	 * @param sgReadySignals first and second SG Ready control signals
	 * @return An integer representing the control modes 1 to 4
	 */
	private int sgready2Int(boolean[] sgReadySignals) {
		if (sgReadySignals[0]) {
			if (sgReadySignals[1]) {
				return 4; // 11
			}
			return 1; // 10
		}
		if (sgReadySignals[1]) {
			return 3; // 01
		}
		return 2; // 00
	}
	
	/**
	 * Sets the temperature thresholds at which SG Ready control modes 3 and 4 switch to 2
	 * @param threshold3 threshold for control mode 3 in °C
	 * @param threshold4 threshold for control mode 4 in °C
	 */
	private void setTempThresholds(float threshold3, float threshold4) {
		mTempThreshold3 = threshold3;
		mTempThreshold4 = threshold4;
	}
	
	/**
	 * @param controlMode the SG Ready control mode (3 or 4)
	 * @return temperature threshold in °C at which the heat pump switches from SG Ready control mode 3 or 4 to 2.
	 */
	private float getTempThresholdControlMode(AutoControlledControlMode controlMode) {
		return AutoControlledControlMode.AMPLIFIED.equals(controlMode) ? mTempThreshold3 : mTempThreshold4;
	}
	
	/**
	 * Sets the flag indicating whether normal operation has been forced even though control mode 3 or 4 was set.
	 * @param tf <code>true</code> if normal operation has been forced due to the temperature reaching the threshold. <code>false</code> if the temperature falls below the hysteresis.
	 */
	private void setForcedNormalOperation(boolean tf) {
		mForcedNormalOperation = tf;
	}
	
	/**
	 * @return <code>true</code> if normal operation has been forced due to the temperature reaching the threshold, <code>false</code> otherwise.
	 */
	private boolean isForcedNormalOperation() {
		return mForcedNormalOperation;
	}
	
	/**
	 * Sets the flag indicating whether normal operation has been forced even though control mode 1 was set.
	 * @param <code>true</code> if normal operation has been forced, <code>false</code> otherwise.
	 */
	private void setForcedOnOperation(boolean tf) {
		mForcedOnOperation = tf;
	}
	
	/**
	 * @return <code>true</code> if normal operation has been forced even though control mode 1 was set, <code>false</code> otherwise.
	 */
	private boolean isForcedOnOperation() {
		return mForcedOnOperation;
	}
	
	/**
	 * @param t temperature hysteresis in K for automatic control modes. If the temperature falls below the threshold - the hysteresis, the normal control mode is no longer forced.
	 */
	private void setTempHysteresis(float t) {
		mTempHysteresis = t;
	}
	
	/**
	 * @return temperature hysteresis in K for automatic control modes. If the temperature falls below the threshold - the hysteresis, the normal control mode is no longer forced.
	 */
	private float getTempHysteresis() {
		return mTempHysteresis;
	}
	
	/**
	 * @param simulationTime simulation time at which the {@link #OFF_OPERATION} (SG Ready control mode 1) began (simulation time in s)
	 * @see {@link #control(int, boolean, float[], float[], float[], boolean, Map)
	 */
	private void setOffOperationBeginTimeS(int simulationTime) {
		mOffOperationBeginTimeS = simulationTime;
	}
	
	/**
	 * @return simulation time at which the {@link #OFF_OPERATION} (SG Ready control mode 1) began (simulation time in s)
	 * @see {@link #control(int, boolean, float[], float[], float[], boolean, Map)
	 */
	private int getOffOperationBeginTimeS() {
		return mOffOperationBeginTimeS;
	}
	
	/** Enum for the control modes that the heat pump controls automatically (3 & 4).*/
	private enum AutoControlledControlMode {
		/** SG Ready control mode 3. */
		AMPLIFIED(AMPLIFIED_OPERATION),
		/** SG Ready control mode 4. */
		ONMAX(ONMAX_OPERATION);
		
		/** Integer representing the SG Ready control mode */
		private final int mSGreadySignal;
		
		AutoControlledControlMode(int signal) {
			mSGreadySignal = signal;
		}
		
		/**
		 * @return an integer representing the SG Ready control mode.
		 */
		public int getSignal() {
			return mSGreadySignal;
		}
	}
} 

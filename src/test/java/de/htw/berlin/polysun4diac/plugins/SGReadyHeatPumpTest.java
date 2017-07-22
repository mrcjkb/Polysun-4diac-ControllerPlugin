package de.htw.berlin.polysun4diac.plugins;

import static de.htw.berlin.polysun4diac.CommonFunctionsAndConstants.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.velasolaris.plugin.controller.spi.AbstractPluginController;
import com.velasolaris.plugin.controller.spi.IPluginController;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration;
import com.velasolaris.plugin.controller.spi.PluginControllerException;
import com.velasolaris.plugin.controller.spi.PolysunSettings;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.ControlSignal;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Log;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Property;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Sensor;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.AbstractProperty.Type;
import com.velasolaris.plugin.controller.spi.PolysunSettings.PropertyValue;

import de.htw.berlin.polysun4diac.forte.comm.CommLayerParams;
import de.htw.berlin.polysun4diac.forte.comm.ForteServiceType;
import de.htw.berlin.polysun4diac.forte.datatypes.ForteDataType;

/**
 * JUnit tests for the SGReadyHeatPumpController.
 * 
 * @author Marc Jakobi
 *
 */
public class SGReadyHeatPumpTest {

	private static final String SENSOR1 = "Temperature of buffer storage.";
	private static final String CSIGNAL1 = "Heat pump ON/OFF";
	private static final String CSIGNAL2 = "Control mode";
	private static final String CSIGNAL3 = "Internal electric heating element ON/OFF";
	private static final String NAME = "SG Ready Heat Pump Adapter";
	/** Precision for assertions of double/float data */
	private static double PRECISION = 0.000001;
	/** Key for the host parameter. */
	private static final String HOST_KEY = "Host name";
	/** Key for the port parameter. */
	private static final String PORT_KEY = "Port number";
	/** Default temperature thresholds at which the heat pump switches from SG Ready modes 3/4 to 2. */
	private static final float[] DEF_TEMP_THRESHOLDS = new float[] {55, 70};
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
	/** Key for the option to wait for a response from FORTE or not */
	protected static final String WAITFORRSP_KEY = "Wait for response";
	/** Integer indicating not to wait for a response from FORTE */
	protected static final int DONTWAITFORRSP = 0;
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
	/** 
	 * Polysun control mode equivalent to the SG Ready control mode 1: OFF </p>
	 * Default Polysun setting for this mode: </p>
	 *  - heat pump: off (max. 2 hours) </p>
	 * 	- control mode: doesn't matter </p>
	 *  - auxiliary heater: off
	 */
	private float[] MODE1 = {0.0f, 1.0f, 0.0f};
	/** 
	 * Polysun control mode equivalent to the SG Ready control mode 2: NORMAL OPERATION </p>
	 * Default Polysun setting for this mode: </p>
	 *  - heat pump: on </p>
	 * 	- control mode: 1 (normal operation) </p>
	 *  - auxiliary heater: on
	 */
	private float[] MODE2 = {1.0f, 1.0f, 0.0f};
	/** 
	 * Polysun control mode equivalent to the SG Ready control mode 3: AMPLIFIED (with an auxiliary heater).
	 * This mode is enabled until the temperature in the buffer tank exceeds a certain temperature, then it switches to {@link #mSGReadyMode1}.</p>
	 * Default Polysun setting for this mode:</p>
	 *  - heat pump: on </p>
	 * 	- control mode: 0 (maximum power)</p>
	 *  - auxiliary heater: on
	 */
	private float[] MODE3_AUX = {1.0f, 0.0f, 1.0f};
	/** 
	 * Polysun control mode equivalent to the SG Ready control mode 3: AMPLIFIED (with an auxiliary heater).
	 * This mode is enabled until the temperature in the buffer tank exceeds a certain temperature, then it switches to {@link #mSGReadyMode1}.</p>
	 * Default Polysun setting for this mode:</p>
	 *  - heat pump: on </p>
	 * 	- control mode: 0 (maximum power)</p>
	 *  - auxiliary heater: off
	 */
	private float[] MODE3_NOAUX = {1.0f, 0.0f, 0.0f};
	/** 
	 * Polysun control mode equivalent to the SG Ready control mode 4: ONMAX (without an auxiliary heater). This mode runs all the time. </p>
	 * Default Polysun setting for this mode:</p>
	 *  - heat pump: on </p>
	 * 	- control mode: 0 (maximum power)</p>
	 *  - auxiliary heater: on
	 */
	private float[] MODE4_AUX = {1.0f, 0.0f, 1.0f};
	/** 
	 * Polysun control mode equivalent to the SG Ready control mode 4: ONMAX (without an auxiliary heater). This mode runs all the time. </p>
	 * Default Polysun setting for this mode:</p>
	 *  - heat pump: on </p>
	 * 	- control mode: 0 (maximum power)</p>
	 *  - auxiliary heater: off
	 */
	private float[] MODE4_NOAUX = {1.0f, 0.0f, 0.0f};

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	private IPluginController controller;
	private SGReadyEcho echo;
	private boolean wait;
	private boolean run = true;
	private boolean[] echoSensors = new boolean[NUM_RELAYS];

	/**
	 * Returns a PolysunSetting object as it would be returned by Polysun. This
	 * object must correspond to the configuration by
	 * {@link BatterySensorController#getConfiguration(Map)}.
	 */
	private PolysunSettings createPolysunSettings(String host, int port, int waitForRsp, float lowTempThreshold, int lowHeater, 
			float highTempThreshold, int highHeater, float tempHysteresis) {
		List<PropertyValue> properties = new ArrayList<>();
		properties.add(new PropertyValue(HOST_KEY, host));
		properties.add(new PropertyValue(PORT_KEY, port, ""));
		properties.add(new PropertyValue(WAITFORRSP_KEY, waitForRsp, ""));
		properties.add(new PropertyValue(TEMP_THRESHOLD3_KEY, lowTempThreshold, "°C"));
		properties.add(new PropertyValue(HEATING_ELEMENT3_KEY, lowHeater, ""));
		properties.add(new PropertyValue(TEMP_THRESHOLD4_KEY, highTempThreshold, "°C"));
		properties.add(new PropertyValue(HEATING_ELEMENT4_KEY, highHeater, ""));
		properties.add(new PropertyValue(TEMP_HYSTERESIS_KEY, tempHysteresis, "K"));

		List<Sensor> sensors = new ArrayList<>();
		sensors.add(new Sensor(SENSOR1, "°C", true, false, true));

		List<ControlSignal> controlSignals = new ArrayList<>();
		controlSignals.add(new ControlSignal(CSIGNAL1, "", false, true, true));
		controlSignals.add(new ControlSignal(CSIGNAL2, "", false, true, true));
		controlSignals.add(new ControlSignal(CSIGNAL3, "", false, false, true));

		List<Log> logs = new ArrayList<>();

		return new PolysunSettings(properties, sensors, controlSignals, logs);
	}

	/**
	 * Calls the default Polysun settings configuration (wit time stamp sending enabled)
	 */
	private PolysunSettings createPolysunSettingsDefaultConfiguration() {
		return createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, DEF_TEMP_THRESHOLDS[0], HAS_NO_HEATING_ELEMENT,
				DEF_TEMP_THRESHOLDS[1], HAS_HEATING_ELEMENT, DEF_TEMP_HYSTERESIS);
	}

	/**
	 * Creates PolysunSettingsObject from configuration.
	 */
	private PolysunSettings createPolysunSettingsFromConfiguration() throws PluginControllerException {
		PluginControllerConfiguration configuration = controller.getConfiguration(null);
		List<PropertyValue> properties = new ArrayList<>();
		for (Property property : configuration.getProperties()) {
			PropertyValue propertyValue = null;
			if (property.getType() == Type.FLOAT) {
				propertyValue = new PropertyValue(property.getName(), property.getDefaultFloat(),
						property.getUnit());
			} else if (property.getType() == Type.INTEGER) {
				propertyValue = new PropertyValue(property.getName(), property.getDefaultInt(),
						property.getUnit());
			} else if (property.getType() == Type.STRING) {
				propertyValue = new PropertyValue(property.getName(), property.getDefaultString());
			}
			properties.add(propertyValue);
		}

		List<Sensor> sensors = new ArrayList<>();
		for (Sensor sensor : configuration.getSensors()) {
			sensors.add(new Sensor(sensor.getName(), sensor.getUnit(), sensor.isAnalog(), sensor.isRequired(),
					SENSOR1.equals(sensor.getName())));
		}

		List<ControlSignal> controlSignals = new ArrayList<>();
		for (ControlSignal controlSignal : configuration.getControlSignals()) {
			controlSignals.add(new ControlSignal(controlSignal.getName(), controlSignal.getUnit(),
					controlSignal.isAnalog(), controlSignal.isRequired(),
					CSIGNAL1.equals(controlSignal.getName()) || CSIGNAL2.equals(controlSignal.getName()) || CSIGNAL3.equals(controlSignal.getName())));
		}

		List<Log> logs = new ArrayList<>();
		for (Log log : configuration.getLogs()) {
			logs.add(new Log(log.getName(), log.getUnit()));
		}

		return new PolysunSettings(properties, sensors, controlSignals, logs);
	}

	/**
	 * @param tf1 <code>true</code> to release wait in echo Thread
	 */
	synchronized void setWait(boolean tf) {
		wait = tf;
	}

	/**
	 * @param tf <code>true</code> if Thread should stop execution.
	 */
	synchronized void setRun(boolean tf) {
		run = tf;
	}
	
	/**
	 * @param tf1 <code>true</code> to release wait in echo Thread
	 * @param tf2 <code>true</code> if Thread should stop execution after the next one.
	 */
	synchronized void setWaitRun(boolean tf1, boolean tf2) {
		wait = tf1;
		run = tf2;
	}

	/**
	 * @return <code>true</code> if the Thread should wait.
	 */
	synchronized boolean getWait() {
		return wait;
	}

	/**
	 * @return <code>true</code> if the Thread should continue running.
	 */
	synchronized boolean getRun() {
		return run;
	}

	/** 
	 * Sets the SG Ready boolean inputs representing the control modes 1..4 
	 * The control modes and their corresponding settings are as follows:
	 * </p>
	 * <table border="1" align="center|center|center">
	 * <tr>
	 *   <th> Control mode </th> <th> Name </th> <th> Configuration </th>
	 * </tr>
	 * <tr>
	 *   <td> 1 </td> <td> OFF </td> <td> 1 0 </td>
	 * </tr>
	 * <tr>
	 *   <td> 2 </td> <td> NORMAL </td> <td> 0 0 </td>
	 * </tr>
	 * <tr>
	 *   <td> 3 </td> <td> AMPLIFIED </td> <td> 0 1 </td>
	 * </tr>
	 * <tr>
	 *   <td> 4 </td> <td> ONMAX/AMPLIFIED+ </td> <td> 1 1 </td>
	 * </tr>
	 * </table>
	 */
	synchronized void setEchoSensors(boolean relay1, boolean relay2) {
		echoSensors[0] = relay1;
		echoSensors[1] = relay2;
	}

	/**
	 * @return the SG Ready boolean inputs representing the control modes 1..4
	 */
	synchronized boolean[] getEchoSensors() {
		return echoSensors;
	}

	@Before
	public void setUp() throws Exception {
		controller = new SGReadyHeatPumpController();
		echo = new SGReadyEcho();
	}

	@After
	public void tearDown() {
		try {
			echo.disconnect();
		} catch (Exception E) {
			// ignore
		}
	}

	@Test
	public void testGetConfiguration() throws PluginControllerException {
		PluginControllerConfiguration configuration = controller.getConfiguration(null);
		assertEquals("Wrong number of configured properties", 8, configuration.getProperties().size());
		assertEquals("Wrong number of generic properties", 0, configuration.getNumGenericProperties());
		assertEquals("Wrong number of configured sensors", 1, configuration.getSensors().size());
		assertEquals("Wrong number of generic sensors", 0, configuration.getNumGenericSensors());
		assertEquals("Wrong number of configured controlSignals", 3, configuration.getControlSignals().size());
		assertEquals("Wrong number of generic controlSignals", 0, configuration.getNumGenericControlSignals());
		assertEquals("Wrong number of logs", 0, configuration.getLogs().size());
		assertEquals("Wrong controller image", getPluginIconResource(), configuration.getImagePath());

		assertEquals("PolysunSetting sensors different", createPolysunSettingsDefaultConfiguration().getSensors(),
				createPolysunSettingsFromConfiguration().getSensors());
		assertEquals("PolysunSetting control signals different",
				createPolysunSettingsDefaultConfiguration().getControlSignals(),
				createPolysunSettingsFromConfiguration().getControlSignals());
		assertEquals("PolysunSetting property values different",
				createPolysunSettingsDefaultConfiguration().getPropertyValues(),
				createPolysunSettingsFromConfiguration().getPropertyValues());
		assertEquals("PolysunSetting configuration different", createPolysunSettingsDefaultConfiguration(),
				createPolysunSettingsFromConfiguration());
	}

	@Test
	public void testGetControlSignalsToHide() {
		int stateLow = HAS_HEATING_ELEMENT;
		int stateHigh = HAS_HEATING_ELEMENT;
		List<String> controlSignalsToHide = controller
				.getControlSignalsToHide(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, DEF_TEMP_THRESHOLDS[0], stateLow,
						DEF_TEMP_THRESHOLDS[1], stateHigh, DEF_TEMP_HYSTERESIS), null);
		assertEquals("Wrong number of control signals to hide", 0, controlSignalsToHide.size());
		stateLow = HAS_HEATING_ELEMENT;
		stateHigh = HAS_NO_HEATING_ELEMENT;
		controlSignalsToHide = controller
				.getControlSignalsToHide(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, DEF_TEMP_THRESHOLDS[0], stateLow,
						DEF_TEMP_THRESHOLDS[1], stateHigh, DEF_TEMP_HYSTERESIS), null);
		assertEquals("Wrong number of control signals to hide", 0, controlSignalsToHide.size());
		stateLow = HAS_NO_HEATING_ELEMENT;
		stateHigh = HAS_HEATING_ELEMENT;
		controlSignalsToHide = controller
				.getControlSignalsToHide(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, DEF_TEMP_THRESHOLDS[0], stateLow,
						DEF_TEMP_THRESHOLDS[1], stateHigh, DEF_TEMP_HYSTERESIS), null);
		assertEquals("Wrong number of control signals to hide", 0, controlSignalsToHide.size());
		stateLow = HAS_NO_HEATING_ELEMENT;
		stateHigh = HAS_NO_HEATING_ELEMENT;
		controlSignalsToHide = controller
				.getControlSignalsToHide(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, DEF_TEMP_THRESHOLDS[0], stateLow,
						DEF_TEMP_THRESHOLDS[1], stateHigh, DEF_TEMP_HYSTERESIS), null);
		assertEquals("Wrong number of control signals to hide", 1, controlSignalsToHide.size());
	}

	@Test
	public void testGetName() {
		assertEquals(NAME, controller.getName());
	}

	@Test
	public void testGetPropertiesToHide() {
		int stateLow = HAS_HEATING_ELEMENT;
		int stateHigh = HAS_HEATING_ELEMENT;
		List<String> propertiesToHide = controller
				.getPropertiesToHide(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, DEF_TEMP_THRESHOLDS[0], stateLow,
						DEF_TEMP_THRESHOLDS[1], stateHigh, DEF_TEMP_HYSTERESIS), null);
		assertEquals("Wrong number of propreties to hide", 0, propertiesToHide.size());
		stateLow = HAS_HEATING_ELEMENT;
		stateHigh = HAS_NO_HEATING_ELEMENT;
		propertiesToHide = controller
				.getPropertiesToHide(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, DEF_TEMP_THRESHOLDS[0], stateLow,
						DEF_TEMP_THRESHOLDS[1], stateHigh, DEF_TEMP_HYSTERESIS), null);
		assertEquals("Wrong number of propreties to hide", 0, propertiesToHide.size());
		stateLow = HAS_NO_HEATING_ELEMENT;
		stateHigh = HAS_HEATING_ELEMENT;
		propertiesToHide = controller
				.getPropertiesToHide(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, DEF_TEMP_THRESHOLDS[0], stateLow,
						DEF_TEMP_THRESHOLDS[1], stateHigh, DEF_TEMP_HYSTERESIS), null);
		assertEquals("Wrong number of propreties to hide", 0, propertiesToHide.size());
		stateLow = HAS_NO_HEATING_ELEMENT;
		stateHigh = HAS_NO_HEATING_ELEMENT;
		propertiesToHide = controller
				.getPropertiesToHide(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, DEF_TEMP_THRESHOLDS[0], stateLow,
						DEF_TEMP_THRESHOLDS[1], stateHigh, DEF_TEMP_HYSTERESIS), null);
		assertEquals("Wrong number of propreties to hide", 0, propertiesToHide.size());
	}

	@Test
	public void testGetSensorsToHide() {
		int stateLow = HAS_HEATING_ELEMENT;
		int stateHigh = HAS_HEATING_ELEMENT;
		List<String> sensorsToHide = controller
				.getSensorsToHide(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, DEF_TEMP_THRESHOLDS[0], stateLow,
						DEF_TEMP_THRESHOLDS[1], stateHigh, DEF_TEMP_HYSTERESIS), null);
		assertEquals("Wrong number of sensors to hide", 0, sensorsToHide.size());
		stateLow = HAS_HEATING_ELEMENT;
		stateHigh = 1;
		sensorsToHide = controller
				.getSensorsToHide(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, DEF_TEMP_THRESHOLDS[0], stateLow,
						DEF_TEMP_THRESHOLDS[1], stateHigh, DEF_TEMP_HYSTERESIS), null);
		assertEquals("Wrong number of sensors to hide", 0, sensorsToHide.size());
		stateLow = 1;
		stateHigh = HAS_HEATING_ELEMENT;
		sensorsToHide = controller
				.getSensorsToHide(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, DEF_TEMP_THRESHOLDS[0], stateLow,
						DEF_TEMP_THRESHOLDS[1], stateHigh, DEF_TEMP_HYSTERESIS), null);
		assertEquals("Wrong number of sensors to hide", 0, sensorsToHide.size());
		stateLow = HAS_NO_HEATING_ELEMENT;
		stateHigh = HAS_NO_HEATING_ELEMENT;
		sensorsToHide = controller
				.getSensorsToHide(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, DEF_TEMP_THRESHOLDS[0], stateLow,
						DEF_TEMP_THRESHOLDS[1], stateHigh, DEF_TEMP_HYSTERESIS), null);
		assertEquals("Wrong number of sensors to hide", 0, sensorsToHide.size());
	}

	@Test
	public void testPolysunSettingsGetter() throws PluginControllerException {
		controller.build(createPolysunSettingsDefaultConfiguration(), null);
		assertEquals("Wrong property name returned", HOST_KEY,
				((AbstractPluginController) controller).getProperty(HOST_KEY).getName());
		assertEquals("Wrong sensor name returned", CSIGNAL1,
				((AbstractPluginController) controller).getControlSignal(CSIGNAL1).getName());

		assertEquals("Inexistent property failed", null, ((AbstractPluginController) controller).getProperty("XXX"));
		assertEquals("Inexistent property failed", null, ((AbstractPluginController) controller).getSensor("XXX"));
		assertEquals("Inexistent property failed", null,
				((AbstractPluginController) controller).getControlSignal("XXX"));

		assertEquals("Wrong control signal index returned", 0,
				((AbstractPluginController) controller).getControlSignalIndex(CSIGNAL1));

		assertEquals("Wrong sensor index returned", -1, ((AbstractPluginController) controller).getSensorIndex("XXX"));
		assertEquals("Wrong control signal index returned", -1,
				((AbstractPluginController) controller).getControlSignalIndex("XXX"));
	}

	@Test
	public void testNormalControlMode() throws Exception {
		float[] sensors = new float[] {0};
		float[] controlSignals = new float[3];
		float[] logValues = new float[3];
		int simulationTime = 0;
		// Test NORMAL control mode
		setEchoSensors(false, false);
		controller.build(createPolysunSettingsDefaultConfiguration(), null);
		echo.start();
		Thread.sleep(THREAD_SLEEP_TIME); // Give echo time to open connection
		controller.initialiseSimulation(null);
		setWait(false);
		controller.control(simulationTime,  true,  sensors,  controlSignals,  logValues, false, null);
		for (int i = 0; i < controlSignals.length; i++) {
			assertEquals("Wrong control signal " + Integer.toString(i), MODE2[i], controlSignals[i], PRECISION);
		}
		controller.terminateSimulation(null);
		echo.disconnect();
	}

	@Test
	public void testOffControlMode() throws Exception {
		setWait(false);
		float[] sensors = new float[] {0};
		float[] controlSignals = new float[3];
		float[] logValues = new float[3];
		int simulationTime = 0;
		// Test OFF control mode.
		setEchoSensors(true, false); // OFF mode (1)
		controller.build(createPolysunSettingsDefaultConfiguration(), null);
		echo.start();
		Thread.sleep(THREAD_SLEEP_TIME); // Give echo time to open connection
		controller.initialiseSimulation(null);
		setWait(false);
		controller.control(simulationTime,  true,  sensors,  controlSignals,  logValues, false, null);
		for (int i = 0; i < controlSignals.length; i++) {
			assertEquals("Wrong control signal 1: " + Integer.toString(i), MODE1[i], controlSignals[i], PRECISION);
		}
		// Test 2 hour time limitation of OFF operation
		simulationTime += OFF_TIME_LIM_S + 1;
		setWait(false); // Unlock echo Thread
		controller.control(simulationTime,  true,  sensors,  controlSignals,  logValues, false, null);
		for (int i = 0; i < controlSignals.length; i++) {
			assertEquals("Forcing NORMAL operation after 2 hours of OFF operation failed.", MODE2[i], controlSignals[i], PRECISION);
		}
		// Switch to different control mode
		simulationTime += 1;
		setEchoSensors(false, false); // NORMAL mode (2)
		setWait(false);
		controller.control(simulationTime,  true,  sensors,  controlSignals,  logValues, false, null);
		for (int i = 0; i < controlSignals.length; i++) {
			assertEquals("Wrong control signal 2: " + Integer.toString(i), MODE2[i], controlSignals[i], PRECISION);
		}
		// Test switching back to OFF mode within cool down period
		simulationTime += 1;
		setEchoSensors(true, false); // OFF mode (1)
		setWait(false);
		controller.control(simulationTime,  true,  sensors,  controlSignals,  logValues, false, null);
		for (int i = 0; i < controlSignals.length; i++) {
			assertEquals("Maintaining NORMAL operation after switching"
					+ " back to OFF from other control mode within cool donwn period failed: "
					+ Integer.toString(i), controlSignals[i], MODE2[i], PRECISION);
		}
		// Test time limitation cool down
		simulationTime += OFF_TIME_HYSTERESIS_S;
		setWaitRun(false, false);
		controller.control(simulationTime,  true,  sensors,  controlSignals,  logValues, false, null);
		for (int i = 0; i < controlSignals.length; i++) {
			assertEquals("Allowing OFF operation "
					+ "after cool down period failed: " + Integer.toString(i), MODE1[i], controlSignals[i], PRECISION);
		}
		echo.join();
		controller.terminateSimulation(null);
		echo.disconnect();
	}
	
	@Test
	public void testDefaultAmplifiedControlMode() throws Exception {
		setWait(false);
		float[] sensors = new float[] {0};
		float[] controlSignals = new float[3];
		float[] logValues = new float[3];
		int simulationTime = 0;
		// Test AMPLIFIED control mode.
		setEchoSensors(false, true);
		controller.build(createPolysunSettingsDefaultConfiguration(), null);
		echo.start();
		Thread.sleep(THREAD_SLEEP_TIME); // Give echo time to open connection
		controller.initialiseSimulation(null);
		setWait(false);
		controller.control(simulationTime,  true,  sensors,  controlSignals,  logValues, false, null);
		for (int i = 0; i < controlSignals.length; i++) {
			assertEquals("Wrong control signal 1: " + Integer.toString(i), MODE3_NOAUX[i], controlSignals[i], PRECISION);
		}
		// Test behaviour when temperature exceeds threshold
		sensors[0] = DEF_TEMP_THRESHOLDS[0] + 1;
		setWait(false);
		controller.control(simulationTime,  true,  sensors,  controlSignals,  logValues, false, null);
		for (int i = 0; i < controlSignals.length; i++) {
			assertEquals("Forcing NORMAL operation when "
					+ "temperature exceeds threshold failed.", MODE2[i], controlSignals[i], PRECISION);
		}
		// Test behaviour when temperature is below threshold, but above hysteresis
		sensors[0] = DEF_TEMP_THRESHOLDS[0] - DEF_TEMP_HYSTERESIS / 2;
		setWait(false);
		controller.control(simulationTime,  true,  sensors,  controlSignals,  logValues, false, null);
		for (int i = 0; i < controlSignals.length; i++) {
			assertEquals("Maintaining NORMAL operation when "
					+ "temperature falls back below threshold "
					+ "but not below hysteresis failed.", MODE2[i], controlSignals[i], PRECISION);
		}
		// Switch to different control mode
		setEchoSensors(false, false); // NORMAL mode (2)
		setWait(false);
		controller.control(simulationTime,  true,  sensors,  controlSignals,  logValues, false, null);
		for (int i = 0; i < controlSignals.length; i++) {
			assertEquals("Wrong control signal 2: " + Integer.toString(i), MODE2[i], controlSignals[i], PRECISION);
		}
		// Test switching back to OFF mode within cool down period
		setEchoSensors(false, true); // AMPLIFIED mode (1)
		setWait(false);
		controller.control(simulationTime,  true,  sensors,  controlSignals,  logValues, false, null);
		for (int i = 0; i < controlSignals.length; i++) {
			assertEquals("Maintaining NORMAL operation after switching "
					+ "back to OFF from other control mode "
					+ "while still within hysteresis zone failed: "
					+ Integer.toString(i), MODE2[i], controlSignals[i], PRECISION);
		}
		// Test allowing AMPLIFIED mode when temperature falls back below hysteresis
		sensors[0] = DEF_TEMP_THRESHOLDS[0] - DEF_TEMP_HYSTERESIS - 1;
		setWaitRun(false, false);
		controller.control(simulationTime,  true,  sensors,  controlSignals,  logValues, false, null);
		for (int i = 0; i < controlSignals.length; i++) {
			assertEquals("Allowing AMPLIFIED mode when temperature "
					+ "falls back below hysteresis failed: "
					+ Integer.toString(i), MODE3_NOAUX[i], controlSignals[i], PRECISION);
		}
	}
	
	@Test
	public void testDefaultOnMaxControlMode() throws Exception {
		setWait(false);
		float[] sensors = new float[] {0};
		float[] controlSignals = new float[3];
		float[] logValues = new float[3];
		int simulationTime = 0;
		// Test ONMAX control mode.
		setEchoSensors(true, true);
		controller.build(createPolysunSettingsDefaultConfiguration(), null);
		echo.start();
		Thread.sleep(THREAD_SLEEP_TIME); // Give echo time to open connection
		controller.initialiseSimulation(null);
		setWait(false);
		controller.control(simulationTime,  true,  sensors,  controlSignals,  logValues, false, null);
		for (int i = 0; i < controlSignals.length; i++) {
			assertEquals("Wrong control signal 1: " + Integer.toString(i), MODE4_AUX[i], controlSignals[i], PRECISION);
		}
		// Test behaviour when temperature exceeds threshold
		sensors[0] = DEF_TEMP_THRESHOLDS[1] + 1;
		setWait(false);
		controller.control(simulationTime,  true,  sensors,  controlSignals,  logValues, false, null);
		for (int i = 0; i < controlSignals.length; i++) {
			assertEquals("Forcing NORMAL operation when "
					+ "temperature exceeds threshold failed.", MODE2[i], controlSignals[i], PRECISION);
		}
		// Test behaviour when temperature is below threshold, but above hysteresis
		sensors[0] = DEF_TEMP_THRESHOLDS[1] - DEF_TEMP_HYSTERESIS / 2;
		setWait(false);
		controller.control(simulationTime,  true,  sensors,  controlSignals,  logValues, false, null);
		for (int i = 0; i < controlSignals.length; i++) {
			assertEquals("Maintaining NORMAL operation when "
					+ "temperature falls back below threshold "
					+ "but not below hysteresis failed.", MODE2[i], controlSignals[i], PRECISION);
		}
		// Switch to different control mode
		setEchoSensors(false, false); // NORMAL mode (2)
		setWait(false);
		controller.control(simulationTime,  true,  sensors,  controlSignals,  logValues, false, null);
		for (int i = 0; i < controlSignals.length; i++) {
			assertEquals("Wrong control signal 2: " + Integer.toString(i), MODE2[i], controlSignals[i], PRECISION);
		}
		// Test switching back to OFF mode within cool down period
		setEchoSensors(true, true); // ONMAX mode (1)
		setWait(false);
		controller.control(simulationTime,  true,  sensors,  controlSignals,  logValues, false, null);
		for (int i = 0; i < controlSignals.length; i++) {
			assertEquals("Maintaining NORMAL operation after switching "
					+ "back to OFF from other control mode "
					+ "while still within hysteresis zone failed: "
					+ Integer.toString(i), MODE2[i], controlSignals[i], PRECISION);
		}
		// Test allowing ONMAX mode when temperature falls back below hysteresis
		sensors[0] = DEF_TEMP_THRESHOLDS[1] - DEF_TEMP_HYSTERESIS - 1;
		setWaitRun(false, false);
		controller.control(simulationTime,  true,  sensors,  controlSignals,  logValues, false, null);
		for (int i = 0; i < controlSignals.length; i++) {
			assertEquals("Allowing ONMAX/AMPLIFIED+ mode when temperature "
					+ "falls back below hysteresis failed: "
					+ Integer.toString(i), MODE4_AUX[i], controlSignals[i], PRECISION);
		}
	}
	
	@Test
	public void testAmplifiedWithAuxiliaryHeaterControlMode() throws Exception {
		setWait(false);
		float[] sensors = new float[] {0};
		float[] controlSignals = new float[3];
		float[] logValues = new float[3];
		int simulationTime = 0;
		// Test AMPLIFIED control mode.
		setEchoSensors(false, true);
		controller.build(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, DEF_TEMP_THRESHOLDS[0], HAS_HEATING_ELEMENT,
				DEF_TEMP_THRESHOLDS[1], HAS_HEATING_ELEMENT, DEF_TEMP_HYSTERESIS), null);
		echo.start();
		Thread.sleep(THREAD_SLEEP_TIME); // Give echo time to open connection
		controller.initialiseSimulation(null);
		setWait(false);
		controller.control(simulationTime,  true,  sensors,  controlSignals,  logValues, false, null);
		for (int i = 0; i < controlSignals.length; i++) {
			assertEquals("Wrong control signal 1: " + Integer.toString(i), MODE3_AUX[i], controlSignals[i], PRECISION);
		}
	}
	
	@Test
	public void testOnMaxWithoutAuxiliaryHeaterControlMode() throws Exception {
		setWait(false);
		float[] sensors = new float[] {0};
		float[] controlSignals = new float[3];
		float[] logValues = new float[3];
		int simulationTime = 0;
		// Test ONMAX control mode.
		setEchoSensors(false, true);
		controller.build(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, DEF_TEMP_THRESHOLDS[0], HAS_NO_HEATING_ELEMENT,
				DEF_TEMP_THRESHOLDS[1], HAS_NO_HEATING_ELEMENT, DEF_TEMP_HYSTERESIS), null);
		echo.start();
		Thread.sleep(200); // Give echo time to open connection
		controller.initialiseSimulation(null);
		setWait(false);
		controller.control(simulationTime,  true,  sensors,  controlSignals,  logValues, false, null);
		for (int i = 0; i < controlSignals.length; i++) {
			assertEquals("Wrong control signal 1: " + Integer.toString(i), MODE4_NOAUX[i], controlSignals[i], PRECISION);
		}
	}

	/**
	 * Wrapper for an IForteSocket. Runs on a second thread
	 * and sends data to the SGReadyHeatPumpController
	 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
	 */
	public class SGReadyEcho extends IForteSocketEcho {

		public SGReadyEcho() {
			mParams = new CommLayerParams(DEF_TCP_ADDRESS, DEF_PORT_NUMBER);
			mParams.setServiceType(ForteServiceType.SERVER);
			for (int i = 0; i < echoSensors.length; i++) {
				mParams.addInput(ForteDataType.BOOL);
			}
		}

		@Override
		public void run() {
			try {
				if (!isConnected()) {
					mSocket = mParams.makeIPSocket();
					setConnected(true);
				}
				while (getRun()) {
					while (getWait()) {} // Wait for unlock before sending data
					for (int i = 0; i < getEchoSensors().length; i++) {
						mSocket.put(getEchoSensors()[i]);
					}
					mSocket.sendData();
					setWait(true);
				}
			} catch (IOException e) {
				e.printStackTrace();
				disconnect();
				fail("IOException");
			}
		}
	}
}

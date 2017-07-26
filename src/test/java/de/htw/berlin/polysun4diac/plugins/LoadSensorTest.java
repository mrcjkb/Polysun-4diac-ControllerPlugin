package de.htw.berlin.polysun4diac.plugins;

import static de.htw.berlin.polysun4diac.CommonFunctionsAndConstants.*;
import static de.htw.berlin.polysun4diac.forte.datatypes.DateAndTime.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.velasolaris.plugin.controller.spi.AbstractPluginController;
import com.velasolaris.plugin.controller.spi.IPluginController;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.AbstractProperty.Type;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.ControlSignal;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Log;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Property;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Sensor;
import com.velasolaris.plugin.controller.spi.PluginControllerException;
import com.velasolaris.plugin.controller.spi.PolysunSettings;
import com.velasolaris.plugin.controller.spi.PolysunSettings.PropertyValue;

import de.htw.berlin.polysun4diac.forte.datatypes.DateAndTime;

/**
 * JUnit tests for the BatterySensorController.
 * 
 * @author Marc Jakobi
 *
 */
public class LoadSensorTest {

	/** Key for the host parameter. */
	private static final String HOST_KEY = "Host name";
	/** Key for the port parameter. */
	private static final String PORT_KEY = "Port number";
	/** Key for the time stamp option. */
	private static final String TIMESTAMPSETTING_KEY = "Send time stamp";
	/** Key for the simulation start time specification. */
	private static final String SIMULATIONSTART_KEY = "Beginning of simulation";
	/** Key for the option to wait for a response from FORTE or not */
	private static final String WAITFORRSP_KEY = "Wait for response";
	/** Precision for assertions of double/float data */
	private static double PRECISION = 0.000001;
	private static final String SENSOR1 = "Electricity consumption";
	private static final String NAME = "Load Sensor";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	private IPluginController controller;
	private IForteSocketEcho echo;

	/**
	 * Returns a PolysunSetting object as it would be returned by Polysun. This
	 * object must correspond to the configuration by
	 * {@link BatterySensorController#getConfiguration(Map)}.
	 */
	private PolysunSettings createPolysunSettings(String host, int port,
			int timestampSetting, int waitForRsp, boolean measuredLoad, boolean sendTimestamp) {
		List<PropertyValue> properties = new ArrayList<>();
		// Host name and port number
		properties.add(new PropertyValue(HOST_KEY, host));
		properties.add(new PropertyValue(PORT_KEY, port, "")); // Port number
		if (sendTimestamp) {
			properties.add(new PropertyValue(TIMESTAMPSETTING_KEY, timestampSetting, ""));
			int thisYear = Calendar.getInstance().get(Calendar.YEAR);
			DateAndTime dt = new DateAndTime(thisYear, REFMONTH, REFDAY, DEFHOUR, REFMIN, REFSEC, REFMS);
			dt.setSimulationTimeS(0);
			String defaultStart = dt.toString();
			properties.add(new PropertyValue(SIMULATIONSTART_KEY, defaultStart));
		}
		properties.add(new PropertyValue(WAITFORRSP_KEY, waitForRsp, ""));
		
		List<Sensor> sensors = new ArrayList<>();
		sensors.add(new Sensor(SENSOR1, "W", true, true, measuredLoad)); // Sensor 1
		
		List<ControlSignal> controlSignals = new ArrayList<>();
		List<Log> logs = new ArrayList<>();

		return new PolysunSettings(properties, sensors, controlSignals, logs);
	}

	/**
	 * Calls the default Polysun settings configuration (wit time stamp sending enabled)
	 */
	private PolysunSettings createPolysunSettingsDefaultConfiguration() {
		return createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, 0, 0, true, true);
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
					controlSignal.isAnalog(), controlSignal.isRequired()));
		}
		
		List<Log> logs = new ArrayList<>();
		for (Log log : configuration.getLogs()) {
			logs.add(new Log(log.getName(), log.getUnit()));
		}
		
		return new PolysunSettings(properties, sensors, controlSignals, logs);
	}

	@Before
	public void setUp() throws Exception {
		controller = new LoadSensorController();
		echo = new EchoActor(1);
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
		assertEquals("Wrong number of configured properties", 5, configuration.getProperties().size());
		assertEquals("Wrong number of generic properties", 0, configuration.getNumGenericProperties());
		assertEquals("Wrong number of configured sensors", 1, configuration.getSensors().size());
		assertEquals("Wrong number of generic sensors", 0, configuration.getNumGenericSensors());
		assertEquals("Wrong number of configured controlSignals", 0, configuration.getControlSignals().size());
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
		int state = 1;
		List<String> controlSignalsToHide = controller.getControlSignalsToHide(
				createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, state, 0, true, true), null);
		assertEquals("No signals to hide expected", new ArrayList<String>(), controlSignalsToHide);
		state = 0;
		controlSignalsToHide = controller.getControlSignalsToHide(
				createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, state, 0, true, true), null);
		assertEquals("No signals to hide expected", new ArrayList<String>(), controlSignalsToHide);
	}

	@Test
	public void testGetName() {
		assertEquals(NAME, controller.getName());
	}

	@Test
	public void testGetPropertiesToHide() {
		int state = 1;
		List<String> propertiesToHide = controller
				.getPropertiesToHide(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, state, 0, true, true), null);
		assertEquals("Wrong number of propreties to hide", 0, propertiesToHide.size());
		state = 0;
		propertiesToHide = controller
				.getPropertiesToHide(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, state, 0, true, true), null);
		assertEquals("Wrong number of propreties to hide", 1, propertiesToHide.size());
		assertEquals("Beginning of simulation", propertiesToHide.get(0));
	}

	@Test
	public void testGetSensorsToHide() {
		int state = 1;
		List<String> sensorsToHide = controller
				.getSensorsToHide(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, state, 0, true, true), null);
		assertEquals("Wrong number of sensors to hide", 0, sensorsToHide.size());
		state = 0;
		sensorsToHide = controller
				.getSensorsToHide(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, state, 0, true, true), null);
		assertEquals("No sensors to hide expected", 0, sensorsToHide.size());
	}

	@Test
	public void testPolysunSettingsGetter() throws PluginControllerException {
		controller.build(createPolysunSettingsDefaultConfiguration(), null);
		assertEquals("Wrong property name returned", HOST_KEY,
				((AbstractPluginController) controller).getProperty(HOST_KEY).getName());
		assertEquals("Wrong sensor name returned", SENSOR1,
				((AbstractPluginController) controller).getSensor(SENSOR1).getName());

		assertEquals("Inexistent property failed", null, ((AbstractPluginController) controller).getProperty("XXX"));
		assertEquals("Inexistent property failed", null, ((AbstractPluginController) controller).getSensor("XXX"));
		assertEquals("Inexistent property failed", null,
				((AbstractPluginController) controller).getControlSignal("XXX"));

		assertEquals("Wrong sensor index returned", 0,
				((AbstractPluginController) controller).getSensorIndex(SENSOR1));

		assertEquals("Wrong sensor index returned", -1, ((AbstractPluginController) controller).getSensorIndex("XXX"));
		assertEquals("Wrong control signal index returned", -1,
				((AbstractPluginController) controller).getControlSignalIndex("XXX"));
	}
	
	@Test
	public void testControl() throws Exception {
		float[] sensors = new float[] {5};
		float[] controlSignals = new float[0];
		float[] logValues = new float[3];
		int simulationTime = 0;
		controller.build(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, 0, 1, true, true), null);
		echo.start();
		Thread.sleep(THREAD_SLEEP_TIME); // Give echo time to open connection
		controller.initialiseSimulation(null);
		controller.control(simulationTime, true, sensors, controlSignals, logValues, false, null);
		echo.join();
		assertEquals("Wrong value sent", sensors[0], echo.getReceivedData()[0], PRECISION);
		controller.terminateSimulation(null);
	}
}
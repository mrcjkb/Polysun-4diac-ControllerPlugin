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
 * JUnit tests for the PVActorController.
 * 
 * @author Marc Jakobi
 *
 */
public class PVActorTest {

	/** Key for the host parameter. */
	private static final String HOST_KEY = "Host name";
	/** Key for the port parameter. */
	private static final String PORT_KEY = "Port number";
	/** Key for the option to wait for a response from FORTE or not */
	protected static final String WAITFORRSP_KEY = "Wait for response";
	/** Integer indicating not to wait for a response from FORTE */
	protected static final int DONTWAITFORRSP = 0;
	/** Precision for assertions of double/float data */
	private static double PRECISION = 0.000001;
	private static final String CSIGNAL1 = "Derating factor";
	private static final String NAME = "Photovoltaics Actor";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	private IPluginController controller;
	private IForteSocketEcho echo;
	private float[] echoSensors;

	/**
	 * Returns a PolysunSetting object as it would be returned by Polysun. This
	 * object must correspond to the configuration by
	 * {@link BatterySensorController#getConfiguration(Map)}.
	 */
	private PolysunSettings createPolysunSettings(String host, int port, int waitForRsp, int timestampSetting, boolean controlDeratingFactor) {
		List<PropertyValue> properties = new ArrayList<>();
		properties.add(new PropertyValue(HOST_KEY, host));
		properties.add(new PropertyValue(PORT_KEY, port, ""));
		properties.add(new PropertyValue(WAITFORRSP_KEY, waitForRsp, ""));
		
		List<Sensor> sensors = new ArrayList<>();
		
		List<ControlSignal> controlSignals = new ArrayList<>();
		controlSignals.add(new ControlSignal(CSIGNAL1, "", true, true, controlDeratingFactor));
		
		List<Log> logs = new ArrayList<>();

		return new PolysunSettings(properties, sensors, controlSignals, logs);
	}

	/**
	 * Calls the default Polysun settings configuration (wit time stamp sending enabled)
	 */
	private PolysunSettings createPolysunSettingsDefaultConfiguration() {
		return createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, 0, true);
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
			sensors.add(new Sensor(sensor.getName(), sensor.getUnit(), sensor.isAnalog(), sensor.isRequired()));
		}
		
		List<ControlSignal> controlSignals = new ArrayList<>();
		for (ControlSignal controlSignal : configuration.getControlSignals()) {
			controlSignals.add(new ControlSignal(controlSignal.getName(), controlSignal.getUnit(),
					controlSignal.isAnalog(), controlSignal.isRequired(),
					CSIGNAL1.equals(controlSignal.getName())));
		}
		
		List<Log> logs = new ArrayList<>();
		for (Log log : configuration.getLogs()) {
			logs.add(new Log(log.getName(), log.getUnit()));
		}
		
		return new PolysunSettings(properties, sensors, controlSignals, logs);
	}

	@Before
	public void setUp() throws Exception {
		controller = new PVActorController();
		echoSensors = new float[] {5};
		echo = new EchoSensor(echoSensors);
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
		assertEquals("Wrong number of configured properties", 3, configuration.getProperties().size());
		assertEquals("Wrong number of generic properties", 0, configuration.getNumGenericProperties());
		assertEquals("Wrong number of configured sensors", 0, configuration.getSensors().size());
		assertEquals("Wrong number of generic sensors", 0, configuration.getNumGenericSensors());
		assertEquals("Wrong number of configured controlSignals", 1, configuration.getControlSignals().size());
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
				createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, state, true), null);
		assertEquals("No signals to hide expected", new ArrayList<String>(), controlSignalsToHide);
		state = 0;
		controlSignalsToHide = controller.getControlSignalsToHide(
				createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, state, true), null);
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
				.getPropertiesToHide(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, state, true), null);
		assertEquals("Wrong number of propreties to hide", 1, propertiesToHide.size());
		state = 0;
		propertiesToHide = controller
				.getPropertiesToHide(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, state, true), null);
		assertEquals("Wrong number of propreties to hide", 1, propertiesToHide.size());
	}

	@Test
	public void testGetSensorsToHide() {
		int state = 1;
		List<String> sensorsToHide = controller
				.getSensorsToHide(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, state, true), null);
		assertEquals("Wrong number of sensors to hide", 0, sensorsToHide.size());
		state = 0;
		sensorsToHide = controller
				.getSensorsToHide(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, state, true), null);
		assertEquals("No sensors to hide expected", 0, sensorsToHide.size());
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
	public void testControl() throws Exception {
		float[] sensors = new float[0];
		float[] controlSignals = new float[1];
		float[] logValues = new float[3];
		int simulationTime = 0;
		controller.build(createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP, 0, true), null);
		echo.start();
		Thread.sleep(THREAD_SLEEP_TIME); // Give echo time to open connection
		controller.initialiseSimulation(null);
		controller.control(simulationTime, true, sensors, controlSignals, logValues, false, null);
		echo.join();
		assertEquals("Wrong value received", echoSensors[0], controlSignals[0], PRECISION);
		controller.terminateSimulation(null);
	}
	
	public class BatteryEchoSensor extends IForteSocketEcho {
		
		public BatteryEchoSensor() {
			mParams = new CommLayerParams(DEF_TCP_ADDRESS, DEF_PORT_NUMBER);
			mParams.setServiceType(ForteServiceType.SERVER);
			mParams.addInput(ForteDataType.LREAL);
			mParams.addInput(ForteDataType.BOOL);
		}

		@Override
		public void run() {
			try {
				mSocket = mParams.makeIPSocket();
				mSocket.put((double) 5);
				mSocket.put(true);
				mSocket.sendData();
			} catch (IOException e) {
				e.printStackTrace();
				disconnect();
				fail("IOException");
			}
		}
	}

}

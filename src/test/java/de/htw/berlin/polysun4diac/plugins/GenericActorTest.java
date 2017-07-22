package de.htw.berlin.polysun4diac.plugins;

import static de.htw.berlin.polysun4diac.CommonFunctionsAndConstants.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

/**
 * JUnit tests for the GenericActorController.
 * 
 * @author Marc Jakobi
 *
 */
public class GenericActorTest {

	/** Key for the host parameter. */
	private static final String HOST_KEY = "Host name";
	/** Key for the port parameter. */
	private static final String PORT_KEY = "Port number";
	/** Key for the option to wait for a response from FORTE or not */
	protected static final String WAITFORRSP_KEY = "Wait for response";
	/** Integer indicating not to wait for a response from FORTE */
	protected static final int DONTWAITFORRSP = 0;
	/** Precision for assertions of double/float data */
	private static final String NAME = "Generic Actor";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	private IPluginController controller;

	/**
	 * Returns a PolysunSetting object as it would be returned by Polysun. This
	 * object must correspond to the configuration by
	 * {@link BatterySensorController#getConfiguration(Map)}.
	 */
	private PolysunSettings createPolysunSettings(String host, int port, int waitForRsp) {
		List<PropertyValue> properties = new ArrayList<>();
		properties.add(new PropertyValue(HOST_KEY, host));
		properties.add(new PropertyValue(PORT_KEY, port, ""));
		properties.add(new PropertyValue(WAITFORRSP_KEY, waitForRsp, ""));
		
		List<Sensor> sensors = new ArrayList<>();
		
		List<ControlSignal> controlSignals = new ArrayList<>();
		
		List<Log> logs = new ArrayList<>();

		return new PolysunSettings(properties, sensors, controlSignals, logs);
	}

	/**
	 * Calls the default Polysun settings configuration (wit time stamp sending enabled)
	 */
	private PolysunSettings createPolysunSettingsDefaultConfiguration() {
		return createPolysunSettings(DEF_TCP_ADDRESS, DEF_PORT_NUMBER, DONTWAITFORRSP);
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
		controller = new GenericActorController();
	}
	
	@Test
	public void testGetConfiguration() throws PluginControllerException {
		PluginControllerConfiguration configuration = controller.getConfiguration(null);
		assertEquals("Wrong number of configured properties", 3, configuration.getProperties().size());
		assertEquals("Wrong number of generic properties", 0, configuration.getNumGenericProperties());
		assertEquals("Wrong number of configured sensors", 0, configuration.getSensors().size());
		assertEquals("Wrong number of generic sensors", 0, configuration.getNumGenericSensors());
		assertEquals("Wrong number of configured controlSignals", 0, configuration.getControlSignals().size());
		assertEquals("Wrong number of generic controlSignals", MAX_NUM_GENERIC_SIGNALS, configuration.getNumGenericControlSignals());
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
		List<String> controlSignalsToHide = controller.getControlSignalsToHide(
				createPolysunSettingsDefaultConfiguration(), null);
		assertEquals("No signals to hide expected", new ArrayList<String>(), controlSignalsToHide);
	}

	@Test
	public void testGetName() {
		assertEquals(NAME, controller.getName());
	}

	@Test
	public void testGetPropertiesToHide() {
		List<String> propertiesToHide = controller
				.getPropertiesToHide(createPolysunSettingsDefaultConfiguration(), null);
		assertEquals("Wrong number of propreties to hide", 1, propertiesToHide.size());
	}

	@Test
	public void testGetSensorsToHide() {
		List<String> sensorsToHide = controller
				.getSensorsToHide(createPolysunSettingsDefaultConfiguration(), null);
		assertEquals("Wrong number of sensors to hide", 0, sensorsToHide.size());
	}

	@Test
	public void testPolysunSettingsGetter() throws PluginControllerException {
		controller.build(createPolysunSettingsDefaultConfiguration(), null);
		assertEquals("Wrong property name returned", HOST_KEY,
				((AbstractPluginController) controller).getProperty(HOST_KEY).getName());

		assertEquals("Inexistent property failed", null, ((AbstractPluginController) controller).getProperty("XXX"));
		assertEquals("Inexistent property failed", null, ((AbstractPluginController) controller).getSensor("XXX"));
		assertEquals("Inexistent property failed", null,
				((AbstractPluginController) controller).getControlSignal("XXX"));

		assertEquals("Wrong sensor index returned", -1, ((AbstractPluginController) controller).getSensorIndex("XXX"));
		assertEquals("Wrong control signal index returned", -1,
				((AbstractPluginController) controller).getControlSignalIndex("XXX"));
	}
}
package com.velasolaris.plugin.controller.spi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.AbstractProperty;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.ControlSignal;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Log;
import com.velasolaris.plugin.controller.spi.PluginControllerConfiguration.Sensor;

/**
 * Settings of Polysun for the plugin controller.
 * The user sets values for properties, links sensors and controls signals. 
 * 
 * @author rkurmann
 * @since Polysun 9.1
 *
 */
public class PolysunSettings {
	
	/** Prefix for generic properties. Generic properties are consecutively numbered starting by 1 in the order of declaration. */
	public static String GENERIC_PROPERTY_PREFIX = "PluginControllerProperty";
	
	/** Prefix for generic sensors. Generic sensors are consecutively numbered starting by 1 in the order of declaration. */
	public static String GENERIC_SENSOR_PREFIX = "ControllerGenericSensor";

	/** Prefix for generic controls signals. Generic controls signals consecutively numbered starting by 1 in the order of declaration. */
	public static String GENERIC_CONTROL_SIGNAL_PREFIX = "ControllerGenericControlSignal";
	
	/** Prefix for generic logs. Generic logs are consecutively numbered starting by 1 in the order of declaration. */
	public static String GENERIC_LOG_PREFIX = "ControllerGenericLog";
	
	/** All properties of the plugin controller element GUI with values as Map of name to property value. */
	private List<PropertyValue> propertyValues;
	/** All sensors with its state (used or not). */
	private List<Sensor> sensors;
	/** All control signals with its state (used or not). */
	private List<ControlSignal> controlSignals;
	/** All logs with its state. */
	private List<Log> logs;
	
	/** Cache for fast mapping property value names to index in the property value array. */
	private Map<String, Integer> logsIndexCache = new HashMap<>();
	/** Cache for fast mapping property value names to index in the property value array. */
	private Map<String, Integer> propertyVavluesIndexCache = new HashMap<>();
	/** Cache for fast mapping sensor names to index in the float array. */
	private Map<String, Integer> sensorsIndexCache = new HashMap<>();
	/** Cache for fast mapping control signal names to index in the float array. */
	private Map<String, Integer> controlSignalsIndexCache = new HashMap<>();

	/**
	 * Constructor creating PolysunSettings.
	 * 
	 * @param propertyValues property setting values
	 * @param sensors Sensors (inputs) of Polysun
	 * @param controlSignals Controls signals (outputs) of Polysun
	 * @param logs Logs of Polysun
	 */
	public PolysunSettings(List<PropertyValue> propertyValues, List<Sensor> sensors, List<ControlSignal> controlSignals, List<Log> logs) {
		if (propertyValues == null || sensors == null || controlSignals == null) {
			throw new NullPointerException("Parameters propertyValues, sensors or controlSignals must not be null");
		}
		this.propertyValues = propertyValues;
		this.sensors = sensors;
		this.controlSignals = controlSignals;
		this.logs = logs;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PolysunSettings other = (PolysunSettings) obj;
		if (this.controlSignals == null) {
			if (other.controlSignals != null) {
				return false;
			}
		} else if (!this.controlSignals.equals(other.controlSignals)) {
			return false;
		}
		if (this.logs == null) {
			if (other.logs != null) {
				return false;
			}
		} else if (!this.logs.equals(other.logs)) {
			return false;
		}
		if (this.propertyValues == null) {
			if (other.propertyValues != null) {
				return false;
			}
		} else if (!this.propertyValues.equals(other.propertyValues)) {
			return false;
		}
		if (this.sensors == null) {
			if (other.sensors != null) {
				return false;
			}
		} else if (!this.sensors.equals(other.sensors)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the control signal setting for a control signal name.
	 * 
	 * @param controlSignalName name of the control signal as name given in {@linkplain PluginControllerConfiguration}
	 * Generic control signals are named with {@link PolysunSettings#GENERIC_CONTROL_SIGNAL_PREFIX} + number where the number starts with 1, e.g. PluginControllerGenericControlSignal1.
	 * @return control signal object, or <code>null</code> if not found
	 */
	public ControlSignal getControlSignal(String controlSignalName) {
		int index;
		if ((index = getControlSignalIndex(controlSignalName)) != -1) {
			return controlSignals.get(index);
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the index of a control signal in the control signal float array of the {@link IPluginController#control(int, boolean, float[], float[], float[], boolean, Map)} method.
	 * 
	 * @param controlSignalName name of the control signal as name given in {@linkplain PluginControllerConfiguration}
	 * Generic control signals are named with {@link PolysunSettings#GENERIC_CONTROL_SIGNAL_PREFIX} + number where the number starts with 1, e.g. PluginControllerGenericControlSignal1.
	 * @return the index in the float array, or -1 if not found
	 */
	public int getControlSignalIndex(String controlSignalName) {
		Integer cacheVal;
		if ((cacheVal = controlSignalsIndexCache.get(controlSignalName)) != null) {
			return cacheVal;
		}
		for (int i = 0; i < controlSignals.size(); i++) {
			if (controlSignalName.equals(controlSignals.get(i).getName())) {
				controlSignalsIndexCache.put(controlSignalName, i);
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Returns all control signals (configured and generic).
	 * @return List of control signals in the order as shown in the GUI (configured control signals and genric control signals).
	 * 	The same order is also used in the control signal float array of {@link IPluginController#control(int, boolean, float[], float[], float[], boolean, Map)}.
	 */
	public List<ControlSignal> getControlSignals() {
		return controlSignals;
	}
	
	/**
	 * Returns all control signals (configured and generic).
	 * @return Array of control signals in the order as shown in the GUI (configured control signals and genric control signals).
	 * 	The same order is also used in the control signal float array of {@link IPluginController#control(int, boolean, float[], float[], float[], boolean, Map)}.
	 */
	public ControlSignal[] getControlSignalsArray() {
		return controlSignals.toArray(new ControlSignal[0]);
	}
	
	/**
	 * Returns an boolean array for control signals where <code>true</code> at i if this control signal is used in Polysun.
	 * @return boolean array indicating if a control signal is set in Polysun configuration
	 */
	public boolean[] getControlSignalsUsed() {
		boolean[] used = new boolean[controlSignals.size()];
		for (int i = 0; i < controlSignals.size(); i++) {
			used[i] = controlSignals.get(i).isUsed();
		}
		return used;
	}
	
	/**
	 * Gets the log setting for a log name.
	 * 
	 * @param logName name of the log as name given in {@linkplain PluginControllerConfiguration}
	 * Logs defined without name are named with {@link PolysunSettings#GENERIC_LOG_PREFIX} + number where the number starts with 1, e.g. PluginControllerGenericLog1.
	 * @return log object, or <code>null</code> if not found
	 */
	public Log getLog(String logName) {
		int index;
		if ((index = getLogIndex(logName)) != -1) {
			return logs.get(index);
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the log setting for a log name.
	 * 
	 * @param logName name of the log as name given in {@linkplain PluginControllerConfiguration}
	 * Logs defined without name are named with {@link PolysunSettings#GENERIC_LOG_PREFIX} + number where the number starts with 1, e.g. PluginControllerGenericLog1.
	 * @return the index in the float array, or -1 if not found
	 */
	public int getLogIndex(String logName) {
		Integer cacheVal;
		if ((cacheVal = logsIndexCache.get(logName)) != null) {
			return cacheVal;
		}
		for (int i = 0; i < logs.size(); i++) {
			if (logName.equals(logs.get(i).getName())) {
				logsIndexCache.put(logName, i);
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Returns all logs.
	 * @return List of logs in the order as declared.
	 */
	public List<Log> getLogs() {
		return logs;
	}
	
	/**
	 * Returns all logs.
	 * @return Array of logs in the order as declared.
	 */
	public Log[] getLogsArray() {
		return logs.toArray(new Log[0]);
	}

	/**
	 * Gets the property value for a property name as set by the user in the plugin controller GUI.
	 * 
	 * @param index index of the property as name given in {@linkplain PluginControllerConfiguration}.
	 * Generic properties are named with {@link PolysunSettings#GENERIC_PROPERTY_PREFIX} + number where the number starts with 1, e.g. PluginControllerProperty1.
	 * @return property value or <code>null</code> if not found
	 */
	public PropertyValue getPropertyValue(int index) {
		return propertyValues.get(index);
	}
	
	/**
	 * Gets the property value for a property name as set by the user in the plugin controller GUI.
	 * 
	 * @param propertyName name of the property as name given in {@linkplain PluginControllerConfiguration}.
	 * Generic properties are named with {@link PolysunSettings#GENERIC_PROPERTY_PREFIX} + number where the number starts with 1, e.g. PluginControllerProperty1.
	 * @return property value or <code>null</code> if not found
	 */
	public PropertyValue getPropertyValue(String propertyName) {
		int index;
		if ((index = getPropertyValueIndex(propertyName)) != -1) {
			return propertyValues.get(index);
		} else {
			return null;
		}
	}

	/**
	 * Gets the index of a property in the property array method.
	 * 
	 * @param propertyName name of the property as name given in {@linkplain PluginControllerConfiguration}.
	 * Generic properties are named with {@link PolysunSettings#GENERIC_PROPERTY_PREFIX} + number where the number starts with 1, e.g. PluginControllerProperty1.
	 * @return the index in the float array, or -1 if not found
	 */
	public int getPropertyValueIndex(String propertyName) {
		Integer cacheVal;
		if ((cacheVal = propertyVavluesIndexCache.get(propertyName)) != null) {
			return cacheVal;
		}
		for (int i = 0; i < propertyValues.size(); i++) {
			if (propertyName.equals(propertyValues.get(i).getName())) {
				propertyVavluesIndexCache.put(propertyName, i);
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Returns all property values (configured and generic).
	 * @return Map of name to property values
	 */
	public Map<String, PropertyValue> getPropertyValueMap() {
		Map<String, PropertyValue> map = new HashMap<String, PropertyValue>();
		for (PropertyValue propertyValue : propertyValues) {
			map.put(propertyValue.getName(), propertyValue);
		}
		return map;
	}
	
	/**
	 * Returns all property values (configured and generic).
	 * @return List property values in the order as shown in the GUI (configured properties and generic properties).
	 */
	public List<PropertyValue> getPropertyValues() {
		return propertyValues;
	}
	
	/**
	 * Returns all property values (configured and generic).
	 * @return Array of property values in the order as shown in the GUI (configured properties and generic properties).
	 */
	public PropertyValue[] getPropertyValuesArray() {
		return propertyValues.toArray(new PropertyValue[0]);
	}

	/**
	 * Gets the sensor setting for a sensor name.
	 * 
	 * @param sensorName name of the sensor as name given in {@linkplain PluginControllerConfiguration}.
	 * Generic sensors are named with {@link PolysunSettings#GENERIC_SENSOR_PREFIX} + number where the number starts with 1, e.g. PluginControllerGenericSensor1.
	 * @return sensor object, or <code>null</code> if not found
	 */
	public Sensor getSensor(String sensorName) {
		int index;
		if ((index = getSensorIndex(sensorName)) != -1) {
			return sensors.get(index);
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the index of a sensor in the sensor float array of the {@link IPluginController#control(int, boolean, float[], float[], float[], boolean, Map)} method.
	 * 
	 * @param sensorName name of the sensor as name given in {@linkplain PluginControllerConfiguration}.
	 * Generic sensors are named with {@link PolysunSettings#GENERIC_SENSOR_PREFIX} + number where the number starts with 1, e.g. PluginControllerGenericSensor1.
	 * @return the index in the float array, or -1 if not found
	 */
	public int getSensorIndex(String sensorName) {
		Integer cacheVal;
		if ((cacheVal = sensorsIndexCache.get(sensorName)) != null) {
			return cacheVal;
		}
		for (int i = 0; i < sensors.size(); i++) {
			if (sensorName.equals(sensors.get(i).getName())) {
				sensorsIndexCache.put(sensorName, i);
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the all sensors (configured and generic).
	 * 
	 * @return List of sensors in the order as shown in the GUI (configured sensors and generic sensors).
	 * 		The same order is also used in the control signal float array of {@link IPluginController#control(int, boolean, float[], float[], float[], boolean, Map)}.
	 */
	public List<Sensor> getSensors() {
		return sensors;
	}
	
	/**
	 * Returns the all sensors (configured and generic).
	 * 
	 * @return Array of sensors in the order as shown in the GUI (configured sensors and generic sensors).
	 * 		The same order is also used in the control signal float array of {@link IPluginController#control(int, boolean, float[], float[], float[], boolean, Map)}.
	 */
	public Sensor[] getSensorsArray() {
		return sensors.toArray(new Sensor[0]);
	}
	
	/**
	 * Returns an boolean array for sensors where <code>true</code> at i if this sensor is used in Polysun.
	 * @return boolean array indicating if a sensor is set in Polysun configuration
	 */
	public boolean[] getSensorUsed() {
		boolean[] used = new boolean[sensors.size()];
		for (int i = 0; i < sensors.size(); i++) {
			used[i] = sensors.get(i).isUsed();
		}
		return used;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.controlSignals == null) ? 0 : this.controlSignals.hashCode());
		result = prime * result + ((this.logs == null) ? 0 : this.logs.hashCode());
		result = prime * result + ((this.propertyValues == null) ? 0 : this.propertyValues.hashCode());
		result = prime * result + ((this.sensors == null) ? 0 : this.sensors.hashCode());
		return result;
	}

	/**
	 * Is sensor used in Polysun? I.e. linked to a component?
	 * 
	 * @param controlSignalName name of the sensor as name given in {@linkplain PluginControllerConfiguration}.
	 * Generic sensors are named with {@link PolysunSettings#GENERIC_SENSOR_PREFIX} + number where the number starts with 1, e.g. PluginControllerGenericSensor1.
	 * @return <code>true</code> if set in Polysun, otherwise <code>false</code>, <code>null</code> if name not found
	 */
	public Boolean isControlSignalUsed(String controlSignalName) {
		ControlSignal controlSignal = getControlSignal(controlSignalName);
		if (controlSignal != null) {
			return controlSignal.isUsed();
		} else {
			return null;
		}
	}

	/**
	 * Is sensor used in Polysun? I.e. linked to a component?
	 * 
	 * @param sensorName name of the sensor as name given in {@linkplain PluginControllerConfiguration}.
	 * Generic sensors are named with {@link PolysunSettings#GENERIC_SENSOR_PREFIX} + number where the number starts with 1, e.g. PluginControllerGenericSensor1.
	 * @return <code>true</code> if set in Polysun, otherwise <code>false</code>, <code>null</code> if name not found
	 */
	public Boolean isSensorUsed(String sensorName) {
		Sensor sensor = getSensor(sensorName);
		if (sensor != null) {
			return sensor.isUsed();
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return "PolysunSettings [propertyValues=" + propertyValues + ", sensors=" + sensors + ", controlSignals="
				+ controlSignals + "]";
	}

	/**
	 * Class representing a property setting in the plugin controller element GUI.
	 * 
	 * @author rkurmann
	 * @since Polysun 9.1
	 *
	 */
	public static class PropertyValue extends AbstractProperty {
		
		private static final long serialVersionUID = 1L;
		/** Int value of the property. */
		private int intValue;
		/** Float value of the property. */
		private float floatValue;
		/** String value of the property. */
		private String stringValue;
		/**
		 * Constructor for setting a float property value.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key)
		 * @param floatValue Float value of the property
		 * @param unit Unit as defined in Polysun, e.g. "l/h"
		 */
		public PropertyValue(String name, float floatValue, String unit) {
			this(name, Type.FLOAT, (int) floatValue, floatValue, "" + floatValue, unit);
		}
		
		/**
		 * Constructor for setting a int property value.
		 * 
		 * Options are also returned als int values, the index of the options beginning with 0.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key)
		 * @param intValue Int value of the property
		 * @param unit Unit as defined in Polysun, e.g. "l/h"
		 */
		public PropertyValue(String name, int intValue, String unit) {
			this(name, Type.INTEGER, intValue, intValue, "" + intValue, unit);
		}
		
		/**
		 * Constructor for setting a string property value.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key)
		 * @param stringValue String value of the property
		 */
		public PropertyValue(String name, String stringValue) {
			this(name, Type.STRING, 0, Float.NaN, stringValue, null);
		}
		
		/**
		 * Full constructor setting all fields.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param type Type of property.
		 * @param intValue Int value of the property
		 * @param floatValue Float value of the property
		 * @param stringValue String value of the property
		 * @param unit Unit as defined in Polysun, e.g. "l/h"
		 */
		private PropertyValue(String name, Type type, int intValue, float floatValue, String stringValue, String unit) {
			this.name = name;
			this.type = type;
			this.intValue = intValue;
			this.floatValue = floatValue;
			this.stringValue = stringValue;
			this.unit = unit;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			PropertyValue other = (PropertyValue) obj;
			if (Float.floatToIntBits(floatValue) != Float.floatToIntBits(other.floatValue)) {
				return false;
			}
			if (intValue != other.intValue) {
				return false;
			}
			if (name == null) {
				if (other.name != null) {
					return false;
				}
			} else if (!name.equals(other.name)) {
				return false;
			}
			if (stringValue == null) {
				if (other.stringValue != null) {
					return false;
				}
			} else if (!stringValue.equals(other.stringValue)) {
				return false;
			}
			if (type != other.type) {
				return false;
			}
			if (unit == null) {
				if (other.unit != null) {
					return false;
				}
			} else if (!unit.equals(other.unit)) {
				return false;
			}
			return true;
		}
		
		/**
		 * Returns the float value of the property.
		 * @return int values are also set as float, 0 is returned for a string value
		 */
		public float getFloat() {
			return floatValue;
		}
		
		/**
		 * Returns the int value of the property.
		 * @return float values are also casted to int, 0 is returned for a string value
		 */
		public int getInt() {
			return intValue;
		}
		
		/**
		 * Returns the string value of the property.
		 * @return float and int values are also set as string
		 */
		public String getString() {
			return stringValue;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Float.floatToIntBits(floatValue);
			result = prime * result + intValue;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((stringValue == null) ? 0 : stringValue.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			result = prime * result + ((unit == null) ? 0 : unit.hashCode());
			return result;
		}
		
		@Override
		public String toString() {
			return "PropertyValue [name=" + name + ", type=" + type + ", float=" + floatValue + ", int="
					+ intValue + ", string=" + stringValue + ", unit=" + unit + "]";
		}
		
	}

}

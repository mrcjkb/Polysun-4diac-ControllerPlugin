package com.velasolaris.plugin.controller.spi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Class holding the configuration for a plugin controller. A plugin controller in Polysun is configured by this class.
 * immutable object.
 * 
 * This object and its fields are serialized for detection of configuration changes between Polysun start. Be aware of this fact.
 * 
 * Properties, sensors and control signals can be configured with name and unit. There is also the possibility to have generic
 * properties, sensors and control signals. For configured sensors and control signals the selection dialog shows display properties
 * having the right unit. Generic properties do not have a unit and all display properties are shown.
 * 
 * @author rkurmann
 * @since Polysun 9.1
 *
 */
public class PluginControllerConfiguration implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** Setting this constant makes Polysun use its default timestep. */
	public static final int DEFAULT_TIMESTEP = 0;

	private List<Property> properties;

	private List<Sensor> sensors;

	private List<ControlSignal> controlSignals;
	
	/** Configured values for the simulation analysis logs or Log &amp; Parameterize. */
	private List<Log> logs;
	
	/** Number of generic properties (as in the ProgrammableController). The user can see and edit these properties. */
	private int numGenericProperties;

	/** Number of generic sensors (inputs) (as in the ProgrammableController). */
	private int numGenericSensors;
	
	/** Number of generic control signal (outputs) (as in the ProgrammableController). */
	private int numGenericControlSignals;
	
	/** Name of a property that configures the number of generic properties. Not yet working in PluginController. <code>null</code> = no property set */
	private String propertyNameNumGenericProperties;
	/** Name of a property that configures the number of generic sensors. <code>null</code> = no property set */
	private String propertyNameNumGenericSensors;
	/** Name of a property that configures the number of generic control signals. <code>null</code> = no property set */
	private String propertyNameNumGenericControlSignals;
	/** Name of a property that configures the number of generic logs. <code>null</code> = no property set */
	private String propertyNameNumGenericLogs;
	
	/** Path to the image for this plugin controller in the classpath. <code>null</code> is for the standard controller image. */
	private String imagePath;
	
	/** Generic parameters */
	private Map<String, Object> parameters;
	
	/**
	 * Constructor for creating ht plugin controller configuration.
	 * 
	 * Hint: In Eclipse with Ctrl-Space Space, the parmater declaration is shown.
	 * 
	 * @param properties List of properties or <code>null</code> for no configured properties
	 * @param sensors List of sensors or <code>null</code> for no configured sensors
	 * @param controlSignals List of control signals or <code>null</code> for no configured control signals
	 * @param logs List of logs for simulation analysis or <code>null</code> for no configured logs
	 * @param numGenericProperties The number of generic properties (as in ProgrammableController), a value less than 1 for no generic properties
	 * @param numGenericSensors The number of generic sensors (as in ProgrammableController), a value less than 1 for no generic sensors
	 * @param numGenericControlSignals The number of generic control signals (as in ProgrammableController), a value less than 1 for no generic control signals
	 * @param imagePath Path to the image for this plugin controller in the classpath. <code>null</code> is for the standard controller image.
	 * @param parameters Generic parameters
	 */
	public PluginControllerConfiguration(List<Property> properties, List<Sensor> sensors,
			List<ControlSignal> controlSignals, List<Log> logs, int numGenericProperties,
			int numGenericSensors, int numGenericControlSignals, String imagePath,
			Map<String, Object> parameters) {
		this(properties, sensors, controlSignals, logs, numGenericProperties, numGenericSensors, numGenericControlSignals, imagePath, null, null, null, parameters);
	}
	/**
	 * Constructor for creating ht plugin controller configuration.
	 * 
	 * Hint: In Eclipse with Ctrl-Space Space, the parmater declaration is shown.
	 * 
	 * @param properties List of properties or <code>null</code> for no configured properties
	 * @param sensors List of sensors or <code>null</code> for no configured sensors
	 * @param controlSignals List of control signals or <code>null</code> for no configured control signals
	 * @param logs List of logs for simulation analysis or <code>null</code> for no configured logs
	 * @param numGenericProperties The number of generic properties (as in ProgrammableController), a value less than 1 for no generic properties
	 * @param numGenericSensors The number of generic sensors (as in ProgrammableController), a value less than 1 for no generic sensors
	 * @param numGenericControlSignals The number of generic control signals (as in ProgrammableController), a value less than 1 for no generic control signals
	 * @param imagePath Path to the image for this plugin controller in the classpath. <code>null</code> is for the standard controller image.
	 * @param propertyNameNumGenericSensors Name of a property that configures the number of generic sensors, <code>null</code> = no such property
	 * @param propertyNameNumGenericControlSignals Name of a property that configures the number of generic controls signals, <code>null</code> = no such property
	 * @param propertyNameNumGenericLogs Name of a property that configures the number of generic logs, <code>null</code> = no such property
	 * @param parameters Generic parameters
	 */
	public PluginControllerConfiguration(List<Property> properties, List<Sensor> sensors,
			List<ControlSignal> controlSignals, List<Log> logs, int numGenericProperties,
			int numGenericSensors, int numGenericControlSignals, String imagePath,
			String propertyNameNumGenericSensors, String propertyNameNumGenericControlSignals, String propertyNameNumGenericLogs, Map<String, Object> parameters) {
		this.properties = properties;
		this.sensors = sensors;
		this.controlSignals = controlSignals;
		this.logs = logs;
		this.numGenericProperties = numGenericProperties;
		this.numGenericSensors = numGenericSensors;
		this.numGenericControlSignals = numGenericControlSignals;
		this.imagePath = imagePath;
		this.parameters = parameters;
		this.propertyNameNumGenericProperties = null; // Not yet working in PluginController
		this.propertyNameNumGenericSensors = propertyNameNumGenericSensors;
		this.propertyNameNumGenericControlSignals = propertyNameNumGenericControlSignals;
		this.propertyNameNumGenericLogs = propertyNameNumGenericLogs;
	}

	/**
	 * List of properties. Configurable properties in controller settings.
	 * 
	 * @return List of properties, always not <code>null</code>
	 */
	public List<Property> getProperties() {
		if (properties == null) {
			return new ArrayList<>();
		}
		return properties;
	}

	/**
	 * List of configured sensors.
	 * 
	 * @return List of sensors, always not <code>null</code>
	 */
	public List<Sensor> getSensors() {
		if (sensors == null) {
			return new ArrayList<>();
		}
		return sensors;
	}

	/**
	 * List of configured control signals.
	 * 
	 * @return List of control signals, always not <code>null</code>
	 */
	public List<ControlSignal> getControlSignals() {
		if (controlSignals == null) {
			return new ArrayList<>();
		}
		return controlSignals;
	}

	/**
	 * List of simulation analysis logs.
	 * 
	 * @return List of logs, always not <code>null</code>
	 */
	public List<Log> getLogs() {
		if (logs == null) {
			return new ArrayList<>();
		}
		return logs;
	}

	/**
	 * The number of generic properties (as in ProgrammableController), a value less than 1 for no generic properties.
	 * @return Number of generic properties, always bigger than or equals 0
	 */
	public int getNumGenericProperties() {
		if (numGenericProperties < 0) {
			return 0;
		}
		return numGenericProperties;
	}

	/**
	 * The number of generic sensors (as in ProgrammableController), a value less than 1 for no sensors.
	 * @return Number of generic sensors, always bigger than or equals 0
	 */
	public int getNumGenericSensors() {
		if (numGenericSensors < 0) {
			return 0;
		}
		return numGenericSensors;
	}

	/**
	 * The number of generic control signals (as in ProgrammableController), a value less than 1 for no control signals.
	 * @return Number of generic control signals, always bigger than or equals 0
	 */
	public  int getNumGenericControlSignals() {
		if (numGenericControlSignals < 0) {
			return 0;
		}
		return numGenericControlSignals;
	}

	/**
	 * Are generic properties configured?
	 * 
	 * @return <code>true</code> if generic properties are configured, otherwise <code>false</code>
	 */
	public boolean hasGenericProperties() {
		return numGenericProperties > 0;
	}
	
	/**
	 * Are generic sensors configured?
	 * 
	 * @return <code>true</code> if generic sensors are configured, otherwise <code>false</code>
	 */
	public boolean hasGenericSensors() {
		return numGenericSensors > 0;
	}
	
	/**
	 * Are generic control signals configured?
	 * 
	 * @return <code>true</code> if generic control signals are configured, otherwise <code>false</code>
	 */
	public boolean hasGenericControlSignals() {
		return numGenericControlSignals > 0;
	}
	
	/**
	 * Returns the name of a property that configures the number of generic properties, <code>null</code> = no such property
	 * @return property name or <code>null</code>
	 */
	public String getPropertyNameNumGenericProperties() {
		return propertyNameNumGenericProperties;
	}
	
	/**
	 * Returns the name of a property that configures the number of generic sensors, <code>null</code> = no such property
	 * @return property name or <code>null</code>
	 */
	public String getPropertyNameNumGenericSensors() {
		return propertyNameNumGenericSensors;
	}
	
	/**
	 * Returns the name of a property that configures the number of generic controls signal, <code>null</code> = no such property
	 * @return property name or <code>null</code>
	 */
	public String getPropertyNameNumGenericControlSignals() {
		return propertyNameNumGenericControlSignals;
	}
	
	/**
	 * Returns the name of a property that configures the number of generic logs, <code>null</code> = no such property
	 * @return property name or <code>null</code>
	 */
	public String getPropertyNameNumGenericLogs() {
		return propertyNameNumGenericLogs;
	}
	
	/**
	 * Path to the image for this plugin controller in the classpath. <code>null</code> is for the standard controller image.
	 * @return Path to the image for this plugin controller in the classpath, or <code>null</code> is for the standard controller image.
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * Generic parameters.
	 * @return the the general parameters, key is a String, see {@link IPluginController}
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	@Override
	public String toString() {
		return "PluginControllerConfiguration [properties=" + properties + ", sensors=" + sensors + ", controlSignals="
				+ controlSignals + ", logs=" + logs + ", numGenericProperties=" + numGenericProperties
				+ ", numGenericSensors=" + numGenericSensors + ", numGenericControlSignals=" + numGenericControlSignals
				+ ", imagePath="
				+ imagePath + ", parameters=" + parameters + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.controlSignals == null) ? 0 : this.controlSignals.hashCode());
		result = prime * result + ((this.imagePath == null) ? 0 : this.imagePath.hashCode());
		result = prime * result + ((this.logs == null) ? 0 : this.logs.hashCode());
		result = prime * result + this.numGenericControlSignals;
		result = prime * result + this.numGenericProperties;
		result = prime * result + this.numGenericSensors;
		result = prime * result + ((this.properties == null) ? 0 : this.properties.hashCode());
		result = prime * result + ((this.sensors == null) ? 0 : this.sensors.hashCode());
		return result;
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
		PluginControllerConfiguration other = (PluginControllerConfiguration) obj;
		if (this.controlSignals == null) {
			if (other.controlSignals != null) {
				return false;
			}
		} else if (!this.controlSignals.equals(other.controlSignals)) {
			return false;
		}
		if (this.imagePath == null) {
			if (other.imagePath != null) {
				return false;
			}
		} else if (!this.imagePath.equals(other.imagePath)) {
			return false;
		}
		if (this.logs == null) {
			if (other.logs != null) {
				return false;
			}
		} else if (!this.logs.equals(other.logs)) {
			return false;
		}
		if (this.numGenericControlSignals != other.numGenericControlSignals) {
			return false;
		}
		if (this.numGenericProperties != other.numGenericProperties) {
			return false;
		}
		if (this.numGenericSensors != other.numGenericSensors) {
			return false;
		}
		if (this.properties == null) {
			if (other.properties != null) {
				return false;
			}
		} else if (!this.properties.equals(other.properties)) {
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
	 * Base class for properties, sensors, control signals and logs.
	 * 
	 * Name and unit are shared among all of them. Object orient progamming will be possibe.
	 * 
	 * @author rkurmann
	 * @since Polysun 9.1
	 *
	 */
	public static abstract class Exchange implements Serializable {
		
		private static final long serialVersionUID = 1L;
		/** Name of the property, either a human readable text (or internally, a Polysun translation key). */
		protected String name;
		/** Unit as defined in Polysun, e.g. "l/h". */
		protected String unit;
		/** Tooltip text of the property, either a human readable text (or internally, a Polysun translation key). */
		protected String tooltip;
		
		/**
		 * Name of the property.
		 * @return Human readable text (or internally, a Polysun translation key)
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Returns the tooltip text of the property.
		 * @return Human readable text (or internally, a Polysun translation key) or <code>null</code> if no tooltip set
		 */
		public String getTooltip() {
			return tooltip;
		}
		
		/**
		 * Unit as defined in Polysun, e.g. "l/h"
		 * @return Unit string as defined in the Unit catalog of Polysun.
		 */
		public String getUnit() {
			if (unit == null) {
				return "";
			}
			return unit;
		}
		
	}
	
	/**
	 * Property base class that is parent of properties for setting up the configuration
	 * and for returning property values to the plugin controller.
	 * 
	 * @author rkurmann
	 * @since Polysun 9.1
	 *
	 */
	public static abstract class AbstractProperty extends Exchange {

		private static final long serialVersionUID = 1L;
		/** Type of user input. */
		public enum Type {INTEGER, FLOAT, STRING}

		/** Type of proptery. */
		protected Type type;

		/**
		 * Type of property.
		 * @return Property type
		 */
		public Type getType() {
			return type;
		}

	}
	/**
	 * Property configuration class. Configures a property in the controller element GUI.
	 * 
	 * @author rkurmann
	 * @since Polysun 9.1
	 *
	 */
	public static class Property extends AbstractProperty implements Serializable {
		
		private static final long serialVersionUID = 1L;
		/** Default int value for the property. */
		private int defaultInt;
		/** Default float value for the property. */
		private float defaultFloat;
		/** Default string value for the property. <code>null</code> means no default String. */
		private String defaultString;
		/** Min allowed value. */
		private float min;
		/** Max allowed value. */
		private float max;
		/** Scale of the float value. */
		private int scale;
		/** Precision of the float value. */
		private int precision;
		/** Option values delimited by ";". These options will be shown as drop down list in the GUI. Either a human readable text or a translation key. */
		private String options;
		/** Is this property editable? */
		private boolean editable;
		/** Is this property visible? */
		private boolean visible;
		
		/**
		 * Full constructor settings all fields.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param type Type of property.
		 * @param defaultInt Default int value for the property.
		 * @param defaultFloat Default float value for the property.
		 * @param defaultString Default string value for the property. <code>null</code> means no default String.
		 * @param unit Unit as defined in Polysun, e.g. "l/h"
		 * @param scale Scale of the float value
		 * @param precision Precision of the float value
		 * @param options Option values delimited by ";". These options will be shown as drop down list in the GUI. Either a human readable text or a translation key.
		 * @param editable Is this property editable?
		 * @param visible Is this property visible?
		 * @param minValue Min allowed value
		 * @param maxValue Max allowed value
		 * @param tooltip Tooltip to show, or <code>null</code>. This tooltip overwrites the default translated tooltip derived from the key.
		 */
		protected Property(String name, Type type, int defaultInt, float defaultFloat,
				String defaultString, String unit, float minValue, float maxValue, int scale, int precision,
				String options, boolean editable, boolean visible, String tooltip) {
			this.name = name;
			this.type = type;
			this.defaultInt = defaultInt;
			this.defaultFloat = defaultFloat;
			this.defaultString = defaultString;
			this.unit = unit;
			this.min = minValue;
			this.max = maxValue;
			this.scale = scale;
			this.precision = precision;
			this.options = options;
			this.editable = editable;
			this.visible = visible;
			this.tooltip = tooltip;
		}
		
		/**
		 * Constructor creating an integer property.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param defaultInt The default int value
		 * @param min Min allowed value
		 * @param max Max allowed value
		 * @param tooltip Tooltip to show, or <code>null</code>. This tooltip overwrites the default translated tooltip derived from the key.
		 */
		public Property(String name, int defaultInt, int min, int max, String tooltip) {
			this(name, Type.INTEGER, defaultInt, 0, null, null, min, max, 0, 0, null, true, true, tooltip);
		}
		
		/**
		 * Constructor creating an integer property.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param defaultInt The default int value
		 * @param min Min allowed value
		 * @param max Max allowed value
		 */
		public Property(String name, int defaultInt, int min, int max) {
			this(name, Type.INTEGER, defaultInt, 0, null, null, min, max, 0, 0, null, true, true, null);
		}
		
		/**
		 * Constructor creating an integer property.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param defaultInt The default int value
		 * @param min Min allowed value
		 * @param max Max allowed value
		 * @param editable Is this property editable?
		 * @param tooltip Tooltip to show, or <code>null</code>. This tooltip overwrites the default translated tooltip derived from the key.
		 */
		public Property(String name, int defaultInt, int min, int max, boolean editable, String tooltip) {
			this(name, Type.INTEGER, defaultInt, 0, null, null, min, max, 0, 0, null, editable, true, tooltip);
		}
		
		/**
		 * Constructor creating an integer property.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param defaultInt The default int value
		 * @param min Min allowed value
		 * @param max Max allowed value
		 * @param editable Is this property editable?
		 */
		public Property(String name, int defaultInt, int min, int max, boolean editable) {
			this(name, Type.INTEGER, defaultInt, 0, null, null, min, max, 0, 0, null, editable, true, null);
		}
		
		/**
		 * Constructor creating string options which are shown as drop down list in the controller element GUI.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param options String array of options
		 * @param defaultOptionIndex A value between 0 and options.length
		 * @param tooltip Tooltip to show, or <code>null</code>. This tooltip overwrites the default translated tooltip derived from the key.
		 */
		public Property(String name, String[] options, int defaultOptionIndex, String tooltip) {
			// String.join(options, ";") is only available since Java 8
			// See http://stackoverflow.com/questions/1978933/a-quick-and-easy-way-to-join-array-elements-with-a-separator-the-opposite-of-sp
			this(name, Type.INTEGER, defaultOptionIndex, 0, null, null, 0, options.length, 0, 0, ("" + Arrays.asList(options)).replaceAll("(^.|.$)", "").replace(", ", ";" ), true, true, tooltip);
			if (defaultOptionIndex < 0 || defaultOptionIndex >= options.length) {
				this.defaultInt = 0;
			}
		}
		
		/**
		 * Constructor creating string options which are shown as drop down list in the controller element GUI.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param options String array of options
		 * @param defaultOptionIndex A value between 0 and options.length
		 */
		public Property(String name, String[] options, int defaultOptionIndex) {
			// String.join(options, ";") is only available since Java 8
			// See http://stackoverflow.com/questions/1978933/a-quick-and-easy-way-to-join-array-elements-with-a-separator-the-opposite-of-sp
			this(name, Type.INTEGER, defaultOptionIndex, 0, null, null, 0, options.length, 0, 0, ("" + Arrays.asList(options)).replaceAll("(^.|.$)", "").replace(", ", ";" ), true, true, null);
			if (defaultOptionIndex < 0 || defaultOptionIndex >= options.length) {
				this.defaultInt = 0;
			}
		}
		
		/**
		 * Constructor creating an float property.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param defaultFloat The default int value
		 * @param min Min allowed value
		 * @param max Max allowed value
		 * @param unit Unit as defined in Polysun, e.g. "l/h"
		 * @param tooltip Tooltip to show, or <code>null</code>. This tooltip overwrites the default translated tooltip derived from the key.
		 */
		public Property(String name, float defaultFloat, float min, float max, String unit, String tooltip) {
			this(name, Type.FLOAT, 0, defaultFloat, null, unit, min, max, 0, 0, null, true, true, tooltip);
		}
		
		/**
		 * Constructor creating an float property.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param defaultFloat The default int value
		 * @param min Min allowed value
		 * @param max Max allowed value
		 * @param unit Unit as defined in Polysun, e.g. "l/h"
		 */
		public Property(String name, float defaultFloat, float min, float max, String unit) {
			this(name, Type.FLOAT, 0, defaultFloat, null, unit, min, max, 0, 0, null, true, true, null);
		}
		
		/**
		 * Constructor creating an float property.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param defaultFloat The default int value
		 * @param min Min allowed value
		 * @param max Max allowed value
		 * @param scale Scale of the float value
		 * @param precision Precision of the float value
		 * @param unit Unit as defined in Polysun, e.g. "l/h"
		 * @param tooltip Tooltip to show, or <code>null</code>. This tooltip overwrites the default translated tooltip derived from the key.
		 */
		public Property(String name, float defaultFloat, float min, float max, int scale, int precision, String unit, String tooltip) {
			this(name, Type.FLOAT, 0, defaultFloat, null, unit, min, max, scale, precision, null, true, true, tooltip);
		}
		
		/**
		 * Constructor creating an float property.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param defaultFloat The default int value
		 * @param min Min allowed value
		 * @param max Max allowed value
		 * @param scale Scale of the float value
		 * @param precision Precision of the float value
		 * @param unit Unit as defined in Polysun, e.g. "l/h"
		 */
		public Property(String name, float defaultFloat, float min, float max, int scale, int precision, String unit) {
			this(name, Type.FLOAT, 0, defaultFloat, null, unit, min, max, scale, precision, null, true, true, null);
		}
		
		/**
		 * Constructor creating an float property.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param defaultFloat The default int value
		 * @param min Min allowed value
		 * @param max Max allowed value
		 * @param scale Scale of the float value
		 * @param precision Precision of the float value
		 * @param unit Unit as defined in Polysun, e.g. "l/h"
		 * @param editable Is this property editable?
		 * @param tooltip Tooltip to show, or <code>null</code>. This tooltip overwrites the default translated tooltip derived from the key.
		 */
		public Property(String name, float defaultFloat, float min, float max, int scale, int precision, String unit, boolean editable, String tooltip) {
			this(name, Type.FLOAT, 0, defaultFloat, null, unit, min, max, scale, precision, null, editable, true, tooltip);
		}
		
		/**
		 * Constructor creating an float property.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param defaultFloat The default int value
		 * @param min Min allowed value
		 * @param max Max allowed value
		 * @param scale Scale of the float value
		 * @param precision Precision of the float value
		 * @param unit Unit as defined in Polysun, e.g. "l/h"
		 * @param editable Is this property editable?
		 */
		public Property(String name, float defaultFloat, float min, float max, int scale, int precision, String unit, boolean editable) {
			this(name, Type.FLOAT, 0, defaultFloat, null, unit, min, max, scale, precision, null, editable, true, null);
		}
		
		/**
		 * Constructor creating an float property.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param defaultFloat The default int value
		 * @param min Min allowed value
		 * @param max Max allowed value
		 * @param unit Unit as defined in Polysun, e.g. "l/h"
		 * @param editable Is this property editable?
		 * @param tooltip Tooltip to show, or <code>null</code>. This tooltip overwrites the default translated tooltip derived from the key.
		 */
		public Property(String name, float defaultFloat, float min, float max, String unit, boolean editable, String tooltip) {
			this(name, Type.FLOAT, 0, defaultFloat, null, unit, min, max, 0, 0, null, editable, true, tooltip);
		}
		
		/**
		 * Constructor creating an float property.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param defaultFloat The default int value
		 * @param min Min allowed value
		 * @param max Max allowed value
		 * @param unit Unit as defined in Polysun, e.g. "l/h"
		 * @param editable Is this property editable?
		 */
		public Property(String name, float defaultFloat, float min, float max, String unit, boolean editable) {
			this(name, Type.FLOAT, 0, defaultFloat, null, unit, min, max, 0, 0, null, editable, true, null);
		}
		
		/**
		 * Constructor creating an string property.
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key). <code>null</code> means no default String.
		 * @param defaultString The default string value
		 * @param tooltip Tooltip to show, or <code>null</code>. This tooltip overwrites the default translated tooltip derived from the key.
		 */
		public Property(String name, String defaultString, String tooltip) {
			this(name, Type.STRING, 0, 0, defaultString, null, 0, 0, 0, 0, null, true, true, tooltip);
		}

		/**
		 * Constructor creating an string property.
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key). <code>null</code> means no default String.
		 * @param defaultString The default string value
		 */
		public Property(String name, String defaultString) {
			this(name, Type.STRING, 0, 0, defaultString, null, 0, 0, 0, 0, null, true, true, null);
		}
		
		/**
		 * Constructor creating an string property.
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key). <code>null</code> means no default String.
		 * @param defaultString The default string value
		 * @param editable Is this property editable?
		 * @param tooltip Tooltip to show, or <code>null</code>. This tooltip overwrites the default translated tooltip derived from the key.
		 */
		public Property(String name, String defaultString, boolean editable, String tooltip) {
			this(name, Type.STRING, 0, 0, defaultString, null, 0, 0, 0, 0, null, editable, true, tooltip);
		}
		
		/**
		 * Constructor creating an string property.
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key). <code>null</code> means no default String.
		 * @param defaultString The default string value
		 * @param editable Is this property editable?
		 */
		public Property(String name, String defaultString, boolean editable) {
			this(name, Type.STRING, 0, 0, defaultString, null, 0, 0, 0, 0, null, editable, true, null);
		}
		
		/**
		 * Return default int value for the property.
		 * @return the default int value
		 */
		public int getDefaultInt() {
			return defaultInt;
		}
		
		/**
		 * Return default float value for the property.
		 * @return the default float value
		 */
		public float getDefaultFloat() {
			return defaultFloat;
		}
		
		/**
		 * Return default string value for the property.
		 * @return Returns never <code>null</code>
		 */
		public String getDefaultString() {
			if (defaultString != null) {
				return defaultString;
			} else {
				return "";
			}
		}
		
		/**
		 * Returns min allowed value.
		 * @return the minimum allowed value of this property
		 */
		public float getMin() {
			return min;
		}
		
		/**
		 * Returns max allowed value.
		 * @return the maximum allowed value of this property
		 */
		public float getMax() {
			return max;
		}
		
		/**
		 * Returns scale of the float value.
		 * @return factor to shift a value, e.g. 1000
		 */
		public int getScale() {
			return scale;
		}
		
		/**
		 * Returns precision of the float value.
		 * @return number of decimal numbers, 0 means an integer value
		 */
		public int getPrecision() {
			return precision;
		}
		
		/**
		 * Returns option values delimited by ";". These options will be shown as drop down list in the GUI. Either a human readable text or a translation key.
		 * @return Option values delimited by ";"
		 */
		public String getOptions() {
			return options;
		}
		
		/**
		 * Is this property editable?
		 * @return <code>true</code> if editable
		 */
		public boolean isEditable() {
			return editable;
		}
		
		/**
		 * Is this property visible?
		 * @return <code>true</code> if visible
		 */
		public boolean isVisible() {
			return visible;
		}
		
		@Override
		public String toString() {
			return "Property [name=" + name + ", type=" + type + ", defaultInt=" + defaultInt
					+ ", defaultFloat=" + defaultFloat + ", defaultString=" + defaultString
					+ ", unit=" + unit + ", min=" + min + ", max=" + max + ", scale=" + scale
					+ ", precision=" + precision + ", options=" + options + ", editable=" + editable + ", visible="
					+ visible + "]";
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Float.floatToIntBits(defaultFloat);
			result = prime * result + defaultInt;
			result = prime * result + ((defaultString == null) ? 0 : defaultString.hashCode());
			result = prime * result + (editable ? 1231 : 1237);
			result = prime * result + Float.floatToIntBits(max);
			result = prime * result + Float.floatToIntBits(min);
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((options == null) ? 0 : options.hashCode());
			result = prime * result + precision;
			result = prime * result + scale;
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			result = prime * result + ((unit == null) ? 0 : unit.hashCode());
			result = prime * result + (visible ? 1231 : 1237);
			return result;
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
			Property other = (Property) obj;
			if (Float.floatToIntBits(defaultFloat) != Float.floatToIntBits(other.defaultFloat)) {
				return false;
			}
			if (defaultInt != other.defaultInt) {
				return false;
			}
			if (defaultString == null) {
				if (other.defaultString != null) {
					return false;
				}
			} else if (!defaultString.equals(other.defaultString)) {
				return false;
			}
			if (editable != other.editable) {
				return false;
			}
			if (Float.floatToIntBits(max) != Float.floatToIntBits(other.max)) {
				return false;
			}
			if (Float.floatToIntBits(min) != Float.floatToIntBits(other.min)) {
				return false;
			}
			if (name == null) {
				if (other.name != null) {
					return false;
				}
			} else if (!name.equals(other.name)) {
				return false;
			}
			if (options == null) {
				if (other.options != null) {
					return false;
				}
			} else if (!options.equals(other.options)) {
				return false;
			}
			if (precision != other.precision) {
				return false;
			}
			if (scale != other.scale) {
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
			if (visible != other.visible) {
				return false;
			}
			return true;
		}
		
	}
	
	/**
	 * Abstract base class for sensor (input) and control signal (output) configuration.
	 * 
	 * @author rkurmann
	 * @since Polysun 9.1
	 *
	 */
	public static abstract class Connection extends Exchange implements Serializable {
	
		private static final long serialVersionUID = 1L;

		/** Is this an analog or a digital value? */
		protected boolean analog;
		
		/** Is this input/output signal required in the GUI? */
		protected boolean required;
		
		/**
		 * Is this input/output set in the Polysun controller element GUI?
		 * 
		 * This field is not used for the configuration, but for returning the configuration of Polysun.
		 * 
		 * <code>true</code>, if the input/output is set, <code>false</code> if it is not set,
		 * <code>null</code> if this value has not been initialized.
		 */
		protected Boolean used;

		/**
		 * Constructor creating an input/output configuration.
		 * 
		 * Internal hint: The same order as in ControllerDisplayProperty() is used.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param unit Unit as defined in Polysun, e.g. "l/h".
		 * @param analog  Is this an analog or a digital value?
		 * @param required Is this input/output required in the GUI?
		 * @param used Is this input/output set in the Polysun controller element GUI?
		 * 		This field is not used for the configuration, but for returning the configuration of Polysun.
		 * 		<code>true</code>, if the input/output is set, <code>false</code> if it is not set,
		 * 		<code>null</code> if this value has not been initialized.
		 * @param tooltip Tooltip to show, or <code>null</code>. This tooltip overwrites the default translated tooltip derived from the key.
		 */
		protected Connection(String name, String unit, boolean analog, boolean required, Boolean used, String tooltip) {
			this.name = name;
			this.unit = unit;
			this.analog = analog;
			this.required = required;
			this.used = used;
			this.tooltip = tooltip;
		}

		/**
		 * Is this an analog or a digital value?
		 * @return <code>true</code> if the value is analog, <code>false</code> if the value is digital, i.e. 0/1
		 */
		public boolean isAnalog() {
			return analog;
		}
		
		/**
		 * Is this input/output signal required in the GUI?
		 * @return <code>true</code> if the value is mandatory
		 */
		public boolean isRequired() {
			return required;
		}
		
		/**
		 * Is this input/output set in the Polysun GUI?
		 * @return <code>true</code> if a sensor/control signal is set, <code>false</code> if not set or not initialized.
		 */
		public boolean isUsed() {
			return used != null && used; // Prevent NPE, null is treated as false here
		}

		/**
		 * Is this a sensor (input)?
		 * @return <code>true</code> for a sensor (input), <code>false</code> for a control signal (output).
		 */
		public abstract boolean isSensor();

		@Override
		public String toString() {
			return "Connection [name=" + name + ", unit=" + unit + ", analog=" + analog + ", required=" + required
					+ ", used=" + used + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (analog ? 1231 : 1237);
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((tooltip == null) ? 0 : tooltip.hashCode());
			result = prime * result + (required ? 1231 : 1237);
			result = prime * result + ((unit == null) ? 0 : unit.hashCode());
			result = prime * result + ((used == null) ? 0 : used.hashCode());
			return result;
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
			Connection other = (Connection) obj;
			if (analog != other.analog) {
				return false;
			}
			if (name == null) {
				if (other.name != null) {
					return false;
				}
			} else if (!name.equals(other.name)) {
				return false;
			}
			if (tooltip == null) {
				if (other.tooltip != null) {
					return false;
				}
			} else if (!tooltip.equals(other.tooltip)) {
				return false;
			}
			if (required != other.required) {
				return false;
			}
			if (unit == null) {
				if (other.unit != null) {
					return false;
				}
			} else if (!unit.equals(other.unit)) {
				return false;
			}
			if (used == null) {
				if (other.used != null) {
					return false;
				}
			} else if (!used.equals(other.used)) {
				return false;
			}
			return true;
		}

	}
	
	/**
	 * Sensor configuration class. Input of a plugin controller. E.g. a temperature of a pipe.
	 * @author rkurmann
	 * @since Polysun 9.1
	 *
	 */
	public static class Sensor extends Connection {

		private static final long serialVersionUID = 1L;

		/**
		 * Constructor creating an sensor configuration.
		 * This constructor should be used for the configuration in the plugin controller.
		 * 
		 * Internal hint: The same order as in ControllerDisplayProperty() is used.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param unit Unit as defined in Polysun, e.g. "l/h".
		 * @param analog  Is this sensor an analog or a digital value?
		 * @param required Is this sensor required in the GUI?
		 * @param tooltip Tooltip to show, or <code>null</code>. This tooltip overwrites the default translated tooltip derived from the key.
		 */
		public Sensor(String name, String unit, boolean analog, boolean required, String tooltip) {
			this(name, unit, analog, required, null, tooltip);
		}
		
		/**
		 * Constructor creating an sensor configuration.
		 * This constructor should be used for the configuration in the plugin controller.
		 * 
		 * Internal hint: The same order as in ControllerDisplayProperty() is used.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param unit Unit as defined in Polysun, e.g. "l/h".
		 * @param analog  Is this sensor an analog or a digital value?
		 * @param required Is this sensor required in the GUI?
		 */
		public Sensor(String name, String unit, boolean analog, boolean required) {
			this(name, unit, analog, required, null, null);
		}
		
		/**
		 * Constructor creating an sensor configuration.
		 * 
		 * This constructor will be used for the actual configuration of the plugin controller in Polysun.
		 * 
		 * Internal hint: The same order as in ControllerDisplayProperty() is used.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param unit Unit as defined in Polysun, e.g. "l/h".
		 * @param analog  Is this an analog or a digital value?
		 * @param required Is this sensor required in the GUI?
		 * @param used Is this sensor in the Polysun controller element GUI?
		 * 		This field is not used for the configuration, but for returning the configuration of Polysun.
		 * 		<code>true</code>, if the sensor is set, <code>false</code> if it is not set,
		 * 		<code>null</code> if this value has not been initialized.
		 * @param tooltip Tooltip to show, or <code>null</code>. This tooltip overwrites the default translated tooltip derived from the key.
		 */
		public Sensor(String name, String unit, boolean analog, boolean required, Boolean used, String tooltip) {
			super(name, unit, analog, required, used, tooltip);
		}
		
		/**
		 * Constructor creating an sensor configuration.
		 * 
		 * This constructor will be used for the actual configuration of the plugin controller in Polysun.
		 * 
		 * Internal hint: The same order as in ControllerDisplayProperty() is used.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param unit Unit as defined in Polysun, e.g. "l/h".
		 * @param analog  Is this an analog or a digital value?
		 * @param required Is this sensor required in the GUI?
		 * @param used Is this sensor in the Polysun controller element GUI?
		 * 		This field is not used for the configuration, but for returning the configuration of Polysun.
		 * 		<code>true</code>, if the sensor is set, <code>false</code> if it is not set,
		 * 		<code>null</code> if this value has not been initialized.
		 */
		public Sensor(String name, String unit, boolean analog, boolean required, Boolean used) {
			super(name, unit, analog, required, used, null);
		}
		
		@Override
		public boolean isSensor() {
			return true;
		}

	}
	
	/**
	 * Control signal configuration class. Output of a plugin controller. E.g. a status 0/1 for a pump.
	 * @author rkurmann
	 * @since Polysun 9.1
	 *
	 */
	public static class ControlSignal extends Connection{
		
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor creating an control signal configuration.
		 * 
		 * This constructor will be used for the actual configuration of the plugin controller in Polysun.
		 * 
		 * Internal hint: The same order as in ControllerDisplayProperty() is used.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param unit Unit as defined in Polysun, e.g. "l/h".
		 * @param analog  Is this an analog or a digital value?
		 * @param required Is this control signal required in the GUI?
		 * @param tooltip Tooltip to show, or <code>null</code>. This tooltip overwrites the default translated tooltip derived from the key.
		 */
		public ControlSignal(String name, String unit, boolean analog, boolean required, String tooltip) {
			this(name, unit, analog, required, null, tooltip);
		}
		
		/**
		 * Constructor creating an control signal configuration.
		 * 
		 * This constructor will be used for the actual configuration of the plugin controller in Polysun.
		 * 
		 * Internal hint: The same order as in ControllerDisplayProperty() is used.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param unit Unit as defined in Polysun, e.g. "l/h".
		 * @param analog  Is this an analog or a digital value?
		 * @param required Is this control signal required in the GUI?
		 */
		public ControlSignal(String name, String unit, boolean analog, boolean required) {
			this(name, unit, analog, required, null, null);
		}
		
		/**
		 * Constructor creating an control signal configuration.
		 * 
		 * This constructor will be used for the actual configuration of the plugin controller in Polysun.
		 * 
		 * Internal hint: The same order as in ControllerDisplayProperty() is used.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param unit Unit as defined in Polysun, e.g. "l/h".
		 * @param analog  Is this an analog or a digital value?
		 * @param required Is this control signal required in the GUI?
		 * @param used Is this control signal set in the Polysun controller element GUI?
		 * 		This field is not used for the configuration, but for returning the configuration of Polysun.
		 * 		<code>true</code>, if the control signal is set, <code>false</code> if it is not set,
		 * 		<code>null</code> if this value has not been initialized.
		 * @param tooltip Tooltip to show, or <code>null</code>. This tooltip overwrites the default translated tooltip derived from the key.
		 */
		public ControlSignal(String name, String unit, boolean analog, boolean required, Boolean used, String tooltip) {
			super(name, unit, analog, required, used, tooltip);
		}
		
		/**
		 * Constructor creating an control signal configuration.
		 * 
		 * This constructor will be used for the actual configuration of the plugin controller in Polysun.
		 * 
		 * Internal hint: The same order as in ControllerDisplayProperty() is used.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param unit Unit as defined in Polysun, e.g. "l/h".
		 * @param analog  Is this an analog or a digital value?
		 * @param required Is this control signal required in the GUI?
		 * @param used Is this control signal set in the Polysun controller element GUI?
		 * 		This field is not used for the configuration, but for returning the configuration of Polysun.
		 * 		<code>true</code>, if the control signal is set, <code>false</code> if it is not set,
		 * 		<code>null</code> if this value has not been initialized.
		 */
		public ControlSignal(String name, String unit, boolean analog, boolean required, Boolean used) {
			super(name, unit, analog, required, used, null);
		}
		
		@Override
		public boolean isSensor() {
			return false;
		}

	}
	
	/**
	 * The Log class configures display properties that are shown in the simulation analysis or log and parameterize.
	 * 
	 * @author rkurmann
	 * @since Polysun 9.1
	 */
	public static class Log extends Exchange implements Serializable {
		
		private static final long serialVersionUID = 1L;

		/**
		 * Constructs a log with name and unit.
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 * @param unit Unit as defined in Polysun, e.g. "l/h".
		 */
		public Log(String name, String unit) {
			this.name = name;
			this.unit = unit;
		}
		
		/**
		 * Constructs a log with name.
		 * 
		 * @param name Name of the property, either a human readable text (or internally, a Polysun translation key).
		 */
		public Log(String name) {
			this(name, null);
		}
		
		/**
		 * Constructs a log without name and unit.
		 */
		public Log() {
			this(null, null);
		}

		@Override
		public String toString() {
			return "Log [name=" + name + ", unit=" + unit + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((unit == null) ? 0 : unit.hashCode());
			return result;
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
			Log other = (Log) obj;
			if (name == null) {
				if (other.name != null) {
					return false;
				}
			} else if (!name.equals(other.name)) {
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

		
	}

	
}

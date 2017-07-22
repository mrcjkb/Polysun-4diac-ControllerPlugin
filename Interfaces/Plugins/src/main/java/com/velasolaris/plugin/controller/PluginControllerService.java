package com.velasolaris.plugin.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.velasolaris.plugin.WildcardClassLoader;
import com.velasolaris.plugin.controller.spi.ControllerPlugin;
import com.velasolaris.plugin.controller.spi.IPluginController;
import com.velasolaris.plugin.controller.spi.PluginControllerException;

/**
 * Singleton service returning all plugin controllers that are available through controllers plugins.
 * 
 * Reference:
 * <ul>
 * 	<li>http://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html
 * 	<li>https://docs.oracle.com/javase/tutorial/ext/basics/spi.html
 * 	<li>http://stackoverflow.com/questions/465099/best-way-to-build-a-plugin-system-with-java
 * </ul>
 * 
 * @see IPluginController
 * @see ControllerPlugin
 * @see PluginControllerService
 * see FlowratePluginController

 * @author rkurmann
 * @since Polysun 9.1
 *
 */
public class PluginControllerService {
	
	/** Static instance of the Logger for this class */
	protected static Logger sLog = Logger.getLogger(PluginControllerService.class.getName());

	/** Singleton. */
	private static PluginControllerService service;
	
	/** Root path to the plugins. Setting in init(). */
	private static String rootPath;
	/** Default classpath to the plugins, e.g. "*.jar" relative to rootPath. Setting in init(). */
	private static String defaultClasspathPattern;
	
	/** Java Service loader to dynamically load controller plugins. */
	private ServiceLoader<ControllerPlugin> controllerPluginLoader;
	
	/** The ClassLoader to use the plugins. */
	private WildcardClassLoader classLoader;

	/** Controllers cache. */
	private List<PluginControllerDefinition> controllers;
	
	/**
	 * Returns the singleton.
	 * @return the signleton
	 */
	public static synchronized PluginControllerService getInstance() {
		if (service == null) {
			service = new PluginControllerService();
		}
		return service;
	}
	
	/**
	 * Init method set settings fo the singleton, i.e. the root path where the plugins should be loaded from.
	 * @param rootPath the root path where the plugins should be loaded from
	 */
	public static synchronized void init(String rootPath) {
		PluginControllerService.rootPath = rootPath;
	}
	
	/**
	 * Init method set settings fo the singleton, i.e. the root path where the plugins should be loaded from.
	 * @param rootPath the root path where the plugins should be loaded from
	 * @param defaultClasspathPattern the classpath to load at runtime the plugins, can contain *, e.g. "*.jar"
	 * 
	 * @see WildcardClassLoader#addClassPath(String)
	 */
	public static synchronized void init(String defaultClasspathPattern, String rootPath) {
		PluginControllerService.rootPath = rootPath;
		PluginControllerService.defaultClasspathPattern = defaultClasspathPattern;
	}
	
	/** Hidden constructor for singleton. */
	private PluginControllerService() {
		classLoader = new WildcardClassLoader(defaultClasspathPattern, rootPath, getClass().getClassLoader());
		controllerPluginLoader = ServiceLoader.load(ControllerPlugin.class, classLoader);
	}
	
	/**
	 * Loads all configured controller plugins and returns the enabled plugin controllers as a list.
	 * 
	 * Not enabled plugins and controllers are ignored.
	 * 
	 * @param reloadPlugins if <code>true</code>, plugins are looked up again in the classpath
	 * @param parameters Generic parameters
	 * 
	 * @return list of plugin controller definitions
	 * @throws PluginControllerException For any problem with the automatic service loading
	 */
	public List<PluginControllerDefinition> getPluginControllers(boolean reloadPlugins, Map<String, Object> parameters) throws PluginControllerException {
		try {
			if (reloadPlugins) {
				classLoader.reload();
				controllerPluginLoader.reload();
				controllers = null;
			}
			if (controllers == null) {
				controllers = new ArrayList<>();
				for (ControllerPlugin plugin : controllerPluginLoader) {
					if (plugin.isEnabled(parameters)) {
						List<Class<? extends IPluginController>> newControllers = plugin.getControllers(parameters);
						if (newControllers != null) {
							for (Class<? extends IPluginController> controller : newControllers) {
								PluginControllerDefinition controllerDefinition = new PluginControllerDefinition(controller, plugin);
								if (controllerDefinition.isEnabled(parameters)) {
									if (!controllers.contains(controllerDefinition)) {
										controllers.add(controllerDefinition);
									} else {
										sLog.warning("Plugin controllers with the same id " + controllerDefinition.getId() + ", first " + controllers.get(controllers.indexOf(controllerDefinition.getId())) + ", second " + controllerDefinition);
									}
								}
							}
						}
					}
				}
			}
			return Collections.unmodifiableList(controllers);
		} catch (ServiceConfigurationError e) {
			throw new PluginControllerException("Could not load plugin controllers", e);
		}
	}
	
	/**
	 * Reloads a plugin controller definition.
	 * This method must be called during deserialization of a variant.
	 * Custom code must be refreshed.
	 * 
	 * @param controllerToReload Plugin controller to reload
	 * @param parameters Generic parameters
	 * @param reloadPlugins if <code>true</code>, plugins are looked up again in the classpath
	 * @return Refreshed controllerToReload definition
	 * @throws PluginControllerException For any problem with the controller plugin reloading
	 */
	public PluginControllerDefinition relaod(PluginControllerDefinition controllerToReload, boolean reloadPlugins, Map<String, Object> parameters) throws PluginControllerException {
		List<PluginControllerDefinition> pluginControllers = getPluginControllers(reloadPlugins, parameters);
		for (PluginControllerDefinition controller : pluginControllers) {
			if (controller.equals(controllerToReload)) {
				controllerToReload.setControllerClass(controller.getControllerClass());
				controllerToReload.setPlugin(controller.getPlugin());
				return controllerToReload;
			}
		}
		return null;
	}
	
	/**
	 * For testing purposes.
	 * @param args command line arguments, they will be ignored
	 * @throws PluginControllerException for plugin problems
	 */
	public static void main(String... args) throws PluginControllerException {
		System.out.println("Available plugin controllers:");
		System.out.println("" + PluginControllerService.getInstance().getPluginControllers(false, null));
	}
	
	/**
	 * Class representing a plugin controller.
	 * 
	 * Classes and controllers are transient in order to avoid ClassNotFoundException.
	 * Nothing of external classes must be serialized. All external classes must be reloaded at deserialization.
	 * 
	 * @author rkurmann
	 * @since Polysun 9.1
	 *
	 */
	public static class PluginControllerDefinition implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		/** Technical ID of the plugin controller, i.e. the class name of the plugin controller. */
		private String id;
		/** Store the plugin controller name in order to have it if there is deserialization problem. Reset after deserialization. */
		private String name;
		/** Store the plugin controller simple class name in order to have it if there is deserialization problem. Reset after deserialization. */
		private String technicalName;
		/** Class of the plugin controller as returned by the plugin. */
		private transient Class<? extends IPluginController> controllerClass;
		/** Private instance of the plugin controller in order to get name and other metadata. As well as to test instantiation. Reset after deserialization. */
		private transient IPluginController controller;
		/** Reference to the plugin where the plugin controller is contained. Reset after deserialization. */
		private transient ControllerPlugin plugin;
		
		/** Exception occurred during deserialization (reload of variant). Is <code>null</code> if no exception occurred. */
		private transient PluginControllerException reloadException;
		
		/**
		 * Constructor.
		 * 
		 * @param pluginControllerClass Plugin controller
		 * @param plugin Enclosing controller plugin
		 */
		/*package private*/ PluginControllerDefinition(Class<? extends IPluginController> pluginControllerClass, ControllerPlugin plugin) throws PluginControllerException {
			id = pluginControllerClass.getName(); // Set only once at construction, do not reload the id
			technicalName = pluginControllerClass.getSimpleName(); // Set only once at construction, do not reload the technicalName
			setControllerClass(pluginControllerClass);
			setPlugin(plugin);
		}
		
		/**
		 * Returns the technical ID of the plugin controller, i.e. the class name of the plugin controller.
		 * 
		 * This id is used for equality tests.
		 * 
		 * @return Technical id (String representation of the class name)
		 */
		public String getId() {
			return id;
		}

		/**
		 * Returns the name of plugin controller as specified by the plugin controller.
		 * @return name of the controller
		 */
		public String getName() {
			return name;
		}

		/**
		 * Returns the description of plugin controller as specified by the plugin controller.
		 * @return description of the controller
		 */
		public String getDescription() {
			if (controller.getDescription() == null) {
				return null;
			} else {
				return controller.getDescription();
			}
		}
		
		/**
		 * Returns the technical name of plugin controller (simple class name)
		 * @return technical name of the controller (simple class name)
		 */
		public String getTechnicalName() {
			return technicalName;
		}
		
		/**
		 * Is this plugin controller enabled?
		 * 
		 * @param parameters Generic parameters
		 * @return <code>true</code> if enabled, otherwise <code>false</code>
		 */
		public boolean isEnabled(Map<String, Object> parameters) {
			return controller.isEnabled(parameters);
		}

		/**
		 * Factory method to create a new plugin controller.
		 * 
		 * @exception PluginControllerException For any creation problem
		 * @return a new instance plugin controller instance
		 */
		public IPluginController createPluginController() throws PluginControllerException {
			if (reloadException != null) {
				throw reloadException;
			}
			try {
				return controllerClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new PluginControllerException("Could not create plugin controller: " + id, e);
			}
		}

		/**
		 * Returns the plugin controller class.
		 * 
		 * @return Plugin controller class, or <code>null</code> if not reloaded properly
		 */
		public Class<? extends IPluginController> getControllerClass() {
			return controllerClass;
		}
		
		/**
		 * Get the enveloping plugin.
		 * 
		 * @return Controller plugin, or <code>null</code> if not reloaded properly
		 */
		public ControllerPlugin getPlugin() {
			return plugin;
		}
		
		/**
		 * Returns the name of the jar which contains the plugin controller.
		 * 
		 * @return the name or an empty string if plugin controller is not in a jar (e.g. run in Eclipse)
		 */
		public String getPluginJarName() {
			if (controllerClass != null) {
				String urlPath = controllerClass.getResource(controllerClass.getSimpleName() + ".class").toString();
				Matcher matcher = Pattern.compile("([^/]+)\\.jar!").matcher(urlPath);
				if (matcher.find()) {
					return matcher.group(1);
				}
			}
			return "";
		}
		
		/**
		 * Returns the name of the plugin which contains the plugin controller.
		 * 
		 * @return the name or an empty string if not set
		 */
		public String getPluginName() {
			if (plugin != null && plugin.getName() != null && !"".equals(plugin.getName())) {
				return plugin.getName();
			} else {
				return "";
			}
		}
		
		/**
		 * Returns the description of the plugin which contains the plugin controller.
		 * 
		 * @return the description or an empty string if not set
		 */
		public String getPluginDescription() {
			if (plugin != null && plugin.getDescription() != null && !"".equals(plugin.getDescription())) {
				return plugin.getDescription();
			} else {
				return "";
			}
		}
		
		/**
		 * Returns the description of the plugin which contains the plugin controller.
		 * 
		 * @return the description or an empty string if not set
		 */
		public String getPluginCreator() {
			if (plugin != null && plugin.getCreator() != null && !"".equals(plugin.getCreator())) {
				return plugin.getCreator();
			} else {
				return "";
			}
		}
		
		/**
		 * Returns the name of the plugin which contains the plugin controller.
		 * 
		 * @return the name or an empty string if not set
		 */
		public String getPluginVersion() {
			if (plugin != null && plugin.getVersion() != null && !"".equals(plugin.getVersion())) {
				return plugin.getVersion();
			} else {
				return "";
			}
		}
		
		/**
		 * Returns the supported interface version of the plugin which contains the plugin controller.
		 * 
		 * @param parameters General parameters
		 * @return 1 for Polysun 9.1 interface version
		 */
		public int getPluginSupportedInterfaceVersion(Map<String, Object> parameters) {
			if (plugin != null) {
				return plugin.getSupportedInterfaceVersion(parameters);
			} else {
				return -1;
			}
		}
		
		/**
		 * Returns the supported interface version of the plugin which contains the plugin controller.
		 * 
		 * @param parameters General parameters
		 * @return 1 for Polysun 9.1 interface version
		 */
		public boolean isPluginEnabled(Map<String, Object> parameters) {
			if (plugin != null) {
				return plugin.isEnabled(parameters);
			} else {
				return false;
			}
		}
		
		/**
		 * Sets the controller class, creates a controller instance and updates stored fields.
		 * 
		 * Used for reload.
		 * 
		 * @param controllerClass controller class to set
		 * @throws PluginControllerException For any problems while creating the controller instance
		 */
		/*package private*/ void setControllerClass(Class<? extends IPluginController> controllerClass) throws PluginControllerException {
			this.controllerClass = controllerClass;
			controller = createPluginController();
			name = controller.getName();
			if (name == null || "".equals(name)) {
				throw new PluginControllerException("Plugin controller name must not be empty or null");
			}
		}
		
		/**
		 * Sets the reference to the enclosing plugin.
		 * 
		 * @param plugin The plugin belonging to the plugin controller.
		 */
		/*package private*/ void setPlugin(ControllerPlugin plugin) {
			this.plugin = plugin;
		}
		
		/**
		 * Deserialization.
		 * Reload controller plugin definition.
		 * 
		 * @param inputStream input stream to deserialize
		 * @throws IOException for any I/O problems
		 * @throws ClassNotFoundException for classes not found
		 */
		private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
			inputStream.defaultReadObject();
			
			try {
				if (PluginControllerService.getInstance().relaod(this, false, null) == null) {
					throw new PluginControllerNotFoundException("Plugin controller could not be found for reload: " + getId());
				}
			} catch (PluginControllerException e) {
				// Store exception for later handling
				reloadException = e;
			}
		}
		
		@Override
		public String toString() {
			return "PluginControllerDefinition [controllerName=" + this.getName() + ", id="
					+ this.getId() + ", pluginJar=" + this.getPluginJarName() + "]";
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
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
			PluginControllerDefinition other = (PluginControllerDefinition) obj;
			if (this.id == null) {
				if (other.id != null) {
					return false;
				}
			} else if (!this.id.equals(other.id)) {
				return false;
			}
			return true;
		}
	}
	
	
}

package com.velasolaris.plugin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.apache.commons.io.filefilter.WildcardFileFilter;


/**
 * ClassLoader to load classes from jars or directories. Wildcard * is supported for jars and class files.
 * Classes from jars and directories can be loaded at runtime.
 * 
 * For easy usage of plugins, user should be able to place the plugin jars in a user writable directory.
 * The Polysun data folder is set in the installation setup and is not known in advance. Thus,
 * we cannot add the data/plugins folder to the classpath of Polysun.
 * intallj4 .vmoptions setting
 * <code>-classpath/a ${installer:dataInstallDir}/plugins/*</code> does not work, since
 * only classes and not jars are added.
 * 
 * However, this classloader allows to reload plugin jars at runtime.
 * 
 * Ref. http://stackoverflow.com/questions/402330/is-it-possible-to-add-to-classpath-dynamically-in-java
 * @author rkurmann
 * @since Polysun 9.1
 *
 */
public class WildcardClassLoader extends URLClassLoader {

	/**
	 * Inherited constructor.
	 * 
     * @param urls the URLs from which to load classes and resources
     * @param parent the parent class loader for delegation
     * @param factory the URLStreamHandlerFactory to use when creating URLs
     * 
	 * @see URLClassLoader#URLClassLoader(URL[], ClassLoader, URLStreamHandlerFactory)
	 */
	public WildcardClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
		super(urls, parent, factory);
	}

	/**
	 * Inherited constructor.
	 * 
     * @param urls the URLs from which to load classes and resources
     * @param parent the parent class loader for delegation
     * 
	 * @see URLClassLoader#URLClassLoader(URL[], ClassLoader)
	 */
	public WildcardClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	/**
	 * Inherited constructor.
	 * 
     * @param urls the URLs from which to load classes and resources
     * 
	 * @see URLClassLoader#URLClassLoader(URL[])
	 */
	public WildcardClassLoader(URL[] urls) {
		super(urls);
	}

	/**
	 * Creates a JarClassLoader for a classpath relative to a rootPath.
	 * 
	 * @param classpath e.g. *.jar
	 * @param rootPath e.g. plugins
	 * @param parent the default classloader
	 * 
	 * @see #addClassPath(String)
	 */
	public WildcardClassLoader(String classpath, String rootPath, ClassLoader parent) {
		super(new URL[0], parent);
		this.rootPath = rootPath;
		this.defaultClasspath = classpath;
		addClassPath(classpath);
	}
	
	/**
	 * Creates a JarClassLoader for a classpath relative to a rootPath.
	 * 
	 * @param classpath e.g. *.jar
	 * @param rootPath e.g. plugins
	 * 
	 * @see #addClassPath(String)
	 */
	public WildcardClassLoader(String classpath, String rootPath) {
		super(new URL[0]);
		this.rootPath = rootPath;
		this.defaultClasspath = classpath;
		addClassPath(classpath);
	}
	
	/**
	 * Creates a JarClassLoader for a classpath relative to a rootPath.
	 * 
	 * @param rootPath e.g. plugins
	 * 
	 * @see #addClassPath(String)
	 */
	public WildcardClassLoader(String rootPath) {
		super(new URL[0]);
		this.rootPath = rootPath;
	}
	
	/**
	 * Creates a JarClassLoader relative to a rootPath.
	 * 
	 * @param rootPath e.g. plugins
	 * @param parent the default classloader
	 * 
	 * @see #addClassPath(String)
	 */
	public WildcardClassLoader(String rootPath, ClassLoader parent) {
		super(new URL[0], parent);
		this.rootPath = rootPath;
	}
	
	/**
	 * Default constructor without any classpath.
	 */
	public WildcardClassLoader() {
		super(new URL[0]);
	}
	
	/** Static instance of the Logger for this class */
	protected static Logger sLog = Logger.getLogger(WildcardClassLoader.class.getName());
	
	String defaultClasspath;
	String classPath = "";
	String rootPath;
	List<String> classpathElements = new ArrayList<>();

	/**
	 * Reloads from JarClassloader classpath again.
	 * Useful if the classpath contains *. 
	 */
	public void reload() {
		addClassPath(defaultClasspath);
	}
	
	/**
	 * Add classPath to this loader's classpath.
	 * <p>
	 * The classpath may contain elements that include a generic file base name.
	 * A generic basename is a filename without the extension that may begin
	 * and/or end with an asterisk. Use of the asterisk denotes a partial match.
	 * Any files with an extension of ".jar" whose base name match the specified
	 * basename will be added to this class loaders classpath. The case of the
	 * filename is ignored. For example "/somedir/*abc" means all files in
	 * somedir that end with "abc.jar", "/somedir/abc*" means all files that
	 * start with "abc" and end with ".jar", and "/somedir/*abc*" means all
	 * files that contain "abc" and end with ".jar".
	 *
	 * @param additionalClasspath the classpath to add to load classes from, e.g. *.jar (relative to a root folder)
	 */
	public void addClassPath(String additionalClasspath) {
		
		if (additionalClasspath == null) {
			return;
		}
		
		String seps = File.pathSeparator; // separators

		// Want to accept both system separator and ';'
		if (!File.pathSeparator.equals(";")) {
			seps += ";";
		} 
		
		for (StringTokenizer st = new StringTokenizer(additionalClasspath, seps, false); st.hasMoreTokens();) {
			String token = st.nextToken();
			String classpathLastFileName = null;

			if (token.length() == 0) {
				continue;
			}

			File classpathFile = new File(token);
			if (classpathFile.getName().indexOf('*') != -1) {
				classpathLastFileName = classpathFile.getName();
				classpathFile = classpathFile.getParentFile();
			}

			if (classpathFile == null && rootPath != null) {
				classpathFile = new File(rootPath);
			} else if ((classpathFile == null || !classpathFile.isAbsolute() && token.charAt(0) != '/' && token.charAt(0) != '\\')  && rootPath != null) {
				classpathFile = new File(rootPath, classpathFile.getPath());
			}
			
			try {
				classpathFile = classpathFile.getCanonicalFile();
			} catch (IOException e) {
				sLog.fine("Skipping non-existent classpath element '" + classpathFile + "' (" + e + ").");
				continue;
			}
			if (classpathLastFileName != null && !"".equals(classpathLastFileName)) {
				classpathFile = new File(classpathFile, classpathLastFileName);
			}

			if (classpathLastFileName != null && !"".equals(classpathLastFileName)) {
				addJars(classpathFile.getParentFile(), classpathLastFileName);
			} else if (!classpathFile.exists()) { // s/never be due getCanonicalFile() above
				sLog.fine("Could not find classpath element '" + classpathFile + "'");
			} else if (classpathFile.isDirectory()) {
				addURL(createUrl(classpathFile));
			} else if (classpathFile.getName().toLowerCase().endsWith(".zip") || classpathFile.getName().toLowerCase().endsWith(".jar")) {
				addURL(createUrl(classpathFile));
			} else {
				sLog.fine("ClassPath element '" + classpathFile
						+ "' is not an existing directory and is not a file ending with '.zip' or '.jar'");
			}
		}
		sLog.fine("Class loader is using classpath: \"" + classPath + "\".");
	}

	/**
	 * Adds a set of JAR files using a generic base name to this loader's
	 * classpath. See @link:addClassPath(String) for details of the generic base
	 * name.
	 * @param dir the directory of the jar
	 * @param name jar name, incl. .jar, e.g. *.jar
	 */
	public void addJars(File dir, String name) {
		String[] jars; // matching jar files

		if (!dir.exists()) {
			sLog.severe("Could not find directory for Class Path element '" + dir + File.separator + name + ".jar'");
			return;
		}
		if (!dir.canRead()) {
			sLog.severe("Could not read directory for Class Path element '" + dir + File.separator + name + ".jar'");
			return;
		}

		// http://stackoverflow.com/questions/794381/how-to-find-files-that-match-a-wildcard-string-in-java
		WildcardFileFilter filter = new WildcardFileFilter(name);
		if ((jars = dir.list(filter)) == null) {
			sLog.severe("Error accessing directory for Class Path element '" + dir + File.separator + name + ".jar'");
		} else if (jars.length == 0) {
			sLog.fine("No JAR files match specification '" + new File(dir, name) + ".jar'");
		} else {
			 sLog.fine("Adding files matching specification '" + dir + File.separator + name + ".jar'");
			Arrays.sort(jars, String.CASE_INSENSITIVE_ORDER);
			for (int i = 0; i < jars.length; i++) {
				addURL(createUrl(new File(dir, jars[i])));
			}
		}
	}

	/**
	 * Add classpath url from file to our classpath.
	 * Necessary, since ClassLoader operate with URLs.
	 * 
	 * @param file the file to add to the classpath.
	 * @return the URL of the file
	 */
	private URL createUrl(File file) {
		try {
			URL url = file.toURI().toURL();
			sLog.fine("Added URL: '" + url.toString() + "'");
			if (classPath.length() > 0) {
				classPath += File.pathSeparator;
			}
			this.classPath += file.getPath();
			return url;
		} catch (MalformedURLException thr) {
			sLog.fine("Classpath element '" + file + "' could not be used to create a valid file system URL");
			return null;
		}
	}
	
	
}

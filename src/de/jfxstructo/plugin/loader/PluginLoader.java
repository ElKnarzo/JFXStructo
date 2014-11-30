package de.jfxstructo.plugin.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import de.jfxstructo.plugin.JARFileFilter;
import de.jfxstructo.plugin.interfaces.Pluggable;

public class PluginLoader {

	public static LinkedHashMap<String,Pluggable> loadPlugins(File plugDir) throws IOException {

		File[] plugJars = plugDir.listFiles(new JARFileFilter());
		ClassLoader cl = new URLClassLoader(PluginLoader.fileArrayToURLArray(plugJars));
		LinkedHashMap<String,Class<Pluggable>> plugClasses = PluginLoader.extractClassesFromJARs(plugJars, cl);
		return PluginLoader.createPluggableObjects(plugClasses);
//		return plugClasses;
	}

	private static URL[] fileArrayToURLArray(File[] files)
			throws MalformedURLException {

		URL[] urls = new URL[files.length];
		for (int i = 0; i < files.length; i++) {
			urls[i] = files[i].toURI().toURL();
		}
		return urls;
	}

	private static LinkedHashMap<String,Class<Pluggable>> extractClassesFromJARs(File[] jars,
			ClassLoader cl) throws IOException {

		LinkedHashMap<String,Class<Pluggable>> classes = new LinkedHashMap<String, Class<Pluggable>>();
		for (File jar : jars) {
			classes.putAll(PluginLoader.extractClassesFromJAR(jar, cl));
		}
		return classes;
	}

	@SuppressWarnings("unchecked")
	private static LinkedHashMap<String,Class<Pluggable>> extractClassesFromJAR(File jar, ClassLoader cl) throws IOException {

		LinkedHashMap<String,Class<Pluggable>> classes = new LinkedHashMap<String, Class<Pluggable>>();
		JarInputStream jaris = new JarInputStream(new FileInputStream(jar));
		JarEntry ent = null;
		while ((ent = jaris.getNextJarEntry()) != null) {
			if (ent.getName().toLowerCase().endsWith(".class")) {
				try {
					Class<?> cls = cl.loadClass(ent.getName().substring(0, ent.getName().length() - 6).replace('/', '.'));
					if (PluginLoader.isPluggableClass(cls)) {
						classes.put(cls.getName(),(Class<Pluggable>)cls);
					}
				} catch (ClassNotFoundException e) {
					System.err.println("Can't load Class " + ent.getName());
					e.printStackTrace();
				}
			}
		}
		jaris.close();
		return classes;
	}

	private static boolean isPluggableClass(Class<?> cls) {

		for (Class<?> i : cls.getInterfaces()) {
			if (i.equals(Pluggable.class)) {
				return true;
			}
		}
		return false;
	}

	private static LinkedHashMap<String,Pluggable> createPluggableObjects(LinkedHashMap<String,Class<Pluggable>> pluggables) {

		LinkedHashMap<String,Pluggable> plugs = new LinkedHashMap<String,Pluggable>(pluggables.size());
		Set<String> keys = pluggables.keySet();
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			Class<Pluggable> plug = pluggables.get(iterator.next()) ;
			try {
				plugs.put(plug.getName(), plug.newInstance());
			} catch (InstantiationException e) {
				System.err.println("Can't instantiate plugin: "	+ plug.getName());
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				System.err.println("IllegalAccess for plugin: " + plug.getName());
				e.printStackTrace();
			}
		}
		return plugs;
	}
}
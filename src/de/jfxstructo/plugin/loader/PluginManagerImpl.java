package de.jfxstructo.plugin.loader;

import de.jfxstructo.plugin.interfaces.PluginManager;

public class PluginManagerImpl implements PluginManager {

	@Override
	public void showVisualMessage(String message) {
		System.out.println(message);
	}
}
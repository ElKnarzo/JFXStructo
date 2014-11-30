package de.jfxstructo.plugin.interfaces;

import javafx.stage.Stage;

public interface Pluggable {

	boolean start();

	boolean stop();

	void setStage(Stage stage);

	void setPluginManager(PluginManager manager);
}
package de.jfxstructo.gui;


import javafx.application.Application;
import javafx.stage.Stage;
import de.jfxstructo.Styler;
import de.jfxstructo.config.Configuration;
import de.jfxstructo.gui.controller.MainController;

public class JFXStructo extends Application {

	private static Stage stage;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		JFXStructo.stage = stage;

		Configuration.loadConfig();

		MainController.initStage(stage, "/de/jfxstructo/gui/fxml/JFXStructo.fxml", new MainController(), true);
		stage.setTitle("JFXStructo");
		stage.centerOnScreen();

		Styler.loadStyle(stage);
		stage.show();
	}

	public static Stage getPrimaryStage() {
		return stage;
	}

}

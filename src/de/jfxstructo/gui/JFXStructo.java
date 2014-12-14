package de.jfxstructo.gui;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import de.jfxstructo.Styler;
import de.jfxstructo.config.Configuration;
import de.jfxstructo.config.Resolution;
import de.jfxstructo.gui.views.JFXStructoView;

public class JFXStructo extends Application {

	private static Stage stage;
	private static Resolution res = (Resolution) Configuration.getConfig("resolution");

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

		stage.setTitle("JFXStructo");
		stage.centerOnScreen();

		stage.setHeight(res.getHeight());
		stage.setWidth(res.getWidth());
		stage.setOnCloseRequest(closeEvent);

		Styler.loadStyle(stage);

		stage.setScene(new Scene(new JFXStructoView()));
		stage.show();
	}

	@Override
	public void stop() {
		res.setDimension(stage.getWidth(), stage.getHeight());
		Configuration.saveConfig();
	}

	public static Stage getPrimaryStage() {
		return stage;
	}


	private final EventHandler<WindowEvent> closeEvent = new EventHandler<WindowEvent>() {
		@Override
		public void handle(WindowEvent arg0) {
			stop();
		}
	};

}

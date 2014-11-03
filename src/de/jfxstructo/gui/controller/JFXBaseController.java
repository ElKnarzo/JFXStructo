package de.jfxstructo.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import de.jfxstructo.Globals;
import de.jfxstructo.Styler;
import de.jfxstructo.config.Configuration;
import de.jfxstructo.config.Resolution;

public abstract class JFXBaseController implements Initializable {

	protected URL location;
	protected ResourceBundle resources;
	
	private static Resolution res = (Resolution) Configuration.getConfig("resolution");
			
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.location = location;
		this.resources = resources;
	}
	
	protected void changeStylesheet(Stage stage, String... stylePath) {
		stage.getScene().getStylesheets().clear();
		for (String string : stylePath) {
			stage.getScene().getStylesheets().add(getClass().getResource(string).toExternalForm());
		}
	}
	
	public static void initStage(final Stage stage, String systemPath, JFXBaseController controller, boolean isPrimary) {
		
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(Globals.getResourceBundle());
			loader.setLocation(JFXBaseController.class.getResource(systemPath));
			loader.setController(controller);
			loader.setBuilderFactory(new JavaFXBuilderFactory());

			Parent p = (Parent) loader.load();
			stage.setScene(new Scene(p));

			Styler.loadStyle(stage);
			
			if (isPrimary) {
				stage.setHeight(res.getHeight());
				stage.setWidth(res.getWidth());
				
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent arg0) {
						res.setDimension(stage.getWidth(), stage.getHeight());
						Configuration.saveConfig();
						System.exit(0);
					}
				});
			}
			
		} catch (Exception ex) {
	        Logger.getLogger(JFXBaseController.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}

}

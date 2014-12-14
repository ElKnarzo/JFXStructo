package de.jfxstructo.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialogs;
import javafx.stage.Stage;

import org.slf4j.LoggerFactory;

import de.jfxstructo.Globals;
import de.jfxstructo.Styler;
import de.jfxstructo.gui.JFXStructo;

public abstract class JFXBaseController implements Initializable {

	protected URL location;
	protected ResourceBundle resources;


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

	public void initStage(final Stage stage, String systemPath) {

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(Globals.getResourceBundle());
			loader.setLocation(JFXBaseController.class.getResource(systemPath));
			loader.setController(this);
			loader.setBuilderFactory(new JavaFXBuilderFactory());

			Parent p = (Parent) loader.load();
			stage.setScene(new Scene(p));

			Styler.loadStyle(stage);

		} catch (Exception ex) {
			LoggerFactory.getLogger(JFXBaseController.class).error(null, ex);
			Dialogs.showErrorDialog(JFXStructo.getPrimaryStage(), "Can't load Scene", null, null, ex);
		}
	}

}

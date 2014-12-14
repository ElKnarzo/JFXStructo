package de.jfxstructo.gui.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import org.slf4j.LoggerFactory;

import de.jfxstructo.Globals;
import de.jfxstructo.gui.JFXStructo;
import de.jfxstructo.gui.controller.JFXBaseController;

public class JFXStructoStatusBar extends HBox {

	@FXML
	private Label leftStatus, rightStatus;

	public JFXStructoStatusBar() {

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(Globals.getResourceBundle());
			loader.setLocation(JFXBaseController.class.getResource("/de/jfxstructo/gui/fxml/JFXStructoStatusBar.fxml"));
			loader.setRoot(this);
			loader.setController(this);
			loader.setBuilderFactory(new JavaFXBuilderFactory());

			loader.load();

		} catch (Exception e) {
			LoggerFactory.getLogger(JFXStructoStatusBar.class).error(null, e);
			Dialogs.showErrorDialog(JFXStructo.getPrimaryStage(), "Can't load Scene", null, null, e);
		}
	}

	public void setLeftStatus(String string) {
		leftStatus.setText(string);
	}

}

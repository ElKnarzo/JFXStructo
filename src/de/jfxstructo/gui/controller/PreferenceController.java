package de.jfxstructo.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;

import com.aquafx_project.controls.skin.styles.TabPaneType;

import de.jfxstructo.Styler;

public class PreferenceController extends JFXBaseController {

	@FXML
	private TabPane root;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		Styler.changeTabPaneStyle(root, TabPaneType.ICON_BUTTONS);
	}

}

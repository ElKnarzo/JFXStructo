package de.jfxstructo.gui.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import de.jfxstructo.Diagram;

public class EditorPaneTab extends Tab {

	@FXML
	private AnchorPane diagramPane;

	private Diagram diagram;

	public EditorPaneTab() {

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/de/jfxstructo/gui/fxml/EditorTab.fxml"));
			loader.setRoot(this);
			loader.setController(this);
			loader.setBuilderFactory(new JavaFXBuilderFactory());

			loader.load();

			diagram = new Diagram();
			diagramPane.getChildren().add(diagram);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Diagram getDiagram() {
		return diagram;
	}



}

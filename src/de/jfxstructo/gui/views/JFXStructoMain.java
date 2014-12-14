package de.jfxstructo.gui.views;

import java.util.HashMap;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import org.slf4j.LoggerFactory;

import de.jfxstructo.Diagram;
import de.jfxstructo.Globals;
import de.jfxstructo.elements.AElement;
import de.jfxstructo.gui.JFXStructo;
import de.jfxstructo.gui.controller.EditorPaneTab;
import de.jfxstructo.gui.controller.JFXBaseController;
import de.jfxstructo.gui.listener.SelectionListener;

public class JFXStructoMain extends TabPane implements SelectionListener {

	private SelectionListener selectionListener;

	private Diagram selectedDiagram;
	private HashMap<Tab, Diagram> diagrams = new HashMap<>();


	public JFXStructoMain() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(Globals.getResourceBundle());
			loader.setLocation(JFXBaseController.class.getResource("/de/jfxstructo/gui/fxml/JFXStructoMain.fxml"));
			loader.setRoot(this);
			loader.setController(this);
			loader.setBuilderFactory(new JavaFXBuilderFactory());

			loader.load();

			getSelectionModel().selectedItemProperty().addListener(changeTabListener);

		} catch (Exception e) {
			LoggerFactory.getLogger(JFXStructoMain.class).error(null, e);
			Dialogs.showErrorDialog(JFXStructo.getPrimaryStage(), "Can't load Scene", null, null, e);
		}
	}

	public Diagram getDiagram() {
		return selectedDiagram;
	}

	public void setSelectionListener(SelectionListener listener) {
		this.selectionListener = listener;
	}

	public void newFile() {

		EditorPaneTab tab = new EditorPaneTab();
		tab.getDiagram().addSelectionListener(this);

		tab.getDiagram().setContextMenu(getContextMenu());

		tab.setText("Diagram" + (diagrams.size() + 1));

		diagrams.put(tab, tab.getDiagram());
		selectedDiagram = tab.getDiagram();

		getTabs().add(tab);
		getSelectionModel().select(tab);

		tab.setOnCloseRequest(tabCloseEvent);
	}

	@Override
	public void selectionChanged(AElement selected) {
		selectionListener.selectionChanged(selected);
	}

	private final ChangeListener<Tab> changeTabListener = new ChangeListener<Tab>() {
		@Override
		public void changed(ObservableValue<? extends Tab> obversableValue, Tab oldTab, Tab newTab) {
			selectedDiagram = diagrams.get(newTab);

			selectionChanged(selectedDiagram.getSelectedElement());
		}
	};

	private final EventHandler<Event> tabCloseEvent = new EventHandler<Event>() {
		@Override
		public void handle(Event e) {
			diagrams.remove(e.getSource());
			if(diagrams.size() == 0) { newFile(); }
		}
	};

}

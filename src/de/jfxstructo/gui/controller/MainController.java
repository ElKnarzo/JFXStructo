package de.jfxstructo.gui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import org.slf4j.LoggerFactory;

import com.aquafx_project.controls.skin.styles.ButtonType;

import de.jfxstructo.Diagram;
import de.jfxstructo.Globals;
import de.jfxstructo.Restarter;
import de.jfxstructo.Saver;
import de.jfxstructo.Styler;
import de.jfxstructo.config.Configuration;
import de.jfxstructo.config.Language;
import de.jfxstructo.config.Resolution;
import de.jfxstructo.elements.AElement;
import de.jfxstructo.gui.JFXStructo;
import de.jfxstructo.gui.listener.SelectionListener;
import de.jfxstructo.plugin.interfaces.Pluggable;
import de.jfxstructo.plugin.interfaces.PluginManager;
import de.jfxstructo.plugin.loader.PluginLoader;
import de.jfxstructo.plugin.loader.PluginManagerImpl;

public class MainController extends JFXBaseController implements SelectionListener {

	// Configs
	private Language lang;
	private Resolution res;

	private Diagram selectedDiagram;
	private final HashMap<Tab, Diagram> diagrams = new HashMap<>();

	@FXML
	private MenuBar menuBar;
	@FXML
	private Menu menuLanguage;
	@FXML
	private MenuItem menuNewFile, menuSaveFile, menuSaveFileAs, menuOpenFile, menuCloseFile, test;
	@FXML
	private MenuItem menuUndo, menuRedo, menuCut, menuCopy, menuPaste, menuQuit;
	@FXML
	private Button btnNew, btnOpen, btnSave, btnUndo, btnRedo, btnCut, btnCopy, btnPaste, btnRemove;
	@FXML
	private Menu plugins;

	@FXML
	private TabPane editorPane;
	@FXML
	private Label leftStatus;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		lang = (Language) Configuration.getConfig("language");
		for(MenuItem item : menuLanguage.getItems()) {
			if(item.getId().equals("menuLang"+lang.getCountry())) {
				((RadioMenuItem) item).setSelected(true);
			}
		}

		res = (Resolution) Configuration.getConfig("resolution");

		// ####################################################################
		// --------------------------------------------------------------------
		Styler.changeButtonStyle(btnNew, ButtonType.LEFT_PILL);
		Styler.changeButtonStyle(btnOpen, ButtonType.CENTER_PILL);
		Styler.changeButtonStyle(btnSave, ButtonType.RIGHT_PILL);
		// --------------------------------------------------------------------

		// --------------------------------------------------------------------
		Styler.changeButtonStyle(btnUndo, ButtonType.LEFT_PILL);
		Styler.changeButtonStyle(btnRedo, ButtonType.RIGHT_PILL);
		// --------------------------------------------------------------------

		// --------------------------------------------------------------------
		Styler.changeButtonStyle(btnCut, ButtonType.LEFT_PILL);
		Styler.changeButtonStyle(btnCopy, ButtonType.CENTER_PILL);
		Styler.changeButtonStyle(btnPaste, ButtonType.RIGHT_PILL);
		// --------------------------------------------------------------------
		// ####################################################################

		// ####################################################################
		newFile();
		// ####################################################################

		editorPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> obversableValue, Tab oldTab, Tab newTab) {
				selectedDiagram = diagrams.get(newTab);

				updateCopyCutPaste();
				updateRedoUndo();
			}
		});

		// ####################################################################
		// --------------------------------------------------------------------
		menuNewFile.setOnAction(newFileEvent);
		menuOpenFile.setOnAction(openFileEvent);
		menuQuit.setOnAction(closeEvent);
		// --------------------------------------------------------------------

		// --------------------------------------------------------------------
		menuUndo.setOnAction(undoEvent);
		menuRedo.setOnAction(redoEvent);
		// --------------------------------------------------------------------

		// --------------------------------------------------------------------
		menuCut.setOnAction(cutEvent);
		menuCopy.setOnAction(copyEvent);
		menuPaste.setOnAction(pasteEvent);
		// --------------------------------------------------------------------
		// ####################################################################

		// ####################################################################
		// --------------------------------------------------------------------
		btnNew.setOnAction(newFileEvent);
		btnOpen.setOnAction(openFileEvent);
		// --------------------------------------------------------------------

		// --------------------------------------------------------------------
		btnRedo.setOnAction(redoEvent);
		btnUndo.setOnAction(undoEvent);
		// --------------------------------------------------------------------

		// --------------------------------------------------------------------
		btnCut.setOnAction(cutEvent);
		btnCopy.setOnAction(copyEvent);
		btnPaste.setOnAction(pasteEvent);
		// --------------------------------------------------------------------
		// ####################################################################

		btnRemove.setOnAction(removeEvent);


		test.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					LinkedHashMap<String, Pluggable> plugins = PluginLoader.loadPlugins(new File("plugins"));
					PluginManager manager = new PluginManagerImpl();

					Set<String> keys = plugins.keySet();
					for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
						Pluggable p = plugins.get(iterator.next());
						p.setPluginManager(manager);
						p.setStage(JFXStructo.getPrimaryStage());
					}
					for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
						Pluggable p = plugins.get(iterator.next());
						p.start();
					}


				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	private void updateCopyCutPaste() {
		btnCut.setDisable(!selectedDiagram.canCopyCut());
		btnCopy.setDisable(!selectedDiagram.canCopyCut());
		btnPaste.setDisable(!selectedDiagram.canPaste());

		menuCut.setDisable(!selectedDiagram.canCopyCut());
		menuCopy.setDisable(!selectedDiagram.canCopyCut());
		menuPaste.setDisable(!selectedDiagram.canPaste());
	}

	private void updateRedoUndo() {
		btnRedo.setDisable(!selectedDiagram.canRedo());
		btnUndo.setDisable(!selectedDiagram.canUndo());

		menuRedo.setDisable(!selectedDiagram.canRedo());
		menuUndo.setDisable(!selectedDiagram.canUndo());
	}

	private void newFile() {

		final EditorPaneTab tab = new EditorPaneTab();
		tab.getDiagram().addSelectionListener(this);

		ContextMenu cm = new ContextMenu(new SeparatorMenuItem(),menuCut,menuCopy,menuPaste);
		tab.getDiagram().setContextMenu(cm);

		tab.setText("Diagram" + (diagrams.size() + 1));

		diagrams.put(tab, tab.getDiagram());
		selectedDiagram = tab.getDiagram();

		editorPane.getTabs().add(tab);
		editorPane.getSelectionModel().select(tab);

//		tab.getDiagram().addEventHandler(MouseEvent.MOUSE_MOVED, new DiagramMouseMoveListener(leftStatus));

		tab.setOnCloseRequest(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				diagrams.remove(tab);
				if(diagrams.size() == 0) { newFile(); }
			}
		});

	}

	private void openFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.showOpenDialog(JFXStructo.getPrimaryStage());
	}

	private void close() {
		Window window = JFXStructo.getPrimaryStage();
		res.setDimension(window.getWidth(), window.getHeight());

		Configuration.saveConfig();
		System.exit(0);
	}

	@FXML
	private void onBtnSaveClick() {
		Saver.save(selectedDiagram, "test.xml");
	}

	@FXML
	private void onMenuStructureClick(ActionEvent event) {
		Stage s = new Stage();
		PreferenceController preferenceController = new PreferenceController();
		preferenceController.initStage(s, "/de/jfxstructo/gui/fxml/Preferences.fxml");

		s.setTitle(Globals.getResourceBundle().getString("Menu.menuPreferences.text"));
		s.initModality(Modality.APPLICATION_MODAL);
		s.initOwner(JFXStructo.getPrimaryStage());

		Styler.changeStageStyle(s, StageStyle.UTILITY);
	    s.showAndWait();
	}

	@FXML
	private void onMenuLangClick(ActionEvent event) {

		for(MenuItem item : menuLanguage.getItems()) {
			((RadioMenuItem) item).setSelected(false);
		}

		RadioMenuItem menuItem = (RadioMenuItem) event.getSource();
		switch (menuItem.getId()) {
		case "menuLangEN":
			lang.setLocale(new Locale("en", "EN"));
			break;

		case "menuLangDE":
			lang.setLocale(new Locale("de", "DE"));
			break;

		case "menuLangES":
			lang.setLocale(new Locale("es", "ES"));
			break;

		case "menuLangRU":
			lang.setLocale(new Locale("ru", "RU"));
			break;

		default:
			break;
		}

		Configuration.saveConfig();
		Globals.setResourceBundle(lang.getLocale());

		try {
			Restarter.restartApplication(null);
		} catch (IOException e) {
			LoggerFactory.getLogger(MainController.class).error(null, e);
			Dialogs.showErrorDialog(JFXStructo.getPrimaryStage(), null, null, null, e);
		}

//		Stage stage = (Stage) root.getScene().getWindow();
//		initStage(stage, "/de/jfxstructo/gui/JFXStructo.fxml", true);
//		stage.show();

	}

	private final EventHandler<ActionEvent> newFileEvent = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent arg0) {
			newFile();
		}
	};

	private final EventHandler<ActionEvent> openFileEvent = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent arg0) {
			openFile();
		}
	};

	private final EventHandler<ActionEvent> closeEvent = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent arg0) {
			close();
		}
	};

	private final EventHandler<ActionEvent> redoEvent = new EventHandler<ActionEvent>(){
		@Override
		public void handle(ActionEvent arg0) {
			selectedDiagram.redo();

			updateRedoUndo();
		}
	};

	private final EventHandler<ActionEvent> undoEvent = new EventHandler<ActionEvent>(){
		@Override
		public void handle(ActionEvent arg0) {
			selectedDiagram.undo();

			updateRedoUndo();
		}
	};

	private final EventHandler<ActionEvent> cutEvent = new EventHandler<ActionEvent>(){
		@Override
		public void handle(ActionEvent arg0) {
			selectedDiagram.cut();

			updateCopyCutPaste();
			updateRedoUndo();
		}
	};

	private final EventHandler<ActionEvent> copyEvent = new EventHandler<ActionEvent>(){
		@Override
		public void handle(ActionEvent arg0) {
			selectedDiagram.copy();

			btnPaste.setDisable(!selectedDiagram.canPaste());
		}
	};

	private final EventHandler<ActionEvent> pasteEvent = new EventHandler<ActionEvent>(){
		@Override
		public void handle(ActionEvent arg0) {
			selectedDiagram.paste();

			updateCopyCutPaste();
			updateRedoUndo();
		}
	};

	private final EventHandler<ActionEvent> removeEvent = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent arg0) {
			selectedDiagram.removeElement();

			updateCopyCutPaste();
			updateRedoUndo();
		}
	};


	@Override
	public void selectionChanged(AElement selected) {
		updateCopyCutPaste();
	}

}

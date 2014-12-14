package de.jfxstructo.gui.views;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.slf4j.LoggerFactory;

import de.jfxstructo.Diagram;
import de.jfxstructo.Globals;
import de.jfxstructo.Restarter;
import de.jfxstructo.Styler;
import de.jfxstructo.config.Configuration;
import de.jfxstructo.config.Language;
import de.jfxstructo.gui.JFXStructo;
import de.jfxstructo.gui.controller.JFXBaseController;
import de.jfxstructo.gui.controller.PreferenceController;
import de.jfxstructo.plugin.interfaces.Pluggable;
import de.jfxstructo.plugin.interfaces.PluginManager;
import de.jfxstructo.plugin.loader.PluginLoader;
import de.jfxstructo.plugin.loader.PluginManagerImpl;


public class JFXStructoMenuBar extends MenuBar {

	private final Language lang = (Language) Configuration.getConfig("language");

	@FXML
	private MenuBar menuBar;
	@FXML
	private Menu menuLanguage;
	@FXML
	private MenuItem menuNewFile, menuSaveFile, menuSaveFileAs, menuOpenFile, menuCloseFile, menuQuit, test;
	@FXML
	private MenuItem menuUndo, menuRedo, menuCut, menuCopy, menuPaste, menuMoveUp, menuMoveDown;
	@FXML
	private Menu plugins;


	public JFXStructoMenuBar() {

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(Globals.getResourceBundle());
			loader.setLocation(JFXBaseController.class.getResource("/de/jfxstructo/gui/fxml/JFXStructoMenuBar.fxml"));
			loader.setRoot(this);
			loader.setController(this);
			loader.setBuilderFactory(new JavaFXBuilderFactory());

			loader.load();

			ContextMenu cm = new ContextMenu();
			cm.getItems().addAll(menuCut,menuCopy,menuPaste,new SeparatorMenuItem(),menuMoveUp,menuMoveDown);
			setContextMenu(cm);

			// Test Plugins
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

		} catch (Exception e) {
			LoggerFactory.getLogger(JFXStructoMenuBar.class).error(null, e);
			Dialogs.showErrorDialog(JFXStructo.getPrimaryStage(), "Can't load Scene", null, null, e);
		}
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
			LoggerFactory.getLogger(JFXStructoMenuBar.class).error(null, e);
			Dialogs.showErrorDialog(JFXStructo.getPrimaryStage(), null, null, null, e);
		}

	}

	public void updateCopyCutPaste(Diagram diagram) {
		menuCut.setDisable(!diagram.canCopyCut());
		menuCopy.setDisable(!diagram.canCopyCut());
		menuPaste.setDisable(!diagram.canPaste());
	}

	public void updateRedoUndo(Diagram diagram) {
		menuRedo.setDisable(!diagram.canRedo());
		menuUndo.setDisable(!diagram.canUndo());
	}

	public void updateMoveUpMoveDown(Diagram diagram) {
		menuMoveUp.setDisable(!diagram.canMoveUp());
		menuMoveDown.setDisable(!diagram.canMoveDown());
	}


	public void setNewFileEvent(EventHandler<ActionEvent> newFileEvent) {
		menuNewFile.setOnAction(newFileEvent);
	}

	public void setOpenFileEvent(EventHandler<ActionEvent> openFileEvent) {
		menuOpenFile.setOnAction(openFileEvent);
	}

	public void setSaveFileEvent(EventHandler<ActionEvent> saveFileEvent) {
		menuSaveFile.setOnAction(saveFileEvent);
	}

	public void setCloseEvent(EventHandler<ActionEvent> closeEvent) {
		menuQuit.setOnAction(closeEvent);
	}

	public void setUndoEvent(EventHandler<ActionEvent> undoEvent) {
		menuUndo.setOnAction(undoEvent);
	}

	public void setRedoEvent(EventHandler<ActionEvent> redoEvent) {
		menuRedo.setOnAction(redoEvent);
	}

	public void setCutEvent(EventHandler<ActionEvent> cutEvent) {
		menuCut.setOnAction(cutEvent);
	}

	public void setCopyEvent(EventHandler<ActionEvent> copyEvent) {
		menuCopy.setOnAction(copyEvent);
	}

	public void setPasteEvent(EventHandler<ActionEvent> pasteEvent) {
		menuPaste.setOnAction(pasteEvent);
	}

	public void setMoveUpEvent(EventHandler<ActionEvent> moveUpEvent) {
		menuMoveUp.setOnAction(moveUpEvent);
	}

	public void setMoveDownEvent(EventHandler<ActionEvent> moveDownEvent) {
		menuMoveDown.setOnAction(moveDownEvent);
	}

}

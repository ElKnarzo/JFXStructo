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
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import com.aquafx_project.controls.skin.styles.ButtonType;

import de.jfxstructo.Diagram;
import de.jfxstructo.Globals;
import de.jfxstructo.Restarter;
import de.jfxstructo.Saver;
import de.jfxstructo.Styler;
import de.jfxstructo.config.Configuration;
import de.jfxstructo.config.Language;
import de.jfxstructo.config.Resolution;
import de.jfxstructo.gui.JFXStructo;
import de.jfxstructo.gui.listener.DiagramMouseMoveListener;
import de.jfxstructo.plugin.interfaces.Pluggable;
import de.jfxstructo.plugin.interfaces.PluginManager;
import de.jfxstructo.plugin.loader.PluginLoader;
import de.jfxstructo.plugin.loader.PluginManagerImpl;

public class MainController extends JFXBaseController {

	// Configs
	private Language lang;
	private Resolution res;

	private Diagram selectedDiagram;
	private final HashMap<Tab, Diagram> diagrams = new HashMap<>();

	@FXML
	private VBox root;
	@FXML
	private MenuBar menuBar;
	@FXML
	private Menu menuLanguage;
	@FXML
	private MenuItem menuNewFile, menuSaveFile, menuSaveFileAs, menuOpenFile, menuCloseFile, menuQuit, test;
	@FXML
	private Button btnNew, btnOpen, btnSave, btnUndo, btnRedo, btnCut, btnCopy, btnPaste, btnRemove;
	@FXML
	private Menu plugins;

	@FXML
	private ScrollPane scroll;
	@FXML
	private Pane pane;
	@FXML
	private TabPane diagramPane;
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

		diagramPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> obversableValue, Tab oldTab, Tab newTab) {
				selectedDiagram = diagrams.get(newTab);

				if(oldTab != null && newTab == null) {
//					createNewDiagram();
				}

				System.out.println("old:"+oldTab+" new:"+newTab);
				updateCopyCutPaste();
				updateRedoUndo();
			}
		});

		// ####################################################################
		// --------------------------------------------------------------------
		menuNewFile.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				newFile();
			}
		});

		menuOpenFile.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				openFile();
			}
		});

		menuQuit.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				close();
			}
		});


		// --------------------------------------------------------------------
		// ####################################################################

		// ####################################################################
		// --------------------------------------------------------------------
		btnNew.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				newFile();
			}
		});
		// --------------------------------------------------------------------

		// --------------------------------------------------------------------
		btnRedo.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				selectedDiagram.redo();

				updateRedoUndo();
			}
		});

		btnUndo.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				selectedDiagram.undo();

				updateRedoUndo();
			}
		});
		// --------------------------------------------------------------------

		// --------------------------------------------------------------------
		btnCut.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				selectedDiagram.cut();

				updateCopyCutPaste();
				updateRedoUndo();
			}
		});

		btnCopy.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				selectedDiagram.copy();

				btnPaste.setDisable(!selectedDiagram.canPaste());
			}
		});

		btnPaste.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent arg0) {
				selectedDiagram.paste();

				updateCopyCutPaste();
				updateRedoUndo();
			}
		});
		// --------------------------------------------------------------------
		// ####################################################################

		btnRemove.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				selectedDiagram.removeElement();

				updateCopyCutPaste();
				updateRedoUndo();
			}
		});


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
						p.setStage((Stage) root.getScene().getWindow());
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
	}

	private void updateRedoUndo() {
		btnRedo.setDisable(!selectedDiagram.canRedo());
		btnUndo.setDisable(!selectedDiagram.canUndo());
	}

	private void newFile() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/de/jfxstructo/gui/fxml/newTab.fxml"));
			loader.setBuilderFactory(new JavaFXBuilderFactory());

			final Tab tab = loader.load();

			tab.setText("Diagram"+(diagrams.size()+1));
			ScrollPane scroll = (ScrollPane) tab.getContent();
			AnchorPane anchor = (AnchorPane) scroll.getContent();

			final Diagram diagram = new Diagram(anchor);
			anchor.getChildren().add(diagram.getCanvas());

			diagrams.put(tab, diagram);
			selectedDiagram = diagram;

			diagramPane.getTabs().add(tab);
			diagramPane.getSelectionModel().select(tab);

			diagram.getCanvas().addEventHandler(MouseEvent.MOUSE_MOVED, new DiagramMouseMoveListener(leftStatus));
			diagram.getCanvas().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					diagram.selectElement(e.getX(), e.getY());
					diagram.getCanvas().requestFocus();
					updateCopyCutPaste();
				}
			});

			tab.setOnCloseRequest(new EventHandler<Event>() {
				@Override
				public void handle(Event arg0) {
					newFile();
					diagrams.remove(tab);
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void openFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.showOpenDialog(root.getScene().getWindow());
	}

	private void close() {
		Window window = root.getScene().getWindow();
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
		PreferenceController.initStage(s, "/de/jfxstructo/gui/fxml/Preferences.fxml", new PreferenceController(), false);

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		Stage stage = (Stage) root.getScene().getWindow();
//		initStage(stage, "/de/jfxstructo/gui/JFXStructo.fxml", true);
//		stage.show();

	}

//	private void drawShapes(GraphicsContext gc) {
//		gc.setFill(Color.GREEN);
//      gc.setStroke(Color.BLUE);
//      gc.setLineWidth(2);
//      gc.strokeRect(350, 250, 2050, 50);
//      gc.strokeRect(10, 10, 250, 50);
//
//      double[] x = {10,135,260};
//      double[] y = {10,60,10};
//      gc.strokePolyline(x, y, 3);
//
//      gc.strokeRect(10, 60, 125, 150);
//      gc.strokeRect(135, 60, 125, 150);
//
//      gc.setLineWidth(0.55);
//      gc.fillText(new Text("True").getText(), 13.22, 56.78);
//      gc.setTextAlign(TextAlignment.RIGHT);
//      gc.fillText("False", 256.78, 56.78);
//      gc.setTextAlign(TextAlignment.CENTER);
//      gc.fillText("(x <= 4)", 138.22, 40);
//	}

}

package de.jfxstructo.gui.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import de.jfxstructo.Diagram;
import de.jfxstructo.Saver;
import de.jfxstructo.config.Configuration;
import de.jfxstructo.config.Resolution;
import de.jfxstructo.elements.AElement;
import de.jfxstructo.gui.JFXStructo;
import de.jfxstructo.gui.listener.CoordinationListener;
import de.jfxstructo.gui.listener.SelectionListener;

public class JFXStructoView extends VBox implements SelectionListener {

	private final Resolution res = (Resolution) Configuration.getConfig("resolution");

	private final JFXStructoMenuBar menuBar;
	private final JFXStructoToolBar toolBar;
	private final JFXStructoMain main;
	private final JFXStructoStatusBar statusBar;

	public JFXStructoView() {

		menuBar = new JFXStructoMenuBar();
		menuBar.setNewFileEvent(newFileEvent);
		menuBar.setSaveFileEvent(saveFileEvent);
		menuBar.setOpenFileEvent(openFileEvent);
		menuBar.setCloseEvent(closeEvent);
		menuBar.setCutEvent(cutEvent);
		menuBar.setCopyEvent(copyEvent);
		menuBar.setPasteEvent(pasteEvent);
		menuBar.setMoveUpEvent(moveUpEvent);
		menuBar.setMoveDownEvent(moveDownEvent);
		menuBar.setRedoEvent(redoEvent);
		menuBar.setUndoEvent(undoEvent);

		toolBar = new JFXStructoToolBar();
		toolBar.setNewFileEvent(newFileEvent);
		toolBar.setSaveFileEvent(saveFileEvent);
		toolBar.setOpenFileEvent(openFileEvent);
		toolBar.setCutEvent(cutEvent);
		toolBar.setCopyEvent(copyEvent);
		toolBar.setPasteEvent(pasteEvent);
		toolBar.setMoveUpEvent(moveUpEvent);
		toolBar.setMoveDownEvent(moveDownEvent);
		toolBar.setRedoEvent(redoEvent);
		toolBar.setUndoEvent(undoEvent);

		main = new JFXStructoMain();
		main.setSelectionListener(this);
		main.setContextMenu(menuBar.getContextMenu());

		statusBar = new JFXStructoStatusBar();

		BorderPane borderPane = new BorderPane();
		VBox.setVgrow(borderPane, Priority.ALWAYS);
		borderPane.setTop(toolBar);
		borderPane.setCenter(main);
		borderPane.setBottom(statusBar);


		this.getChildren().addAll(menuBar, borderPane);

		// Test
		newFile();
	}

	private void updateUIState() {
		Diagram diagram = main.getDiagram();

		menuBar.updateCopyCutPaste(diagram);
		toolBar.updateCopyCutPaste(diagram);

		menuBar.updateRedoUndo(diagram);
		toolBar.updateRedoUndo(diagram);

		menuBar.updateMoveUpMoveDown(diagram);
		toolBar.updateMoveUpMoveDown(diagram);
	}

	private void newFile() {
		main.newFile();

		// DEBUG
		main.getDiagram().addEventHandler(MouseEvent.MOUSE_MOVED, new CoordinationListener(statusBar));
	}

	private void saveFile() {
		Saver.save(main.getDiagram(), "test.xml");
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

	@Override
	public void selectionChanged(AElement selected) {
		System.out.println("al");
		updateUIState();
	}

	private final EventHandler<ActionEvent> newFileEvent = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent arg0) {
			newFile();
		}
	};

	private final EventHandler<ActionEvent> saveFileEvent = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent arg0) {
			saveFile();
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
			main.getDiagram().redo();

			updateUIState();
		}
	};

	private final EventHandler<ActionEvent> undoEvent = new EventHandler<ActionEvent>(){
		@Override
		public void handle(ActionEvent arg0) {
			main.getDiagram().undo();

			updateUIState();
		}
	};

	private final EventHandler<ActionEvent> cutEvent = new EventHandler<ActionEvent>(){
		@Override
		public void handle(ActionEvent arg0) {
			main.getDiagram().cut();

			updateUIState();
		}
	};

	private final EventHandler<ActionEvent> copyEvent = new EventHandler<ActionEvent>(){
		@Override
		public void handle(ActionEvent arg0) {
			main.getDiagram().copy();

			updateUIState();
		}
	};

	private final EventHandler<ActionEvent> pasteEvent = new EventHandler<ActionEvent>(){
		@Override
		public void handle(ActionEvent arg0) {
			main.getDiagram().paste();

			updateUIState();
		}
	};

	private final EventHandler<ActionEvent> moveUpEvent = new EventHandler<ActionEvent>(){
		@Override
		public void handle(ActionEvent arg0) {
			main.getDiagram().moveUp(main.getDiagram().getSelectedElement());

			updateUIState();
		}
	};

	private final EventHandler<ActionEvent> moveDownEvent = new EventHandler<ActionEvent>(){
		@Override
		public void handle(ActionEvent arg0) {
			main.getDiagram().moveDown(main.getDiagram().getSelectedElement());

			updateUIState();
		}
	};

}

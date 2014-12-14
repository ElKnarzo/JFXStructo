package de.jfxstructo.gui.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Dialogs;
import javafx.scene.control.ToolBar;

import org.slf4j.LoggerFactory;

import com.aquafx_project.controls.skin.styles.ButtonType;

import de.jfxstructo.Diagram;
import de.jfxstructo.Globals;
import de.jfxstructo.Styler;
import de.jfxstructo.gui.JFXStructo;
import de.jfxstructo.gui.controller.JFXBaseController;

public class JFXStructoToolBar extends ToolBar {

	@FXML
	private Button btnNewFile, btnOpenFile, btnSaveFile, btnUndo, btnRedo, btnCut, btnCopy, btnPaste, btnMoveUp, btnMoveDown;

	public JFXStructoToolBar() {

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(Globals.getResourceBundle());
			loader.setLocation(JFXBaseController.class.getResource("/de/jfxstructo/gui/fxml/JFXStructoToolBar.fxml"));
			loader.setRoot(this);
			loader.setController(this);
			loader.setBuilderFactory(new JavaFXBuilderFactory());

			loader.load();

			// ####################################################################
			// --------------------------------------------------------------------
			Styler.changeButtonStyle(btnNewFile, ButtonType.LEFT_PILL);
			Styler.changeButtonStyle(btnOpenFile, ButtonType.CENTER_PILL);
			Styler.changeButtonStyle(btnSaveFile, ButtonType.RIGHT_PILL);
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

		} catch (Exception e) {
			LoggerFactory.getLogger(JFXStructoToolBar.class).error(null, e);
			Dialogs.showErrorDialog(JFXStructo.getPrimaryStage(), "Can't load Scene", null, null, e);
		}
	}

	public void updateCopyCutPaste(Diagram diagram) {
		btnCut.setDisable(!diagram.canCopyCut());
		btnCopy.setDisable(!diagram.canCopyCut());
		btnPaste.setDisable(!diagram.canPaste());
	}

	public void updateRedoUndo(Diagram diagram) {
		btnRedo.setDisable(!diagram.canRedo());
		btnUndo.setDisable(!diagram.canUndo());
	}

	public void updateMoveUpMoveDown(Diagram diagram) {
		btnMoveUp.setDisable(!diagram.canMoveUp());
		btnMoveDown.setDisable(!diagram.canMoveDown());
	}

	public void setNewFileEvent(EventHandler<ActionEvent> newFileEvent) {
		btnNewFile.setOnAction(newFileEvent);
	}

	public void setOpenFileEvent(EventHandler<ActionEvent> openFileEvent) {
		btnOpenFile.setOnAction(openFileEvent);
	}

	public void setSaveFileEvent(EventHandler<ActionEvent> saveFileEvent) {
		btnSaveFile.setOnAction(saveFileEvent);
	}

	public void setUndoEvent(EventHandler<ActionEvent> undoEvent) {
		btnUndo.setOnAction(undoEvent);
	}

	public void setRedoEvent(EventHandler<ActionEvent> redoEvent) {
		btnRedo.setOnAction(redoEvent);
	}

	public void setCutEvent(EventHandler<ActionEvent> cutEvent) {
		btnCut.setOnAction(cutEvent);
	}

	public void setCopyEvent(EventHandler<ActionEvent> copyEvent) {
		btnCopy.setOnAction(copyEvent);
	}

	public void setPasteEvent(EventHandler<ActionEvent> pasteEvent) {
		btnPaste.setOnAction(pasteEvent);
	}

	public void setMoveUpEvent(EventHandler<ActionEvent> moveUpEvent) {
		btnMoveUp.setOnAction(moveUpEvent);
	}

	public void setMoveDownEvent(EventHandler<ActionEvent> moveDownEvent) {
		btnMoveDown.setOnAction(moveDownEvent);
	}


}

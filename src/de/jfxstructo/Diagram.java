package de.jfxstructo;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialogs;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jfxstructo.elements.AElement;
import de.jfxstructo.elements.Root;
import de.jfxstructo.elements.Subqueue;
import de.jfxstructo.graphics.Frame;
import de.jfxstructo.gui.JFXStructo;
import de.jfxstructo.gui.listener.SelectionListener;

public class Diagram extends Canvas {

	private final List<SelectionListener> listeners = new ArrayList<>();
	private ContextMenu cm;

	private Stack<AElement> redo = new Stack<AElement>();
	private Stack<AElement> undo = new Stack<AElement>();

	private static boolean isCut = false;

	private AElement root = new Root();
	private AElement selected;
	private static AElement copied = null;

    private final Board board = new Board(this.getGraphicsContext2D());

    public Diagram() {
    	parentProperty().addListener(parentListener);
    	setOnKeyPressed(keyEvent);
		addEventHandler(MouseEvent.MOUSE_CLICKED, clickEvent);
		addEventHandler(MouseEvent.MOUSE_CLICKED, openContextMenuEvent);

    	repaint();
    }

    public void addSelectionListener(SelectionListener listener) {
    	listeners.add(listener);
    }

    public void setContextMenu(ContextMenu cm) {
    	this.cm = cm;
    }

    private void showContextMenu(double x, double y) {
    	cm.show(this, x, y);
    }

	public AElement getSelectedElement() {
		return selected;
	}

	public AElement getRoot() {
		return root;
	}

	public void selectElement(double x, double y) {
		if(x <= this.getWidth() && y <= this.getHeight()) {
			Logger logger = LoggerFactory.getLogger(Diagram.class);

			logger.info("X:"+x + ", Y:" + y);
			selected = root.selectElementByCoord(x, y);
			logger.info(selected.toString());
			logger.info(selected.getFrame().toString());
			logger.info("Height: "+selected.getHeight()+", Width: "+selected.getWidth());
			board.drawRect(selected.getFrame(), Color.AQUA);
		}
	}

	public void repaint() {
			Frame f = root.prepareDraw(board);
			LoggerFactory.getLogger(Diagram.class).debug(f.toString());
//			int d = Math.max(f.getWidth(), f.getHeight());

	    	this.setWidth(f.getWidth()+2);
			this.setHeight(f.getHeight()+2);

			root.draw(board, f);
		}

	public void undo() {
		if (undo.size() > 0) {
			redo.add(root.clone());

			root = undo.pop();
			repaint();
		}
	}

	public void redo() {
		if (redo.size() > 0) {
			undo.add(root.clone());

			root = redo.pop();
			repaint();
		}
	}

	public boolean canUndo() {
		return (undo.size() > 0);
	}

	public boolean canRedo() {
		return (redo.size() > 0);
	}

	public void addUndo() {
		undo.add(root.clone());
		clearRedo();
	}

	public void clearRedo() {
		redo = new Stack<>();
	}

	public void clearUndo() {
		undo = new Stack<>();
	}

	public void cut() {
		if (selected != null) {
			copied = selected.clone();
			addUndo();
			((Root) root).removeElement(selected);
			repaint();
			selected = null;
			isCut = true;
		}
	}

	public void copy() {
		if (selected != null) {
			copied = selected.clone();
		}
		isCut = false;
	}

	public void paste() {
		if (selected != null && copied != null) {
			addUndo();

			if(selected instanceof Subqueue) {
				((Subqueue) selected).addElement(copied.clone());
			} else {
				((Subqueue) selected.getParent()).addElement(copied.clone());
			}
			if(isCut) copied = null;
			selected = null;

			repaint();
		}
	}

	public boolean canPaste() {
		boolean cond = (copied != null && selected != null);
		if (selected != null) {
			cond = cond && !selected.getClass().getSimpleName().equals("Root");
		}
		return cond;
	}

	public boolean canCopyCut() {
		boolean cond = (selected != null);
		if (selected != null) {
			cond = cond && !selected.getClass().getSimpleName().equals("Root");
			cond = cond && !selected.getClass().getSimpleName().equals("Subqueue");
		}
		return cond;
	}

	public void removeElement() {
		if(selected.getParent() != null && !(selected instanceof Subqueue)) {
			addUndo();
			((Subqueue) selected.getParent()).removeElement(selected);

			selected = null;
			repaint();
		}
	}

	private void notifyListener() {
		for (SelectionListener selectionListener : listeners) {
			selectionListener.selectionChanged(selected);
		}
	}

	private final ChangeListener<Parent> parentListener = new ChangeListener<Parent>() {

		@Override
		public void changed(ObservableValue<? extends Parent> observable, Parent oldValue, Parent newValue) {
			if(oldValue == null && newValue != null) {
				Region region = (Region) newValue;
				region.prefHeightProperty().bind(heightProperty());
				region.prefWidthProperty().bind(widthProperty());
			}
		}
	};

	private final EventHandler<KeyEvent> keyEvent = new EventHandler<KeyEvent>() {

		@Override
		public void handle(KeyEvent event) {
			LoggerFactory.getLogger(Diagram.class).info("Key Pressed: " + event.getCode());
			if ((event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE)
					&& getSelectedElement() != null) {
				removeElement();

				notifyListener();
			}
		}
	};

	private final EventHandler<MouseEvent> clickEvent = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {

			if(e.getClickCount() == 2) {
				Dialogs.showInformationDialog(JFXStructo.getPrimaryStage(), "Double klick");
			}

			selectElement(e.getX(), e.getY());
			requestFocus();

			notifyListener();
		}
	};

	private final EventHandler<MouseEvent> openContextMenuEvent = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {
			if (e.getButton() == MouseButton.SECONDARY) {
				showContextMenu(e.getScreenX(), e.getScreenY());
			}
		}
	};

}

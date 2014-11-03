package de.jfxstructo;

import java.util.Stack;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import de.jfxstructo.elements.Element;
import de.jfxstructo.elements.Root;
import de.jfxstructo.elements.Subqueue;
import de.jfxstructo.graphics.Frame;

public class Diagram {
	
	private Stack<Element> redo = new Stack<Element>();
	private Stack<Element> undo = new Stack<Element>();
	
	private static boolean isCut = false;
	
	private Element root = new Root();
	private Element selected;
	private static Element copied = null;
	
	private Pane pane;
	
	private Canvas canvas = new Canvas();
    private Board board = new Board(canvas.getGraphicsContext2D());
	
    public Diagram(Pane pane)  {
    	this.pane = pane;
    	repaint();		
    }
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public Element getSelectedElement() {
		return selected;
	}
	
	public Element getRoot() {
		return root;
	}

	public void selectElement(double x, double y) {
		if(x <= canvas.getWidth() && y <= canvas.getHeight()) {
			System.out.println("X:"+x + ", Y:" + y);
			selected = root.selectElementByCoord(x, y);
			System.out.println(selected);
			System.out.println(selected.getFrame());
			System.out.println("Height: "+selected.getHeight()+", Width: "+selected.getWidth());
			board.drawRect(selected.getFrame(), Color.AQUA);
		}
	}
	
	public void repaint() {
			Frame f = root.prepareDraw(board);  
	    	System.out.println(f);
	//    	int d = Math.max(f.getWidth(), f.getHeight());
	    	
	    	canvas.setWidth(f.getWidth()+2);
			canvas.setHeight(f.getHeight()+2);
					
			this.pane.setPrefHeight(f.getHeight()+2);
			this.pane.setPrefWidth(f.getWidth()+2);
	
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
	
}

package de.jfxstructo.gui.listener;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class DiagramMouseMoveListener implements EventHandler<MouseEvent> {

	private Label output;
	
	public DiagramMouseMoveListener(Label label) {
		output = label;
	}
	
	@Override
	public void handle(MouseEvent e) {
		output.setText("X:"+e.getX() + ", Y:" + e.getY() + ", Z:" + e.getZ());
	}

}

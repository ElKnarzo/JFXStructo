package de.jfxstructo.gui.listener;

import de.jfxstructo.gui.views.JFXStructoStatusBar;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class CoordinationListener implements EventHandler<MouseEvent> {

	private final JFXStructoStatusBar statusBar;

	public CoordinationListener(JFXStructoStatusBar statusBar) {
		this.statusBar = statusBar;
	}

	@Override
	public void handle(MouseEvent e) {
		statusBar.setLeftStatus("X:"+e.getX() + ", Y:" + e.getY() + ", Z:" + e.getZ());
	}

}

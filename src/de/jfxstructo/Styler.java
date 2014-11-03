package de.jfxstructo;

import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.aquafx_project.controls.skin.styles.ButtonType;
import com.aquafx_project.controls.skin.styles.TabPaneType;


public class Styler {

	public static void loadStyle(Stage stage) {
		
		if(OSValidator.isMac()) {
//			AquaFx.style();
		}
		
		if(OSValidator.isWindows()) {
//			stage.getScene().getStylesheets().add(Styler.class.getResource("/de/jfxstructo/gui/styles/win7glass.css").toExternalForm());
//			stage.getScene().getStylesheets().add(Styler.class.getResource("/de/jfxstructo/gui/styles/Windows-7.css").toExternalForm());
//			stage.getScene().getStylesheets().add(Styler.class.getResource("/de/jfxstructo/gui/styles/caspian.css").toExternalForm());
//			stage.getScene().getStylesheets().add(Styler.class.getResource("/de/jfxstructo/gui/styles/modena.css").toExternalForm());			
		}
		
	}
	
	public static void changeStageStyle(Stage stage, StageStyle style){
		if(OSValidator.isMac()) {
//			Platform.isSupported(ConditionalFeature.UNIFIED_WINDOW);
//			AquaFx.styleStage(stage, style);
		}		
	}
	
	public static void changeButtonStyle(Button button, ButtonType type) {
		if(OSValidator.isMac()) {
//			ButtonStyler.create().setType(type).style(button);
		}		
	}
	
	public static void changeTabPaneStyle(TabPane pane, TabPaneType type) {
		if(OSValidator.isMac()) {
//			TabPaneStyler.create().setType(type).style(pane);
		}
	}

}

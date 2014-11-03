package de.jfxstructo;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import de.jfxstructo.graphics.Frame;

public class Board {

	private GraphicsContext gc;
	private Font font = Font.font("Verdana", 13);
	
	public Board(GraphicsContext graphicsContext2D) {
		this.gc = graphicsContext2D;
		gc.setLineWidth(1.4);
		gc.setFont(font);
	}
	
	public GraphicsContext getContext() {
		return gc;
	}

	public double getTextHeight(String text) {
		return getTextHeight(new Text(text));
	}
	
	public double getTextWidth(String text) {
		return getTextWidth(new Text(text));
	}

	public double getTextHeight(Text text) {
		return Math.ceil(text.getLayoutBounds().getHeight()/10)*10;
	}
	
	public double getTextWidth(Text text) {
		return Math.ceil(text.getLayoutBounds().getWidth()/10)*10;
	}
	
	public void fillRect(Frame f) {
		fillRect(f, Color.WHITE);
	}
	
	public void fillRect(Frame f, Color color) {
//		gc.setFill(color);
		Stop[] stops = new Stop[]{new Stop(0, Color.LIGHTGRAY), new Stop(1, Color.WHITE)};
		LinearGradient lg = new LinearGradient(0, 0, 0, 1, true, CycleMethod.REFLECT, stops);
		gc.setFill(lg);
		gc.fillRect(f.getLeft()+1, f.getTop()+1, f.getWidth(), f.getHeight());
	}
	
	public void drawRect(Frame f) {
		drawRect(f, Color.BLACK);
	}
	
	public void drawRect(Frame f, Color color) {
		gc.setStroke(color);
		gc.strokeRect(f.getLeft()+1, f.getTop()+1, f.getWidth(), f.getHeight());
	}
	
	public void fillText(String text, double x, double y) {
		gc.setFill(Color.BLACK);
		gc.fillText(text, x, y);
	}
	
	public void fillTextRight(String text, double x, double y) {
		gc.setTextAlign(TextAlignment.RIGHT);
		fillText(text, x, y);
		gc.setTextAlign(TextAlignment.LEFT);
	}
	
	public void fillTextCenter(String text, double x, double y) {
		gc.setTextAlign(TextAlignment.CENTER);
		fillText(text, x, y);
		gc.setTextAlign(TextAlignment.LEFT);
	}
	
	public void fillText(Text text, double x, double y) {
		fillText(text.getText(), x, y);
	}
	
	public void fillTextRight(Text text, double x, double y) {
		fillTextRight(text.getText(), x, y);
	}
	
	public void fillTextCenter(Text text, double x, double y) {
		fillTextCenter(text.getText(), x, y);
	}

	public void drawLine(double x1, double y1, double x2, double y2) {
		gc.setStroke(Color.BLACK);
		gc.strokeLine(x1, y1, x2, y2);
	}
	
}

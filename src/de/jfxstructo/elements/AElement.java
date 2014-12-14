package de.jfxstructo.elements;

import javafx.scene.text.Text;
import de.jfxstructo.graphics.Frame;
import de.jfxstructo.interfaces.Drawable;


public abstract class AElement implements Drawable, Cloneable {
	
	public static int E_PADDING = 20;
	
	protected Text text = new Text();
	protected AElement parent;
	
	protected int width, height;
	protected boolean selected = false;
	
	protected Frame frame = new Frame();
	
	public AElement() {
	}
	
	public AElement(String text) {
		this.text.setText(text);
	}
	
	public String getText() {
		return text.getText();
	}

	public void setText(String text) {
		this.text.setText(text);
	}

	public AElement getParent() {
		return parent;
	}
	
	public void setParent(AElement parent) {
		this.parent = parent;
	}
	
	public Frame getFrame() {
		return frame;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	protected void setDimension() {
		width = frame.getWidth();
		height = frame.getHeight();
	}

	public abstract AElement clone();
	
	public AElement selectElementByCoord(double x, double y) {
		AElement ele = this;
		while (ele.parent != null)
			ele = ele.parent;

		Frame tmp = ele.getFrame();
		if ((frame.getLeft() - tmp.getLeft() < x)
				&& (x < frame.getRight() - tmp.getLeft())
				&& (frame.getTop() - tmp.getTop() < y)
				&& (y < frame.getBottom() - tmp.getTop())) {
			return this;
		} else {
			selected = false;
			return null;
		}
		
	}
}

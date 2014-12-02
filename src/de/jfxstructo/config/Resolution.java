package de.jfxstructo.config;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import org.jdom2.Element;

public class Resolution extends Config {

	private double width, height;

	public Resolution() {
		width = 1280;
		height = 720;


		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

		double minWidth = 0, minHeight = 0;

		for (DisplayMode dm : gd.getDisplayModes()) {
			System.out.println(dm.getHeight() + " " + dm.getWidth());
			if(minHeight == 0 && minWidth == 0) {
				minWidth = dm.getWidth();
				minHeight = dm.getHeight();
			}
			minHeight = Math.min(minHeight, dm.getHeight());
			minWidth = Math.min(minWidth, dm.getWidth());
 		}
		width = minWidth;
		height = minHeight;

	}

	public Resolution(Element el) {
		super(el);
		setXMLElement(el);
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public void setDimension(double width, double height) {
		setWidth(width);
		setHeight(height);
	}

	@Override
	public void setXMLElement(Element el) {
		key = el.getName();
		setWidth(Double.parseDouble(el.getChildText("width")));
		setHeight(Double.parseDouble(el.getChildText("height")));
	}

	@Override
	public Element getXMLElement() {
		Element el = new Element(key);

		Element w = new Element("width");
		w.setText(width+"");

		Element h = new Element("height");
		h.setText(height+"");

		el.addContent(w);
		el.addContent(h);
		return el;
	}

}

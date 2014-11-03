package de.jfxstructo.config;

import org.jdom2.Element;

public class Resolution extends Config {
	
	private double width, height;

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

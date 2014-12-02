package de.jfxstructo.config;

import org.jdom2.Element;

public abstract class Config {

	protected String key;
	protected Element el;

	public Config() {
		this.key = getClass().getSimpleName().toLowerCase();
	}

	public Config(Element el) {
		setXMLElement(el);
	}

	public String getXMLKey() {
		return key;
	}

	public abstract void setXMLElement(Element el);
	public abstract Element getXMLElement();

}

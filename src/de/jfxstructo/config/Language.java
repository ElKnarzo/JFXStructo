package de.jfxstructo.config;

import java.util.Locale;

import org.jdom2.Element;

public class Language extends Config {

	private Locale locale;

	public Language() {
		super(Language.class.getSimpleName().toLowerCase());
		locale = new Locale("en", "EN");
	}

	public Language(Element el) {
		super(el);
		setXMLElement(el);
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getCountry() {
		return locale.getCountry();
	}

	@Override
	public void setXMLElement(Element el) {
		key = el.getName();
		locale = new Locale(el.getChildText("shortcut"), el.getChildText("country"));
	}

	@Override
	public Element getXMLElement() {
		Element el = new Element(key);

		Element shortcut = new Element("shortcut");
		shortcut.setText(locale.getLanguage());

		Element country = new Element("country");
		country.setText(locale.getCountry());

		el.addContent(shortcut);
		el.addContent(country);
		return el;
	}

}

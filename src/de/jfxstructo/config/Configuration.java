package de.jfxstructo.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import main.xml.XML;
import main.xml.XPath;

import org.jdom2.Document;
import org.jdom2.Element;


public class Configuration {

	private static HashMap<String, Config> configs = new HashMap<>();

	public static Config getConfig(String key) {
		if(!configs.containsKey(key)) {
			loadConfig();
		}
		return configs.get(key);
	}

	public static void setConfig(Config type) {
		configs.put(type.getXMLKey(), type);
	}

	public static void loadConfig() {
		Document doc = XML.loadXMLFile("cfg/config.xml");
		Element root = doc.getRootElement();
		for (Element el : root.getChildren()) {
			Element[] elements = XPath.get(doc, "//"+el.getName().toLowerCase());

			switch (el.getName()) {
			case "language":
				setConfig(new Language(elements[0]));
				break;
			case "resolution":
				setConfig(new Resolution(elements[0]));
				break;
			}
		}
	}

	public static void saveConfig() {
		Document doc = new Document();
		Element root = new Element("configuration");
		doc.setRootElement(root);

		Set<String> keys = configs.keySet();
		for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
			String string = iterator.next();
			root.addContent(configs.get(string).getXMLElement());
		}

		XML.writeXMLFile("cfg/config.xml", doc);
	}

	public static Document getDefaultConfig() {
		Document doc = new Document();
		Element root = new Element("configuration");
		doc.setRootElement(root);

		root.addContent(new Language().getXMLElement());
		root.addContent(new Resolution().getXMLElement());

		return doc;
	}

}

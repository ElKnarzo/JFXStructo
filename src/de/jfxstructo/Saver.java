package de.jfxstructo;

import main.xml.XML;

import org.jdom2.Document;
import org.jdom2.Element;


public class Saver {
	
	public static void save(Diagram diagram, String path) {
		
		Document doc = convertToXML(diagram);
		XML.writeXMLFile(path, doc);
		
	}

	private static Document convertToXML(Diagram diagram) {
		Document doc = new Document();
		
		Element root = new Element(diagram.getRoot().getClass().getSimpleName().toLowerCase());
		root.setAttribute("text", diagram.getRoot().getText());
		
		doc.setRootElement(root);
		
		return doc;
	}

}

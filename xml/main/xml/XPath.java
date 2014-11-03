package main.xml;

import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class XPath {
	
	private static XPathFactory xpathFactory = XPathFactory.instance();
	
	public static Element[] get(Document doc, String xpathquery){
		XPathExpression<Object> xpath = xpathFactory.compile(xpathquery);
		List<Object> results = xpath.evaluate(doc);
		
		return (Element[]) results.toArray(new Element[0]);
	}

}

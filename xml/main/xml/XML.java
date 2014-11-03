package main.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderSchemaFactory;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xml.sax.SAXException;

/**
 * @author Markus Drechsel
 *
 */
public class XML {

	private static String schemaPath = "/main/xml/schema/schema.xsd";
	private static SAXBuilder builder = new SAXBuilder();

	public static Document loadXMLFile(String path) {
		return loadXMLFile(new File(path));
	}

	/**
	 * @param file
	 * @return
	 */
	public static Document loadXMLFile(File file) {

		try {
			return loadXMLFile(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * @param is
	 * @return
	 */
	public static Document loadXMLFile(InputStream is) {

		try {

			SchemaFactory schemafac = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemafac.newSchema(new File(XML.class.getResource(schemaPath).toURI()));
			XMLReaderJDOMFactory factory = new XMLReaderSchemaFactory(schema);
			builder.setXMLReaderFactory(factory);

			return builder.build(is);
		} catch (JDOMException | IOException e) {
			System.out.println(e.getClass().getCanonicalName());
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * @param file
	 * @param doc
	 * @return
	 */
	public static boolean writeXMLFile(String file, Document doc){
		return writeXMLFile(new File(file), doc);
	}

	/**
	 * @param file
	 * @param doc
	 * @return
	 */
	public static boolean writeXMLFile(File file, Document doc) {

		try {
			FileOutputStream fos = new FileOutputStream(file);
			XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
			outputter.output(doc, fos);

			fos.flush();
			fos.close();

			return true;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;

	}

}

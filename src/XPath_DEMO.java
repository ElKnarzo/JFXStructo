import java.io.File;
import java.io.IOException;

import main.xml.XPath;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


/**
 * @author Knarz
 *
 */
public class XPath_DEMO {

	public static void main(String[] args){
		new XPath_DEMO("config.xml");
	}

	/**
	 * @param path
	 */
	public XPath_DEMO(String path){

		try {

			Document doc = new SAXBuilder().build(new File(path));


			Element[] res = XPath.get(doc, "//language" );
			System.out.println(res[0].getAttributeValue("index"));
//			System.out.println(new Integer(res[0].getText()) + " x " + new Integer(res[1].getText()));

		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}

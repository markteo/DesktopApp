package dataParser;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class XML_Parser {

	private File XmlFile;

	public XML_Parser(File filePath) {
		this.XmlFile = filePath;
	}

	public String getLink() {
		String link = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(XmlFile);

			doc.getDocumentElement().normalize();

			link = doc.getElementsByTagName("link").item(0).getTextContent();
			return link;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}

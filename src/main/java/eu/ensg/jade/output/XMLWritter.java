package eu.ensg.jade.output;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * XMLWriter is the class implementing the writing of the XML file defining the scene
 * 
 * Info on XML I/O:
 * https://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/
 * https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
 * https://www.mkyong.com/java/how-to-modify-xml-file-in-java-dom-parser/
 * 
 * @author JADE
 */

public class XMLWritter extends SceneVisitor{
	
	private DocumentBuilder docBuilder;
	private Transformer transformer;
	
// ========================== ATTRIBUTES ===========================


// ========================== CONSTRUCTORS =========================	
	public XMLWritter() {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			this.docBuilder = docFactory.newDocumentBuilder();
			this.transformer = transformerFactory.newTransformer();
		} catch (ParserConfigurationException | TransformerConfigurationException e) {
			e.printStackTrace();
		}
	}



// ========================== GETTERS/SETTERS ======================


// ========================== METHODS ==============================
	
	@Override
	public void visit() {
		// TODO Do something for the visitor pattern
		
	}
	
	public void writeAll(Map<String, String> parameters) {
		String fileInteraction = parameters.get("FileInteraction");
		String fileScenario = parameters.get("FileScenario");
		String fileScene = parameters.get("FileScene");
		String fileSettings = parameters.get("FileSettings");
		this.createMainXml(fileInteraction, fileScenario, fileScene, fileSettings);
		
		this.createInteractionXml(fileInteraction);
		
		this.createSceneXml(fileScene);
		
		this.createScenarioXml(fileScenario, 0, 0, 0);
		
		this.createSettingsXml(fileSettings);
	}
	
	private void createMainXml(String fileInteraction, String fileScenario, String fileScene, String fileSettings) {
		try {
			Document doc = this.importXml("assets/DrivingTasks/Template/main.xml");
			
			NodeList nodeList = doc.getElementsByTagName("entry");
			Node node;
			for (int i = 0; i < nodeList.getLength(); i++) {
				node = nodeList.item(i);
				
				if(fileInteraction != null && node.getAttributes().getNamedItem("key").equals("interaction")) {
					node.setTextContent(fileInteraction);
				}
				if(fileScenario != null && node.getAttributes().getNamedItem("key").equals("scenario")) {
					node.setTextContent(fileScenario);
				}
				if(fileScene != null && node.getAttributes().getNamedItem("key").equals("scene")) {
					node.setTextContent(fileScene);
				}
				if(fileSettings != null && node.getAttributes().getNamedItem("key").equals("setting")) {
					node.setTextContent(fileSettings);
				}
			}
			
			this.exportXml("assets/DrivingTasks/Projects/RGE/main.xml", doc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void createInteractionXml(String fileName) {
		try {
			Document doc = this.importXml("assets/DrivingTasks/Template/interaction.xml");
			
			// TODO: add the ability to edit the 'interaction.xml' file
			
			this.exportXml("assets/DrivingTasks/Projects/RGE/"+fileName, doc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void createSceneXml(String fileName) {
		try {
			Document doc = this.importXml("assets/DrivingTasks/Template/scene.xml");
			
			// TODO: add the ability to edit the 'scene.xml' file
			
			this.exportXml("assets/DrivingTasks/Projects/RGE/"+fileName, doc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void createScenarioXml(String fileName, double snowCoeff, double rainCoeff, double fogCoeff) {
		try {
			Document doc = this.importXml("assets/DrivingTasks/Template/scenario.xml");
			
			doc.getElementsByTagName("snowingPercentage").item(0).setTextContent(String.valueOf(snowCoeff));
			doc.getElementsByTagName("rainingPercentage").item(0).setTextContent(String.valueOf(rainCoeff));
			doc.getElementsByTagName("fogPercentage").item(0).setTextContent(String.valueOf(fogCoeff));
			
			this.exportXml("assets/DrivingTasks/Projects/RGE/"+fileName, doc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void createSettingsXml(String fileName) {
		try {
			Document doc = this.importXml("assets/DrivingTasks/Template/settings.xml");
			
			// TODO: add the ability to edit the 'settings.xml' file
			
			this.exportXml("assets/DrivingTasks/Projects/RGE/"+fileName, doc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Document importXml(String filePath) {
		Document doc = this.docBuilder.newDocument();
		try {
			doc = docBuilder.parse(filePath);
			System.out.println("XML imported succefully!");
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	private void exportXml(String filePath, Document xml) {
		try {
			DOMSource source = new DOMSource(xml);
			
			StreamResult result = new StreamResult(new File(filePath));
//			StreamResult result = new StreamResult(System.out);	// Output to console for testing
			
			this.transformer.transform(source, result);
			System.out.println("XML exported succefully!");
			
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	

}

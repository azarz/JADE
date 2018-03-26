package eu.ensg.jade.output;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eu.ensg.jade.xml.XMLGroundModel;
import eu.ensg.jade.xml.XMLModel;
import eu.ensg.jade.xml.XMLVehicle;

/**
 * XMLWriter is the class implementing the writing of the XML files,
 * which define the world inside OpenDS
 * 
 * Info on XML I/O:
 * https://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/
 * https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
 * https://www.mkyong.com/java/how-to-modify-xml-file-in-java-dom-parser/
 * 
 * @author JADE
 */
public class XMLWriter {
	
// ========================== ATTRIBUTES ===========================
	
	/**
	 * Generic object to create new XML documents
	 */
	private DocumentBuilder docBuilder;
	
	/**
	 * Generic object to save XML documents
	 */
//	private Transformer transformer;
	
	/**
	 * Directory used to save the new driving task
	 */
	private String mainDirectory = "assets/DrivingTasks/Projects/RGE/";
	
	/**
	 * The HashMap containing the configuration of all XML files
	 */
	private Map<String, String> globalConfig;
	
	private List<XMLModel> modelList;
	private List<XMLGroundModel> terrainList;
	private List<XMLVehicle> vehicleList;


// ========================== CONSTRUCTORS =========================
	
	/**
	 * Default (empty) class constructor
	 */
	public XMLWriter() {
		this.globalConfig = new HashMap<String, String>();
		
		this.initGlobalConfig();
		
		this.modelList = new ArrayList<>();
		this.terrainList = new ArrayList<>();
		this.vehicleList = new ArrayList<>();		
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try {
			this.docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}



// ========================== GETTERS/SETTERS ======================
	
	public void setMainDirectory(String directory) {
		this.mainDirectory = directory;
		
		if(!this.mainDirectory.endsWith("/")) {
			this.mainDirectory += "/";
		}
	}
	
	public String getMainDirectory() {
		return this.mainDirectory;
	}
	
	
	public void updateConfig(String key, String value) {
		this.globalConfig.put(key, value);
	}
	
	public String getConfigValue(String key) {
		return this.globalConfig.get(key);
	}
	
	public Set<String> getConfigKeys() {
		return this.globalConfig.keySet();
	}	
	

	public void addModel(XMLModel model) {
		modelList.add(model);
	}
	
	public void addTerrain(XMLGroundModel model) {
		terrainList.add(model);
	}
	
	public void addVehicle(XMLVehicle vehicle) {
		vehicleList.add(vehicle);
	}



// ========================== METHODS ==============================

	/**
	 * Write all the XML files to define a new driving task in OpenDS
	 */
	public void createAllXml() {
		this.createMainXml();
		
		this.createInteractionXml();
		
		this.createSceneXml();
		
		this.createScenarioXml();
		
		this.createSettingsXml();
	}



	/**
	 * Create the main XML file, which is the entry point of the driving task
	 */
	public void createMainXml() {
		try {
			Document doc = this.importXml("assets/DrivingTasks/Template/main.xml");
			
			NodeList nodeList = doc.getElementsByTagName("entry");
			Node node;			
			for (int i = 0; i < nodeList.getLength(); i++) {
				node = nodeList.item(i);
				
				if(node.getAttributes().getNamedItem("key").equals("interaction")) {
					node.setTextContent(this.globalConfig.get("fileInteractionXML"));
				}
				else if(node.getAttributes().getNamedItem("key").equals("scenario")) {
					node.setTextContent(this.globalConfig.get("fileScenarioXML"));
				}
				else if(node.getAttributes().getNamedItem("key").equals("scene")) {
					node.setTextContent(this.globalConfig.get("fileSceneXML"));
				}
				else if(node.getAttributes().getNamedItem("key").equals("settings")) {
					node.setTextContent(this.globalConfig.get("fileSettingsXML"));
				}
			}
			
			this.exportXml(this.mainDirectory + this.globalConfig.get("fileMainXML"), doc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the interaction XML file, which define actions triggered by specific conditions.
	 * Nothing useful to change here
	 */
	public void createInteractionXml() {
		try {
			Document doc = this.importXml("assets/DrivingTasks/Template/interaction.xml");
			
			// TODO: add the ability to edit the 'interaction.xml' file
			
			this.exportXml(this.mainDirectory + this.globalConfig.get("fileInteractionXML"), doc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the scene XML file, which define every object populating the world in OpenDS
	 */
	public void createSceneXml() {
		try {
			Document doc = this.importXml("assets/DrivingTasks/Template/scene.xml");
			
			doc.getElementsByTagName("gravity").item(0).setTextContent(this.globalConfig.get("gravity"));
			
			Node models = doc.getElementsByTagName("models").item(0);
			Node geometries = doc.getElementsByTagName("geometries").item(0);
			Element e;
			
			// Add every model to the scene
			for(XMLModel model: modelList) {
				e = model.toXMLElement(doc);
				models.appendChild(e);
			}
			
			// Add every terrain + model
			for(XMLGroundModel model: terrainList) {
				e = model.toXMLElement(doc);
				models.appendChild(e);
				
				e = model.getTerrain().toXMLElement(doc);
				geometries.appendChild(e);
			}
			
			this.exportXml(this.mainDirectory + this.globalConfig.get("fileSceneXML"), doc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the scenario XML file, which contains information about the world configuration such as:
	 * <ul>
	 * <li>the weather</li>
	 * <li>the car physic parameters</li>
	 * <li>the automated traffic</li>
	 * <li>traffic light triggers</li>
	 * </ul>
	 * 
	 */
	public void createScenarioXml() {
		try {
			Document doc = this.importXml("assets/DrivingTasks/Template/scenario.xml");
			
			doc.getElementsByTagName("snowingPercentage").item(0)
				.setTextContent(this.globalConfig.get("snowCoefficient"));
			
			doc.getElementsByTagName("rainingPercentage").item(0)
				.setTextContent(this.globalConfig.get("rainCoefficient"));
			
			doc.getElementsByTagName("fogPercentage").item(0)
				.setTextContent(this.globalConfig.get("fogCoefficient"));
			
			this.exportXml(this.mainDirectory + this.globalConfig.get("fileScenarioXML"), doc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create the settings XML file, used by OpenDS for various server configuration.
	 * Nothing useful to change here
	 */
	public void createSettingsXml() {
		try {
			Document doc = this.importXml("assets/DrivingTasks/Template/settings.xml");
			
			// TODO: add the ability to edit the 'settings.xml' file
			
			this.exportXml(this.mainDirectory + this.globalConfig.get("fileSettingsXML"), doc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Utility method that loads a XML file and build a Document object with it
	 * 
	 * @param filePath
	 * @return the loaded XML file
	 */
	private Document importXml(String filePath) {
		Document doc = this.docBuilder.newDocument();
		try {
			doc = docBuilder.parse(filePath);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	/**
	 * Utility method that save a XML file from a Document object
	 * 
	 * @param filePath the path to the XML file
	 * @param xml the document to write as XMl
	 */
	private void exportXml(String filePath, Document xml) {
		try {
			Files.deleteIfExists((new File(filePath)).toPath());
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			
			DOMSource source = new DOMSource(xml);
			StreamResult result = new StreamResult(new File(filePath));
			
			DocumentType doctype = xml.getDoctype();
			if(doctype != null) {
				transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
			}			
			
//			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			
			transformer.transform(source, result);
			
		} catch (IOException | TransformerException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Method initializing some primary configuration values
	 */
	private void initGlobalConfig() {
		this.globalConfig.put("fileMainXML", "main.xml");
		this.globalConfig.put("fileInteractionXML", "interaction.xml");
		this.globalConfig.put("fileScenarioXML", "scenario.xml");
		this.globalConfig.put("fileSceneXML", "scene.xml");
		this.globalConfig.put("fileSettingsXML", "settings.xml");
		
		// Settings config
		this.globalConfig.put("snowCoefficient", "-1");
		this.globalConfig.put("rainCoefficient", "-1");
		this.globalConfig.put("fogCoefficient", "-1");
		
		// Scene config
		this.globalConfig.put("gravity", "9.81");
	}

}

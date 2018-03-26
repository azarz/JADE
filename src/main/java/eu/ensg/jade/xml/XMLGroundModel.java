package eu.ensg.jade.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * XMLGroundModel is the class which hold data representing a model connected to a terrain (heightmap),
 * as defined in a scene.xml file
 * 
 * @author JADE
 *
 */
public class XMLGroundModel extends XMLModel implements IXMLExport {
	
// ========================== ATTRIBUTES ===========================
	private String material;
	
	private XMLTerrain terrain;
	
	
// ========================== CONSTRUCTORS =========================

	/**
	 * Simple constructor with basic model informations
	 * 
	 * @param id the ID of the ground model
	 * @param material the path to the material used by this model
	 * @param terrain the terrain geometry linked to this model
	 */
	public XMLGroundModel(String id, String material, XMLTerrain terrain) {
		super(id, "");
		this.material = material;
		this.terrain = terrain;
	}
	
	/**
	 * Constructor with model information and transformation informations
	 * 
	 * @param id the ID of the ground model
	 * @param material the path to the material used by this model
	 * @param terrain the terrain geometry linked to this model
	 * @param scale the scale Vector
	 * @param rotation the rotation Vector
	 * @param translation the translation Vector
	 */
	public XMLGroundModel(String id, String material, XMLTerrain terrain, double[] scale, double[] rotation, double[] translation) {
		super(id, "", scale, rotation, translation);
		this.material = material;
		this.terrain = terrain;
	}
	
// ========================== GETTERS/SETTERS ======================

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}
	
	public XMLTerrain getTerrain() {
		return terrain;
	}

	public void setTerrain(XMLTerrain terrain) {
		this.terrain = terrain;
	}
	
	
// ========================== METHODS ==============================
	
	
	/**
	 * @see eu.ensg.jade.xml.XMLModel#toXMLElement(org.w3c.dom.Document)
	 */
	@Override
	public Element toXMLElement(Document doc){
		Element model = super.toXMLElement(doc);
		
		Element shadowMode = doc.createElement("shadowMode");
		shadowMode.appendChild(doc.createTextNode("Receive"));
		model.appendChild(shadowMode);
		
		// Set the material of the Ground (as a key)
		Element material = doc.createElement("material");
		material.setAttribute("key", this.material);
		Node scale = model.getElementsByTagName("visible").item(0);
		model.insertBefore(material, scale);
		
		
		// Link model to terrain geometry
		model.setAttribute("ref", this.terrain.getId());
		
		return model;
	}	
	
}

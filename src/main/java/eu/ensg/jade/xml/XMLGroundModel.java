package eu.ensg.jade.xml;

import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLGroundModel extends XMLModel implements IXMLExport {
	
// ========================== ATTRIBUTES ===========================
	private String material;
	
	private String terrainId;
	
	
// ========================== CONSTRUCTORS =========================
	
	public XMLGroundModel(String id, String material, String terrainId) {
		super(id, "");
		this.material = material;
		this.terrainId = terrainId;
	}
	
	public XMLGroundModel(String id, String material, XMLTerrain terrain) {
		super(id, "");
		this.material = material;
		this.terrainId = terrain.getId();
	}
	
	public XMLGroundModel(String id, String material, String terrainId, Vector<Double> scale, Vector<Double> rotation, Vector<Double> translation) {
		super(id, "", scale, rotation, translation);
		this.material = material;
		this.terrainId = terrainId;
	}
	
// ========================== GETTERS/SETTERS ======================

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}
	
	
// ========================== METHODS ==============================
	
	
	@Override
	public Element toXMLElement(Document doc){
		Element model = super.toXMLElement(doc);
		
		// Set the material of the Ground (as a key)
		Element material = doc.createElement("material");
		material.setAttribute("key", this.material);
		model.appendChild(material);
		
		model.appendChild(doc.createElement("shadowMode").appendChild(doc.createTextNode("Receive")));
		
		// Link model to terrain geometry
		model.setAttribute("ref", this.terrainId);
		
		return model;
	}	
	
}

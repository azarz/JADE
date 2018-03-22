package eu.ensg.jade.xml;

import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLGroundModel extends XMLModel implements INodeExport {
	
// ========================== ATTRIBUTES ===========================
	private String material;
	
	private XMLTerrain terrain;
	
// ========================== CONSTRUCTORS =========================
	
	public XMLGroundModel(String id, String material, XMLTerrain terrain) {
		super(id, "");
		this.material = material;
		this.terrain = terrain;
	}
	
	public XMLGroundModel(String id, String material, XMLTerrain terrain, Vector<Double> scale, Vector<Double> rotation, Vector<Double> translation) {
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
	
	
	
// ========================== METHODS ==============================
	
	
	@Override
	public Element toNode(Document doc){
		
		Element material = doc.createElement("material");
		material.setAttribute("key", this.material);
		return null;
		
	}
	
	
	
}

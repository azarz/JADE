package eu.ensg.jade.xml;

import java.util.Vector;

public class XMLGroundModel extends XMLModel {
	
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
	
	
	
}

package eu.ensg.jade.xml;

import java.util.Vector;

public class XMLModel extends XMLElement {
	
// ========================== ATTRIBUTES ===========================
	
	protected String key;
	protected String ref;
	
	protected double mass;
	protected boolean visible;
	protected String collision;	
	
// ========================== CONSTRUCTORS =========================
	
	public XMLModel(String id, String key) {
		super(id);
		this.key = key;
		this.ref = "";
		
		this.collision = "meshShape";
		this.visible = true;
		this.mass = 0;
	}
	
	public XMLModel(String id, String key, Vector<Double> scale, Vector<Double> rotation, Vector<Double> translation) {
		super(id, scale, rotation, translation);
		this.key = key;
		this.ref = "";
		
		this.collision = "meshShape";
		this.visible = true;
		this.mass = 0;
	}
	
// ========================== GETTERS/SETTERS ======================

	public String getCollision() {
		return collision;
	}

	public void setCollision(String collision) {
		this.collision = collision;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}
	
	
	
// ========================== METHODS ==============================
}

package eu.ensg.jade.xml;

import java.util.Vector;

public class XMLElement {
	
// ========================== ATTRIBUTES ===========================
	
	protected String id;
	
	protected Vector<Double> scale;
	protected Vector<Double> rotation;
	protected Vector<Double> translation;
	
// ========================== CONSTRUCTORS =========================
	
	public XMLElement(String id) {
		this.id = id;
	}
	
	public XMLElement(String id, Vector<Double> scale, Vector<Double> rotation, Vector<Double> translation) {
		this.id = id;
		
		this.scale = scale;
		this.rotation = rotation;
		this.translation = translation;
	}
	
// ========================== GETTERS/SETTERS ======================

	public Vector<Double> getScale() {
		return scale;
	}

	public void setScale(Vector<Double> scale) {
		this.scale = scale;
	}

	public Vector<Double> getRotation() {
		return rotation;
	}

	public void setRotation(Vector<Double> rotation) {
		this.rotation = rotation;
	}

	public Vector<Double> getTranslation() {
		return translation;
	}

	public void setTranslation(Vector<Double> translation) {
		this.translation = translation;
	}
	
	

}

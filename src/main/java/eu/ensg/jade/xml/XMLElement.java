package eu.ensg.jade.xml;

import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLElement implements INodeExport {
	
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
	
// ========================== METHODS ==============================

	@Override
	public Element toNode(Document doc) {
		Element origin = doc.createElement("origin");
		
		Element scale = doc.createElement("scale");
		Element rotation = doc.createElement("rotation");
		Element translation = doc.createElement("translation");
	
		Element vector;
		
		
		// Scale node
		vector = doc.createElement("vector");
		vector.setAttribute("jtype", "java_lang_Float");
		vector.setAttribute("size", "3");
		for(Double value: this.scale) {
			vector.appendChild(doc.createElement("entry").appendChild(doc.createTextNode(String.valueOf(value))));
		}
		scale.appendChild(vector);
		
		// Rotation node
		vector = doc.createElement("vector");
		vector.setAttribute("jtype", "java_lang_Float");
		vector.setAttribute("size", "3");
		for(Double value: this.rotation) {
			vector.appendChild(doc.createElement("entry").appendChild(doc.createTextNode(String.valueOf(value))));
		}
		rotation.appendChild(vector);
		rotation.setAttribute("quaternion", "false");
		
		// Translation node
		vector = doc.createElement("vector");
		vector.setAttribute("jtype", "java_lang_Float");
		vector.setAttribute("size", "3");
		for(Double value: this.translation) {
			vector.appendChild(doc.createElement("entry").appendChild(doc.createTextNode(String.valueOf(value))));
		}
		translation.appendChild(vector);		
		
		return origin;
	}	

}

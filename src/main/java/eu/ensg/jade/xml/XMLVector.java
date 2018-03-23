package eu.ensg.jade.xml;

import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * XMLVector is the class which hold data representing a transformation vector.
 * It is composed of a scaling vector, a rotation vector, and a translation vector
 * 
 * @author JADE
 *
 */
public class XMLVector implements IXMLExport {
	
// ========================== ATTRIBUTES ===========================
	
	protected Vector<Double> scale;
	protected Vector<Double> rotation;
	protected Vector<Double> translation;
	
// ========================== CONSTRUCTORS =========================
	
	public XMLVector() {
		this.scale = new Vector<>(3);
		this.rotation = new Vector<>(3);
		this.translation = new Vector<>(3);
	}
	
	public XMLVector(Vector<Double> scale, Vector<Double> rotation, Vector<Double> translation) {		
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

	/**
	 * @see eu.ensg.jade.xml.IXMLExport#toXMLElement(org.w3c.dom.Document)
	 */
	@Override
	public Element toXMLElement(Document doc) {
		Element origin = doc.createElement("origin");		
	
		Element vector;
		
		// Scale node
		Element scale = doc.createElement("scale");
		vector = doc.createElement("vector");
		vector.setAttribute("jtype", "java_lang_Float");
		vector.setAttribute("size", "3");
		for(Double value: this.scale) {
			vector.appendChild(doc.createElement("entry").appendChild(doc.createTextNode(String.valueOf(value))));
		}
		scale.appendChild(vector);
		origin.appendChild(scale);
		
		// Rotation node
		Element rotation = doc.createElement("rotation");
		vector = doc.createElement("vector");
		vector.setAttribute("jtype", "java_lang_Float");
		vector.setAttribute("size", "3");
		for(Double value: this.rotation) {
			vector.appendChild(doc.createElement("entry").appendChild(doc.createTextNode(String.valueOf(value))));
		}
		rotation.appendChild(vector);
		rotation.setAttribute("quaternion", "false");
		origin.appendChild(rotation);
		
		// Translation node
		Element translation = doc.createElement("translation");
		vector = doc.createElement("vector");
		vector.setAttribute("jtype", "java_lang_Float");
		vector.setAttribute("size", "3");
		for(Double value: this.translation) {
			vector.appendChild(doc.createElement("entry").appendChild(doc.createTextNode(String.valueOf(value))));
		}
		translation.appendChild(vector);
		origin.appendChild(translation);
		
		return origin;
	}	

}

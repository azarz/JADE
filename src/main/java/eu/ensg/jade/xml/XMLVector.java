package eu.ensg.jade.xml;

import java.util.Arrays;

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
	
	protected double[] scale;
	protected double[] rotation;
	protected double[] translation;
	
// ========================== CONSTRUCTORS =========================
	
	public XMLVector() {
		this.scale = new double[3];
		this.rotation = new double[3];
		this.translation = new double[3];

		Arrays.fill(this.scale, 0);
		Arrays.fill(this.rotation, 0);
		Arrays.fill(this.translation, 0);
	}
	
	public XMLVector(double[] scale, double[] rotation, double[] translation) {		
		this.scale = scale;
		this.rotation = rotation;
		this.translation = translation;
	}
	
// ========================== GETTERS/SETTERS ======================

	public double[] getScale() {
		return scale;
	}

	public void setScale(double[] scale) {
		this.scale = scale;
	}

	public double[] getRotation() {
		return rotation;
	}

	public void setRotation(double[] rotation) {
		this.rotation = rotation;
	}

	public double[] getTranslation() {
		return translation;
	}

	public void setTranslation(double[] translation) {
		this.translation = translation;
	}
	
// ========================== METHODS ==============================

	/**
	 * @see eu.ensg.jade.xml.IXMLExport#toXMLElement(org.w3c.dom.Document)
	 */
	@Override
	public Element toXMLElement(Document doc) {
		Element origin = doc.createElement("origin");
	
		Element vector; Element entry;
		
		// Scale node
		Element scale = doc.createElement("scale");
		vector = doc.createElement("vector");
		vector.setAttribute("jtype", "java_lang_Float");
		vector.setAttribute("size", "3");
		
		for(double value: this.scale) {
			entry = doc.createElement("entry");
			entry.appendChild(doc.createTextNode(String.valueOf(value)));
			vector.appendChild(entry);
		}
		scale.appendChild(vector);
		origin.appendChild(scale);
		
		// Rotation node
		Element rotation = doc.createElement("rotation");
		vector = doc.createElement("vector");
		vector.setAttribute("jtype", "java_lang_Float");
		vector.setAttribute("size", "3");
		
		for(double value: this.rotation) {
			entry = doc.createElement("entry");
			entry.appendChild(doc.createTextNode(String.valueOf(value)));
			vector.appendChild(entry);
		}
		rotation.appendChild(vector);
		rotation.setAttribute("quaternion", "false");
		origin.appendChild(rotation);
		
		// Translation node
		Element translation = doc.createElement("translation");
		vector = doc.createElement("vector");
		vector.setAttribute("jtype", "java_lang_Float");
		vector.setAttribute("size", "3");
		
		for(double value: this.translation) {
			entry = doc.createElement("entry");
			entry.appendChild(doc.createTextNode(String.valueOf(value)));
			vector.appendChild(entry);
		}
		translation.appendChild(vector);
		origin.appendChild(translation);
		
		return origin;
	}	

}

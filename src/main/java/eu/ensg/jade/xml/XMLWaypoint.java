package eu.ensg.jade.xml;

import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * XMLWaypoint is the class which hold data representing a way point, as defined in a scenario.xml file
 * 
 * @author JADE
 *
 */
public class XMLWaypoint implements IXMLExport {
	
// ========================== ATTRIBUTES ===========================
	
	private String id;
	
	private double speed;
	
	private Vector<Double> translation;
	
// ========================== CONSTRUCTORS =========================
	
	public XMLWaypoint(String id) {
		this.id = id;
		this.speed = 0;
		this.translation = new Vector<>(3);
	}
	
	public XMLWaypoint(String id, double speed, Vector<Double> translation) {
		this.id = id;
		this.speed = speed;
		this.translation = translation;
	}
	
// ========================== GETTERS/SETTERS ======================
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
// ========================== METHODS ==============================

	/**
	 * @see eu.ensg.jade.xml.IXMLExport#toXMLElement(org.w3c.dom.Document)
	 */
	@Override
	public Element toXMLElement(Document doc) {
		Element waypoint = doc.createElement("wayPoint");
		waypoint.setAttribute("id", this.id);
		
		Element translation = doc.createElement("translation");
		Element vector = doc.createElement("vector");
		vector.setAttribute("jtype", "java_lang_Float");
		vector.setAttribute("size", "3");
		for(Double value: this.translation) {
			vector.appendChild(doc.createElement("entry").appendChild(doc.createTextNode(String.valueOf(value))));
		}
		translation.appendChild(vector);
		
		translation.appendChild(doc.createTextNode(String.valueOf(this.speed)));
		
		
		waypoint.appendChild(translation);
		
		
		return waypoint;
	}

}

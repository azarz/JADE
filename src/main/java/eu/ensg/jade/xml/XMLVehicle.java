package eu.ensg.jade.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLVehicle implements INodeExport {
	
// ========================== ATTRIBUTES ===========================
	
	private List<Vector<Double>> waypoints;
	
// ========================== CONSTRUCTORS =========================
	
	public XMLVehicle() {
		this.waypoints = new ArrayList<Vector<Double>>();
	}
	
// ========================== GETTERS/SETTERS ======================
	
	public void addWaypoint(Vector<Double> point) {
		if(point.size() == 3){
			this.waypoints.add(point);
		}
	}
	
// ========================== METHODS ==============================
	
	@Override
	public Element toXMLElement(Document doc) {
		// TODO Auto-generated method stub
		return null;
	}

}

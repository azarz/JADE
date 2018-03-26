package eu.ensg.jade.xml;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * XMLVehicle is the class which hold data representing a vehicle, as defined in a scenario.xml file
 * 
 * @author JADE
 *
 */
public class XMLVehicle implements IXMLExport {
	
// ========================== ATTRIBUTES ===========================
	
	private String id;
	
	private String model;
	
	private double mass;
	
	private double acceleration;
	
	private double decelerationBrake;
	
	private double decelerationFreeWheel;
	
	private double maxDistanceFromPath;
	
	private List<List<Double>> waypoints;
	
	private int startWayPoint;
	
// ========================== CONSTRUCTORS =========================
	
	public XMLVehicle(String id) {
		this.id = id;
		this.waypoints = new ArrayList<List<Double>>();
	}
	
// ========================== GETTERS/SETTERS ======================
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public double getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	public double getDecelerationBrake() {
		return decelerationBrake;
	}

	public void setDecelerationBrake(double decelerationBrake) {
		this.decelerationBrake = decelerationBrake;
	}

	public double getDecelerationFreeWheel() {
		return decelerationFreeWheel;
	}

	public void setDecelerationFreeWheel(double decelerationFreeWheel) {
		this.decelerationFreeWheel = decelerationFreeWheel;
	}

	public double getMaxDistanceFromPath() {
		return maxDistanceFromPath;
	}

	public void setMaxDistanceFromPath(double maxDistanceFromPath) {
		this.maxDistanceFromPath = maxDistanceFromPath;
	}

	public int getStartWayPoint() {
		return startWayPoint;
	}

	public void setStartWayPoint(int startWayPoint) {
		this.startWayPoint = startWayPoint;
	}
	
	public void addWaypoint(String id, List<Double> point) {
		if(point.size() == 3){
			this.waypoints.add(point);
		}
	}
	
// ========================== METHODS ==============================
	
	

	/**
	 * @see eu.ensg.jade.xml.IXMLExport#toXMLElement(org.w3c.dom.Document)
	 */
	@Override
	public Element toXMLElement(Document doc) {
		Element vehicle = doc.createElement("vehicle");
		
		vehicle.setAttribute("id", this.id);
		
		
		return vehicle;
	}

}

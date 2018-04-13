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
	
	/**
	 * The id
	 */
	private String id;
	
	/**
	 * The model
	 */
	private String model;
	
	/**
	 * The mass
	 */
	private double mass;
	
	/**
	 * The acceleration
	 */
	private double acceleration;
	
	/**
	 * The decelerationBrake value
	 */
	private double decelerationBrake;
	
	/**
	 * The free wheel deceleration 
	 */
	private double decelerationFreeWheel;
	
	/**
	 * The maximal distance from path
	 */
	private double maxDistanceFromPath;
	
	/**
	 * The way points
	 */
	private List<List<Double>> waypoints;
	
	/**
	 * The start way point index
	 */
	private int startWayPoint;
	
// ========================== CONSTRUCTORS =========================
	
	/**
	 * Constructor with the ID
	 * 
	 * @param id The Id
	 */
	public XMLVehicle(String id) {
		this.id = id;
		this.waypoints = new ArrayList<List<Double>>();
	}
	
// ========================== GETTERS/SETTERS ======================
	
	/**
	 * Allows to get the vehicle id
	 * 
	 * @return The id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Allows to set the id
	 * 
	 * @param id The new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Allows to access to the model
	 * 
	 * @return The model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Allows to ste the model
	 * 
	 * @param model The new model
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Allows to get the mass
	 * 
	 * @return The mass
	 */
	public double getMass() {
		return mass;
	}

	/**
	 * Allows to set the mass
	 * 
	 * @param mass The new mass
	 */
	public void setMass(double mass) {
		this.mass = mass;
	}

	/**
	 * Allows to get the acceleration
	 * 
	 * @return The acceleration
	 */
	public double getAcceleration() {
		return acceleration;
	}

	/**
	 * Allows to set the acceleration
	 * 
	 * @param acceleration The new acceleration
	 */
	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	/**
	 * Allows to access to the deceleration brake
	 * 
	 * @return The deceleration brake value
	 */
	public double getDecelerationBrake() {
		return decelerationBrake;
	}

	/**
	 * Allows to set the deceleration brake
	 * 
	 * @param decelerationBrake The new deceleration brake value
	 */
	public void setDecelerationBrake(double decelerationBrake) {
		this.decelerationBrake = decelerationBrake;
	}
	
	/**
	 * Allows to access to the deceleration free wheel
	 * 
	 * @return The deceleration free wheel value
	 */
	public double getDecelerationFreeWheel() {
		return decelerationFreeWheel;
	}
	
	/**
	 * Allows to set the deceleration free wheel
	 * 
	 * @param decelerationFreeWheel The new deceleration free wheel value
	 */
	public void setDecelerationFreeWheel(double decelerationFreeWheel) {
		this.decelerationFreeWheel = decelerationFreeWheel;
	}

	/**
	 * Allows to get the maximal distance from path
	 * 
	 * @return The maximal distance from path
	 */
	public double getMaxDistanceFromPath() {
		return maxDistanceFromPath;
	}

	/**
	 * Allows to set the maximal distance from path
	 * 
	 * @param maxDistanceFromPath The new maximal distance from path
	 */
	public void setMaxDistanceFromPath(double maxDistanceFromPath) {
		this.maxDistanceFromPath = maxDistanceFromPath;
	}

	/**
	 * Allows to access to the way point start
	 * 
	 * @return The way point start
	 */
	public int getStartWayPoint() {
		return startWayPoint;
	}
	
	/**
	 * Allows to set the way point start
	 * 
	 * @param startWayPoint The new way point start
	 */
	public void setStartWayPoint(int startWayPoint) {
		this.startWayPoint = startWayPoint;
	}
	
	/**
	 * Allows to add a way point
	 * 
	 * @param id The id
	 * @param point The new point
	 */
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

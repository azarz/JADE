package eu.ensg.jade.semantic;

import java.util.HashMap;

public class IntersectionColl {
	
// ========================== ATTRIBUTES ===========================

	/**
	 * Map of intersections and their IDs
	 */
	private HashMap<String, Intersection> mapIntersection;

// ========================== CONSTRUCTORS =========================	

	/**
	 * Constructor with all the fields
	 * 
	 * @param mapIntersection The HashMap of intersections
	 */
	public IntersectionColl(HashMap<String, Intersection> mapIntersection) {
		this.mapIntersection = mapIntersection;
	}

	/**
	 * Empty constructor
	 */
	public IntersectionColl() {
		this.mapIntersection = new HashMap<String,Intersection>();
	}

// ========================== GETTERS/SETTERS ======================

	/**
	 * Allows to access the map of intersections
	 * 
	 * @return the map of intersections
	 */
	public HashMap<String, Intersection> getMapIntersection() {
		return mapIntersection;
	}
	
// ========================== METHODS ==============================	
	
	/**
	 * Allows to access the map of intersections
	 * 
	 * @param id the identifier of the intersection : str(xcoord+ycoord)
	 * @param inter the intersection to add to the network
	 */
	public void addIntersection(String id, Intersection inter) {
		this.mapIntersection.put(id, inter);
	}
	
}

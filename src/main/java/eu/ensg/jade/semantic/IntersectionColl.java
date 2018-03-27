package eu.ensg.jade.semantic;

import java.util.HashMap;

import com.sun.javafx.collections.MappingChange.Map;

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
	 * @param mapIntersection
	 */
	public IntersectionColl(HashMap<String, Intersection> mapIntersection) {
		super();
		this.mapIntersection = mapIntersection;
	}

	/**
	 * Empty constructor
	 */
	public IntersectionColl() {
		super();
		this.mapIntersection = new HashMap<String,Intersection>();
		// TODO Auto-generated constructor stub
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

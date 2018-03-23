package eu.ensg.jade.semantic;

import com.sun.javafx.collections.MappingChange.Map;

public class IntersectionColl {
	
	// ========================== ATTRIBUTES ===========================

	/**
	 * Map of intersections and their IDs
	 */
	private Map<String, Intersection> mapIntersection;

	// ========================== CONSTRUCTORS =========================	
	
	/**
	 * Constructor with all the fields
	 * @param mapIntersection
	 */
	public IntersectionColl(Map<String, Intersection> mapIntersection) {
		super();
		this.mapIntersection = mapIntersection;
	}

	/**
	 * Empty constructor
	 */
	public IntersectionColl() {
		super();
		// TODO Auto-generated constructor stub
	}

	// ========================== GETTERS/SETTERS ======================

	/**
	 * Allows to access the map of intersections
	 * 
	 * @return the map of intersections
	 */
	public Map<String, Intersection> getMapIntersection() {
		return mapIntersection;
	}
	
	
	
}

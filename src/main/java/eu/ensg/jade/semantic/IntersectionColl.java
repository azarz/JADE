package eu.ensg.jade.semantic;

import com.sun.javafx.collections.MappingChange.Map;

public class IntersectionColl {
	
	// ========================== ATTRIBUTES ===========================

	private Map<String, Intersection> mapIntersection;

	// ========================== CONSTRUCTORS =========================	

	public IntersectionColl(Map<String, Intersection> mapIntersection) {
		super();
		this.mapIntersection = mapIntersection;
	}

	public IntersectionColl() {
		super();
		// TODO Auto-generated constructor stub
	}

	// ========================== GETTERS/SETTERS ======================

	public Map<String, Intersection> getMapIntersection() {
		return mapIntersection;
	}
	
	
	
}

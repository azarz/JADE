package eu.ensg.jade.semantic;

import java.util.HashMap;

import com.sun.javafx.collections.MappingChange.Map;

public class IntersectionColl {
	
	// ========================== ATTRIBUTES ===========================

	private HashMap<String, Intersection> mapIntersection;

	// ========================== CONSTRUCTORS =========================	

	public IntersectionColl(HashMap<String, Intersection> mapIntersection) {
		super();
		this.mapIntersection = mapIntersection;
	}

	public IntersectionColl() {
		super();
		this.mapIntersection = new HashMap<String,Intersection>();
		// TODO Auto-generated constructor stub
	}

// ========================== GETTERS/SETTERS ======================

	public HashMap<String, Intersection> getMapIntersection() {
		return mapIntersection;
	}
	
// ========================== METHODS ==============================	
	
	public void addIntersection(String id, Intersection inter) {
		this.mapIntersection.put(id, inter);
		
	}
	
	
	
}

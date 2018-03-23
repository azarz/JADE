package eu.ensg.jade.semantic;



import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

/**
 * Intersection is the class implementing
 * 
 * @author JADE
 */

public class Intersection {
	

// ========================== ATTRIBUTES ===========================
	private Coordinate geometry;
	private List<String> roadId;
	private static int idInter = 0;

// ========================== CONSTRUCTORS =========================	
	
	public Intersection(Coordinate geometry, List<String> roadId) {
		this.geometry = geometry;
		this.roadId = roadId;
		idInter++;
	}

	public Intersection(Coordinate geometry) {
		this.geometry = geometry;
		this.roadId = new ArrayList<>();
		idInter++;
	}

	public Intersection() {
		
	}

// ========================== GETTERS/SETTERS ======================

	public Coordinate getGeometry() {
		return geometry;
	}

	public List<String> getRoadId() {
		return roadId;
	}

	public int getIdInter() {
		return idInter;
	}
	
// ========================== METHODS ==============================

	public void addRoadID(String ID){
		this.roadId.add(ID);
	}

}

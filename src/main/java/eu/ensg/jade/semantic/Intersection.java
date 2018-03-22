package eu.ensg.jade.semantic;

import java.util.List;

import eu.ensg.jade.geometricObject.Point;

/**
 * Intersection is the class implementing
 * 
 * @author JADE
 */

public class Intersection {
	

// ========================== ATTRIBUTES ===========================
	private Point geometry;
	private List<SurfaceRoad> roads;
	private String roadId;

// ========================== CONSTRUCTORS =========================	
	
	public Intersection(Point geometry, List<SurfaceRoad> roads, String roadId) {
		this.geometry = geometry;
		this.roads = roads;
		this.roadId = roadId;
	}

	public Intersection() {
		
	}


// ========================== GETTERS/SETTERS ======================

	public Point getGeometry() {
		return geometry;
	}

	public List<SurfaceRoad> getRoads() {
		return roads;
	}

	public String getRoadId() {
		return roadId;
	}
	

// ========================== METHODS ==============================


}

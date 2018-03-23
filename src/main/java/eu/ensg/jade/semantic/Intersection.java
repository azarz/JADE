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
	/**
	 * The attribute containing the geometry of the intersection
	 */
	private Point geometry;
	
	/**
	 * The attribute containing the list of roads composing the intersection
	 */
	private List<SurfaceRoad> roads;
	
	/**
	 * The attribute containing the road IDs (String ? List ?)
	 */
	private String roadId;

// ========================== CONSTRUCTORS =========================	
	
	/**
	 * Constructor using all fields
	 * @param geometry
	 * @param roads
	 * @param roadId
	 */
	public Intersection(Point geometry, List<SurfaceRoad> roads, String roadId) {
		this.geometry = geometry;
		this.roads = roads;
		this.roadId = roadId;
	}

	/**
	 * Empty constructor
	 */
	public Intersection() {
		
	}


// ========================== GETTERS/SETTERS ======================

	/**
	 * Allows to access the intersection geometry
	 * 
	 * @return the intersection geometry
	 */
	public Point getGeometry() {
		return geometry;
	}

	/**
	 * Allows to access the list of roads composing the intersection 
	 * 
	 * @return the list of roads composing the intersection
	 */
	public List<SurfaceRoad> getRoads() {
		return roads;
	}

	/**
	 * Allows to access the intersection roads IDs
	 * 
	 * @return the intersection roads IDs
	 */
	public String getRoadId() {
		return roadId;
	}
	

// ========================== METHODS ==============================


}

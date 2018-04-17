package eu.ensg.jade.semantic;



import java.util.HashMap;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;

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
	private Coordinate geometry;
	
	/**
	 * The attribute containing the list of ID of roads composing the intersection
	 */
	private Map<String, Boolean> roadId;

// ========================== CONSTRUCTORS =========================	
	
	/**
	 * Constructor using geometry field
	 * 
	 * @param geometry The geometry of the new object
	 */
	public Intersection(Coordinate geometry) {
		this.geometry = geometry;
		this.roadId = new HashMap<String, Boolean>();
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
	public Coordinate getGeometry() {
		return geometry;
	}

	/**
	 * Allows to access the list of ID of roads composing the intersection 
	 * 
	 * @return the list of ID of roads composing the intersection
	 */
	public Map<String, Boolean> getRoadId() {
		return roadId;
	}
	
// ========================== METHODS ==============================
	
	/**
	 * Adds a road to the intersection 
	 * 
	 * @param ID The ID of the new Road
	 * @param init The boolean value if the first point is on the intersection or not
	 */
	public void addRoadID(String ID,Boolean init ){
		this.roadId.put(ID, init);
	}

}

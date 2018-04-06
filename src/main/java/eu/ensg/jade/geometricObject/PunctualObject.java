package eu.ensg.jade.geometricObject;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Point is the class implementing the punctual objects to be added to the scene
 * 
 * @author JADE
 */

public abstract class PunctualObject {

// ========================== ATTRIBUTES ===========================
	/**
	 * The punctual coordinate of the furniture
	 */
	protected Coordinate coord;
	
// ========================== CONSTRUCTORS =========================
	
	public PunctualObject(Coordinate coord) {
		this.coord = coord;
	}
	
// ========================== GETTERS/SETTERS ======================
	
	/**
	 * Gets the coordinates of the furniture
	 * 
	 * @return the (x,y,z) coordinate of the punctual object
	 */
	public Coordinate getCoord() {
		return coord;
	}

// ========================== METHODS ==============================
	
}

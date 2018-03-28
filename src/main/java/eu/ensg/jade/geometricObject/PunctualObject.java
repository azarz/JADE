package eu.ensg.jade.geometricObject;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Point is the class implementing the punctual objects to be added to the scene
 * 
 * @author JADE
 */

public abstract class PunctualObject extends WorldObject {

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
	
	/**
	 * This method will have a return that will soon be specified
	 * 
	 * @see eu.ensg.jade.geometricObject.WorldObject#toOBJ(java.util.List)
	 */
	@Override
	public abstract String toOBJ(List<Integer> indexOffsets, double xOffset, double yOffset);
}

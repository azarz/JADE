package eu.ensg.jade.semantic;

import java.util.List;

import eu.ensg.jade.geometricObject.Point;

/**
 * Vegetation is the class implementing the vegetation to be added to the scene
 * 
 * @author JADE
 */

public class PointVegetation extends Point {
	
// ========================== ATTRIBUTES ===========================
	
	/**
	 * The furniture nature (stop, pedestrian crossing...)
	 */
	private String nature;
	
	/**
	 * The tree geometry
	 */
	private String geometry;

// ========================== METHODS ==============================
	
	
	/**
	 * @see eu.ensg.jade.geometricObject.Point#toOBJ(java.util.List, double, double)
	 */
	@Override
	public String toOBJ(List<Integer> indexOffsets, double xOffset, double yOffset) {
		// TODO Auto-generated method stub
		return null;
	}

}

package eu.ensg.jade.semantic;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

import eu.ensg.jade.geometricObject.PunctualObject;

/**
 * StreetFurniture is the class implementing the street furniture to be added to the scene
 * 
 * @author JADE
 */

public class StreetFurniture extends PunctualObject {
	

// ========================== ATTRIBUTES ===========================

	/**
	 * The furniture type (folder path)
	 */
	private String path;
	
	/**
	 * The furniture id
	 */
	private static int id=0;	
	
	/**
	 * The furniture rotation
	 */
	private double rotation;
	
// ========================== CONSTRUCTORS =========================			

	public StreetFurniture(Coordinate coord) {
		super(coord);
		id++;
	}
	
	public StreetFurniture(String path, Coordinate coord, double rotation) {
			super(coord);
			this.path = path;
			this.rotation = rotation;
			id++;
		}

// ========================== GETTERS/SETTERS ======================
	
	/**
	 * Gets the Nature of the furniture
	 * 
	 * @return the nature of the furniture
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Gets the ID of the furniture
	 * 
	 * @return the id of the furniture
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the rotation of the furniture
	 * 
	 * @return the rotation of the furniture
	 */
	public double getRotation() {
		return rotation;
	}

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

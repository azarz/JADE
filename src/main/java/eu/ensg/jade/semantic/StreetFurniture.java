package eu.ensg.jade.semantic;

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

	/**
	 * Constructor with the coordinates
	 * 
	 * @param coord The coordinates of the new furniture
	 */
	public StreetFurniture(Coordinate coord) {
		super(coord);
		id++;
	}
	
	/**
	 * Construction with all fields
	 * 
	 * @param path Path to the file for texture and shape
	 * @param coord The coordinates of the new furniture
	 * @param rotation The rotation to make the pannel face the right way
	 */
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

}

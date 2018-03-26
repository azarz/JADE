package eu.ensg.jade.semantic;

import com.vividsolutions.jts.geom.Coordinate;

import eu.ensg.jade.geometricObject.Point;

/**
 * StreetFurniture is the class implementing the street furniture to be added to the scene
 * 
 * @author JADE
 */

public class StreetFurniture extends Point {
	
// ========================== ENUM ===========================

	/**
	 * The different types of street furniture available
	 */
	private enum type {
		
	};

// ========================== ATTRIBUTES ===========================

	/**
	 * The furniture nature (stop, pedestrian crossing...)
	 */
	private String nature;
	
	/**
	 * The punctual coordinate of the furniture
	 */
	private Coordinate coord;
	
	/**
	 * The furniture id
	 */
	private String id;	
	
// ========================== CONSTRUCTORS =========================			
	
	/**
	 * Empty constructor
	 */
	public StreetFurniture() {
			super();
			// TODO Auto-generated constructor stub
		}
	
	public StreetFurniture(String nature, Coordinate coord, String id) {
			super();
			this.nature = nature;
			this.coord = coord;
			this.id = id;
		}

// ========================== GETTERS/SETTERS ======================
	
	/**
	 * Gets the Nature of the furniture
	 * 
	 * @return the nature of the furniture
	 */
	public String getNature() {
		return nature;
	}

	/**
	 * Gets the coordinates of the furniture
	 * 
	 * @return the (x,y,z) coordinate of the punctual object
	 */
	public Coordinate getCoord() {
		return coord;
	}

	/**
	 * Gets the ID of the furniture
	 * 
	 * @return the if of the furniture
	 */
	public String getId() {
		return id;
	}

// ========================== METHODS ==============================

	/**
	 * This method will have a return that will soon be specified
	 */
	public void toOBJ(){
		
	}
}

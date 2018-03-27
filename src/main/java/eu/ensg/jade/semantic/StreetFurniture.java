	package eu.ensg.jade.semantic;

import eu.ensg.jade.geometricObject.Point;

/**
 * StreetFurniture is the class implementing the street furniture to be added to the scene
 * 
 * @author JADE
 */

public class StreetFurniture extends Point {

// ========================== ATTRIBUTES ===========================

	/**
	 * The furniture nature (stop, pedestrian crossing...)
	 */
	private String nature;
	
	/**
	 * The furniture geometry
	 */
	private Point geometry;
	
	/**
	 * The furniture id
	 */
	private String id;
	
	/**
	 * The different types of street furniture available
	 *
	 */
	private enum type {
		
	};
	
	
	
	// ========================== CONSTRUCTORS =========================			
	
	/**
	 * Empty constructor
	 */
	public StreetFurniture() {
			super();
			// TODO Auto-generated constructor stub
		}
	
	
	
	
	/**
	 * Constructor with every fields
	 * @param nature
	 * @param geometry
	 * @param id
	 */
	public StreetFurniture(String nature, Point geometry, String id) {
			super();
			this.nature = nature;
			this.geometry = geometry;
			this.id = id;
		}




	// ========================== GETTERS/SETTERS ======================


	/**
	 * Allows to access the street furniture nature
	 * 
	 * @return the street furniture nature
	 */
	public String getNature() {
		return nature;
	}


	/**
	 * Allows to access the street furniture geometry
	 * 
	 * @return the street furniture geometry
	 */
	public Point getGeometry() {
		return geometry;
	}


	/**
	 * Allows to access the street furniture id
	 * 
	 * @return the street furniture id
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

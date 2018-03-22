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
	
	private enum type {
		
	};
	
	
	
	// ========================== CONSTRUCTORS =========================			
	
	
	public StreetFurniture() {
			super();
			// TODO Auto-generated constructor stub
		}
	
	
	
	
	
	
	public StreetFurniture(String nature, Point geometry, String id) {
			super();
			this.nature = nature;
			this.geometry = geometry;
			this.id = id;
		}




	// ========================== GETTERS/SETTERS ======================


	
	public String getNature() {
		return nature;
	}






	public Point getGeometry() {
		return geometry;
	}






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

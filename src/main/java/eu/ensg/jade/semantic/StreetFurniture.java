package eu.ensg.jade.semantic;

import com.vividsolutions.jts.geom.Coordinate;

import java.util.List;

import eu.ensg.jade.geometricObject.PunctualObject;

/**
 * StreetFurniture is the class implementing the street furniture to be added to the scene
 * 
 * @author JADE
 */

public class StreetFurniture extends PunctualObject {
	
// ========================== ENUM ===========================

	/**
	 * The different types of street furniture available
	 */
	private enum type {
		
	}

	private enum nat {
		
	}
// ========================== ATTRIBUTES ===========================

	/**
	 * The furniture nature (stop, pedestrian crossing...)
	 */
	private String nature;
	
	/**
	 * The furniture id
	 */
<<<<<<< HEAD
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
=======
	private static int id=0;	
	
// ========================== CONSTRUCTORS =========================			

	public StreetFurniture(Coordinate coord) {
		super(coord);
		this.id++;
	}
	
	public StreetFurniture(String nature, Coordinate coord) {
			super(coord);
>>>>>>> 3f3d954840c6c1509edaea803b41225f8301b9c7
			this.nature = nature;
			this.coord = coord;
			this.id++;
		}

<<<<<<< HEAD



	// ========================== GETTERS/SETTERS ======================


	/**
	 * Allows to access the street furniture nature
	 * 
	 * @return the street furniture nature
=======
// ========================== GETTERS/SETTERS ======================
	
	/**
	 * Gets the Nature of the furniture
	 * 
	 * @return the nature of the furniture
>>>>>>> 3f3d954840c6c1509edaea803b41225f8301b9c7
	 */
	public String getNature() {
		return nature;
	}

<<<<<<< HEAD

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
=======
	/**
	 * Gets the ID of the furniture
	 * 
	 * @return the if of the furniture
	 */
	public int getId() {
>>>>>>> 3f3d954840c6c1509edaea803b41225f8301b9c7
		return id;
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

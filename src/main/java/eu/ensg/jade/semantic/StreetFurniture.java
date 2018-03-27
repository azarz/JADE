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
	private static int id=0;	
	
// ========================== CONSTRUCTORS =========================			

	public StreetFurniture(Coordinate coord) {
		super(coord);
		this.id++;
	}
	
	public StreetFurniture(String nature, Coordinate coord) {
			super(coord);
			this.nature = nature;
			this.coord = coord;
			this.id++;
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
	 * Gets the ID of the furniture
	 * 
	 * @return the if of the furniture
	 */
	public int getId() {
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

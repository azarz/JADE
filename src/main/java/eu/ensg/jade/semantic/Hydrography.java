package eu.ensg.jade.semantic;

import java.util.List;

import com.vividsolutions.jts.geom.MultiPolygon;

import eu.ensg.jade.geometricObject.WorldObject;

/**
 * Hydrography is the class implementing a water surface object from the RGE
 * 
 * @author JADE
 */

public class Hydrography extends WorldObject {
	

// ========================== ATTRIBUTES ===========================

	/**
	 * The water surface nature
	 */
	private String nature;
	
	/** 
	 * The water surface average elevation
	 */
	private double z_average;
	
	/**
	 * The water surface geometry
	 */
	private MultiPolygon geometry;
			
// ========================== CONSTRUCTORS =========================	

	/**
	 * Constructor using all fields
	 * 
	 * @param nature
	 * @param z_average
	 * @param geometry
	 */
	public Hydrography(String nature,double z_average, MultiPolygon geometry) {
		this.nature = nature;
		this.z_average = z_average;
		this.geometry = geometry;
	}
		
// ========================== GETTERS/SETTERS ======================

	/**
	 * Allows to access the water surface nature (lake, water fall...)
	 * 
	 * @return the water surface nature
	 */
	public String getNature() {
		return nature;
	}


	/**
	 * Allows to access the water surface average elevation
	 * 
	 * @return the water surface average elevation
	 */
	public double getZ_average() {
		return z_average;
	}


	/**
	 * Allows to access the water surface geometry
	 * 
	 * @return the water surface geometry
	 */
	public MultiPolygon getGeometry() {
		return geometry;
	}

// ========================== METHODS ==============================

	/**
	 * Allows to add an elevation to a water surface
	 */
	public void addHeight() {

	}

	/**
	 * This method will have a return that will soon be specified
	 * 
	 * @see eu.ensg.jade.geometricObject.WorldObject#toOBJ(java.util.List)
	 */
	@Override
	public String toOBJ(List<Integer> indexOffsets, double xOffset, double yOffset) {
		// TODO Auto-generated method stub
		return null;
	}

}

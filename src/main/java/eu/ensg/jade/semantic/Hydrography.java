package eu.ensg.jade.semantic;

import com.jme3.app.R.string;
import com.vividsolutions.jts.geom.Polygon;

import eu.ensg.jade.geometricObject.ISurfacicObject;

/**
 * Hydrography is the class implementing a water surface object from the RGE
 * 
 * @author JADE
 */

public class Hydrography implements ISurfacicObject {
	

// ========================== ATTRIBUTES ===========================

	/**
	 * The water surface nature
	 */
	private String nature;
	/**
	 * The water surface height
	 */
	private double height;
	/**
	 * The water surface minimum elevation
	 */
	private double z_min;
	/** 
	 * The water surface maximum elevation
	 */
	private double z_max;
	/**
	 * The water surface geometry
	 */
	private Polygon geometry;

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
	 * Allows to set the water surface nature
	 * 
	 * @param nature the nature to be attributed 
	 */
	public void setNature(String nature) {
		this.nature = nature;
	}


	/**
	 * Allows to access the water surface height
	 * 
	 * @return the water surface height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Allows to set the water surface height
	 * 
	 * @param height the height to be attributed 
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	/**
	 * Allows to access the water surface minimum elevation
	 * 
	 * @return the water surface minimum elevation
	 */
	public double getZ_min() {
		return z_min;
	}

	/**
	 * Allows to set the water surface minimum elevation
	 * 
	 * @param z_min the minimum elevation to be attributed 
	 */
	public void setZ_min(double z_min) {
		this.z_min = z_min;
	}

	/**
	 * Allows to access the water surface maximum elevation
	 * 
	 * @return the water surface maximum elevation
	 */
	public double getZ_max() {
		return z_max;
	}

	/**
	 * Allows to set the water surface maximum elevation
	 * 
	 * @param z_max the maximum elevation to be attributed 
	 */
	public void setZ_max(double z_max) {
		this.z_max = z_max;
	}

// ========================== METHODS ==============================

	/**
	 * Allows to add an elevation to a water surface
	 */
	@Override
	public void addHeight() {

	}
	
	/**
	 * This method will have a return that will soon be specified
	 */
	public void toOBJ(){
		
	}

}

package eu.ensg.jade.semantic;

import java.util.List;

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
	 * The water surface average elevation
	 */
	private double z_average;
	/**
	 * The water surface geometry
	 */
	private Polygon geometry;
	
		
	// ========================== CONSTRUCTORS =========================	

		/**
		 * Constructor using all fields
		 * 
		 * @param nature
		 * @param z_average
		 * @param geometry
		 */
		public Hydrography(String nature,double z_average, Polygon geometry) {
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
	 * Allows to set the water surface nature
	 * 
	 * @param nature the nature to be attributed 
	 */
	public void setNature(String nature) {
		this.nature = nature;
	}

	/**
	 * Allows to access the water surface average elevation
	 * 
	 * @return the water surface average elevation
	 */
	public double getZ_avrage() {
		return z_average;
	}

	/**
	 * Allows to set the water surface average elevation
	 * 
	 * @param z_min the average elevation to be attributed 
	 */
	public void setZ_average(double z_min) {
		this.z_average = z_average;
	}

	/**
	 * Allows to access the water surface maximum elevation
	 * 
	 * @return the water surface maximum elevation
	 */
	public Polygon get_geometry() {
		return geometry;
	}

	/**
	 * Allows to set the water surface maximum elevation
	 * 
	 * @param z_max the maximum elevation to be attributed 
	 */
	public void set_geometry(Polygon geometry) {
		this.geometry = geometry;
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

package eu.ensg.jade.semantic;

import com.vividsolutions.jts.geom.MultiPolygon;

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
	public void setZ_average(double z_average) {
		this.z_average = z_average;
	}

	/**
	 * Allows to access the water surface geometry
	 * 
	 * @return the water surface geometry
	 */
	public MultiPolygon get_geometry() {
		return geometry;
	}

	/**
	 * Allows to set the water surface geometry
	 * 
	 * @param geometry the geometry to be attributed 
	 */
	public void set_geometry(MultiPolygon geometry) {
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

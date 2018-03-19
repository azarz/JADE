package eu.ensg.jade.semantic;

import com.vividsolutions.jts.geom.MultiPolygon;

import eu.ensg.jade.geometricObject.WorldObject;

/**
 * Vegetation is the class implementing the vegetation to be added to the scene
 * 
 * @author JADE
 */

public class SurfacicVegetation extends WorldObject {
	
// ========================== ATTRIBUTES ===========================
	
	/**
	 * The furniture nature (arboreal zone, forest...)
	 */
	private String nature;
	
	/**
	 * The tree geometry
	 */
	private MultiPolygon geometry;

// ========================== CONSTRUCTORS =========================	

	/**
	 * Constructor with all the fiels
	 * 
	 * @param geometry the polygon geometry of the vegetation
	 * @param natuer the nature of the vegetation surface
	 * 
	 **/

	public SurfacicVegetation(MultiPolygon geometry, String nature) {
		this.nature = nature;
		this.geometry = geometry;
	}
	
// ========================== GETTERS AND SETTERS ==============================
	
	/**
	 * @return the nature
	 */
	public String getNature() {
		return nature;
	}

	/**
	 * @return the geometry
	 */
	public MultiPolygon getGeometry() {
		return geometry;
	}
	
	
// ========================== METHODS ==============================
	
	/**
	 * This method will have a return that will soon be specified
	 */
	public void toOBJ(){
		
	}

	public void addHeight() {
		// TODO Auto-generated method stub
		
	}

}

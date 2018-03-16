package eu.ensg.jade.semantic;

import com.jme3.app.R.string;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

import eu.ensg.jade.geometricObject.ISurfacicObject;
import eu.ensg.jade.geometricObject.Point;

/**
 * Vegetation is the class implementing the vegetation to be added to the scene
 * 
 * @author JADE
 */

public class SurfacicVegetation implements ISurfacicObject {
	
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
	
// ========================== METHODS ==============================
	
	/**
	 * This method will have a return that will soon be specified
	 */
	public void toOBJ(){
		
	}

	

	@Override
	public void addHeight() {
		// TODO Auto-generated method stub
		
	}

}

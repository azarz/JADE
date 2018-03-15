package eu.ensg.jade.semantic;

import com.jme3.app.R.string;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Polygon;

import eu.ensg.jade.geometricObject.ISurfacicObject;

/**
 * SurfacicRoad is the class implementing a surfacic road object
 * 
 * @author JADE
 */

public class SurfacicRoad implements ISurfacicObject {
	
// ========================== ATTRIBUTES ===========================

	/**
	 * Road width as defined in the RGE
	 */
	private double width;
	/**
	 * The road number of ways
	 */
	private int wayNumber;
	/**
	 * The elevation at the initial road summit
	 */
	private double z_ini;
	/**
	 * The elevation at the final road summit
	 */
	private double z_fin;
	/**
	 * The way driving direction
	 */
	private String direction;
	/**
	 * The Geometry
	 */
	private Polygon geom;
	/**
	 * The linear road that has been enlarged to create this road
	 */
	private LinearRoad linearRoad;
	
	
	public SurfacicRoad(double width, int wayNumber, double z_ini, double z_fin, String direction, Polygon geom,
			LinearRoad linearRoad) {
		this.width = width;
		this.wayNumber = wayNumber;
		this.z_ini = z_ini;
		this.z_fin = z_fin;
		this.direction = direction;
		this.geom = geom;
		this.linearRoad = linearRoad;
	}


// ========================== GETTERS/SETTERS ======================
	
	/**
	 * Allows to access the road width
	 * 
	 * @return the road width
	 */
	public double getWidth() {
		return width;
	}


	/**
	 * Allows to access the road number of ways
	 * 
	 * @return the road number of ways
	 */
	public int getWayNumber() {
		return wayNumber;
	}

	/**
	 * Allows to access the elevation at the road initial summit
	 * 
	 * @return the elevation at the road initial summit
	 */
	public double getZ_ini() {
		return z_ini;
	}


	/**
	 * Allows to access the elevation at the road final summit
	 * 
	 * @return the elevation at the final road summit
	 */
	public double getZ_fin() {
		return z_fin;
	}

	/**
	 * Allows to access the road driving direction
	 * 
	 * @return the road driving direction
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * Allows to access the geometry of the road
	 * 
	 * @return the road original linear road
	 */
	public Polygon getGeom() {
		return geom;
	}
	
	/**
	 * Allows to access the current road original linear road
	 * 
	 * @return the road original linear road
	 */
	public LinearRoad getLinearRoad() {
		return linearRoad;
	}

// ========================== METHODS ==============================

	/**
	 * Allows to add an height to a road. It is defines by a elevation to its extremity (z_ini and z_fin)
	 */
	public void addHeight(){
		
	}
	
	/**
	 * This method will have a return that will soon be specified
	 */
	public void toOBJ(){
		
	}
}

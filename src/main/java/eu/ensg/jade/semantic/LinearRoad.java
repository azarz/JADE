package eu.ensg.jade.semantic;

import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Polygon;

import eu.ensg.jade.geometricObject.Road;

/**
 * LinearRoad is the class implementing the linear roads from the RGE
 * 
 * @author JADE
 */

public class LinearRoad extends Road{
	
// ========================== ATTRIBUTES ===========================
	
	/**
	 * the attribute containing the geometry of the road
	 */
	protected MultiLineString geometry;
	
	
// ========================== CONSTRUCTORS =========================

	/**
	 * Constructor using all fields
	 * 
	 * @param width
	 * @param wayNumber
	 * @param z_ini
	 * @param z_fin
	 * @param direction
	 * @param geometry
	 */
	public LinearRoad(double width, int wayNumber, double z_ini, double z_fin, String direction, MultiLineString geometry) {
		super(width, wayNumber, z_ini, z_fin, direction);
		
		this.geometry = geometry;
	}

	
// ========================== GETTERS/SETTERS ======================


	/**
	 * Allows to access the geometry of the road
	 * 
	 * @return the road original linear road
	 */
	public MultiLineString getGeom() {
		return this.geometry;
	}
	
// ========================== METHODS ==============================
	
	/**
	 * Enlarges the current road
	 * 
	 * @return the surfacic road creates
	 */
	public ArealRoad enlarge(){
		
		Polygon geometry =  (Polygon) this.geometry.buffer(width/2);
		
		ArealRoad surfacicRoad = new ArealRoad(width, wayNumber, z_ini, z_fin, direction, geometry);
		
		return surfacicRoad;
		
	}
	
	/**
	 * Smoothes the surfacic road previously created
	 * 
	 * @return the surfacic smoothed road
	 */
	public ArealRoad smooth(){
		ArealRoad surf = this.enlarge();
		return null;
	}
	
}

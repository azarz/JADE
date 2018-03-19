package eu.ensg.jade.semantic;

import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Polygon;

/**
 * LinearRoad is the class implementing the linear roads from the RGE
 * 
 * @author JADE
 */

public class LinearRoad extends Road{
	
// ========================== ATTRIBUTES ===========================

	/**
	 * Constructor using all fields
	 * 
	 * @param width
	 * @param wayNumber
	 * @param z_ini
	 * @param z_fin
	 * @param direction
	 * @param geom
	 */
	public LinearRoad(double width, int wayNumber, double z_ini, double z_fin, String direction, MultiLineString geom) {
		super(width, wayNumber, z_ini, z_fin, direction,geom);
	}

	
// ========================== GETTERS/SETTERS ======================


	/**
	 * Allows to access the geometry of the road
	 * 
	 * @return the road original linear road
	 */
	@Override
	public MultiLineString getGeom() {
		return (MultiLineString) geom;
	}
	
// ========================== METHODS ==============================
	
	/**
	 * Enlarges the current road
	 * 
	 * @return the surfacic road creates
	 */
	public SurfacicRoad enlarge(){
		Polygon geometry =  (Polygon) geom.buffer(width/2);
		
		SurfacicRoad surfacicRoad = new SurfacicRoad(width, wayNumber, z_ini, z_fin, direction, geometry, this);
		
		return surfacicRoad;
		
	}
	
	/**
	 * Smoothes the surfacic road previously created
	 * 
	 * @return the surfacic smoothed road
	 */
	public SurfacicRoad smooth(){
		SurfacicRoad surf = this.enlarge();
		return null;
	}
	
}

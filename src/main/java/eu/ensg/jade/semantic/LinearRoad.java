package eu.ensg.jade.semantic;

import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Polygon;

/**
 * LinearRoad is the class implementing the linear roads from the RGE
 * 
 * @author JADE
 */

public class LinearRoad {
	
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
	private MultiLineString geom;
	
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
	 * Allows to set the road width
	 * 
	 * @param width the width to be attributed to the road
	 */
	public void setWidth(double width) {
		this.width = width;
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
	 * Allows to set the number of ways
	 * 
	 * @param width the way number to be attributed to the road
	 */
	public void setWayNumber(int wayNumber) {
		this.wayNumber = wayNumber;
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
	 * Allows to set the elevation at the road initial summit
	 * 
	 * @param z_ini the elevation to be attributed to the road initial summit
	 */
	public void setZ_ini(double z_ini) {
		this.z_ini = z_ini;
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
	 * Allows to set the elevation at the road final summit
	 * 
	 * @param z_fin the elevation to be attributed to the road final summit
	 */
	public void setZ_fin(double z_fin) {
		this.z_fin = z_fin;
	}

	/**
	 * Allows to access the road driving direction
	 * 
	 * @return the road driving direction
	 */
	public MultiLineString getGeom() {
		return geom;
	}
	
	/**
	 * Allows to set the road driving direction
	 * 
	 * @param direction the driving direction to be attributed to the road
	 */	
	public void setDirection(MultiLineString geom) {
		this.geom = geom;
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
	 * Allows to set the road driving direction
	 * 
	 * @param direction the driving direction to be attributed to the road
	 */	
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
// ========================== METHODS ==============================
	
	/**
	 * Enlarges the current road
	 * 
	 * @return the surfacic road creates
	 */
	public SurfacicRoad enlarge(){
		SurfacicRoad surfacicRoad= new SurfacicRoad();
		surfacicRoad.setDirection(direction);
		surfacicRoad.setLinearRoad(this);
		surfacicRoad.setWayNumber(wayNumber);
		surfacicRoad.setWidth(width);
		surfacicRoad.setZ_fin(z_fin);
		surfacicRoad.setZ_ini(z_ini);
		surfacicRoad.setGeom((Polygon) geom.buffer(width/2));
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

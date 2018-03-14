/**
 * 
 */
package eu.ensg.jade.semantic;

import com.jme3.app.R.string;

/**
 * LinearRoad is the class implementing the linear roads from the RGE
 * 
 * @author JADE
 *
 */
public class LinearRoad {
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
	 * The linear road geometry
	 */
	private string geometry;
	
	
	/**
	 * This method allows to access the road width
	 * @return the road width
	 */
	public double getWidth() {
		return width;
	}
	
	/**
	 * This method allows to set the road width
	 * @param width the width to be attributed to the road
	 */
	public void setWidth(double width) {
		this.width = width;
	}
	
	/**
	 * This method allows to access the road number of ways
	 * @return the road number of ways
	 */
	public int getWayNumber() {
		return wayNumber;
	}
	
	/**
	 * This method allows to set the number of ways
	 * @param width the way number to be attributed to the road
	 */
	public void setWayNumber(int wayNumber) {
		this.wayNumber = wayNumber;
	}
	
	/**
	 * This method allows to access the elevation at the road initial summit
	 * @return the elevation at the road initial summit
	 */
	public double getZ_ini() {
		return z_ini;
	}
	
	/**
	 * This method allows to set the elevation at the road initial summit
	 * @param z_ini the elevation to be attributed to the road initial summit
	 */
	public void setZ_ini(double z_ini) {
		this.z_ini = z_ini;
	}
	
	/**
	 * This method allows to access the elevation at the road final summit
	 * @return the elevation at the final road summit
	 */
	public double getZ_fin() {
		return z_fin;
	}
	
	/**
	 * This method allows to set the elevation at the road final summit
	 * @param z_fin the elevation to be attributed to the road final summit
	 */
	public void setZ_fin(double z_fin) {
		this.z_fin = z_fin;
	}
	
	/**
	 * This method allows to access the road driving direction
	 * @return the road driving direction
	 */
	public String getDirection() {
		return direction;
	}
	
	/**
	 * This method allows to set the road driving direction
	 * @param direction the driving direction to be attributed to the road
	 */	
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	/**
	 * This method enlarges the current road
	 * 
	 * @return the surfacic road creates
	 */
	public SurfacicRoad enlarge(){
		return null;
		
	}
	
	/**
	 * This method smoothes the surfacic road previously created
	 * 
	 * @return the surfacic smoothed road
	 */
	public SurfacicRoad smooth(){
		SurfacicRoad surf = this.enlarge();
		return null;
	}
	
	
}

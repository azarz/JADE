/**
 * 
 */
package eu.ensg.jade.semantic;

import com.jme3.app.R.string;

import eu.ensg.jade.geometricObject.ISurfacicObject;

/**
 * SurfacicRoad is the class implementing a surfacic road object
 * 
 * @author JADE
 *
 */
public class SurfacicRoad implements ISurfacicObject {
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
	private string direction;
	/**
	 * The linear road that has been enlarged to create this road
	 */
	private LinearRoad linearRoad;
	
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
	public string getDirection() {
		return direction;
	}

	/**
	 * This method allows to set the road driving direction
	 * @param direction the driving direction to be attributed to the road
	 */	
	public void setDirection(string direction) {
		this.direction = direction;
	}

	/**
	 * This method allows to access the current road original linear road
	 * @return the road original linear road
	 */
	public LinearRoad getLinearRoad() {
		return linearRoad;
	}

	/**
	 * This method allows to set the linear road related to the current road
	 * @param linearRoad the linear road to be attributed to the road
	 */
	public void setLinearRoad(LinearRoad linearRoad) {
		this.linearRoad = linearRoad;
	}


	/**
	 * This method allows to add an height to a road. It is defines by a elevation to its extremity (z_ini and z_fin)
	 */
	public void addHeight(){
		
	}
	
	/**
	 * This method will have a return that will soon be specified
	 */
	public void toOBJ(){
		
	}
}

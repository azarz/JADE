/**
 * 
 */
package eu.ensg.jade.semantic;

import com.jme3.app.R.string;

import eu.ensg.jade.geometricObject.Point;

/**
 * Vegetation is the class implementing the vegetation to be added to the scene
 * 
 * @author JADE
 *
 */
public class Vegetation extends Point {
	
	/**
	 * The furniture nature (stop, pedestrian crossing...)
	 */
	private string nature;
	
	/**
	 * The minimum vegetation elevation
	 */
	private float z_min;
	
	/**
	 * The maximum vegetation elevation
	 */
	private float z_max;
	
	/**
	 * This method will have a return that will soon be specified
	 */
	public void toOBJ(){
		
	}

}

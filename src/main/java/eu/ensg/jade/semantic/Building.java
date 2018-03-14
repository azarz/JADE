/**
 * 
 */
package eu.ensg.jade.semantic;

import com.jme3.app.R.string;

import eu.ensg.jade.geometricObject.ISurfacicObject;

/**
 * Building is the class implementing a building from the RGE
 * 
 * @author JADE
 *
 */
public class Building implements ISurfacicObject {

	/**
	 * The building height
	 */
	private double height;
	/**
	 * The building minimum elevation
	 */
	private double z_min;
	/**
	 * the building maximum elevation
	 */
	private double z_max;
	/**
	 * The building geometry
	 */
	private string geometry;
	
	
	/**
	 * This methods adds an elevation to a building
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

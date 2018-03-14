/**
 * 
 */
package eu.ensg.jade.semantic;

import java.util.List;

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
	 * the building coordinates
	 */
	private List<double[]> vertices;
	
	/**
	 * Constructor using all fields
	 * @param height
	 * @param z_min
	 * @param z_max
	 * @param vertices
	 */
	public Building(double height, double z_min, double z_max, List<double[]> vertices) {
		this.height = height;
		this.z_min = z_min;
		this.z_max = z_max;
		this.vertices = vertices;
	}

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

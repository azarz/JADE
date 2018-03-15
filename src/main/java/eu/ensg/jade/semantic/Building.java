package eu.ensg.jade.semantic;

import com.jme3.app.R.string;
import java.util.List;

import eu.ensg.jade.geometricObject.ISurfacicObject;

/**
 * Building is the class implementing a building from the RGE
 * 
 * @author JADE
 */

public class Building implements ISurfacicObject {
	
// ========================== ATTRIBUTES ===========================

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

// ========================== CONSTRUCTORS =========================	

	/**
	 * Constructor using all fields
	 * 
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
		deleteLastVertex();
	}
	
// ========================== GETTERS/SETTERS ======================	
	
	/**
	 * Gets the building vertices
	 * 
	 * @return building vertices
	 */
	public List<double[]> getVertices() {
		return vertices;
	}
	
// ========================== METHODS ==============================

	/**
	 * Adds an elevation to a building
	 */
	@Override
	public void addHeight() {
		// Getting the initial number of vertices
		int size = vertices.size();
		// Going through the list
		for (int i = 0; i < size; i++) {
			// Copying the coordinates of the vertex
			double[] coords = vertices.get(i).clone();
			// Adding the height
			coords[2] += height;
			// Adding the computed vertex to the list
			vertices.add(coords);
		}
	}
	
	/**
	 * Deletes the last vertex of the polygon 
	 * (since it's used to close the surface in the shapefile)
	 */
	private void deleteLastVertex() {
		vertices.remove(vertices.size() - 1);
	}
	
	/**
	 * This method will have a return that will soon be specified
	 */
	public void toOBJ(){
		
	}

}

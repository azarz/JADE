package eu.ensg.jade.semantic;

import com.jme3.app.R.string;

import java.util.ArrayList;
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
	}
	
// ========================== GETTERS/SETTERS ======================	
	

	/**
	 * Gets the building height
	 * 
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Gets the building minimum elevation
	 * 
	 * @return the z_min
	 */
	public double getZ_min() {
		return z_min;
	}

	/**
	 * Gets the building maximum elevation
	 * 
	 * @return the z_max
	 */
	public double getZ_max() {
		return z_max;
	}
	
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
	 * Calculates the distance between 2 3D points
	 * 
	 * @param p1 3D point as double[3]
	 * @param p2 3D point as double[3]
	 * @return distance between p1 and p2
	 */
	private static double getDistance(double[] p1, double[] p2) {
	    return Math.sqrt(Math.pow(p1[0] - p2[0], 2) +
	    		Math.pow(p1[1] - p2[1], 2) + 
	    		Math.pow(p1[2] - p2[2], 2));
	}

	/**
	 * This method will have a return that will soon be specified
	 */
	public void toOBJ(List<Integer> offsets){
		
		// Fetching the offsets from the offsets parameter
		int vertexIndexOffset  = offsets.get(0);
		int textureIndexOffset = offsets.get(1);
		int normalIndexOffset  = offsets.get(2);
		
		String vertexCoords = "";
		String uvCoords     = "";
		String normalCoords = "";
		
		String faces = "";
		
		// Adding the vertex coords as in a obj file
		for (int i = 0; i < vertices.size(); i++) {
			vertexCoords += "v " + vertices.get(i)[0] + " "
							     + vertices.get(i)[2] + " "
							     + vertices.get(i)[1] + "\n";
			
		}
		
		for (int i = 0; i < vertices.size()/2 - 1; i++) {
			// Calculating the texture coordinates
			uvCoords += "vt 0 0" + "\n";
			uvCoords += "vt " + getDistance(vertices.get(i), vertices.get(i+1)) + " 0" + "\n";
			uvCoords += "vt " + getDistance(vertices.get(i), vertices.get(i+1)) + " " + height/3 + "\n";
			uvCoords += "vt 0 " + height/3 + "\n";
			
			// Calculating the differences between 3 points of th face to calculate the normal vector
			double diff1_x = vertices.get(i+1)[0] - vertices.get(i)[0];
			double diff1_y = vertices.get(i+1)[2] - vertices.get(i)[2];
			double diff1_z = vertices.get(i+1)[1] - vertices.get(i)[1];
			
			double diff2_x = vertices.get(i + vertices.size()/2 )[0] - vertices.get(i)[0];
			double diff2_y = vertices.get(i + vertices.size()/2 )[2] - vertices.get(i)[2];
			double diff2_z = vertices.get(i + vertices.size()/2 )[1] - vertices.get(i)[1];
			
			double normal_x = (diff1_y * diff2_z) - (diff1_z * diff2_y);
			double normal_y = (diff1_z * diff2_x) - (diff1_x * diff2_z);
			double normal_z = (diff1_x * diff2_y) - (diff1_y * diff2_x);
			
			normalCoords += "vn " + normal_x + " " + normal_y + " " + normal_z + "\n";

			// Calculating the face corresponding indices
			faces += "f " + (i + vertexIndexOffset) + "/" + 
								(4*i + textureIndexOffset) + "/" + 
								(i+normalIndexOffset) + " "
								
						  + (i+1 + vertexIndexOffset) + "/" + 
								(4*i+1 + textureIndexOffset) + "/" + 
								(i+normalIndexOffset) + " "
								
						  + (i+1 + vertices.size()/2 + vertexIndexOffset) + "/" + 
								(4*i+3 + textureIndexOffset) + "/" + 
								(i+normalIndexOffset) + " "
								
						  + (i + vertices.size()/2 + vertexIndexOffset) + "/" + 
								(4*i+2 + textureIndexOffset) + "/" + 
								(i+normalIndexOffset) + "\n";
		}
		
		normalIndexOffset  += vertices.size()/2 - 1;
		
		normalCoords += "vn 0 1 0\n";
		
		faces += "f";
		for (int i = vertices.size()/2; i < vertices.size(); i++) {
			faces += " " + (i + vertexIndexOffset) + "//" + normalIndexOffset;	
		}
		faces += "\n";
		
		vertexIndexOffset  += vertices.size();
		textureIndexOffset += 4*vertices.size();
		normalIndexOffset++;
		
		System.out.println(vertexCoords);
		System.out.println(uvCoords);
		System.out.println(normalCoords);
		System.out.println(faces);

		// Updating the offsets
		offsets.set(0, vertexIndexOffset);
		offsets.set(1, textureIndexOffset);
		offsets.set(2, normalIndexOffset);
	}

}

package eu.ensg.jade.semantic;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.polytriangulate.EarClipper;

import eu.ensg.jade.geometricObject.WorldObject;
import eu.ensg.jade.utils.JadeUtils;

/**
 * Building is the class implementing a building from the RGE
 * 
 * @author JADE
 */

public class Building extends WorldObject {
	
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
	
	/**
	 * Boolean to know if the height was added or not
	 */
	private boolean hasHeight = false;


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
		this.hasHeight = false;
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
		
		this.hasHeight = true;
	}

	/**
	 * Converts a Building into a string corresponding to the .obj description of it
	 * 
	 * @param indexOffsets a list of 3 integers wich correspond to the offset of
	 * 			- vertex index
	 * 			- uv coordinates index
	 * 			- normal coordinates indexs
	 * in the file
	 * @return A string corresponding to the .obj description of the Building
	 */
	public String toOBJ(List<Integer> indexOffsets, double xOffset, double yOffset){
		// Checking if the height was already calculated
		if (!hasHeight) {
			this.addHeight();
		}
		
		// Fetching the offsets from the offsets parameter
		int vertexIndexOffset  = indexOffsets.get(0);
		int textureIndexOffset = indexOffsets.get(1);
		int normalIndexOffset  = indexOffsets.get(2);
		
		String vertexCoords = "";
		String uvCoords     = "";
		String normalCoords = "";
		
		String faces = "usemtl Wall\n";
		
		// Adding the vertex coords as in a obj file
		for (int i = 0; i < vertices.size(); i++) {
			vertexCoords += "v " + (vertices.get(i)[0] - xOffset) + " "
							     + vertices.get(i)[2] + " "
							     + -1*(vertices.get(i)[1] - yOffset) + "\n";
			
		}
		
		for (int i = 0; i < vertices.size()/2 - 1; i++) {
			// Calculating the texture coordinates
			uvCoords += "vt 0 0" + "\n";
			uvCoords += "vt " + JadeUtils.getDistance(vertices.get(i), vertices.get(i+1)) + " 0" + "\n";
			uvCoords += "vt " + JadeUtils.getDistance(vertices.get(i), vertices.get(i+1)) + " " + height/3 + "\n";
			uvCoords += "vt 0 " + height/3 + "\n";
			
			// Calculating the normal vector
			double[] normalVector = JadeUtils.getNormalVector(vertices.get(i), 
					vertices.get(i+1), vertices.get(i + vertices.size()/2 ));
	
			normalCoords += "vn " + normalVector[0] + " " + 
									normalVector[1] + " " + 
									normalVector[2] + "\n";

			// Calculating the face corresponding indices
			faces += "f " + (i + vertexIndexOffset) + "/" + 
								(4*i+1 + textureIndexOffset) + "/" + 
								(i+normalIndexOffset) + " "
								
						  + (i+1 + vertexIndexOffset) + "/" + 
								(4*i + textureIndexOffset) + "/" + 
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
		
		// Adding the roof to the building
		faces += "usemtl Roof\n";
		Coordinate[] roofCoords = new Coordinate[vertices.size()/2 + 1];
		for (int i = vertices.size()/2; i < vertices.size(); i++) {
			double[] vertex = vertices.get(i);
			roofCoords[i-vertices.size()/2] = new Coordinate(vertex[0], vertex[1], vertex[2]);	
		}
		roofCoords[vertices.size()/2] = roofCoords[0];
				
		GeometryFactory geometryFactory = new GeometryFactory();
		Polygon polygon = geometryFactory.createPolygon(roofCoords);

		int newVertexOffset = vertices.size();
		
		try {
			// Using the class from https://github.com/dhtong to triangulate the polygon
			EarClipper earClipper = new EarClipper(polygon);
			GeometryCollection triangles = (GeometryCollection) earClipper.getResult();
			
			int numTriangles = triangles.getNumGeometries();

			for (int tri = 0; tri < numTriangles; tri++) {
				
				Polygon triangle = (Polygon) triangles.getGeometryN(tri);
				
				Coordinate[] coords = triangle.getCoordinates();
				
				faces += "f";
				
				// Adding the vertex coords as in a obj file
				for (int i = 0; i < coords.length - 1; i++) {
					vertexCoords += "v " + (coords[i].x - xOffset) + " "
									     + coords[i].z + " "
									     + -1*(coords[i].y - yOffset) + "\n";
					
					faces += " " + (i + vertexIndexOffset + newVertexOffset) + "//" + normalIndexOffset;
				}
				
				faces += "\n";
				newVertexOffset += 3;
			}
		} catch (IllegalStateException e){
			System.out.println("unable to create roof");
		}
		
		// Updating the offsets
		vertexIndexOffset  += newVertexOffset;
		textureIndexOffset += 4*(vertices.size()/2 - 1);
		normalIndexOffset++;

		indexOffsets.set(0, vertexIndexOffset);
		indexOffsets.set(1, textureIndexOffset);
		indexOffsets.set(2, normalIndexOffset);
		
		// Filling the output string
		String outputString = vertexCoords + uvCoords + normalCoords + faces;
		
		return outputString;
	}

}

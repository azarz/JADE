package eu.ensg.jade.semantic;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

import eu.ensg.jade.output.IObjExport;
import eu.ensg.jade.utils.JadeUtils;

/**
 * Building is the class implementing a building from the RGE
 * 
 * @author JADE
 */

public class Building implements IObjExport {
	
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
	 * @param height The height of the building
	 * @param z_min The altitude of the base of the building
	 * @param z_max The altitude of the roof of the building
	 * @param vertices The vertices of the building
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
		if (height == 0) {
			height = 10;
		}
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
	 * Transforms the Z coordinates of the geometry according to a DTM parameter
	 * 
	 * @param dtm for the building to match
	 */
	public void setZfromDTM(DTM dtm) {
		if (hasHeight) {
			for (int i = vertices.size() - 1; i >= vertices.size()/2; i--) {
				vertices.remove(i);
			}
		}
		
		for (int i = 0; i < vertices.size(); i++) {	
			vertices.get(i)[2] = dtm.getHeightAtPoint(vertices.get(i)[0], vertices.get(i)[1]) - .5;
		}
		
		if (hasHeight) {
			addHeight();
		}
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
		// Defining a new decimal format in order to have smaller obj files
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		DecimalFormat format = (DecimalFormat)nf;
		format.applyPattern("#.###");
		
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
		
		String wallChoice = "";
		double wallRandomNumber = Math.random();
		// Adding the roof to the building
		if (wallRandomNumber < 0.25) {
			wallChoice = "2";
		}
		else if (wallRandomNumber < 0.5) {
			wallChoice = "3";			
		} 
		else if (wallRandomNumber < 0.75) {
			wallChoice = "4";
		}
		
		String faces = "usemtl Wall" + wallChoice + "\n";
		
		// Adding the vertex coords as in a obj file
		for (int i = 0; i < vertices.size(); i++) {
			vertexCoords += "v " + format.format(vertices.get(i)[0] - xOffset) + " "
							     + format.format(vertices.get(i)[2]) + " "
							     + format.format(-1*(vertices.get(i)[1] - yOffset)) + "\n";
			
		}
		
		for (int i = 0; i < vertices.size()/2 - 1; i++) {
			// Calculating the texture coordinates
			uvCoords += "vt 0 0" + "\n";
			uvCoords += "vt " + format.format(JadeUtils.getDistance(vertices.get(i), vertices.get(i+1))) + " 0" + "\n";
			uvCoords += "vt " + format.format(JadeUtils.getDistance(vertices.get(i), vertices.get(i+1))) + " " + height/3 + "\n";
			uvCoords += "vt 0 " + format.format(height/3) + "\n";
			
			// Calculating the normal vector
			double[] normalVector = JadeUtils.getNormalVector(vertices.get(i), 
					vertices.get(i+1), vertices.get(i + vertices.size()/2 ));
	
			normalCoords += "vn " + format.format(normalVector[0]) + " " + 
									format.format(normalVector[1]) + " " + 
									format.format(normalVector[2]) + "\n";

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
		
		String roofChoice = "";
		// Adding the roof to the building
		if (Math.random() < 0.5) {
			roofChoice = "2";
		}
		faces += "usemtl Roof"+ roofChoice +"\n";
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
			// Triangulating the polygon using the utils class
			GeometryCollection triangles = (GeometryCollection) JadeUtils.triangulate(polygon);
			
			int numTriangles = triangles.getNumGeometries();

			for (int tri = 0; tri < numTriangles; tri++) {
				
				Polygon triangle = (Polygon) triangles.getGeometryN(tri);
				
				Coordinate[] coords = triangle.getCoordinates();
				
				faces += "f";
				
				// Adding the vertex coords as in a obj file
				for (int i = 0; i < coords.length - 1; i++) {
					vertexCoords += "v " + format.format(coords[i].x - xOffset) + " "
									     + format.format(coords[i].z) + " "
									     + format.format(-1*(coords[i].y - yOffset)) + "\n";
					
					faces += " " + (i + vertexIndexOffset + newVertexOffset) + "//" + normalIndexOffset;
				}
				
				faces += "\n";
				newVertexOffset += 3;
			}
		} catch (RuntimeException  e){
			//System.out.println("unable to create roof");
		} catch (StackOverflowError  e){
			//System.out.println("unable to create roof");
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

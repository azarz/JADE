package eu.ensg.jade.semantic;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.polytriangulate.EarClipper;

import eu.ensg.jade.geometricObject.Road;
import eu.ensg.jade.utils.JadeUtils;

/**
 * SurfacicRoad is the class implementing a surfacic road object
 * 
 * @author JADE
 */

public class SurfaceRoad extends Road {
	
// ========================== ATTRIBUTES ===========================
	
	/**
	 * the attribute containing the geometry of the road
	 */
	Polygon geometry;
	
	
// ========================== CONSTRUCTORS =========================

	/**
	 * Constructor using all fields
	 * 
	 * @param width
	 * @param wayNumber
	 * @param z_ini
	 * @param z_fin
	 * @param direction
	 * @param geometry
	 */
	public SurfaceRoad(double width, int wayNumber, double z_ini, double z_fin, String direction, String nature, String importance, String number, Polygon geometry) {
		super(width, wayNumber, z_ini, z_fin, direction, nature, importance, number);
		this.geometry = geometry;
	}
	
	/**
	 * Constructor using a LinearRoad and a specific width
	 * @param width the width used as a buffer around the LinearRoad
	 * @param lineRoad the original LinearRoad
	 */
	public SurfaceRoad(LineRoad lineRoad) {
		super(lineRoad.getWidth(), lineRoad.getLaneNumber(), lineRoad.getZ_ini(), lineRoad.getZ_fin(), lineRoad.getDirection(), lineRoad.getNature(), lineRoad.getImportance(), lineRoad.getNumber());
		this.geometry =  (Polygon) lineRoad.getGeom().buffer(this.width/2);
	}


// ========================== GETTERS/SETTERS ======================
	/**
	 * Allows to access the geometry of the road
	 * 
	 * @return the road original linear road
	 */
	public Polygon getGeom() {
		return this.geometry;
	}
	

// ========================== METHODS ==============================

	/**
	 * Converts a SurfaceRoad into a string corresponding to the .obj description of it
	 * 
	 * @param indexOffsets a list of 3 integers wich correspond to the offset of
	 * 			- vertex index
	 * 			- uv coordinates index
	 * 			- normal coordinates indexs
	 * in the file
	 * @return A string corresponding to the .obj description of the SurfaceRoad
	 */
	public String toOBJ(List<Integer> indexOffsets, double xOffset, double yOffset){
		
		// Fetching the offsets from the offsets parameter
		int vertexIndexOffset  = indexOffsets.get(0);
		int textureIndexOffset = indexOffsets.get(1);
		int normalIndexOffset  = indexOffsets.get(2);
		
		String vertexCoords = "";
		String uvCoords     = "";
		String normalCoords = "";
		
		String faces = "usemtl Road\n";
		
		int numGeometries = geometry.getNumGeometries();
		
		int newVertexOffset = 0;
		int newNormalIndexOffset = 0;
		
		for (int N = 0; N < numGeometries; N++) {
			Polygon polygon = (Polygon) geometry.getGeometryN(N);
			int numCoords = polygon.getCoordinates().length;
			
			if(numCoords < 3) {
				continue;
			}
			
			// Using the class from https://github.com/dhtong to triangulate the polygon
			EarClipper earClipper = new EarClipper(polygon);
			GeometryCollection triangles = (GeometryCollection) earClipper.getResult();
			
			int numTriangles = triangles.getNumGeometries();
			
			for (int tri = 0; tri < numTriangles; tri++) {
				
				Polygon triangle = (Polygon) triangles.getGeometryN(tri);
				
				Coordinate[] coords = triangle.getCoordinates();
				
				// Calculating the normal vector
				double[] normalVector = JadeUtils.getNormalVector(coords[0], 
						coords[1], coords[2]);
				
				normalCoords += "vn " + normalVector[0] + " " + 
										normalVector[1] + " " + 
										normalVector[2] + "\n";
				newNormalIndexOffset++;
				
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
		}
		
		
		// Updating the offsets
		vertexIndexOffset  += newVertexOffset;
		textureIndexOffset += 0;
		normalIndexOffset  += newNormalIndexOffset;

		indexOffsets.set(0, vertexIndexOffset);
		indexOffsets.set(1, textureIndexOffset);
		indexOffsets.set(2, normalIndexOffset);
		
		// Filling the output string
		String outputString = vertexCoords + uvCoords + normalCoords + faces;
		
		return outputString;
		
	}
}

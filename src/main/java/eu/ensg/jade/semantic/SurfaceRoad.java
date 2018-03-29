package eu.ensg.jade.semantic;

import java.util.List;

import com.vividsolutions.jts.densify.Densifier;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFilter;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.Polygon;
import eu.ensg.jade.geometricObject.Road;
import eu.ensg.jade.utils.JadeTriangulation;
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
	 * @param nature
	 * @param importance
	 * @param number
	 * @param speed
	 * @param geometry
	 */
	public SurfaceRoad(double width, int wayNumber, double z_ini, double z_fin, String direction, String nature, String importance, String number,  String name, Polygon geometry) {
		super(width, wayNumber, z_ini, z_fin, direction, nature, importance, number, name);
		this.geometry = geometry;
	}
	
	/**
	 * Constructor using a LinearRoad and a specific width
	 * @param width the width used as a buffer around the LinearRoad
	 * @param lineRoad the original LinearRoad
	 */
	public SurfaceRoad(LineRoad road) {
		super(road.getWidth(), road.getLaneNumber(), road.getZ_ini(), road.getZ_fin(), road.getDirection(), road.getNature(), road.getImportance(), road.getNumber(), road.getName());
		this.geometry =  road.enlarge().getGeom();
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
		String faces;
		
		if (direction.equals("Double")) {
			faces = "usemtl RoadDouble\n";
		} else if (direction.equals("Direct")) {
			faces = "usemtl RoadDirect\n";
		} else {
			faces = "usemtl RoadInverse\n";
		}
		
		
		int numGeometries = geometry.getNumGeometries();
		
		int newVertexOffset = 0;
		
		for (int N = 0; N < numGeometries; N++) {
			Polygon polygon = (Polygon) geometry.getGeometryN(N);
			int numCoords = polygon.getCoordinates().length;
			
			if(numCoords < 3) {
				continue;
			}
			
			// Triangulating the polygon using th utils class
			GeometryCollection triangles = (GeometryCollection) JadeTriangulation.triangulate(polygon);
				
			int numTriangles = triangles.getNumGeometries();
			
			for (int tri = 0; tri < numTriangles; tri++) {
				
				Polygon triangle = (Polygon) triangles.getGeometryN(tri);
				
				Coordinate[] coords = triangle.getCoordinates();
				
				// Calculating the normal vector
				double[] normalVector = JadeUtils.getNormalVector(coords[0], 
						coords[1], coords[2]);
				
				// If it is pointing downwards, inverting it
				if(normalVector[1] < 0){
					normalVector = JadeUtils.getNormalVector(coords[0], 
							coords[2], coords[1]);
				}
				
				
				normalCoords += "vn " + normalVector[0] + " " + 
										normalVector[1] + " " + 
										normalVector[2] + "\n";
				normalIndexOffset++;
				
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

		indexOffsets.set(0, vertexIndexOffset);
		indexOffsets.set(1, textureIndexOffset);
		indexOffsets.set(2, normalIndexOffset);
		
		// Filling the output string
		String outputString = vertexCoords + uvCoords + normalCoords + faces;
		return outputString;
		
	}
	
	/**
	 * Transforms the Z coordinates of the geometry according to a DTM parameter
	 * @param dtm for the road to match
	 */
	public void setZfromDTM(DTM dtm) {	
		// Densifying the geometry so it has a number of vertices corresponding tO
		// the DTM
		if(geometry.getCoordinates().length > 0) {
			geometry = (Polygon) Densifier.densify(geometry, 5);
		}
		
		// Defining a coordinate filter to set the z according to the DTM
		// using bilinear interpolation
		CoordinateSequenceFilter filter = new CoordinateSequenceFilter() {
			
			@Override
			public void filter(CoordinateSequence seq, int i) {
				
				// Fetching the points coordinate
				double xCoord = seq.getCoordinate(i).x;
				double yCoord = seq.getCoordinate(i).y;
				
				// Setting the Z coordinate
				seq.getCoordinate(i).z = JadeUtils.interpolatedDtmValue(xCoord, yCoord, dtm);
			}

			@Override
			public boolean isDone() {
				return false;
			}

			@Override
			public boolean isGeometryChanged() {
				return true;
			}
			
		};
		
		// Applying the filter
		geometry.apply(filter);
	}
}

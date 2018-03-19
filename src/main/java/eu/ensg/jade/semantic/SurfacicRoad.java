package eu.ensg.jade.semantic;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;

import eu.ensg.jade.geometricObject.Road;

/**
 * SurfacicRoad is the class implementing a surfacic road object
 * 
 * @author JADE
 */

public class SurfacicRoad extends Road {
	
// ========================== ATTRIBUTES ===========================
	
	/**
	 * the attribute containing the geometry of the road
	 */
	MultiPolygon geometry;
	
	
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
	public SurfacicRoad(double width, int wayNumber, double z_ini, double z_fin, String direction, MultiPolygon geometry) {
		super(width, wayNumber, z_ini, z_fin, direction);
		this.geometry = geometry;
	}
	
	/**
	 * Constructor using a LinearRoad and a specific width
	 * @param width the width used as a buffer around the LinearRoad
	 * @param lineRoad the original LinearRoad
	 */
	public SurfacicRoad(double width, LinearRoad lineRoad) {
		super(width, lineRoad.getWayNumber(), lineRoad.getZ_ini(), lineRoad.getZ_fin(), lineRoad.getDirection());
		this.geometry =  (MultiPolygon) lineRoad.getGeom().buffer(width/2);
	}


// ========================== GETTERS/SETTERS ======================
	/**
	 * Allows to access the geometry of the road
	 * 
	 * @return the road original linear road
	 */
	public MultiPolygon getGeom() {
		return this.geometry;
	}
	

// ========================== METHODS ==============================

	/**
	 * Allows to add an height to a road. It is defines by a elevation to its extremity (z_ini and z_fin)
	 */
	public void addHeight(){
		
	}
	
	/**
	 * This method will have a return that will soon be specified
	 */
	public String toOBJ(List<Integer> indexOffsets){
		
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
		
		for (int N = 0; N < numGeometries; N++) {
			Geometry polygon = geometry.getGeometryN(N);
			Coordinate[] coords = polygon.getCoordinates();
			
			// Calculating the differences between 3 points of the face to calculate the normal vector
			double diff1_x = coords[1].x - coords[0].x;
			double diff1_y = coords[1].z - coords[0].z;
			double diff1_z = coords[1].y - coords[0].y;
			
			double diff2_x = coords[2].x - coords[0].x;
			double diff2_y = coords[2].z - coords[0].z;
			double diff2_z = coords[2].y - coords[0].y;
			
			double normal_x = (diff1_y * diff2_z) - (diff1_z * diff2_y);
			double normal_y = (diff1_z * diff2_x) - (diff1_x * diff2_z);
			double normal_z = (diff1_x * diff2_y) - (diff1_y * diff2_x);
			
			normalCoords += "vn " + normal_x + " " + normal_y + " " + normal_z + "\n";
			
			faces += "f";
			
			// Adding the vertex coords as in a obj file
			for (int i = 0; i < coords.length; i++) {
				vertexCoords += "v " + coords[i].x + " "
								     + coords[i].z + " "
								     + coords[i].y + "\n";
				
				faces += " " + i + vertexIndexOffset + "//" + normalIndexOffset;
				
				newVertexOffset++;
			}
			
			faces += "\n";
		}
		
		
		// Updating the offsets
		vertexIndexOffset  += newVertexOffset;
		textureIndexOffset += 0;
		normalIndexOffset++;

		indexOffsets.set(0, vertexIndexOffset);
		indexOffsets.set(1, textureIndexOffset);
		indexOffsets.set(2, normalIndexOffset);
		
		// Filling the output string
		String outputString = vertexCoords + uvCoords + normalCoords + faces;
		
		return outputString;
		
	}
}

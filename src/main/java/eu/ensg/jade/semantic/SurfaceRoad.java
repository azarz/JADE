package eu.ensg.jade.semantic;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFilter;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.buffer.BufferParameters;
import com.vividsolutions.jts.operation.distance.DistanceOp;
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
	 * @param nature
	 * @param importance
	 * @param number
	 * @param speed
	 * @param geometry
	 */
	public SurfaceRoad(double width, int wayNumber, double z_ini, double z_fin, String direction, String nature, String importance, String number, String speed, Polygon geometry) {
		super(width, wayNumber, z_ini, z_fin, direction, nature, importance, number, speed);
		this.geometry = geometry;
	}
	
	/**
	 * Constructor using a LinearRoad and a specific width
	 * @param width the width used as a buffer around the LinearRoad
	 * @param lineRoad the original LinearRoad
	 */
	public SurfaceRoad(LineRoad lineRoad) {
		super(lineRoad.getWidth(), lineRoad.getLaneNumber(), lineRoad.getZ_ini(), lineRoad.getZ_fin(), lineRoad.getDirection(), lineRoad.getNature(), lineRoad.getImportance(), lineRoad.getNumber(), lineRoad.getSpeed());
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
	
	/**
	 * Transforms the Z coordinates of the geometry according to a DTM parameter
	 * @param dtm for the road to match
	 */
	public void setZfromDTM(DTM dtm) {	
		// Defining a coordinate filter to set the z according to the DTM
		// using bilinear interpolation
		CoordinateSequenceFilter filter = new CoordinateSequenceFilter() {
			
			@Override
			public void filter(CoordinateSequence seq, int i) {
				
				// Fetching the points coordinate
				double xCoord = seq.getCoordinate(i).x;
				double yCoord = seq.getCoordinate(i).y;
				
				// Fetching the DTM data
				double xllCorner = dtm.getXllcorner();
				double yllCorner = dtm.getYllcorner();
				double cellsize = dtm.getCellsize();
				
				// Calculating the indices of the 4 cells around the point
				int westIndex = (int) Math.floor((xCoord - xllCorner)/cellsize);
				int eastIndex = (int) Math.ceil((xCoord - xllCorner)/cellsize);
				int southIndex = (int) Math.ceil(dtm.getNrows() - ((yCoord - yllCorner)/cellsize));
				int northIndex = (int) Math.floor(dtm.getNrows() - ((yCoord - yllCorner)/cellsize));
				
				// Getting the 4 cells values
				double northWestValue = dtm.getTabDTM()[northIndex][westIndex];
				double northEastValue = dtm.getTabDTM()[northIndex][eastIndex];
				double southWestValue = dtm.getTabDTM()[southIndex][westIndex];
				double southEastValue = dtm.getTabDTM()[southIndex][eastIndex];
				
				// Calculating the distances between the point's coordinates
				// and the corners coordinates
				double fracWest = xCoord - xllCorner + westIndex*cellsize;
				double fracEast = xllCorner + eastIndex*cellsize - xCoord;
				double fracSouth = yCoord - yllCorner + southIndex*cellsize;
				double fracNorth = yllCorner + northIndex*cellsize - yCoord;
				
				// Calculating the interpolated north value
				double interpolatedNorthValue = (fracWest * northEastValue 
						+ fracEast * northWestValue)/cellsize;
				
				// Calculating the interpolated south value
				double interpolatedSouthValue = (fracWest * southEastValue 
						+ fracEast * southWestValue)/cellsize;			
				
				// Calculating the final interpolated value
				double newZ = (fracNorth * interpolatedSouthValue
						+ fracSouth * interpolatedNorthValue)/cellsize;

				// Setting the Z coordinate
				seq.getCoordinate(i).z = newZ;
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

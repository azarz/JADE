package eu.ensg.jade.semantic;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.densify.Densifier;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFilter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.buffer.BufferParameters;
import com.vividsolutions.jts.operation.distance.DistanceOp;

import eu.ensg.jade.geometricObject.Road;
import eu.ensg.jade.output.IObjExport;
import eu.ensg.jade.utils.JadeUtils;

/**
 * SurfacicRoad is the class implementing a surfacic road object
 * 
 * @author JADE
 */

public class SurfaceRoad extends Road implements IObjExport{
	
// ========================== ATTRIBUTES ===========================
	
	/**
	 * the attribute containing the geometry of the road
	 */
	Polygon geometry;
	
	MultiLineString oldGeometry;
	
	
// ========================== CONSTRUCTORS =========================

	/**
	 * Constructor using all fields
	 * 
	 * @param width The width of the road
	 * @param wayNumber The number of lane of the road
	 * @param z_ini The starting altitude of the road
	 * @param z_fin The ending altitude of the road
	 * @param direction The direction of the road
	 * @param nature The nature of the road
	 * @param importance The importance of the road
	 * @param number The number of the road
	 * @param geometry The geometry of the road
	 * @param name The name of the road
	 */
	public SurfaceRoad(double width, int wayNumber, double z_ini, double z_fin, String direction, String nature, String importance, String number,  String name, Polygon geometry, 
			MultiLineString oldGeometry) {
		super(width, wayNumber, z_ini, z_fin, direction, nature, importance, number, name);
		this.geometry = geometry;
		this.oldGeometry = oldGeometry;
	}
	
	/**
	 * Constructor using a LinearRoad
	 * 
	 * @param road the original LinearRoad
	 */
	public SurfaceRoad(LineRoad road) {
		super(road.getWidth(), road.getLaneNumber(), road.getZ_ini(), road.getZ_fin(), road.getDirection(), road.getNature(), road.getImportance(), road.getNumber(), road.getName());
		this.geometry =  road.enlarge().getGeom();
		this.oldGeometry = road.getGeom();
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
	
	public void mergePolygon(Polygon other) {
		this.geometry.union(other);
	}

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
			faces = "usemtl RoadDouble\n";
		} else {
			faces = "usemtl RoadDouble\n";
		}
		
		
		int numGeometries = geometry.getNumGeometries();
		
		int newVertexOffset = 0;
		
		for (int N = 0; N < numGeometries; N++) {
			Polygon fullPolygon = (Polygon) geometry.getGeometryN(N);
			int numCoords = fullPolygon.getCoordinates().length;
			
			if(numCoords < 3) {
				continue;
			}
			
			// Filling the polygon holes
			LineString exteriorRing = fullPolygon.getExteriorRing();
			GeometryFactory factory = new GeometryFactory();
			Polygon polygon = factory.createPolygon((LinearRing) exteriorRing);
			
			// Triangulating the polygon using the utils class
			GeometryCollection triangles = (GeometryCollection) JadeUtils.triangulate(polygon);
				
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
		// Densify the geometry so it has a number of vertices corresponding to the DTM
		if(geometry.getCoordinates().length > 0) {
			geometry = (Polygon) Densifier.densify(geometry, 5);
		}
		
		// Defining a coordinate filter to set the z according to the DTM
		// using bilinear interpolation
		CoordinateSequenceFilter filter = new CoordinateSequenceFilter() {
			
			@Override
			public void filter(CoordinateSequence seq, int i) {
				seq.setOrdinate(i, 2, dtm.getHeightAtPoint(seq.getX(i), seq.getY(i)));
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
	
	/**
	 * Computes a sidewalks with a buffer and difference.
	 * @return sidewalk geometry
	 */
	private Geometry computeSidewalks(Geometry fullRoads) {
		Geometry buffer = oldGeometry.buffer(1 + width/2, 0, BufferParameters.CAP_SQUARE);
		// Defining a coordinate filter to add the z back
		CoordinateSequenceFilter filter = new CoordinateSequenceFilter() {
			
			@Override
			public void filter(CoordinateSequence seq, int i) {
				seq.getCoordinate(i).z = 0;	
				
				// Creating a point from the coordinate
				GeometryFactory factory = new GeometryFactory();
				Point point = factory.createPoint(seq.getCoordinate(i));
				
				// Creating a multipoint from the initial geometry (so only vertices are taken
				// into account)
				Coordinate[] geomCoords = oldGeometry.getCoordinates();
				if (geomCoords.length > 0) {
					MultiPoint geomAsMultiPoint = factory.createMultiPoint(geomCoords);
									
					// Calculating the nearest coordinate in the collection
					Coordinate[] coords = DistanceOp.nearestPoints(geomAsMultiPoint, point);
					
					seq.getCoordinate(i).z = coords[0].z;
				}
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
		
		Geometry sidewalk = buffer.difference(fullRoads);
		// Applying the filter
		sidewalk.apply(filter);
		return sidewalk;
	}
	
	public String sidewalksToOBJ(List<Integer> indexOffsets, double xOffset, double yOffset, Geometry fullRoads) {
		// Fetching the offsets from the offsets parameter
		int vertexIndexOffset  = indexOffsets.get(0);
		int textureIndexOffset = indexOffsets.get(1);
		int normalIndexOffset  = indexOffsets.get(2);
		
		String vertexCoords = "";
		String uvCoords     = "";
		String normalCoords = "";
		
		String faces = "usemtl Sidewalk\n";
		
		Geometry sidewalk = computeSidewalks(fullRoads);
		int numGeometries = sidewalk.getNumGeometries();
		   		  
		for (int N = 0; N < numGeometries; N++) {
			// List containing all the sidewalk coordinates
			List<double[]> vertices = new ArrayList<double[]>(); 
			
			if (N > 5) {
				continue;
			}
			Polygon fullPolygon = (Polygon) sidewalk.getGeometryN(N);
					
			Coordinate[] coords = fullPolygon.getCoordinates();
			
			if (coords.length < 3) {
				continue;
			}
			   
			for (int i = 0; i < coords.length; i++) {
				double[] pointCoords = new double[3];
				   
				// Extracting the planar coordinates
				pointCoords[0] = coords[i].x;
				pointCoords[1] = coords[i].y;
				pointCoords[2] = coords[i].z;

				vertices.add(pointCoords);
			}
		
			// Getting the initial number of vertices
			int size = vertices.size();
			// Going through the list
			for (int i = 0; i < size; i++) {
				// Copying the coordinates of the vertex
				double[] coord = vertices.get(i).clone();
				// Adding the height
				coord[2] += 0.2;
				// Adding the computed vertex to the list
				vertices.add(coord);
			}
			
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
				uvCoords += "vt " + JadeUtils.getDistance(vertices.get(i), vertices.get(i+1)) + " 0.2\n";
				uvCoords += "vt 0 0.2\n";
				
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
			Coordinate[] roofCoords = new Coordinate[vertices.size()/2 + 1];
			for (int i = vertices.size()/2; i < vertices.size(); i++) {
				double[] vertex = vertices.get(i);
				roofCoords[i-vertices.size()/2] = new Coordinate(vertex[0], vertex[1], vertex[2]);	
			}
			roofCoords[vertices.size()/2] = roofCoords[0];
					
			GeometryFactory geometryFactory = new GeometryFactory();
			Polygon roofPolygon = geometryFactory.createPolygon(roofCoords);
	
			int newVertexOffset = vertices.size();
			
			try {
				// Triangulating the polygon using the utils class
				GeometryCollection triangles = (GeometryCollection) JadeUtils.triangulate(roofPolygon);
				int numTriangles = triangles.getNumGeometries();
	
				for (int tri = 0; tri < numTriangles; tri++) {
					Polygon triangle = (Polygon) triangles.getGeometryN(tri);
					
					Coordinate[] coordinates = triangle.getCoordinates();
					
					faces += "f";
					
					// Adding the vertex coords as in a obj file
					for (int i = 0; i < coordinates.length - 1; i++) {
						vertexCoords += "v " + (coordinates[i].x - xOffset) + " "
										     + coordinates[i].z + " "
										     + -1*(coordinates[i].y - yOffset) + "\n";
						
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
		}
		
		// Filling the output string
		String outputString = vertexCoords + uvCoords + normalCoords + faces;
		return outputString;
	}
}

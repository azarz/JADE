package eu.ensg.jade.semantic;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFilter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.operation.buffer.BufferParameters;

import eu.ensg.jade.output.IObjExport;
import eu.ensg.jade.utils.JadeUtils;

/**
 * Sidewalk is a class to manage sidewalks (as expected)
 * 
 * @author JADE
 */
public class Sidewalk implements IObjExport{
	
// ========================== ATTRIBUTES ===========================
	
	/*
	 * Geometry of the associated LineRoad
	 */
	private MultiLineString oldGeometry;
	/*
	 * Width of the associated LineRoad
	 */
	private double width;
	/*
	 * Geometry of the merged SurfaceRoad of the scene
	 */
	private Geometry fullRoads;
	/*
	 * DTM of the scene
	 */
	private DTM dtm;
	
// ========================== CONSTRUCTORS =========================
	
	/*
	 * Constructor using all fields
	 */
	public Sidewalk(MultiLineString oldGeometry, double width, Geometry fullRoads, DTM dtm) {
		this.oldGeometry = oldGeometry;
		this.width = width;
		this.fullRoads = fullRoads;
		this.dtm = dtm;
	}

// ========================== METHODS ==============================

	/**
	 * Computes a sidewalk with a buffer and difference.
	 * @return sidewalk geometry
	 */
	private Geometry computeSidewalks(DTM dtm) {
		Geometry buffer = oldGeometry.buffer(1 + width/2, 0, BufferParameters.CAP_SQUARE);
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
		
		Geometry sidewalk = buffer.difference(fullRoads);
		// Applying the filter
		sidewalk.apply(filter);
		return sidewalk;
	}
	
	/*
	 * Adds an height of 0.2 to a set of vertices
	 */
	private void addHeight(List<double[]> vertices) {
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
	}
	
	/*
	 * Extracts a polygon's points into a list of vertices
	 */
	private List<double[]> extractPolyPoints(Polygon poly){
		// List containing all the sidewalk coordinates
		List<double[]> vertices = new ArrayList<double[]>(); 
		
		Coordinate[] coords = poly.getCoordinates();
		
		if (coords.length < 3) {
			return null;
		}
		   
		for (int i = 0; i < coords.length; i++) {
			double[] pointCoords = new double[3];
			   
			// Extracting the planar coordinates
			pointCoords[0] = coords[i].x;
			pointCoords[1] = coords[i].y;
			pointCoords[2] = coords[i].z;

			vertices.add(pointCoords);
		}
		
		return vertices;
		
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see eu.ensg.jade.output.IObjExport#toOBJ(java.util.List, double, double)
	 */
	public String toOBJ(List<Integer> indexOffsets, double xOffset, double yOffset) {
		// Defining a new decimal format in order to have smaller obj files
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		DecimalFormat format = (DecimalFormat)nf;
		format.applyPattern("#.###");
		
		// Fetching the offsets from the offsets parameter
		int vertexIndexOffset  = indexOffsets.get(0);
		int textureIndexOffset = indexOffsets.get(1);
		int normalIndexOffset  = indexOffsets.get(2);
		
		String vertexCoords = "";
		String uvCoords     = "";
		String normalCoords = "";
		
		String faces = "usemtl Sidewalk\n";
		
		Geometry sidewalk = computeSidewalks(dtm);
		int numGeometries = sidewalk.getNumGeometries();
		   		  
		for (int N = 0; N < numGeometries; N++) {
			
			if (N > 5) {
				continue;
			}
			
			// List containing all the sidewalk coordinates
			List<double[]> vertices = extractPolyPoints((Polygon) sidewalk.getGeometryN(N)); 
			
			if (vertices==null) {
				continue;
			}
			
			addHeight(vertices);
			
			// Adding the vertex coords as in a obj file
			for (int i = 0; i < vertices.size(); i++) {
				vertexCoords += "v " + format.format((vertices.get(i)[0] - xOffset)) + " "
								     + format.format(vertices.get(i)[2]) + " "
								     + format.format(-1*(vertices.get(i)[1] - yOffset)) + "\n";
				
			}
			
			for (int i = 0; i < vertices.size()/2 - 1; i++) {
				// Calculating the texture coordinates
				uvCoords += "vt 0 0" + "\n";
				uvCoords += "vt " + format.format(JadeUtils.getDistance(vertices.get(i), vertices.get(i+1))) + " 0" + "\n";
				uvCoords += "vt " + format.format(JadeUtils.getDistance(vertices.get(i), vertices.get(i+1))) + " 0.2\n";
				uvCoords += "vt 0 0.2\n";
				
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
			
			// Adding the top surface
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
						vertexCoords += "v " + format.format(coordinates[i].x - xOffset) + " "
										     + format.format(coordinates[i].z) + " "
										     + format.format(-1*(coordinates[i].y - yOffset)) + "\n";
						
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

package eu.ensg.jade.semantic;

import java.util.List;

import com.vividsolutions.jts.densify.Densifier;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFilter;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

import eu.ensg.jade.output.IObjExport;
import eu.ensg.jade.utils.JadeUtils;

/**
 * Vegetation is the class implementing the vegetation to be added to the scene
 * 
 * @author JADE
 */

public class SurfaceVegetation implements IObjExport {
	
// ========================== ATTRIBUTES ===========================
	
	/**
	 * The furniture nature (arboreal zone, forest...)
	 */
	private String nature;
	
	/**
	 * The tree geometry
	 */
	private MultiPolygon geometry;

// ========================== CONSTRUCTORS =========================	

	/**
	 * Constructor with all the fields
	 * 
	 * @param geometry the polygon geometry of the vegetation
	 * @param nature the nature of the vegetation surface
	 * 
	 **/

	public SurfaceVegetation(MultiPolygon geometry, String nature) {
		this.nature = nature;
		this.geometry = geometry;
	}
	
// ========================== GETTERS AND SETTERS ==============================
	
	/**
	 * @return the nature
	 */
	public String getNature() {
		return nature;
	}

	/**
	 * @return the geometry
	 */
	public MultiPolygon getGeometry() {
		return geometry;
	}
	
	
// ========================== METHODS ==============================
	
	/**
	 * This method will have a return that will soon be specified
	 * 
	 * @param indexOffsets The list of indexes
	 * @param xOffset the offset following x axis
	 * @param yOffset the offset following y axis
	 * 
	 * @return The string for the OBJ file
	 */
	@Override
	public String toOBJ(List<Integer> indexOffsets, double xOffset, double yOffset) {
		// Fetching the offsets from the offsets parameter
				int vertexIndexOffset  = indexOffsets.get(0);
				int textureIndexOffset = indexOffsets.get(1);
				int normalIndexOffset  = indexOffsets.get(2);
				
				String vertexCoords = "";
				String uvCoords     = "";
				String normalCoords = "";
				String faces;

				faces = "usemtl Water\n";
				
				int numGeometries = geometry.getNumGeometries();
				
				int newVertexOffset = 0;
				
				for (int N = 0; N < numGeometries; N++) {
					
					Polygon fullPolygon = (Polygon) geometry.getGeometryN(N);
					
					// Filling the polygon holes
					LineString exteriorRing = fullPolygon.getExteriorRing();
					GeometryFactory factory = new GeometryFactory();
					Polygon polygon = factory.createPolygon((LinearRing) exteriorRing);
					
					int numCoords = polygon.getCoordinates().length;
					
					if(numCoords < 3) {
						continue;
					}
					
					try {
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
												  + 0 + " "
												     + -1*(coords[i].y - yOffset) + "\n";
								
								faces += " " + (i + vertexIndexOffset + newVertexOffset) + "//" + normalIndexOffset;
							}
							
							faces += "\n";
							newVertexOffset += 3;
						}
					} catch (RuntimeException  e){
						System.out.println("unable to create vege surface");
					} catch (StackOverflowError  e){
						System.out.println("unable to create vege surface");
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
	 * 
	 * @param dtm for the road to match
	 */
	public void setZfromDTM(DTM dtm) {
		// Densify the geometry so it has a number of vertices corresponding to the DTM
		if(geometry.getCoordinates().length > 0) {
//			System.out.println(Densifier.densify(geometry, 5).getGeometryType());			
			geometry = (MultiPolygon) Densifier.densify(geometry, 5);
//			Polygon[] listPoly = new Polygon[]{poly};

//			geometry = new MultiPolygon(listPoly, new GeometryFactory());
//			geometry = (MultiPolygon) geom;

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
	

}

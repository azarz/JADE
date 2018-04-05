package eu.ensg.jade.semantic;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

import eu.ensg.jade.geometricObject.WorldObject;
import eu.ensg.jade.utils.JadeUtils;

/**
 * Vegetation is the class implementing the vegetation to be added to the scene
 * 
 * @author JADE
 */

public class SurfaceVegetation extends WorldObject {
	
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
	 * Constructor with all the fiels
	 * 
	 * @param geometry the polygon geometry of the vegetation
	 * @param natuer the nature of the vegetation surface
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

	public void addHeight() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * This method will have a return that will soon be specified
	 * 
	 * @see eu.ensg.jade.geometricObject.WorldObject#toOBJ(java.util.List)
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
				
				int var = 0;
				
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
						
						int var2 = numTriangles*numGeometries; 
						
						
						
						for (int tri = 0; tri < numTriangles; tri++) {
							var++;
							
							System.out.println("Nous en sommes à "+var+" sur "+var2);
							
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
	

}

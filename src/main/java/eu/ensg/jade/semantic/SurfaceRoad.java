package eu.ensg.jade.semantic;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.vividsolutions.jts.densify.Densifier;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFilter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

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
	Geometry geometry;
	
	
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
	public SurfaceRoad(double width, int wayNumber, double z_ini, double z_fin, String direction, String nature, String importance, String number,  String name, Geometry geometry) {
		super(width, wayNumber, z_ini, z_fin, direction, nature, importance, number, name);
		this.geometry = geometry;
	}
	
	/**
	 * Constructor using a LinearRoad
	 * 
	 * @param road the original LinearRoad
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
	public Geometry getGeom() {
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
		String faces;
		
		faces = "usemtl Road\n";
		
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
				
				
				normalCoords += "vn " + format.format(normalVector[0]) + " " + 
										format.format(normalVector[1]) + " " + 
										format.format(normalVector[2]) + "\n";
				normalIndexOffset++;
				
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
			geometry = Densifier.densify(geometry, dtm.getCellsize()*2);
		}
		
		// Defining a coordinate filter to set the z according to the DTM
		// using bilinear interpolation
		CoordinateSequenceFilter filter = new CoordinateSequenceFilter() {
			
			@Override
			public void filter(CoordinateSequence seq, int i) {
				double dtmHeight = dtm.getHeightAtPoint(seq.getX(i), seq.getY(i));
				seq.setOrdinate(i, 2, dtmHeight + 0.075);
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

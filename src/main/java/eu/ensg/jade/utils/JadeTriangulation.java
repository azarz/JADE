package eu.ensg.jade.utils;

import java.util.ArrayList;
import java.util.List;

import org.poly2tri.Poly2Tri;

import com.jogamp.graph.curve.tess.Triangulation;
import com.jogamp.graph.curve.tess.Triangulator;
import com.jogamp.graph.geom.Outline;
import com.jogamp.graph.geom.SVertex;
import com.jogamp.graph.geom.Triangle;
import com.jogamp.graph.geom.Vertex;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;


/**
 * Triangulation is an utility class to triangulate a JTS polygon using
 * JOGL triangulation
 * 
 * @author JADE
 */
public class JadeTriangulation {
	public static GeometryCollection triangulate(Polygon polygon){
		// List of JTS polygons triangle result
		List<Polygon> resultingList = new ArrayList<Polygon>();
		// List of JOGL triangles to calculate
		List<Triangle> triangleList = new ArrayList<Triangle>();
		
		// Getting the coords of the polygon
		Coordinate[] coordinates = polygon.getCoordinates();
		// JOGL class similar to JTS polygon
		Outline outline = new Outline();
		
		// Converting the JTS polygon into the outline
		for(int i = 0; i < coordinates.length; i++) {
			Vertex vertex = new SVertex();
			vertex.setCoord( (float)coordinates[i].x, 
							 (float)coordinates[i].y, 
							 (float)coordinates[i].z );
			outline.addVertex(vertex);
		}
		
		// Triangulating the outline into the triangle list
		Triangulator triangulator = Triangulation.create();
		triangulator.addCurve(triangleList, outline, 0.5f);
		triangulator.generate(triangleList);

		GeometryFactory factory = new GeometryFactory();
		
		for(int i = 0; i < triangleList.size(); i++) {
			Vertex[] vertices = triangleList.get(i).getVertices();
			Coordinate[] triangleCoords = new Coordinate[4];
			for (int j = 0; j < 4; j++) {
				double x = vertices[j%3].getX();
				double y = vertices[j%3].getY();
				double z = vertices[j%3].getZ();
				triangleCoords[j] = new Coordinate(x, y, z);
			}
			
			Polygon jtsTriangle = factory.createPolygon(triangleCoords);
			resultingList.add(jtsTriangle);
		}
		
		Geometry[] resultArray = resultingList.toArray(new Geometry[resultingList.size()]);
		return factory.createGeometryCollection(resultArray);
	}
}

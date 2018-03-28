package eu.ensg.jade.utils;

import java.util.ArrayList;
import java.util.List;

import org.poly2tri.Poly2Tri;
import org.poly2tri.geometry.polygon.PolygonPoint;
import org.poly2tri.triangulation.TriangulationPoint;
import org.poly2tri.triangulation.delaunay.DelaunayTriangle;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;


/**
 * Triangulation is an utility class to triangulate a JTS polygon using
 * Poly2Tri
 * 
 * @author JADE
 */
public class JadeTriangulation {
	public static GeometryCollection triangulate(com.vividsolutions.jts.geom.Polygon polygon){
		// List of JTS polygons triangle result
		List<com.vividsolutions.jts.geom.Polygon> resultingList = new ArrayList<com.vividsolutions.jts.geom.Polygon>();
		
		// Getting the coords of the polygon
		Coordinate[] coordinates = polygon.getCoordinates();
		
		List<PolygonPoint> polygonPoints = new ArrayList<PolygonPoint>();
		
		// Converting the JTS polygon into polygonPoints
		for(int i = 0; i < coordinates.length - 1; i++) {
			polygonPoints.add(new PolygonPoint(coordinates[i].x, 
											   coordinates[i].y, 
											   coordinates[i].z));
		}
		org.poly2tri.geometry.polygon.Polygon poly2triPolygon = 
				new org.poly2tri.geometry.polygon.Polygon(polygonPoints);
		
		// Triangulating the outline into the triangle list
		Poly2Tri.triangulate(poly2triPolygon);
		List<DelaunayTriangle> triangles = poly2triPolygon.getTriangles();
		
		GeometryFactory factory = new GeometryFactory();
		
		for(int i = 0; i < triangles.size(); i++) {
			TriangulationPoint[] vertices = triangles.get(i).points;
			Coordinate[] triangleCoords = new Coordinate[4];
			for (int j = 0; j < 4; j++) {
				double x = vertices[j%3].getX();
				double y = vertices[j%3].getY();
				double z = vertices[j%3].getZ();
				triangleCoords[j] = new Coordinate(x, y, z);
			}
			
			com.vividsolutions.jts.geom.Polygon jtsTriangle = factory.createPolygon(triangleCoords);
			resultingList.add(jtsTriangle);
		}
		
		Geometry[] resultArray = resultingList.toArray(new Geometry[resultingList.size()]);
		return factory.createGeometryCollection(resultArray);
	}
}

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

import eu.ensg.jade.semantic.LineRoad;

/**
 * JadeUtils is an utility class used mostly to do 3d calculations
 * 
 * @author JADE
 */

public class JadeUtils {

	/**
	 * Calculates the distance between 2 3D points
	 * 
	 * @param p1 3D point as double[3]
	 * @param p2 3D point as double[3]
	 * @return distance between p1 and p2
	 */
	public static double getDistance(double[] p1, double[] p2) {
	    return Math.sqrt(
	    		(p1[0]-p2[0]) * (p1[0]-p2[0]) +
	    		(p1[1] - p2[1]) * (p1[1] - p2[1]) + 
	    		(p1[2] - p2[2]) * (p1[2] - p2[2])
	    		);
	}
	
	/**
	 * Calculates the distance between 2 3D points
	 * 
	 * @param p1 3D point as double[3]
	 * @param p2 3D point as double[3]
	 * @return distance between p1 and p2
	 */
	public static double getDistance(Coordinate p1, Coordinate p2) {
	    return Math.sqrt(
	    		(p1.x-p2.x) * (p1.x-p2.x) +
	    		(p1.y - p2.y) * (p1.y - p2.y) + 
	    		(p1.z - p2.z) * (p1.z - p2.z)
	    		);
	}

	
	/**
	 * Calculates the normal vector of a plan defined by 3 points
	 * <strong>CAUTION!!</strong> X, Y and Z are the same as in OBJ file (Y point towards the sky)
	 * 
	 * @param p1 3D point as double[3]
	 * @param p2 3D point as double[3]
	 * @param p3 3D point as double[3]
	 * 
	 * @return the normal vector as double[3]
	 */
	public static double[] getNormalVector(double[] p1, double[] p2, double[] p3) {
		double[] result = new double[3];
		
		// Calculating the differences between 3 points of the face to calculate the normal vector
		double diff1_x = p2[0] - p1[0];
		double diff1_y = p2[2] - p1[2];
		double diff1_z = p2[1] - p1[1];
		
		double diff2_x = p3[0] - p1[0];
		double diff2_y = p3[2] - p1[2];
		double diff2_z = p3[1] - p1[1];
		
		result[0] = (diff1_y * diff2_z) - (diff1_z * diff2_y);
		result[1] = (diff1_z * diff2_x) - (diff1_x * diff2_z);
		result[2] = (diff1_x * diff2_y) - (diff1_y * diff2_x);
		
		double norm = Math.sqrt(Math.pow(result[0],2) + Math.pow(result[1],2) + Math.pow(result[2],2));
		
		result[0] /= norm;
		result[1] /= norm;
		result[2] /= norm;

		return result;
	}
	
	/**
	 * Calculates the normal vector of a plan defined by 3 {@link com.vividsolutions.jts.geom.Coordinate}
	 * <strong>CAUTION!!</strong> X, Y and Z are the same as in OBJ file (Y point towards the sky)
	 * 
	 * @param p1 3D point as jts Coordinate
	 * @param p2 3D point as jts Coordinate
	 * @param p3 3D point as jts Coordinate
	 * 
	 * @return the normal vector as double[3]
	 */
	public static double[] getNormalVector(Coordinate p1, Coordinate p2, Coordinate p3) {
		double[] result = new double[3];
		
		// Calculating the differences between 3 points of the face to calculate the normal vector
		double diff1_x = p2.x - p1.x;
		double diff1_y = p2.z - p1.z;
		double diff1_z = p2.y - p1.y;
		
		double diff2_x = p3.x - p1.x;
		double diff2_y = p3.z - p1.z;
		double diff2_z = p3.y - p1.y;
		
		result[0] = (diff1_y * diff2_z) - (diff1_z * diff2_y);
		result[1] = (diff1_z * diff2_x) - (diff1_x * diff2_z);
		result[2] = (diff1_x * diff2_y) - (diff1_y * diff2_x);
		
		double norm = Math.sqrt(Math.pow(result[0],2) + Math.pow(result[1],2) + Math.pow(result[2],2));
		
		result[0] /= norm;
		result[1] /= norm;
		result[2] /= norm;
		
		return result;
	}

	
	/**
	 * Calculates the angle considering a road and position
	 * 
	 * @param road The road
	 * @param position The position
	 * 
	 * @return The angle of the road
	 */
	public static double roadAngle(LineRoad road, int position){
		
		// We determine the ends of the roads
		Coordinate ini;
		Coordinate end;
		
		if(position == 0){
			ini = road.getGeom().getCoordinates()[0];
			end = road.getGeom().getCoordinates()[1];
		}
		else{	
			ini = road.getGeom().getCoordinates()[position];
			end = road.getGeom().getCoordinates()[position-1];
		}
		
		double theta = 0.0;
		
		// We determine the angle from horizontal in trigo order
	    if (ini.y >= end.y){
			//Top right
	        if(ini.x <= end.x){
	            theta = Math.asin( (end.x - ini.x)/ini.distance(end) );

	        }
	        //bottom right
	        else{
	            theta = 2*Math.PI - Math.asin( (ini.x - end.x)/ini.distance(end) );

	        }
	    }
	    else {
			//top left
	        if(ini.x <= end.x){
	           theta = Math.PI - Math.asin( (end.x-ini.x)/ini.distance(end) );

	        }
	        //bottom left
	        else{
	            theta = Math.PI + Math.asin( (ini.x-end.x)/ini.distance(end) );

	        }
	    }
		
		return theta; 
	}
	
	/**
	 * Splits a polygon into triangles (triangulates it)
	 * 
	 * @param polygon The polygon to triangulate
	 * 
	 * @return The collection of triangles
	 */
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
	
	/**
	 * Linear Interpolation of x between a and b:
	 * n = (1-x) * a  + x * b
	 * 
	 * @param a Lower limit
	 * @param b Upper Limit
	 * @param x Value to interpolate
	 * 
	 * @return The linear interpolation result
	 */
	public static double lerp(double a, double b, double x) {
		return (1-x)*a + x*b;
	}

}

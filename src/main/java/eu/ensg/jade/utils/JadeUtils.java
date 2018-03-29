package eu.ensg.jade.utils;

import com.vividsolutions.jts.geom.Coordinate;

import eu.ensg.jade.semantic.DTM;
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
	 * Calculates the normal vector of a plan defined by 3 points
	 * <strong>CAUTION!!</strong> X, Y and Z are the same as in OBJ file (Y point towards the sky)
	 * 
	 * @param p1 3D point as double[3]
	 * @param p2 3D point as double[3]
	 * @param p3 3D point as double[3]
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
	 * Calculates the interpolated altitude value 
	 * from a DTM and a set of XY coordinates using JME interpolation
	 * (https://github.com/jMonkeyEngine/jmonkeyengine/blob/master/jme3-terrain/src/main/java/com/jme3/terrain/heightmap/AbstractHeightMap.java)
	 * 
	 * @param xCoord X coordinate
	 * @param yCoord Z coordinate
	 * @param dtm the DTM to interpolate from
	 * @return interpolated Z
	 */
	public static double interpolatedDtmValue(double xCoord, double yCoord, DTM dtm) {
		// Fetching the DTM data
		double xllCorner = dtm.getXllcorner();
		double yllCorner = dtm.getYllcorner();
		double cellsize = dtm.getCellsize();
		int nrows = dtm.getNrows();
		
		// Calculating the indices of the 4 cells around the point
		int westIndex = (int) Math.floor((xCoord - xllCorner)/cellsize);
		int eastIndex = 1 + (int) Math.floor((xCoord - xllCorner)/cellsize);
		int southIndex = 1 + (int) Math.floor(nrows- ((yCoord - yllCorner)/cellsize));
		int northIndex = (int) Math.floor(nrows - ((yCoord - yllCorner)/cellsize));
		
		// Getting the 4 cells values
		double northWestValue = dtm.getTabDTM()[northIndex][westIndex];
		double northEastValue = dtm.getTabDTM()[northIndex][eastIndex];
		double southWestValue = dtm.getTabDTM()[southIndex][westIndex];
		double southEastValue = dtm.getTabDTM()[southIndex][eastIndex];
		
		// Calculating the distances between the point's coordinates
		// and the corners coordinates
		double fracWest = xCoord - (xllCorner + westIndex*cellsize);
		double fracEast = xllCorner + eastIndex*cellsize - xCoord;
		double fracSouth = yCoord - (yllCorner + (nrows - southIndex)*cellsize);
		double fracNorth = yllCorner + (nrows - northIndex)*cellsize - yCoord;
		
		double intX = ((northEastValue - northWestValue) * fracWest) + northWestValue*cellsize;	
		
		double intY = ((southWestValue - northWestValue) * fracNorth) + northWestValue*cellsize;

		return (intX + intY)/(2*cellsize);
	}
	
	/**
	 * 
	 */
	public static double roadAngle(LineRoad road){
		
		// We determine the ends of the roads
		Coordinate ini = road.getGeom().getCoordinates()[0];
		Coordinate end = road.getGeom().getCoordinates()[road.getGeom().getCoordinates().length-1];
		
		double theta = 0.0;
		
		// We determine the angle from horizontal in trigo order
	    if (ini.x >= end.x){
			//Top right
	        if(ini.y >= end.y){
	            theta = Math.asin( (ini.y - end.y)/ini.distance(end) );
	        }
	        //bottom right
	        else{
	            theta = 2*Math.PI + Math.asin( (ini.y - end.y)/ini.distance(end) );
	        }
	    }
	    else {
			//top left
	        if(ini.y >= end.y){
	           theta = Math.PI - Math.asin( (ini.y-end.y)/ini.distance(end) );
	        }
	        //bottom left
	        else{
	            theta = Math.PI - Math.asin( (ini.y-end.y)/ini.distance(end) );
	        }
	    }
		
		return theta; 
	}
	
	public static double lineAngle(Coordinate p1, Coordinate p2) {
		double dx = p2.x - p1.x;
		double dy = p2.y - p1.y;
		
		double dist = Math.sqrt(dx*dx + dy*dy);
		dx /= dist;
		dy /= dist;
		
		return Math.atan(dy / dx);
	}

}

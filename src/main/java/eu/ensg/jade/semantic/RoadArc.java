package eu.ensg.jade.semantic;

import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.jts.CircularArc;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.operation.distance.DistanceOp;



/**
 * Arc is the class arc in the intersection of each two roads
 *
 * @author JADE
 */

public class RoadArc {
	
	// ========================== ATTRIBUTES ===========================
    /**
    * The attribute containing the radius of the arc
    */
    private double radius;
    
    private LineRoad road1;
    private LineRoad road2;
    
 // ========================== CONSTRUCTORS =========================  
    /**
    * Constructor using all fields
    *
    * @param road1 One of the roads
    * @param road2 The other road
    */  
    public RoadArc(LineRoad road1, LineRoad road2){
    	this.road1 = road1;
    	this.road2 = road2;
    	this.radius = this.computeRadius(road1, road2);
    }
    
    // ========================== GETTERS/SETTERS ======================
    
    /**
    * Allows to access the radius
    *
    * @return arc's radius
    */

    public double getRadius() {
        return radius;
    }

// ========================== METHODS ==============================

	/**
	 * Determines the cutting points on each road by projecting both extremities of the arc
	 * on each road. Only farthest point from the intersection are conserved
	 *
	 * @param extremity1 One extremity
	 * @param extremity2 Another extremity
	 * @param r1 One road
	 * @param r2 Another road
	 * @param pt_I intersection point
	 * @return List of the cutting points
	 * 
	 */
	public static List<Point> cuttingPoint(Point extremity1, Point extremity2, LineRoad r1, LineRoad r2, Point pt_I) {
		GeometryFactory geomFactory = new GeometryFactory();
		List<Point> result = new ArrayList<Point>();
		Geometry geomRoad1 = r1.getGeom();
		Geometry geomRoad2 = r2.getGeom(); 

		//Projecting both extremities of the arc on the first road
		DistanceOp do1 = new DistanceOp(geomRoad1,extremity1);
		DistanceOp do2 = new DistanceOp(geomRoad1,extremity2);
		Coordinate[] coord1 = do1.nearestPoints();
		Coordinate[] coord2 = do2.nearestPoints();
		Point pointOnLine1 = geomFactory.createPoint(new Coordinate(coord1[0].x,coord1[0].y));
		Point pointOnLine2 = geomFactory.createPoint(new Coordinate(coord2[0].x,coord2[0].y));
		double d1 = pt_I.distance(pointOnLine1);
		double d2 = pt_I.distance(pointOnLine2);
		
		//conserving the farthest projected point from the intersection
		if(d1>d2) {
			result.add(pointOnLine1);
		}
		else {
			result.add(pointOnLine2);
		}

		//Projecting both extremities of the Arc on the second road
		do1 = new DistanceOp(geomRoad2,extremity1);
		do2 = new DistanceOp(geomRoad2,extremity2);
		coord1 = do1.nearestPoints();
		coord2 = do2.nearestPoints();
		pointOnLine1 = geomFactory.createPoint(new Coordinate(coord1[0].x,coord1[0].y));
		pointOnLine2 = geomFactory.createPoint(new Coordinate(coord2[0].x,coord2[0].y));
		double d3 = pt_I.distance(pointOnLine1);
		double d4 = pt_I.distance(pointOnLine2);
		
		//conserving the farthest projected point from the intersection	
		if(d3>d4) { 
			result.add(pointOnLine1);
		}
		else {
			result.add(pointOnLine2);
		}

		return result;
	}
	
	/*
	 * Instance methods
	 */
	
	/**
	 * Get the point of intersection between a buffer and a Line
	 *
	 * @param buffer The buffer
	 * @param element Geometric element
	 * @param pt_I Closest point to the wanted intersection point
	 * 
	 * @return Point of intersection
	 */
	private Point getIntersection(Geometry buffer, Geometry element, Point pt_I) {
		//Getting the intersection
		Geometry geom;
        geom = buffer.intersection(element);
		if(!geom.isEmpty()) {
			//We get the intersection point
			LineString ls = (LineString ) geom.getGeometryN(0);
			Point ptStart = ls.getStartPoint();
			Point ptEnd = ls.getEndPoint();
			Point ptInter = (pt_I.distance(ptStart) > pt_I.distance(ptEnd))? ptStart : ptEnd;
			return ptInter;
		}
		return null;
	}

	/**
	 * Calculate the center of the arc by estimating a radius for each road by
	 * using this formula: r = 18.6 * SQRT(road_speed/ (10 * road_width + 65 - road_speed)
	 *
	 * @param road1 One Arc road
	 * @param road2 Another Arc road 
	 *
	 * @return the arc center
	 */
	private Point getCenter(LineRoad road1, LineRoad road2){
		//Getting the intersection of 2 roads
		Point inter = (Point) (road1.getGeom().intersection(road2.getGeom()).getGeometryN(0));
		
		double speed1 = Integer.parseInt( road1.getSpeed().substring(0, 3).trim());
        double speed2 = Integer.parseInt( road2.getSpeed().substring(0, 3).trim());
        if(speed1>70) speed1=35;
        if(speed2>70) speed2=35;
        double r1 = 18.6 * Math.sqrt(speed1/(Math.abs(10*(road1.getWidth())+65-speed1)));
        double r2 = 18.6 * Math.sqrt(speed2/(Math.abs(10*(road2.getWidth())+65-speed2)));
        radius = Math.min(r1, r2);
        
		//Calculate the points for middle
        LineString lineString1 = lineStringIntersection(road1.getGeom(),inter);
        LineString lineString2 = lineStringIntersection(road2.getGeom(),inter);

        double angle = angleBetweenRoads(road1, road2)*Math.PI/180;
        if(angle == 0 ) angle = Math.PI/2;
        Point temp1 = getIntersection(
        		inter.buffer( 0.5*road2.getWidth() / Math.abs(Math.sin(Math.PI-angle))+(radius)),
        		lineString1,inter);
        Point temp2 = getIntersection(
        		inter.buffer(0.5*road1.getWidth() / Math.abs(Math.sin(Math.PI-angle))+(radius)),
        		lineString2,inter);
        
		if(temp1 != null && temp2 != null) {
			//Calculate the coordinates of the arc's center
			double xD =  (temp1.getX() + temp2.getX() - inter.getX());
			double yD = (temp1.getY() + temp2.getY() - inter.getY());
			Point centreArc = (Point) new GeometryFactory().createPoint(new Coordinate(xD,yD));
			return centreArc;
		}
		return null;
	}



	/**
	 * Calculates middle point of an arc knowing the start point, the end point, center and radius.
	 * Used by {@link RoadArc#getCircularArc() getCircularArc}
	 *
	 * @param startPoint The starting point of the arc
	 * @param endPoint The end point of the arc
	 * @return midpoint the middle point of the arc
	 *
	 */
	private Point calculMidPoint(Point startPoint, Point endPoint){
		if(startPoint.isEmpty() || endPoint.isEmpty()) {
			return null;
		}
		
		Point centre = getCenter(road1,road2);
        double xd = (startPoint.getX()+endPoint.getX())/2;
        double yd = (startPoint.getY()+endPoint.getY())/2;
        double[] vect = new double[2];
        vect[0] = xd-centre.getX();
        vect[1] = yd-centre.getY();
        
        double norm = Math.sqrt(vect[0]*vect[0] + vect[1]*vect[1]);
        double xmid = centre.getX()+ (vect[0]/norm)*radius;
        double ymid = centre.getY()+ (vect[1]/norm)*radius;
        
        return new GeometryFactory().createPoint(new Coordinate(xmid,ymid));
	}
	
	
	/**
	 * Creates a new point that represents the limit of an arc.
	 * Used by {@link RoadArc#getArcLimits() getArcLimits}
	 * 
	 * @param road A road from the arc
	 * @param ptIntersection The intersection point of the arc
	 * @return A new point
	 */
	private Point createArcLimit(LineRoad road, Point ptIntersection) {
		GeometryFactory geomFactory = new GeometryFactory();
		
		DistanceOp distOp = new DistanceOp(road.getGeom(),ptIntersection);
        Coordinate[] nearestPoints = distOp.nearestPoints();
        Point nearestPoint = geomFactory.createPoint(new Coordinate(nearestPoints[0].x,nearestPoints[0].y));
        
        double dx = ptIntersection.getX() - nearestPoint.getX();
        double dy = ptIntersection.getY() - nearestPoint.getY();
        double dist = ptIntersection.distance(nearestPoint);
        return geomFactory.createPoint(new Coordinate(
       		 nearestPoint.getX() + (road.getWidth()/2) * dx/dist,
       		 nearestPoint.getY() + (road.getWidth()/2) * dy/dist));
	}

	/**
	 * Calculates the two points limiting the arc.
	 * Used by {@link RoadArc#getCircularArc() getCircularArc}
	 *
	 * @param road1 One Arc road
	 * @param road2 Another Arc road
	 *
	 * @return list of the the two point limiting the arc
	 */
	private List<Point> getArcLimits(){
		Point pt_Intersection = getCenter(road1,road2);
		if(pt_Intersection == null ) return null;
		
        Point p1 = (pt_Intersection.buffer(radius)).intersection(road1.getGeom().buffer(road1.getWidth()/2+0.4)).getCentroid();
        if(p1==null) {
        	 p1 = createArcLimit(road1, pt_Intersection);
        }
        
        Point p2 = (pt_Intersection.buffer(radius)).intersection(road2.getGeom().buffer(road2.getWidth()/2+0.4)).getCentroid();
        if(p2==null) {
        	 p2 = createArcLimit(road1, pt_Intersection);
        }
        
        if( p1 == null || p2 == null ) return null;
        
        List<Point> resultat = new ArrayList<Point>();
		resultat.add(p1);
		resultat.add(p2);
		return resultat;
	}


	/**
	 * Create the CircularArc geometry corresponding to this RoadArc
	 *
	 * @param road1 first road
	 * @param road2 second road
	 * @return circularArc the arc
	 */ 
	public CircularArc getCircularArc(){
		if(road1.getWidth()==0 ||
			road2.getWidth()==0 ||
			road1.equals(road2) ||
			road1.getName().startsWith("PL") ||
			road2.getName().startsWith("PL") ||
			road1.getName().startsWith("RPT") ||
			road2.getName().startsWith("RPT")
			){
			return null;
		}

        if(angleBetweenRoads(road1,road2)<152 ) {
        	List<Point> arcPoint = getArcLimits();
        	if(arcPoint != null) {
        		Point midPoint = calculMidPoint(arcPoint.get(0), arcPoint.get(1)); 
        		if(midPoint != null) {
        			return new CircularArc(
        					arcPoint.get(0).getX(),arcPoint.get(0).getY(),
        					midPoint.getX(),midPoint.getY(),
        					arcPoint.get(1).getX(),arcPoint.get(1).getY()
        					);
        		}
        	}
        }
        return null;
	}

	/**
	 * Calculates the radius of an arc between two roads. The radius is estimated with the formula:<br>
	 * radius = 18.6 * SQRT(road_speed/ (10 * road_width + 65 - road_speed)
	 * 
	 * @param road1 One road
	 * @param road2 Another road
	 * 
	 * @return radius
	 */
	private double computeRadius(LineRoad road1, LineRoad road2){
		//Calculates the radius
		double speed1 = Integer.parseInt( road1.getSpeed().substring(0, 3).trim());
		double speed2 = Integer.parseInt( road2.getSpeed().substring(0, 3).trim());
		double r1 = 18.6 * Math.sqrt(speed1/(Math.abs(10*(road1.getWidth())+65-speed1)));
		double r2 = 18.6 * Math.sqrt(speed2/( Math.abs(10*(road2.getWidth())+65-speed2)));
		return Math.min(r1, r2);
	}
	
	
	/*
	 * Static methods
	 */


	/**
	 * Checks if the first LineString of multilineString is the one forming the intersection.
	 * Used by {@link RoadArc#angleBetweenRoads(LineRoad, LineRoad) angleBetweenRoads}
	 *
	 * @param multi multiLineString to test
	 * @param pointInter Intersection point
	 * 
	 * @return the lineString forming the intersection 
	 */ 
	private static LineString lineStringIntersection(MultiLineString multi, Point pointInter) {
		LineString firstLine = (LineString) multi.getGeometryN(0);
		Coordinate[] multiCoord = multi.getCoordinates();
		Coordinate[] tabCoord1 = {multiCoord[0],multiCoord[1]};
		
		LineString subLine = new GeometryFactory().createLineString(tabCoord1);
		Point first=subLine.getStartPoint();
		Point second=subLine.getEndPoint();
		
		Point ptI= new GeometryFactory().createPoint(new Coordinate(pointInter.getX(),pointInter.getY()));
		if( (!first.equalsExact(ptI)) && (!second.equalsExact(ptI)) ){
			return firstLine;
		}
		return subLine;
	}

	/**
	 * Calculates the angle between two roads
	 *
	 * @param lineroad1 First LineRoad
	 * @param lineroad2 Second LineRoad
	 * 
	 * @return angle between two roads
	 */
	public static double angleBetweenRoads(LineRoad lineroad1, LineRoad lineroad2) {
		//Get the relevent points
		MultiLineString road1 = lineroad1.getGeom();
		MultiLineString road2 = lineroad2.getGeom();
		Point ptIntersection = (Point) road1.intersection(road2).getCentroid();
		if (ptIntersection.isEmpty()){
			return Double.NaN;
		}
		
		LineString linestring1 = lineStringIntersection(road1,ptIntersection);
		LineString linestring2 = lineStringIntersection(road2,ptIntersection);
		Point ptStart1 =  linestring1.getStartPoint();
		Point ptEnd1 = linestring1.getEndPoint();
		Point ptStart2 = linestring2.getStartPoint();
		Point ptEnd2 = linestring2.getEndPoint();
		Point sommet1 = (ptIntersection.distance(ptStart1) > ptIntersection.distance(ptEnd1))? ptStart1 : ptEnd1;
		Point sommet2 = (ptIntersection.distance(ptStart2) > ptIntersection.distance(ptEnd2))? ptStart2 : ptEnd2;

		//Calculate the vectors
		double diffCoord1[] = new double[2];
		diffCoord1[0] = sommet1.getX()-ptIntersection.getX();
		diffCoord1[1] = sommet1.getY()-ptIntersection.getY();

		double diffCoord2[] = new double[2];
		diffCoord2[0] = sommet2.getX()-ptIntersection.getX();
		diffCoord2[1] = sommet2.getY()-ptIntersection.getY();

		//Scalar product
		double prodScalaire = diffCoord1[0]*diffCoord2[0] + diffCoord1[1]*diffCoord2[1];
		//Norm
		double normA = Math.sqrt(diffCoord1[0]*diffCoord1[0] + diffCoord1[1]*diffCoord1[1]);
		double normB = Math.sqrt(diffCoord2[0]*diffCoord2[0] + diffCoord2[1]*diffCoord2[1]);
		//Angle
		double angle = Math.acos(prodScalaire/(normA*normB));
		
		//Returning the result as degree and not radiant
		return angle*(180.0/Math.PI);
	}


	/**
	 * Checks if the arc intersects a road
	 *
	 * @param arc the arc
	 * @param road the road to check for intersection
	 * 
	 * @return true if the arc intersects the road
	 */

	public static boolean intersectOther(CircularArc arc, LineRoad road){
		double[] points = arc.linearize(1);
		boolean result=false;
		GeometryFactory geomFactory = new GeometryFactory();

		// create a list of connected positions
		Coordinate firstArcPoint = new Coordinate(points[0],points[1]);
		Coordinate lastArcPoint = new Coordinate(points[points.length-2],points[points.length-1]);
        Coordinate midArcPoint = new Coordinate(points[(points.length/2)-1],points[(points.length/2)]);

		LineString arcLine1 = geomFactory.createLineString(new Coordinate[]{firstArcPoint,lastArcPoint});
		LineString arcLine2 = geomFactory.createLineString(new Coordinate[]{firstArcPoint,midArcPoint});
		LineString arcLine3 = geomFactory.createLineString(new Coordinate[]{lastArcPoint,midArcPoint});

		int a=road.getGeom().getCoordinates().length;
		Coordinate[] tab= {road.getGeom().getCoordinates()[0] , road.getGeom().getCoordinates()[a-1]};
		LineString roadLine = geomFactory.createLineString(tab);
		if(roadLine.intersects(arcLine1) || roadLine.intersects(arcLine2) || roadLine.intersects(arcLine3)) {
			result = true;
		}
		if(road.getGeom().getNumGeometries()>0) {
			roadLine = (LineString) road.getGeom().getGeometryN((road.getGeom().getNumGeometries())-1);
			if(roadLine.intersects(arcLine1) || roadLine.intersects(arcLine2) || roadLine.intersects(arcLine3)) {
				result = true;
			}
		}
		return result;
	}
}
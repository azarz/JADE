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
    /**
     * The attribute containing the circularArc
     */
    private CircularArc geomArc;
    
 // ========================== CONSTRUCTORS =========================  
    /**
    * Constructor using all fields
    *
    * @param road1 One of the roads
    * @param road2 The other road
    */  
    public RoadArc(LineRoad road1, LineRoad road2){      
            double speed1 = Integer.parseInt( road1.getSpeed().substring(0, 3).trim());
            double speed2 = Integer.parseInt( road2.getSpeed().substring(0, 3).trim());
            double r1 = 18.6 * Math.sqrt(  speed1/(Math.abs( Math.abs(10*(road1.getWidth())+65-speed1) ) ) );
            double r2 = 18.6 * Math.sqrt(  speed2/( Math.abs(10*(road2.getWidth())+65-speed2) )  );
           double r = Math.min(r1, r2);
            this.radius=r;
    }
    
    // ========================== GETTERS/SETTERS ======================
    
    /**
     * Allows to access the circularArc
     *
     * @return the road's table
     */
    public CircularArc getGeomArc() {
        return geomArc;
    }
    
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
	 * Get the point of intersection between a buffer and a Line
	 *
	 * @param buffer The buffer
	 * @param element Geometric element
	 * @param pt_I Closest point to the wanted intersection point
	 * 
	 * @return Point of intersection
	 */
	private Point getIntersection(Geometry buffer, Geometry element, Point pt_I)
	{
		//Getting the intersection
		Geometry geom;
        geom = buffer.intersection(element);		
		if(!geom.isEmpty()) {
			//We get the wanted intersection point
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
	private Point computeCenter(LineRoad road1, LineRoad road2){
		//Getting the intersection of 2 roads
		Point inter = (Point) (road1.getGeom().intersection(road2.getGeom()).getGeometryN(0));

		
		double speed1 = Integer.parseInt( road1.getSpeed().substring(0, 3).trim());
        double speed2 = Integer.parseInt( road2.getSpeed().substring(0, 3).trim());
        if(speed1>70) speed1=35;
        if(speed2>70) speed2=35;
        double r1 = 18.6 * Math.sqrt(speed1/(Math.abs(10*(road1.getWidth())+65-speed1)));
        double r2 = 18.6 * Math.sqrt(speed2/(Math.abs(10*(road2.getWidth())+65-speed2)));
        double r = Math.min(r1, r2);
        this.radius = r;
        
		//Calculate the points for middle
        LineString lineString1= lineStringIntersection(road1.getGeom(),inter);
        LineString lineString2= lineStringIntersection(road2.getGeom(),inter);

        double angle = calculAngle(road1, road2)*Math.PI/180;
        if(angle == 0 ) angle = Math.PI/2 ;
        Point temp1 = getIntersection(inter.buffer((road2.getWidth()/2)/Math.abs(Math.sin(Math.PI-angle))+(r)),lineString1,inter);
        Point temp2 = getIntersection(inter.buffer((road1.getWidth()/2)/Math.abs(Math.sin(Math.PI-angle))+(r)),lineString2,inter);
        
		if(temp1 != null && temp2 != null) {
			//Calculate the coordinates of the arc's center
			double xD =  (temp1.getX() + temp2.getX() - inter.getX());
			double yD = (temp1.getY() + temp2.getY() - inter.getY());
			GeometryFactory gf = new GeometryFactory();
			Point centreArc = (Point) gf.createPoint(new Coordinate(xD,yD));
			return centreArc;}
		return null;
	}

	/**
	 * Calculates the two points limiting the arc
	 *
	 * @param road1 One Arc road
	 * @param road2 Another Arc road
	 *
	 * @return list of the the two point limiting the arc
	 */
	private List<Point> calculerPointArc(LineRoad road1, LineRoad road2){

		List <Point> resultat = new ArrayList<Point>();
		Point pt_Intersection = computeCenter(road1,road2);
		if(pt_Intersection != null ) {
	        Point p1 = (pt_Intersection.buffer(this.radius)).intersection(road1.getGeom().buffer(road1.getWidth()/2+0.4)).getCentroid();
	        if(p1==null) {
	        	 DistanceOp do1 = new DistanceOp(road1.getGeom(),pt_Intersection);
	             Coordinate[] coord2 = do1.nearestPoints();
	             Point project2 = new GeometryFactory().createPoint(new Coordinate(coord2[0].x,coord2[0].y));
	             double norme2 = Math.sqrt( Math.pow(pt_Intersection.getX()-project2.getX(), 2) + Math.pow(pt_Intersection.getY()-project2.getY(), 2) );
	             p1 = new GeometryFactory().createPoint(new Coordinate(project2.getX()+(road1.getWidth()/2)*((pt_Intersection.getX()-project2.getX())/norme2),project2.getY()+(road1.getWidth()/2)*((pt_Intersection.getY()-project2.getY())/norme2)));        
	        }
	        
	        Point p2 = (pt_Intersection.buffer(this.radius)).intersection(road2.getGeom().buffer(road2.getWidth()/2+0.4)).getCentroid();
	        if(p2==null) {
	        	 DistanceOp do2 = new DistanceOp(road2.getGeom(),pt_Intersection);
	             Coordinate[] coord1 = do2.nearestPoints();
	             Point project = new GeometryFactory().createPoint(new Coordinate(coord1[0].x,coord1[0].y));
	             double norme = Math.sqrt( Math.pow(pt_Intersection.getX()-project.getX(), 2) + Math.pow(pt_Intersection.getY()-project.getY(), 2) );
	             p2 = new GeometryFactory().createPoint(new Coordinate(project.getX()+(road2.getWidth()/2)*((pt_Intersection.getX()-project.getX())/norme),project.getY()+(road2.getWidth()/2)*((pt_Intersection.getY()-project.getY())/norme)));             
	        }
	        
	        if( p1 != null && p2 != null ) {
				resultat.add(p1);
				resultat.add(p2);}
			return resultat;}
		return null;
	}

	/**
	 * Checks if the arc intersects a road
	 *
	 * @param arc the arc
	 * @param road the road to check for intersection
	 * @return true if the arc intersects the road
	 */

	public static boolean intersectOther(CircularArc arc, LineRoad road){
		double[] points = arc.linearize(1);
		boolean result=false;

		// create a list of connected positions
		Coordinate firstArcPoint = new Coordinate(points[0],points[1]);
		Coordinate secondArcPoint = new Coordinate(points[points.length-2],points[points.length-1]);
        Coordinate thirdPoint = new Coordinate(points[(points.length/2)-1],points[(points.length/2)]);
		Coordinate tabCoord[]= {firstArcPoint,secondArcPoint};
		Coordinate tabCoord2[]= {firstArcPoint,thirdPoint};
		Coordinate tabCoord3[]= {secondArcPoint,thirdPoint};
		LineString lineRoute=new GeometryFactory().createLineString(tabCoord);
		LineString lineRoute2=new GeometryFactory().createLineString(tabCoord2);
		LineString lineRoute3=new GeometryFactory().createLineString(tabCoord3);

		int a=road.getGeom().getCoordinates().length;
		Coordinate[] tab= {road.getGeom().getCoordinates()[0] , road.getGeom().getCoordinates()[a-1]};
		LineString line = new GeometryFactory().createLineString(tab);
		if(line.intersects(lineRoute) || line.intersects(lineRoute2) || line.intersects(lineRoute3))
			result=true;
		if(road.getGeom().getNumGeometries()>0) {
			LineString line2= (LineString) road.getGeom().getGeometryN((road.getGeom().getNumGeometries())-1);
			if(line2.intersects(lineRoute) || line2.intersects(lineRoute2) || line2.intersects(lineRoute3))
				result=true;}
		return result;
	}

	/**
	 * Determinates the cutting points on each road by projecting both extremities of the arc
	 * on each road. Only furthest point from the intersection are conserved
	 *
	 * @param extremity1 One extremity
	 * @param extremity2 Another extremity
	 * @param r1 One road
	 * @param r2 Another road
	 * @param pt_I intersection point
	 * @return List of the cutting points
	 * 
	 */
	public static List<Point> cuttingPoint(Point extremity1, Point extremity2,LineRoad r1, LineRoad r2, Point pt_I) {
		List<Point> result = new ArrayList<Point>();
		Geometry geomRoad1 =  r1.getGeom();
		Geometry geomRoad2 =  r2.getGeom(); 

		//Projecting both extremities of the arc on the first road
		DistanceOp do1 = new DistanceOp(geomRoad1,extremity1);
		DistanceOp do2 = new DistanceOp(geomRoad1,extremity2);
		Coordinate[] coord1 = do1.nearestPoints();
		Coordinate[] coord2 = do2.nearestPoints();
		Point pointOnLine = new GeometryFactory().createPoint(new Coordinate(coord1[0].x,coord1[0].y));
		Point pointOnLine2 = new GeometryFactory().createPoint(new Coordinate(coord2[0].x,coord2[0].y));
		double d1 = pt_I.distance(pointOnLine);
		double d2 = pt_I.distance(pointOnLine2);
		if(d1>d2) { 
			//conserving the farthest projected point from the intersection
			result.add(pointOnLine);       
		}
		else {
			result.add(pointOnLine2);
		}

		//Projecting both extremities of the Arc on the second road 
		DistanceOp do3 = new DistanceOp(geomRoad2,extremity1);
		DistanceOp do4 = new DistanceOp(geomRoad2,extremity2);
		Coordinate[] coord3 = do3.nearestPoints();
		Coordinate[] coord4 = do4.nearestPoints();
		Point pointOnLine3 = new GeometryFactory().createPoint(new Coordinate(coord3[0].x,coord3[0].y));
		Point pointOnLine4 = new GeometryFactory().createPoint(new Coordinate(coord4[0].x,coord4[0].y));
		double d3 = pt_I.distance(pointOnLine3);
		double d4 = pt_I.distance(pointOnLine4);
		if(d3>d4)  { 
			//conserving the farthest projected point from the intersection	
			result.add(pointOnLine3);
		}
		else {
			result.add(pointOnLine4);
		}

		return result;
	}


	/**
	 * Calculates the angle between two roads
	 *
	 * @param lineroad1 First LineRoad
	 * @param lineroad2 Second LineRoad
	 * @return angle between two roads
	 *
	 */
	public static double calculAngle(LineRoad lineroad1, LineRoad lineroad2)
	{ 
		//Get the relevent points
		MultiLineString road1 = lineroad1.getGeom();
		MultiLineString road2 = lineroad2.getGeom();
		if (isUnconnected(road1, road2)){
			return Double.NaN;
		}
		Point ptIntersection = (Point) road1.intersection(road2).getCentroid();
		LineString linestring1 = lineStringIntersection(road1,ptIntersection);
		LineString linestring2 = lineStringIntersection(road2,ptIntersection);
		Point ptStart1 =  linestring1.getStartPoint();
		Point ptEnd1 = linestring1.getEndPoint();
		Point ptStart2 = linestring2.getStartPoint();
		Point ptEnd2 = linestring2.getEndPoint();
		Point  sommet1 = (ptIntersection.distance(ptStart1) > ptIntersection.distance(ptEnd1))? ptStart1 : ptEnd1;
		Point  sommet2 = (ptIntersection.distance(ptStart2) > ptIntersection.distance(ptEnd2))? ptStart2 : ptEnd2;

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
	 * Calculates middle point of an arc knowing the start point, the end point, center and radius
	 *
	 * @param road1 one of the roads
	 * @param road2 the other road
	 * @param startPoint The starting point of the arc
	 * @param endPoint The end point of the arc
	 * @return midpoint the middle point of the arc
	 *
	 */
	private Point calculMidPoint(LineRoad road1, LineRoad road2,Point startPoint, Point endPoint){

		if( !startPoint.isEmpty() && !endPoint.isEmpty()) {
	        Point centre = this.computeCenter(road1,road2);
	        double xd = (startPoint.getX()+endPoint.getX())/2;
	        double yd = (startPoint.getY()+endPoint.getY())/2;
	        double[] vect = new double[2];
	        vect[0] = xd-centre.getX();
	        vect[1] = yd-centre.getY();
	        double[] norm = new double[2];
	        norm[0] = vect[0]/( Math.sqrt( Math.pow(vect[0], 2) + Math.pow(vect[1], 2) )  );
	        norm[1] = vect[1]/( Math.sqrt( Math.pow(vect[0], 2) + Math.pow(vect[1], 2) )  );
	        double xmid = centre.getX()+ norm[0]*this.radius;
	        double ymid = centre.getY()+norm[1]*this.radius;
	        Point midPoint = new GeometryFactory().createPoint(new Coordinate(xmid,ymid));
	        return midPoint; }
	        return null;
	}


	/**
	 * Create an arc between two intersecting roads 
	 *
	 * @param road1 first road
	 * @param road2 second road
	 * @return circularArc the arc
	 *
	 */ 
	public CircularArc createRoadArc(LineRoad road1, LineRoad road2){
		if (road1.getWidth()==0 || road2.getWidth()==0) return null;
		if(road1.equals(road2)) return null ;
        if(road1.getName().startsWith("PL") || road1.getName().startsWith("RPT")) return null;
        if(road2.getName().startsWith("PL") || road2.getName().startsWith("RPT")) return null;
        if(calculAngle(road1,road2)<152 )
        {
        List<Point> arcPoint = calculerPointArc(road1,road2);
        if(arcPoint != null && !arcPoint.isEmpty()) {
        Point midPoint = calculMidPoint(road1,road2,arcPoint.get(0), arcPoint.get(1)); 
        if(midPoint != null)
        this.geomArc = new CircularArc(arcPoint.get(0).getX(),arcPoint.get(0).getY(), midPoint.getX(),midPoint.getY(),arcPoint.get(1).getX(),arcPoint.get(1).getY());
        return this.geomArc ;   
        }
        } return null;
	}

	/**
	 * Checks if the first LineString of multilineString is the one forming the intersection
	 *
	 * @param multi multiLineString to test
	 * @param pointInter Intersection point
	 * @return the lineString forming the intersection 
	 *
	 */ 
	private static LineString lineStringIntersection(MultiLineString multi, Point pointInter) {

		LineString linestring2=(LineString) multi.getGeometryN(0);
		Coordinate[] multiCoord = multi.getCoordinates();
		Coordinate[] tabCoord1 = {multiCoord[0],multiCoord[1]};
		LineString linestring1 = new GeometryFactory().createLineString(tabCoord1);
		Point first=linestring1.getStartPoint();
		Point second=linestring1.getEndPoint();
		Point ptI= new GeometryFactory().createPoint(new Coordinate(pointInter.getX(),pointInter.getY()));
		if( (!first.equalsExact(ptI)) && (!second.equalsExact(ptI)) ){
			return linestring2;}
		return linestring1;
	}

	/**
	 * Checks if two MultilineString are parallel
	 * @param road1 first MultiLineString
	 * @param road2 second MultiLineString
	 * @return Boolean, true if parallel
	 */
	private static boolean isUnconnected(MultiLineString road1, MultiLineString road2) {
		Point ptIntersection = (Point) road1.intersection(road2).getCentroid();
		return ptIntersection.isEmpty();
	}

	/**
	 * Calculates the radius of the arc between two roads.
	 * @param route1 One road
	 * @param route2 Another road
	 * @return radius
	 */
	private static double radius(LineRoad route1, LineRoad route2){
		//Calculates the radius
		double speed1 = Integer.parseInt( route1.getSpeed().substring(0, 3).trim());
		double speed2 = Integer.parseInt( route2.getSpeed().substring(0, 3).trim());
		double r1 = 18.6 * Math.sqrt(  speed1/(Math.abs( Math.abs(10*(route1.getWidth())+65-speed1) ) ) );
		double r2 = 18.6 * Math.sqrt(  speed2/( Math.abs(10*(route2.getWidth())+65-speed2) )  );
		return Math.min(r1, r2);
	}
}
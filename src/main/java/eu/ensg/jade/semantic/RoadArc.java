package eu.ensg.jade.semantic;

import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.jts.CircularArc;
import org.opengis.referencing.NoSuchAuthorityCodeException;

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
    * The attribute containing two roads between each the arc will be drawn
    */
//    private LineRoad routes[];
   
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
    * Constructor
    *
    * @param route1 One Arc road
    * @param route2 Another Arc road
    */  
    public RoadArc(LineRoad route1, LineRoad route2){  
    		//Calculates the radius
            double speed1 = Integer.parseInt( route1.getSpeed().substring(0, 3).trim());
            double speed2 = Integer.parseInt( route2.getSpeed().substring(0, 3).trim());
            double r1 = 18.6 * Math.sqrt(  speed1/(Math.abs( Math.abs(10*(route1.getWidth())+65-speed1) ) ) );
            double r2 = 18.6 * Math.sqrt(  speed2/( Math.abs(10*(route2.getWidth())+65-speed2) )  );
            this.radius= Math.min(r1, r2);
    }

    // ========================== GETTERS/SETTERS ======================
   
    /**
     * Allows to access the circularArc
     *
     * @return the circular arc
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
   
    public Point getIntersection(Geometry buffer, Geometry element, Point pt_I)
    {
    	//Getting the intersection
        Geometry geom = buffer.intersection(element);
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
   
    public Point calculerCentre(LineRoad road1, LineRoad road2){      
    	//Getting the intersection of 2 roads
    	Point inter = (Point) (road1.getGeom().intersection(road2.getGeom()).getGeometryN(0));

        //Calculate the radius of the two possible arc radius and conserving the smallest
        double speed1 = Integer.parseInt( road1.getSpeed().substring(0, 3).trim())-30;
        double speed2 = Integer.parseInt( road2.getSpeed().substring(0, 3).trim())-30;
        double width1 = road1.getWidth();
        double width2 = road2.getWidth();
        if(speed1<=0) speed1=10;
        if(speed2<=0) speed2=10;
        if(speed1>70) speed1=35;
        if(speed2>70) speed2=35;
        double r1 = (18.6 * Math.sqrt(speed1/(Math.abs(10*(width1)+65-speed1))));
        double r2 = 18.6 * Math.sqrt(speed2/(Math.abs(10*(width2)+65-speed2)));
		this.radius = Math.min(r1, r2);
        
        //Calculate the points for middle
        LineString lineString1= lineStringIntersection(road1.getGeom(),inter);
        LineString lineString2= lineStringIntersection(road2.getGeom(),inter);

        double angle = calculAngle(road1, road2)*Math.PI/180;
        if(angle == 0) angle = Math.PI/2 ;
        Point temp1 = getIntersection(inter.buffer(road2.getWidth()/Math.abs(Math.sin(Math.PI-angle))+(this.getRadius())),lineString1,inter);
        Point temp2 = getIntersection(inter.buffer(road1.getWidth()/Math.abs(Math.sin(Math.PI-angle))+(this.getRadius())),lineString2,inter);

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
    public List<Point> calculerPointArc(LineRoad road1, LineRoad road2){
    	
    	  List <Point> resultat = new ArrayList<Point>();
          Point pt_Intersection = calculerCentre(road1,road2);
          if(pt_Intersection != null ) {
          Point p1 = (pt_Intersection.buffer(this.radius)).intersection(road1.getGeom().buffer(road1.getWidth())).getCentroid();
          Point p2 = (pt_Intersection.buffer(this.radius)).intersection(road2.getGeom().buffer(road2.getWidth())).getCentroid();
          if( p1 != null && p2 != null ) {
          resultat.add(p1);
          resultat.add(p2);}
          return resultat;}
          return null;
    }
   
    /**
    * Checks if the arc intersects a given road
    *
    * @param arc the arc
    * @param road the road to check for intersection
    * @return true if the arc intersects the road
    */
   
    public boolean intersectOther(CircularArc arc, LineRoad road){
        double[] points = arc.linearize(10);
        boolean result=false;
        
        // create a list of connected positions
        Coordinate firstArcPoint = new Coordinate(points[0],points[1]);
        Coordinate secondArcPoint = new Coordinate(points[points.length-2],points[points.length-1]);
        Coordinate thirdPoint = new Coordinate(points[(points.length/2)],points[(points.length/2)+1]);
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
     * @param r1 One Arc road
     * @param r2 Another Arc road
     * @param pt_I intersection point
     * @return List of the cutting points
     * 
    */
    
    public List<Point> cuttingPoint(Point extremity1, Point extremity2,LineRoad r1, LineRoad r2, Point pt_I) {
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
          if (isParallel(road1, road2)){
        	  return 0;
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
    public Point calculMidPoint(LineRoad road1, LineRoad road2,Point startPoint, Point endPoint){

    	   if( !startPoint.isEmpty() && !endPoint.isEmpty()) {
       	    //Center of the circle:
    	        Point centre=this.calculerCentre(road1,road2);
    	        //Vector from the circle center to middle of the 
    	        double xd = (startPoint.getX()+endPoint.getX())/2;
    	        double yd = (startPoint.getY()+endPoint.getY())/2;
    	        double[] vect = new double[2];
    	        vect[0] = xd-centre.getX();
    	        vect[1] = yd-centre.getY();
    	        //Unit Vector
    	        double[] norm = new double[2];
    	        norm[0] = vect[0]/( Math.sqrt( Math.pow(vect[0], 2) + Math.pow(vect[1], 2) )  );
    	        norm[1] = vect[1]/( Math.sqrt( Math.pow(vect[0], 2) + Math.pow(vect[1], 2) )  );
    	      //Translation of the circle center with norm as direction and radius as length
    	        double xmid = centre.getX()+ norm[0]*this.radius;
    	        double ymid = centre.getY()+norm[1]*this.radius;
    	        Point midPoint= new GeometryFactory().createPoint(new Coordinate(xmid,ymid));
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
    	  if(road1.equals(road2)) return null ;
    	  //Arcs are not drawn in roundabouts
          if(road1.getName().contains("PL") || road1.getName().contains("RPT")) return null;
          if(road2.getName().contains("PL") || road2.getName().contains("RPT")) return null;
          if(calculAngle(road1,road2)<140 && calculAngle(road1,road2)>-140)
          {
          List<Point> arcPoint = calculerPointArc(road1,road2);
          if(arcPoint != null && !arcPoint.isEmpty()) {
          Point midPoint = calculMidPoint(road1,road2,arcPoint.get(0), arcPoint.get(1));
          if(midPoint != null)
          this.geomArc = new CircularArc(arcPoint.get(0).getX(),arcPoint.get(0).getY(), midPoint.getX(),midPoint.getY(),arcPoint.get(1).getX(),arcPoint.get(1).getY());
          return this.geomArc ;
          }
          } 
          return null;
    }
    
    /**
     * Checks if the first LineString of multilineString is the one forming the intersection
     *
     * @param multi multiLineString to test
     * @param pointInter Intersection point
     * @return the lineString forming the intersection 
     *
     */ 
    public static LineString lineStringIntersection(MultiLineString multi, Point pointInter) {

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
    private static boolean isParallel(MultiLineString road1, MultiLineString road2) {
    	 Point ptIntersection = (Point) road1.intersection(road2).getCentroid();
    	 return ptIntersection.isEmpty();
    }
   
}
package eu.ensg.jade.semantic;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.geotools.geometry.jts.CircularArc;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Point;



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
    private LineRoad routes[];
   
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
    * @param routes
    * @param rayon
    */  
    public RoadArc(LineRoad route1, LineRoad route2){      
            double speed1 = Integer.parseInt( route1.getSpeed().substring(0, 3).trim());
            double speed2 = Integer.parseInt( route2.getSpeed().substring(0, 3).trim());
            double r1 = 18.6 * Math.sqrt(  speed1/(Math.abs( Math.abs(10*(route1.getWidth())+65-speed1) ) ) );
            double r2 = 18.6 * Math.sqrt(  speed2/( Math.abs(10*(route2.getWidth())+65-speed2) )  );
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
    * Allows to access the road's table
    *
    * @return the road's table
    */
   
    public LineRoad[] getRoutes() {
        return routes;
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
    * Get the point of intersection between a buffer and an element
    *
    * @param buffer
    * @param element
    * @param pt_I
    */
   
    public Point getIntersection(Geometry buffer, Geometry element, Point pt_I)
    {
        Geometry geom;
        geom = buffer.intersection(element);
        LineString ls = (LineString ) geom.getGeometryN(0);
        Point ptStart = ls.getStartPoint();
        Point ptEnd = ls.getEndPoint();
        Point ptInter = (pt_I.distance(ptStart) > pt_I.distance(ptEnd))? ptStart : ptEnd;
        return ptInter;  
    }
   
   
    /**
    * Calculate the center of the arc by estimating a radius for each road by
    * using this formula: r = 18.6 * SQRT(road_speed/ (10 * road_width + 65 - road_speed)
    *
    * @return the arc center
    */
   
    public Point calculerCentre(LineRoad road1, LineRoad road2){      
        //Getting the intersection of 2 roads
        Point inter = (Point) (road1.getGeom().intersection(road2.getGeom())).getCentroid();

        //Calculate the radius of the two possible arc radius and conserving the smallest
        double speed1 = Integer.parseInt( road1.getSpeed().substring(0, 3).trim());
        double speed2 = Integer.parseInt( road2.getSpeed().substring(0, 3).trim());
        double r1 = (18.6 * Math.sqrt(speed1/(Math.abs(10*(road1.getWidth())+65-speed1))));
        double r2 = 18.6 * Math.sqrt(speed2/(Math.abs(10*(road2.getWidth())+65-speed2)));
        double r = Math.min(r1, r2);
        this.radius = r;
        Point temp1 = getIntersection(inter.buffer(road2.getWidth()+(this.getRadius())),road1.getGeom(),inter);
        Point temp2 = getIntersection(inter.buffer(road1.getWidth()+(this.getRadius())),road2.getGeom(),inter);

        //Calculate the coordinates of the arc's center
        double xD =  (temp1.getX() + temp2.getX() - inter.getX());
        double yD = (temp1.getY() + temp2.getY() - inter.getY());
        GeometryFactory gf = new GeometryFactory();
        Point centreArc = (Point) gf.createPoint(new Coordinate(xD,yD));
        return centreArc;
    }
     
    /**
    * Calculates the two points limiting the arc
    *
    * @return list of the the two point limiting the arc
    */
    public List<Point> calculerPointArc(LineRoad road1, LineRoad road2){
        
        List <Point> resultat = new ArrayList<Point>();
        Point pt_Intersection = calculerCentre(road1,road2);
        Point p1 = (pt_Intersection.buffer(this.radius)).intersection(road1.getGeom().buffer(road1.getWidth())).getCentroid();
        Point p2 = (pt_Intersection.buffer(this.radius)).intersection(road2.getGeom().buffer(road2.getWidth())).getCentroid();
        System.out.println("le point1 est "+p1+"et le point 2 est "+p2);
        if( p1 != null && p2 != null ) {
        resultat.add(p1);
        resultat.add(p2);}
        return resultat;
    }
   
    /**
    * Checks is there the arc intersect one of the road attached to the intersection
    *
    * @param arc
    * @param road
    * @return true if the arc intersects the road
    */
   
    public boolean intersectOther(CircularArc arc, LineRoad road){
       double[] points = arc.getControlPoints();
       double cirAXSta = points[0];
       double cirAYSta = points[1];
       double cirAXEnd = points[4];
       double cirAYEnd = points[5];
       Coordinate coordSta = new Coordinate(cirAXSta,cirAYSta);
       Coordinate coordEnd = new Coordinate(cirAXEnd,cirAYEnd);
       Coordinate[] coords = {coordSta,coordEnd};
       LineString line = new LineString(coords,road.getGeom().getPrecisionModel(),road.getGeom().getSRID());
       return line.intersects(road.getGeom());
    }
   
     
    /**
     * Calculates the angle between two roads
     *
     * @param road1
     * @param road2
     * @return angle between two roads
     *
     */
    public static double calculAngle(LineRoad lineroad1, LineRoad lineroad2)
    {
       
        MultiLineString road1 = lineroad1.getGeom();
        MultiLineString road2 = lineroad2.getGeom();
        Point ptIntersection = (Point) road1.intersection(road2).getCentroid();
        Point ptStart1 = ((LineString) road1.getGeometryN(0)).getStartPoint();
        Point ptEnd1 = ((LineString) road1.getGeometryN(0)).getEndPoint();
        Point ptStart2 = ((LineString) road2.getGeometryN(0)).getStartPoint();
        Point ptEnd2 = ((LineString) road2.getGeometryN(0)).getEndPoint();
        Point  sommet1 = (ptIntersection.distance(ptStart1) > ptIntersection.distance(ptEnd1))? ptStart1 : ptEnd1;
        Point  sommet2 = (ptIntersection.distance(ptStart2) > ptIntersection.distance(ptEnd2))? ptStart2 : ptEnd2;

        double diffCoord1[] = new double[2];
        diffCoord1[0] = sommet1.getX()-ptIntersection.getX();
        diffCoord1[1] = sommet1.getY()-ptIntersection.getY();

        double diffCoord2[] = new double[2];
        diffCoord2[0] = sommet2.getX()-ptIntersection.getX();
        diffCoord2[1] = sommet2.getY()-ptIntersection.getY();

        double prodScalaire = diffCoord1[0]*diffCoord2[0] + diffCoord1[1]*diffCoord2[1];
        double normA = Math.sqrt(diffCoord1[0]*diffCoord1[0] + diffCoord1[1]*diffCoord1[1]);
        double normB = Math.sqrt(diffCoord2[0]*diffCoord2[0] + diffCoord2[1]*diffCoord2[1]);
        double angle = Math.acos(prodScalaire/(normA*normB));

        return angle*(180.0/Math.PI);
    }
   
    /**
     * Calculates midPoint of an arc by knowing the start point and the end point
     *
     * @param startPoint
     * @param endPoint
     * @return midpoint
     *
     */
    public Point calculMidPoint(LineRoad road1, LineRoad road2,Point startPoint, Point endPoint){
       if( !startPoint.isEmpty() && !endPoint.isEmpty()) {
        Point centre=this.calculerCentre(road1,road2);
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
        Point midPoint= new GeometryFactory().createPoint(new Coordinate(xmid,ymid));
        return midPoint; }
        return null;
    }
      

   
    /**
     * Create an arc between two intersected roads 
     *
     * @param first road
     * @param second road
     * @return circularArc 
     *
     */ 
    public CircularArc createRoadArc(LineRoad road1, LineRoad road2){
       
        if(calculAngle(road1,road2)<150) {
        List<Point> arcPoint = calculerPointArc(road1,road2);
        if(!arcPoint.isEmpty()) {
        Point midPoint = calculMidPoint(road1,road2,arcPoint.get(0), arcPoint.get(1)); if(midPoint != null)
        this.geomArc = new CircularArc(arcPoint.get(0).getX(),arcPoint.get(0).getY(), midPoint.getX(),midPoint.getY(),arcPoint.get(1).getX(),arcPoint.get(1).getY());
        return this.geomArc ;   
        }
        } return null;
    }}
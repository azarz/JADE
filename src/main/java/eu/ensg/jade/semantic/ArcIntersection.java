package eu.ensg.jade.semantic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.geotools.geometry.jts.CircularArc;
import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import eu.ensg.jade.scene.Scene;


/**
 * 
 *
 * @author JADE
 */

public class ArcIntersection {

	// ========================== ATTRIBUTES ===========================

	/**
	 * General method to smooth roads
	 *
	 * @param scene the scene where the roads will be smoothed
	 * @return List of polygons 
	 */ 
	public static List<Polygon> generateSmoothRoad(Scene scene) {
		Map<String, LineRoad> lineRoads = scene.getLineRoads();
		//		Map<String, SurfaceRoad> surfaceRoads = scene.getSurfaceRoads();

		ArrayList<Polygon> result = new ArrayList<Polygon>();
		for (Intersection inter : scene.getCollIntersect().getMapIntersection().values()) {
			List<LineRoad> tempRoads = new ArrayList<LineRoad>();
			for( String roadId :inter.getRoadId().keySet()) {
				tempRoads.add((LineRoad) scene.getLineRoads().get(roadId));
			}


			// 2 roads intersecting
			if (tempRoads.size()==2) {
				if(tempRoads.get(0).getWidth()==tempRoads.get(1).getWidth() || 
						tempRoads.get(0).getWidth()==0 || tempRoads.get(1).getWidth()==0) {
					continue;
				}
				double angle = RoadArc.calculAngle(tempRoads.get(0), tempRoads.get(1));
				if(angle < 210 && angle > 150 ) {
					Polygon p = trapezoid(tempRoads, inter);
					if(p.isValid()) result.add(p);
				}
				else {
					Polygon p = bufferSmooth(tempRoads, inter);
					if(p.isValid()) result.add(p);

					List<Polygon> polygons2=smoothIntersection(tempRoads, inter);
					for(int k=0 ; k<polygons2.size();k++) {
						p = polygons2.get(k);
						if(p.isValid()) result.add(p);
					}
				}					
			}
			// 3+ roads intersecting
			else if (tempRoads.size()>2 ){
				Polygon p;
				List<Polygon> polygons=smoothIntersection(tempRoads, inter);
				for(int k=0 ; k<polygons.size();k++) {
					p = polygons.get(k);
					if(p.isValid()) result.add(p);
				}
			}
			
		}

		return result;
	}


	/**
	 * General method to smooth roads
	 *
	 * @return List of polygons 
	 *
	 */
	private static Polygon bufferSmooth(List<LineRoad> roads, Intersection inter) {

		//Initialize
		Coordinate coord=new Coordinate(inter.getGeometry().x, inter.getGeometry().y);
		Point pointInter= new GeometryFactory().createPoint(coord);
		LineRoad road=new LineRoad();
		double smallWidth;
		double bigWidth;
		if(roads.get(0).getWidth()>roads.get(1).getWidth()) { 
			road=roads.get(1);
			smallWidth=roads.get(1).getWidth()/2;
			bigWidth = roads.get(0).getWidth()/2;
		}
		else {
			smallWidth=roads.get(0).getWidth()/2;
			road=roads.get(0);
			bigWidth=roads.get(1).getWidth()/2;
		}
		Point ptI=new GeometryFactory().createPoint(new Coordinate(inter.getGeometry()));
		LineString line = (LineString) (ptI.buffer(2*bigWidth)).intersection(road.getGeom());
		Point p1=line.getStartPoint();
		if(p1.equals(pointInter)) p1=line.getEndPoint();

		//Unit vector
		double denominateur= Math.sqrt( Math.pow((p1.getX()-pointInter.getX()),2)   + Math.pow((p1.getY()-pointInter.getY()),2) );
		double ux= (pointInter.getY() - p1.getY())/denominateur;
		double uy= (p1.getX()- pointInter.getX() ) / denominateur;

		//Calculate of the coordinates
		Coordinate coordonate1= new Coordinate(pointInter.getX()+bigWidth*ux, pointInter.getY()+bigWidth*uy );
		Coordinate coordonate2= new Coordinate(p1.getX()+smallWidth*ux, p1.getY()+smallWidth*uy);
		Coordinate coordonate3= new Coordinate(p1.getX()-smallWidth*ux, p1.getY()-smallWidth*uy);
		Coordinate coordonate4= new Coordinate(pointInter.getX()-bigWidth*ux, pointInter.getY()-bigWidth*uy);
		List<Coordinate> trapezeCoor = new ArrayList<Coordinate>();
		trapezeCoor.add(coordonate1);
		trapezeCoor.add(coordonate2);
		trapezeCoor.add(coordonate3);
		trapezeCoor.add(coordonate4);
		trapezeCoor.add(trapezeCoor.get(0));

		//Create the polygon		
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		LinearRing ring = geometryFactory.createLinearRing(trapezeCoor.toArray(new Coordinate[trapezeCoor.size()]));
		LinearRing holes[] = null;
		Polygon polygon = geometryFactory.createPolygon(ring, holes);

		return polygon;
	}

	/**
	 * Draws a trapezoid in the intersection of two roads which have different radius
	 *
	 * @param List of LineRoads
	 * @param Intersection
	 * @return Polygon
	 *
	 */ 
	private static Polygon trapezoid(List<LineRoad> roads, Intersection inter) {
		//List of coordinates to stock the points
		List<Coordinate> trapezeCoor = new ArrayList<Coordinate>();

		//We get the two roads and store them following their weight
		LineRoad rBig = roads.get(1);
		LineRoad rSmall = roads.get(0);
		if (roads.get(0).getWidth()>roads.get(1).getWidth()) {
			rBig = roads.get(0);
			rSmall = roads.get(1);
		}

		//We get the intersection's coordinates
		Coordinate pointInter = inter.getGeometry();

		// First step :
		// generate the points at the intersection,
		// separated by the largest width and with a vector normal to the largest road.
		// First, we calculate the normal:
		//Getting the points
		Point rBStart = ((LineString) rBig.getGeom().getGeometryN(0)).getStartPoint();
		Point rBEnd = ((LineString) rBig.getGeom().getGeometryN(0)).getEndPoint();
		//Normal vector
		double u = -rBStart.getY()+rBEnd.getY();
		double v =  rBStart.getX()-rBEnd.getX();
		//Making it an unit vector (require to translate)
		double un = u/(Math.sqrt(u*u+v*v));
		double vn = v/(Math.sqrt(u*u+v*v));
		//We clone the intersection point
		Coordinate newPoint = (Coordinate) pointInter.clone();
		//We translate this point by the unit vector with half the Width as distance
		newPoint.x = newPoint.x + un * rBig.getWidth()/2;
		newPoint.y = newPoint.y + vn * rBig.getWidth()/2;
		//We add the new point
		trapezeCoor.add(newPoint);
		//Same, but translating the other way (-1 factor to translation)
		newPoint = (Coordinate) pointInter.clone();
		newPoint.x = newPoint.x - un * rBig.getWidth()/2;
		newPoint.y = newPoint.y - vn * rBig.getWidth()/2;
		trapezeCoor.add(newPoint);
		//Second step :
		// Creating the two other points, separated by the sum of the roads radius (half width)
		// from the two first, on the smallest road
		// and as previous translating them by half the width of the smallest road

		// First, we calculate the distance
		double d = rBig.getWidth()/2 + rSmall.getWidth()/2;
		// then we create the vector director and unit normal vector for the smallest road
		// from intersection to its end
		Point rSStart = ((LineString) rSmall.getGeom().getGeometryN(0)).getStartPoint();
		Point rSEnd = ((LineString) rSmall.getGeom().getGeometryN(0)).getEndPoint();
		if (rSEnd.getX()!=pointInter.x && rSEnd.getY()!=pointInter.y) {
			rSEnd = ((LineString) rSmall.getGeom().getGeometryN(0)).getStartPoint();
			rSStart = ((LineString) rSmall.getGeom().getGeometryN(0)).getEndPoint();
		}
		double w =  rSStart.getX()-rSEnd.getX();
		double x = 	rSStart.getY()-rSEnd.getY();
		double wu = w/(Math.sqrt(w*w+x*x));
		double xu = x/(Math.sqrt(w*w+x*x));
		double wn = -xu;
		double xn = wu;
		// We create the middle point (middle of the small basis of the trapezoid
		Coordinate middlePoint = (Coordinate) pointInter.clone(); 
		middlePoint.x = middlePoint.x + wu * d;
		middlePoint.y = middlePoint.y + xu * d;
		//We use this point to get the two last point
		Coordinate a = (Coordinate) middlePoint.clone();
		a.x = a.x - wn * rSmall.getWidth()/2;
		a.y = a.y - xn * rSmall.getWidth()/2;
		Coordinate b = (Coordinate) middlePoint.clone();
		b.x = b.x + wn * rSmall.getWidth()/2;
		b.y = b.y + xn * rSmall.getWidth()/2;
		//We add them with order test to prevent X-shape
		Coordinate coorTest = trapezeCoor.get(0);
		if (a.distance(coorTest)<b.distance(coorTest)) {
			trapezeCoor.add(b);
			trapezeCoor.add(a);
		}
		else {
			trapezeCoor.add(a);
			trapezeCoor.add(b);
		}
		//We add the fist point as last to close the geometry
		trapezeCoor.add(trapezeCoor.get(0));
		//We create the polygon
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		LinearRing ring = geometryFactory.createLinearRing(trapezeCoor.toArray(new Coordinate[trapezeCoor.size()]));
		LinearRing holes[] = null;
		Polygon polygon = geometryFactory.createPolygon(ring, holes);

		return polygon;
	}


	/**
	 * Creates arc in each intersection in order to smooth roads 
	 *
	 * @param List of lineroads
	 * @param Intersection
	 * @return Polygon 
	 *
	 */ 
	private static List<Polygon> smoothIntersection(List<LineRoad> roads, Intersection inter) {

		List<Polygon> polygons = new ArrayList<Polygon>();
		//We go through the road list, with all couples of roads possible
		for(int i=0; i<roads.size()-1; i++ ) {
			for(int j=i+1 ; j<roads.size(); j++) {
				RoadArc roadArc = new RoadArc(roads.get(i), roads.get(j));
				//We create the corresponding arc
				CircularArc arc = roadArc.createRoadArc(roads.get(i), roads.get(j));
				boolean intersectionTest=true;
				for(int k=0 ; k<roads.size(); k++) {
					//Testing if the eventual arc intersect a road. If yes the arc is not conserved	
					if(arc!=null && RoadArc.intersectOther(arc, roads.get(k))) intersectionTest=false;
				}
				
				//Only if there is no intersection
				if(intersectionTest && arc != null) { 
					List<Coordinate> polygonCoor = new ArrayList<Coordinate>();
					GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
					//We linearize the arc
					double[] pointsOfArc = arc.linearize(1);
					//We create the list of Coordinates for the polygon with the points of the arc
					for(int z = 0; z<((pointsOfArc.length)/2)-1; z++) { 
						Coordinate coord = new Coordinate(pointsOfArc[2*z],pointsOfArc[2*z+1]);
						polygonCoor.add(coord);
					}
					//We get the projected points from the ends of the arc to the corresponding roads
					Point extremity1 = new GeometryFactory().createPoint(polygonCoor.get(0));
					Point extremity2 = new GeometryFactory().createPoint(polygonCoor.get(polygonCoor.size()-1));
					Point ptI=new GeometryFactory().createPoint(inter.getGeometry());
					List<Point> listCutting = RoadArc.cuttingPoint(extremity1, extremity2, roads.get(i), roads.get(j), ptI);
					Coordinate coordCutPoint1 = new Coordinate(listCutting.get(0).getX(),listCutting.get(0).getY());
					Coordinate coordCutPoint2 = new Coordinate(listCutting.get(1).getX(),listCutting.get(1).getY());
					Coordinate pointToAdd1 = coordCutPoint2;
					Coordinate pointToAdd2 = coordCutPoint1;
					Coordinate[] tabCoord1= {extremity2.getCoordinate(),pointToAdd1};
					Coordinate[] tabCoord2= {extremity1.getCoordinate(),pointToAdd2};
					LineString line1=new GeometryFactory().createLineString(tabCoord1);
					LineString line2=new GeometryFactory().createLineString(tabCoord2);
					//We add the points
					polygonCoor.add(pointToAdd1);
					polygonCoor.add(new Coordinate(ptI.getX(),ptI.getY()));
					polygonCoor.add(pointToAdd2);
					polygonCoor.add(polygonCoor.get(0));
					//We create the polygon with the list of coordinates and no holes
					LinearRing ring = geometryFactory.createLinearRing(polygonCoor.toArray(new Coordinate[polygonCoor.size()]));
					LinearRing holes[] = null;
					Polygon geom = geometryFactory.createPolygon(ring, holes);
					if(geom.getArea() <1000) 
						polygons.add(geom);

				}
			}
		}
		return polygons ;
	}

}

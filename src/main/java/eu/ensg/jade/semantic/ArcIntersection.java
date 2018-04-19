package eu.ensg.jade.semantic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.geometry.jts.CircularArc;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import eu.ensg.jade.scene.Scene;


/**
 * The class to smooth roads
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
	public static Map<String, Polygon> generateSmoothRoad(Scene scene) {
		Map<String, Polygon> result = new HashMap<>();
		System.out.println("\nNumber of intersections: " + String.valueOf(scene.getCollIntersect().getMapIntersection().size()));
		
		for (Intersection inter : scene.getCollIntersect().getMapIntersection().values()) {
			List<LineRoad> tempRoads = new ArrayList<>();
			List<String> tempIds = new ArrayList<>();
			
			for(String roadId : inter.getRoadId().keySet()) {
				if(scene.getLineRoads().get(roadId).getWidth() != 0) {
					tempIds.add(roadId);
					tempRoads.add(scene.getLineRoads().get(roadId));
				}
			}

			// 2 roads intersecting
			if (tempRoads.size() == 2) {
				if(tempRoads.get(0).getWidth()==tempRoads.get(1).getWidth() || 
						tempRoads.get(0).getWidth()==0 || tempRoads.get(1).getWidth()==0) {
					continue;
				}
				
				double angle = RoadArc.angleBetweenRoads(tempRoads.get(0), tempRoads.get(1));
				if(angle < 210 && angle > 150) {
					System.out.println("Adding trapezoid");
					result.put(tempIds.get(0), trapezoid(tempRoads, inter));
				}
				else {
					System.out.println("Adding bufferSmooth");
					result.put(tempIds.get(0), bufferSmooth(tempRoads, inter));

					Polygon p;
					Map<String, Polygon> polygons = smoothIntersection(tempRoads, tempIds, inter);
					for(String key : polygons.keySet()) {
						System.out.println("Adding smoothIntersection");
						p = polygons.get(key);
						if(!p.isValid()) System.out.println("\tInvalid smoothIntersection");
						else result.put(key, p);
					}
				}					
			}
			// 3+ roads intersecting
			else if (tempRoads.size() > 2) {
				Map<String, Polygon> polygons = smoothIntersection(tempRoads, tempIds, inter);
				Polygon p;
				for(String key : polygons.keySet()) {
					System.out.println("Adding smoothIntersection");
					p = polygons.get(key);
					if(!p.isValid()) System.out.println("\tInvalid smoothIntersection");
					else result.put(key, p);
				}
				
			}			
		}
		System.out.println();
		return result;
	}


	/**
	 * General method to smooth roads
	 *
	 * @return List of polygons 
	 *
	 */
	private static Polygon bufferSmooth(List<LineRoad> roads, Intersection intersection) {
		//Initialize
		GeometryFactory geomFactory = new GeometryFactory();
		Point pointIntersect = geomFactory.createPoint(new Coordinate(intersection.getGeometry()));
		
		LineRoad smallestRoad = new LineRoad();
		double smallWidth;
		double bigWidth;
		if(roads.get(0).getWidth()>roads.get(1).getWidth()) {
			smallestRoad = roads.get(1);
			smallWidth = roads.get(1).getWidth()/2;
			bigWidth = roads.get(0).getWidth()/2;
		}
		else {
			smallestRoad = roads.get(0);
			smallWidth = roads.get(0).getWidth()/2;
			bigWidth = roads.get(1).getWidth()/2;
		}

		LineString line = (LineString) (pointIntersect.buffer(2*bigWidth)).intersection(smallestRoad.getGeom());
		Point p1=line.getStartPoint();
		if(p1.equals(pointIntersect)) p1=line.getEndPoint();

		//Unit vector
		double dist = Math.sqrt( Math.pow((p1.getX()-pointIntersect.getX()),2)+Math.pow((p1.getY()-pointIntersect.getY()),2) );
		double ux = (pointIntersect.getY() - p1.getY()) / dist;
		double uy = (p1.getX()- pointIntersect.getX() ) / dist;

		//Compute the coordinates
		Coordinate coordonate1= new Coordinate(pointIntersect.getX()+bigWidth*ux, pointIntersect.getY()+bigWidth*uy );
		Coordinate coordonate2= new Coordinate(p1.getX()+smallWidth*ux, p1.getY()+smallWidth*uy);
		Coordinate coordonate3= new Coordinate(p1.getX()-smallWidth*ux, p1.getY()-smallWidth*uy);
		Coordinate coordonate4= new Coordinate(pointIntersect.getX()-bigWidth*ux, pointIntersect.getY()-bigWidth*uy);
		List<Coordinate> trapezeCoor = new ArrayList<Coordinate>();
		trapezeCoor.add(coordonate1);
		trapezeCoor.add(coordonate2);
		trapezeCoor.add(coordonate3);
		trapezeCoor.add(coordonate4);
		trapezeCoor.add(trapezeCoor.get(0));

		//Create the polygon		
//		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
		LinearRing ring = geomFactory.createLinearRing(trapezeCoor.toArray(new Coordinate[trapezeCoor.size()]));
//		LinearRing holes[] = null;
		Polygon polygon = geomFactory.createPolygon(ring, null);

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
		GeometryFactory geometryFactory = new GeometryFactory();
		LinearRing ring = geometryFactory.createLinearRing(trapezeCoor.toArray(new Coordinate[trapezeCoor.size()]));
		Polygon polygon = geometryFactory.createPolygon(ring, null);

		return polygon;
	}


	/**
	 * Creates arc in each intersection in order to smooth roads
	 * 
	 * @param roads List of LineRoad that intersect
	 * @param roadId List of ID corresponding to each road
	 * @param inter The intersection
	 * @return a map of Polygon associated to road's ID
	 */
	private static Map<String,Polygon> smoothIntersection(List<LineRoad> roads, List<String> roadId, Intersection inter) {
		GeometryFactory geomFactory = new GeometryFactory();
		Map<String,Polygon> polygons = new HashMap<>();
		
		double zCoord = inter.getGeometry().z;
		
		//We go through the road list, with all couples of roads possible
		for(int i=0; i<roads.size()-1; i++ ) {
			for(int j=i+1 ; j<roads.size(); j++) {
				//We create the corresponding arc
				RoadArc roadArc = new RoadArc(roads.get(i), roads.get(j));
				CircularArc arc = roadArc.getCircularArc();
				if(arc == null) continue;

				//We create the list of Coordinates for the polygon with the points of the arc
				List<Coordinate> polygonCoords = new ArrayList<Coordinate>();
				double[] pointsOfArc = arc.linearize(1);
				for(int z = 0; z<((pointsOfArc.length)/2)-1; z++) {
					polygonCoords.add( new Coordinate(pointsOfArc[2*z],pointsOfArc[2*z+1], zCoord) );
				}
				
				//We get the projected points from the ends of the arc to the corresponding roads
				Point extremity1 = geomFactory.createPoint(polygonCoords.get(0));
				Point extremity2 = geomFactory.createPoint(polygonCoords.get(polygonCoords.size()-1));
				Point ptI = geomFactory.createPoint(inter.getGeometry());
				
				List<Point> listCutting = RoadArc.cuttingPoint(extremity1,extremity2, roads.get(i), roads.get(j), ptI);		
				Coordinate coordCutPoint1 = new Coordinate(listCutting.get(0).getX(),listCutting.get(0).getY(), zCoord);
				
				Coordinate coordCutPoint2 = new Coordinate(listCutting.get(1).getX(),listCutting.get(1).getY(), zCoord);
				
				double dist1 = coordCutPoint1.distance(polygonCoords.get(polygonCoords.size()-1));
				double dist2 = coordCutPoint2.distance(polygonCoords.get(polygonCoords.size()-1));
				if(dist1 < dist2) {
					polygonCoords.add(coordCutPoint1);
					polygonCoords.add(inter.getGeometry());
					polygonCoords.add(coordCutPoint2);
				}
				else {
					polygonCoords.add(coordCutPoint2);
					polygonCoords.add(inter.getGeometry());
					polygonCoords.add(coordCutPoint1);
				}
				
				
				polygonCoords.add(polygonCoords.get(0));
				
				//We create the polygon with the list of coordinates and no holes
				LinearRing ring = geomFactory.createLinearRing(polygonCoords.toArray(new Coordinate[polygonCoords.size()]));
				Polygon geom = geomFactory.createPolygon(ring, null);
				
				if(!geom.isValid()) {
					for(int k=0; k<polygonCoords.size(); k++) {
						System.out.println("Coord: "+polygonCoords.get(k).toString());
					}
				}
				
				if(geom.getArea() < 1000 && geom.getArea() < (arc.getRadius()*arc.getRadius()*Math.PI)/3   ){
					polygons.put(roadId.get(j), geom);
				}
			}
		}
		return polygons ;
	}

}

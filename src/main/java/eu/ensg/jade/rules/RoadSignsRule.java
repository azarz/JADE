package eu.ensg.jade.rules;

import java.util.Map;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequenceFactory;

import eu.ensg.jade.scene.Scene;
import eu.ensg.jade.semantic.Intersection;
import eu.ensg.jade.semantic.IntersectionColl;
import eu.ensg.jade.semantic.LineRoad;
import eu.ensg.jade.semantic.StreetFurniture;
import eu.ensg.jade.semantic.SurfaceRoad;
import eu.ensg.jade.utils.JadeUtils;

/**
 * 
 * @author JADE
 */

public class RoadSignsRule implements RuleShape {
	
// ========================== ATTRIBUTES ===========================

	/**
	 * Path of the pedestrian crossing sign
	 */
	private String pedestrianCrossing = "Models/RoadSigns/dangerSigns/PedestrianCrossingAhead/PedestrianCrossingAhead.scene"; 
	
	/**
	 * Path of the 130 speed limit sign
	 */
	private String speedLimit130 = "Models/RoadSigns/speedLimits/speedLimit130/speedLimit130.scene"; 
	
	/**
	 * Path of the 110 speed limit sign
	 */
	private String speedLimit110 = "Models/RoadSigns/speedLimits/speedLimit110/speedLimit110.scene"; 
	
	/**
	 * Path of the 90 speed limit sign
	 */
	private String speedLimit90 = "Models/RoadSigns/speedLimits/speedLimit90/speedLimit90.scene"; 
	
	/**
	 * Path of the 90 speed limit sign
	 */
	private String speedLimit70 = "Models/RoadSigns/speedLimits/speedLimit70/speedLimit70.scene"; 
	
	/**
	 * Path of the 50 speed limit sign
	 */
	private String speedLimit50 = "Models/RoadSigns/speedLimits/speedLimit50/speedLimit50.scene"; 
	
	
	
// ========================== METHODS ==============================

	/**
	 * Puts signs on roads
	 * 
	 * Example : pedestrian crossing, speed limitation
	 * 
	 * @param scene The scene where to add signs
	 */	
	@Override
	public void addPunctualObject(Scene scene) throws NoSuchAuthorityCodeException, FactoryException {
		// We get all the scene roads and intersections
		Map<String, LineRoad> roads = scene.getLineRoads();
		IntersectionColl interColl = scene.getCollIntersect();
		
		// We create the road to perform the work on
		LineRoad lineRoad = new LineRoad(); 
		// Boolean that tells if the the road start on the intersect
		boolean roadBool;
		
		// We go through all the intersections
		for (Intersection intersect : interColl.getMapIntersection().values()){
			for (String road : intersect.getRoadId().keySet()){
				lineRoad = roads.get(road);
				roadBool = intersect.getRoadId().get(road);
				
				// We calculate the road length
				Coordinate roadStart = lineRoad.getGeom().getCoordinates()[0];
				double[] roadStartDouble = new double[]{roadStart.x, roadStart.y, roadStart.z};
				
				Coordinate roadEnd = lineRoad.getGeom().getCoordinates()[lineRoad.getGeom().getCoordinates().length-1];
				double[] roadEndDouble = new double[]{roadEnd.x, roadEnd.y, roadEnd.z};
										
				double roadLength = JadeUtils.getDistance(roadStartDouble, roadEndDouble);

				// If there is no street furniture on the road
				if (lineRoad.getSF().isEmpty()){
					// If the speed limit is under 90 km/h we can place a pedestrian crossing sign, else
					if (lineRoad.getSpeed().equals("50 km/h") || lineRoad.getSpeed().equals("70 km/h")){
						StreetFurniture streetFurniture = addSigns(lineRoad,roadBool,this.pedestrianCrossing, scene,intersect, 20);
						addStreetFurniture(streetFurniture, lineRoad, scene);
					}
				}
				// If the road is more than 100 m long, we place a speed limit sign at the half of the road
				else if (roadLength >= 100){
					double distance = roadLength / 2;
					
					int speedType = speedType(lineRoad.getSpeed());
					addSpeedLimitSigns(lineRoad, roadBool, speedType, scene, intersect, distance);
					
				}
				else{
					System.out.println("There is no road in this intersection ... ");
					}
				}
			}
		}
		
	/**
	 * Returns an integer associated to each speed limit
	 * 
	 * @param speed1 the road speed to test
	 * @return
	 */
	private int speedType(String speed1){
		// They have 2 different speeds
		
		// Case 0: speed2 is the slowest and its value is 110 km/h
		if (speed1.equals("130 km/h")){
			return 0;
		}
		// Case 1: speed1 is the slowest and its value is 110 km/h
		else if (speed1.equals("110 km/h")){
			return 1;
		}
		// Case 2: speed2 is the slowest and its value is 90 km/h
		if (speed1.equals("90 km/h")){
			return 2;
		}
		// Case 3: speed1 is the slowest and its value is 90 km/h
		if (speed1.equals("70 km/h")){
			return 3;
		}
		// Case 4: speed1 is the slowest and its value is 70 km/h
		if (speed1.equals("50 km/h")){
			return 4;
		}
		return -1;
			
		}
		
	/**
	 * Adds a speed limit sign on the road according to the case in which we are (depending on the road speed limit)
	 * 
	 * @param lineRoad road the road on which the furniture is added 
	 * @param startOnIntersect The boolean to know if the beginning of the road is on the intersection
	 * @param speedType The road speed type regarding it speed limit
	 * @param scene The object containing all the elements of the scene
	 * @param intersect The intersection on which to perform the work
	 * @param distance the distance between the sign and the intersection along the road
	 * 
	 * @throws NoSuchAuthorityCodeException
	 * @throws FactoryException
	 */
	private void addSpeedLimitSigns(LineRoad lineRoad, boolean startOnIntersect, int speedType, Scene scene, Intersection intersect, double distance) throws NoSuchAuthorityCodeException, FactoryException {
		
		// Signs positioning
		switch (speedType) {
		
		case 0:
			System.out.println("Cas 0");
			boolean startOnIntersect0 = startOnIntersect;
			StreetFurniture streetFurniture0 = addSigns(lineRoad, startOnIntersect0, this.speedLimit130, scene, intersect, distance);
			addStreetFurniture(streetFurniture0, lineRoad, scene);
			break;
			
		case 1:
			System.out.println("Cas 1");
			boolean startOnIntersect1 = startOnIntersect;
			StreetFurniture streetFurniture1 = addSigns(lineRoad, startOnIntersect1, this.speedLimit110, scene, intersect, distance);
			addStreetFurniture(streetFurniture1, lineRoad, scene);
			break;

		case 2:
			System.out.println("Cas 2");
			boolean startOnIntersect2 = startOnIntersect;
			StreetFurniture streetFurniture2 = addSigns(lineRoad, startOnIntersect2, this.speedLimit90, scene, intersect, distance);
			addStreetFurniture(streetFurniture2, lineRoad, scene);
			break;
			
		case 3:
			System.out.println("Cas 3");
			boolean startOnIntersect3 = startOnIntersect;
			StreetFurniture streetFurniture3 = addSigns(lineRoad, startOnIntersect3, this.speedLimit70, scene, intersect, distance);
			addStreetFurniture(streetFurniture3, lineRoad, scene);
			break;
			
		case 4:
			System.out.println("Cas 4");
			boolean startOnIntersect4 = startOnIntersect;
			StreetFurniture streetFurniture4 = addSigns(lineRoad, startOnIntersect4, this.speedLimit50, scene, intersect, distance);
			addStreetFurniture(streetFurniture4, lineRoad, scene);
			break;
		
		default:
			System.out.println("Intersection de mauvais type");
			break;
		}
	}
	

	
	/**
	 * Adds street furniture to the associated road and the scene
	 * 
	 * @param streetFurniture the furniture to add
	 * @param road the road on which the furniture is added
	 * @param scene the object containing all the elements of the scene
	 */
	private void addStreetFurniture(StreetFurniture streetFurniture, LineRoad road, Scene scene){

		boolean alreadyOn = false;
		
		if (streetFurniture != null){
			if (!road.getSF().isEmpty()){
				for (int i=0; i < road.getSF().size(); i++){
					if (streetFurniture.getCoord().equals2D(road.getSF().get(i).getCoord())){
						alreadyOn = true;
					}
				}
				if (!alreadyOn){
					road.addSF(streetFurniture);
					scene.addStreetFurniture(streetFurniture);
				}
			}
			else{
				road.addSF(streetFurniture);
				scene.addStreetFurniture(streetFurniture);
			}
		}
	}
	
	/**
	 * Creates new street furniture
	 * 
	 * @param road the road on which the furniture has to be created
	 * @param startOnIntersect the boolean to know if the beginning of the road is on the intersection
	 * @param folder the path toward the right sign
	 * @param scene the object containing all the elements of the scene
	 * @param intersect the intersection on which to perform the work
	 * @param the distance between the sign and the intersection along the road
	 * 
	 * @return a street furniture object 
	 * 
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 */
	private StreetFurniture addSigns(LineRoad road, boolean startOnIntersect, String folder, Scene scene, Intersection intersect, double distance) throws NoSuchAuthorityCodeException, FactoryException{

		if (startOnIntersect){
			// It is possible to return the sign angle in street furniture
			return signPosition(road, 0, folder, scene, intersect, distance);
		}
		else{
			return signPosition(road, road.getGeom().getCoordinates().length-1, folder, scene, intersect, distance);
		}
	}	
	

	
	/**
	 * Gives the possible position of a new street furniture
	 * 
	 * @param road the road on which the furniture is added
	 * @param left the boolean which allow to know if the sign has to be on the right or on the left of the orad
	 * @param position the position in the table of coordinate for the point we need to use
	 * @param distance the distance between the sign and the intersection along the road
	 * 
	 * @return an object Coordinate that give the position of the sign from the intersection
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 */
	private StreetFurniture signPosition(LineRoad road, int position, String folder, Scene scene, Intersection intersection, double distance) throws NoSuchAuthorityCodeException, FactoryException{

		// Variable 
		Coordinate[] coord = road.getGeom().getCoordinates();
		
		double x = coord[position].x;
		double y = coord[position].y;
		
		double newX;
		double newY;
		double rotation = 0;
		double[] sfCoord ;
		

		double D = road.getWidth()/2 + 0.7; // 0.70 meters after the border of the road

		double theta = JadeUtils.roadAngle(road,position); // Angle between road and horizontal line, in counter clockwise
		
		sfCoord = sfPositionning(folder, x, y, distance, D, theta);
		newX = sfCoord[0];
		newY = sfCoord[1];
		rotation = sfCoord[2];
		
		boolean doesIntersect = true;
		while (doesIntersect && distance < distance+10){
			doesIntersect = false;
			sfCoord = sfPositionning(folder, x, y, distance, D, theta);
			newX = sfCoord[0];
			newY = sfCoord[1];
			rotation = sfCoord[2];
			
			/*Coordinate coord1 = new Coordinate(newX, newY);

			GeometryFactory factory = new GeometryFactory();
			Point point = new Point(coord1, null, 0);
			//System.out.println(coord1);*/
			
			PackedCoordinateSequenceFactory factory=PackedCoordinateSequenceFactory.DOUBLE_FACTORY;
			CoordinateSequence seq=factory.create(new Coordinate[]{new Coordinate(newX,newY)});

			Point pt = new Point(seq,new GeometryFactory());

			Geometry g = (Geometry) pt;

			for (String roadId: intersection.getRoadId().keySet()){
				
				SurfaceRoad surfaceRoad = ((LineRoad) scene.getLineRoads().get(roadId)).enlarge();

				if(surfaceRoad.getGeom().contains(g)){
					doesIntersect = true;
					distance = distance + 0.5;
				}
			}
		}
		
		// Be careful y is the vertical axis in OpenDS 
		//Coordinate newCoord = new Coordinate(newX - centroid.x, newZ - centroid.y, road.getZ_ini());
		if(!doesIntersect){
			double newZ = scene.getDtm().getHeightAtPoint(newX,  newY);
			Coordinate newCoord = new Coordinate(newX - scene.getCentroid().x, -1*(newY - scene.getCentroid().y), newZ); 

			return new StreetFurniture(folder, newCoord, rotation);
		}
		return null;
	}
	
	/**
	 * Determines the possible position of the signs (always on the right side of the road)
	 * 
	 * @param folder the path toward the right sign
	 * @param x the x intersection coordinate
	 * @param y the y intersection coordinate
	 * @param d the distance between the sign and the intersection along the road
	 * @param D the distance between the sign and the middle of the road across the road
	 * @param theta the angle between the horizontal and the road in clockwise order
	 * 
	 * @return
	 */
	private double[] sfPositionning (String folder, double x, double y, double d, double D, double theta){
		// We place the sign on the right side of the road
		// Initialization
		double newX;
		double newY; 
		
		double rotation = 0;

		rotation = theta;
		//rotation =  - Math.PI/2 + theta;
		
		// Up-Right quarter
		if (0<= theta && theta <= Math.PI/2){
			newX = x + d*Math.sin(theta) + D*Math.cos(theta);
			newY = y - d*Math.cos(theta) + D*Math.sin(theta);
		}
		// Down-Right quarter
		else if (theta> 3*Math.PI/2 && theta <= 2*Math.PI){
			newX = x - d*Math.sin(2*Math.PI - theta) + D*Math.cos(2*Math.PI - theta);
			newY = y - d*Math.cos(2*Math.PI - theta) - D*Math.sin(2*Math.PI - theta);
		}
		// Up-Left quarter
		else if (theta > Math.PI/2 && theta <= Math.PI){
			newX = x + d*Math.sin(Math.PI - theta) - D*Math.cos(Math.PI - theta);
			newY = y + d*Math.cos(Math.PI - theta) + D*Math.sin(Math.PI - theta);
		}
		// Down-Left quarter
		else{
			newX = x - d*Math.sin(theta - Math.PI) - D*Math.cos(theta - Math.PI);
			newY = y + d*Math.cos(theta - Math.PI) - D*Math.sin(theta - Math.PI);
		}

		return new double[]{newX, newY, rotation};
	}


}



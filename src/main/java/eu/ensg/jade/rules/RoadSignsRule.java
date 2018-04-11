package eu.ensg.jade.rules;

import java.util.List;
import java.util.Map;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequenceFactory;

import eu.ensg.jade.geometricObject.Road;
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
		Map<String, LineRoad> roads = scene.getLineRoads();
		IntersectionColl interColl = scene.getCollIntersect();
		
		LineRoad lineRoad = new LineRoad(); 
		boolean roadBool;
		int enter = -100; 
		
		// We go through all the intersections
		for (Intersection intersect : interColl.getMapIntersection().values()){
			for (String road : intersect.getRoadId().keySet()){
				lineRoad = roads.get(road);
				roadBool = intersect.getRoadId().get(road);
				// We check the road driving direction
				enter = isEntering(lineRoad, roadBool);
				
				Coordinate roadStart = lineRoad.getGeom().getCoordinates()[0];
				double[] roadStartDouble = new double[]{roadStart.x, roadStart.y, roadStart.z};
				
				Coordinate roadEnd = lineRoad.getGeom().getCoordinates()[lineRoad.getGeom().getCoordinates().length-1];
				double[] roadEndDouble = new double[]{roadEnd.x, roadEnd.y, roadEnd.z};
										
				double roadLength = JadeUtils.getDistance(roadStartDouble, roadEndDouble);

				if (lineRoad.getSF().isEmpty()){
					// If the speed limit is under 90 km/h we can place a pedestrian crossing sign, else, we place a speed limit sign
					if (lineRoad.getSpeed().equals("50 km/h")){
						StreetFurniture streetFurniture = addSigns(lineRoad,roadBool,this.pedestrianCrossing, scene,intersect, 10);
						addStreetFurniture(streetFurniture, lineRoad, scene);
					}
				}
				else if (roadLength >= 100){
					double distance = roadLength / 2;
					
					int speedType = slowestRoad(lineRoad.getSpeed());
					addSpeedLimitSigns(lineRoad, roadBool, speedType, scene, intersect, distance);
					
				}
				else{
					System.out.println("There is no road in this intersection ... ");
					}
			}
		}
		
		
//		Map<String, LineRoad> roads = scene.getLineRoads();
//		IntersectionColl interColl = scene.getCollIntersect();
//		
//		boolean road90 = false;
//		boolean road130 = false;
//		
//		// We go through all the intersections
//		for (Intersection intersect : interColl.getMapIntersection().values()){
//			int numberOfRoads = intersect.getRoadId().size();
//			LineRoad[] roadsTab = new LineRoad[numberOfRoads];
//			boolean[] startOnIntersectTab = new boolean[numberOfRoads];
//			
//			roadTabFilling(roadsTab, startOnIntersectTab, intersect, roads);
//			
//			// If we're facing a road narrowing and the roads have 2 different speeds
//			if(intersect.getRoadId().size() == 2) {
//				System.out.println("Intersection à 2 routes !");
//				int larger = widthComparison(roadsTab[0],roadsTab[1]);
//				
//				// If they are different, we compare their speed
//				if (larger != -1){
//					int speedType = slowestRoad(roadsTab[0].getSpeed(), roadsTab[1].getSpeed());
//					addSpeedLimitSigns(roadsTab, startOnIntersectTab, speedType, scene, intersect);
//					}
//				}
//			
//			else{
//				System.out.println("On est dans l'autre cas");
//				// We go through all the roads
//				for (String road : intersect.getRoadId().keySet()){
//					LineRoad lineRoad = (LineRoad) roads.get(road);
//					boolean roadBool = intersect.getRoadId().get(road);
//					// We check the road driving direction
//					int enter = isEntering(lineRoad, roadBool);
//					
////					if (lineRoad.getSpeed().equals("130 km/h")){
////						road130 = true;
////					}
////					else if (lineRoad.getSpeed().equals("90 km/h") && lineRoad.getNature().equals("Bretelle")){
////						road90 = true;
////					}
//					
//					int intersectType = calcIntersectionType(roadsTab,numberOfRoads);
//					
//					// If the intersection has 5 or more roads, we've placed traffic lights
//					// Intersection with 3 or 4 attached roads and traffic lights are harder to determine ...
//					if ((lineRoad.getSpeed().equals("70 km/h") &&
//							((lineRoad.getName().substring(0, 2).equals("PL") || lineRoad.getName().substring(0, 3).equals("RPT"))
//							|| numberOfRoads == 5))){
//						System.out.println("We've got a road to place pedestrian crossing");
//						//addSignsByRoad(enter, lineRoad, roadBool, this.pedestrianCrossing, scene, intersect);
//						StreetFurniture streetFurniture = addSigns(lineRoad,roadBool,this.pedestrianCrossing, scene,intersect);
//						addStreetFurniture(streetFurniture, lineRoad, scene);
//				
//					}
//					else{
//					System.out.println("There is no road in this intersection ... ");
//					}
//				}
//			}
//
//		}
	}		
	
	
	private int slowestRoad(String speed1){
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
	 * @param roadsTab the list of roads attached to the intersection
	 * @param startOnIntersectTab the list of boolean associated to each road that tells if the road start point is on the intersection
	 * @param intersect the intersection on which to perform the work
	 * @param roads the list of roads contained by the scene
	 */

	private void roadTabFilling(LineRoad[] roadsTab, boolean[] startOnIntersectTab, Intersection intersect, Map<String, LineRoad> roads){
		// Roads retrieval
		int k = 0;
		
		// We fill the roadsTab and startOnIntersectTab arrays
		for (String road : intersect.getRoadId().keySet()){
			roadsTab[k] = roads.get(road);
			startOnIntersectTab[k] = intersect.getRoadId().get(road);
			k++;
		}
	}
	
	/**
	 * Compares the width of two roads 
	 * 
	 * @param road1 the first road
	 * @param road2 the second road
	 * 
	 * @return the index of the largest road if there is one, and null if not
	 */
	private int widthComparison(Road road1, Road road2){
		
		if(road1.getWidth() > road2.getWidth()){
			return 0;
		}
		
		else if(road1.getWidth() < road2.getWidth()){
			return 1;
		}
		
		return -1;
	}
	
	/**
	 * Gives the direction of the road compared to the intersection
	 * 
	 * @param road the road to test
	 * @param direction a boolean that specifies if the beginning of the road is on the intersection or not
	 * 
	 * @return -1 if leaving, 0 if double-way, +1 if entering 
	 */
	private int isEntering(Road road, Boolean direction){
		int modDirection = 1;

		// If it is a two-way driving road we return 0
		if(road.getDirection().equals("Double")){
			return 0;
		}
		
		if (road.getDirection().equals("Inverse")){
		// If the road driving direction is reverse
				modDirection=-1;
			}
			
		// And the beginning of the road is on the intersection, the road is entering it
		if(direction) {
			return -1*modDirection;
		}
		// else it is leaving it
		else if(!direction) {
			return 1*modDirection;
		}

		return 0;
	}
	
	/**
	 * Check if there is an importance change between roads
	 * 
	 * @param roadsTab the list of roads attached to the intersection
	 * @param size the number of roads in the considered intersection
	 * 
	 * @return table of int, 1 for bigger, 0 for lesser importance, null if same importance.
	 */
	private int hasDiffImportance(LineRoad[] roadsTab, int size) {
		// By order the greatest are 1, 2, 3, 4, 5
		int greatest = 300;
		int lowest = -1;

		for (int i=0; i<size; i++){
			if (roadsTab[i].getImportance().equals("NC") || roadsTab[i].getImportance().equals("NR")){
				return -1;
			}
			
			if (greatest > Integer.parseInt(roadsTab[i].getImportance())){
				greatest = Integer.parseInt(roadsTab[i].getImportance());
			}
			else if (lowest < Integer.parseInt(roadsTab[i].getImportance())){
				lowest = Integer.parseInt(roadsTab[i].getImportance());

			}
		}
		return lowest - greatest;
	}
	
	
	/**
	 * Calculates the intersection type
	 * 
	 * @param roadsTab the list of roads attached to the intersection
	 * @param size the number of roads in the considered intersection
	 * 
	 * @return int the type of intersection: 0: Traffic Lights, 1: Yield, 2: Break; 3: Priority to the right.
	 */
	private int calcIntersectionType(LineRoad[] roadsTab, int size) {
		int diff = hasDiffImportance(roadsTab, size);
		
		if (diff != -1){
			for(int i=0; i < size; i++){
				if ((roadsTab[i].getDirection().equals("Double") && roadsTab[i].getLaneNumber() >= 4) 
						|| ((roadsTab[i].getDirection().equals("Inverse") || roadsTab[i].getDirection().equals("Direct")) && roadsTab[i].getLaneNumber() >= 3)
						){
					return 1;
				}
				else if(diff == 0){
					if (roadsTab[i].getSpeed() != "" && size != 3){
						return 2;
					}
					return 3;
				}
				else if (diff >= 1){
					return 3;
				}
			}

		}
		return -1;
	}
	
	/**
	 * Creates signs for intersections of more than three roads
	 * 
	 * @param enter integer which tells the road traffic direction regarding the intersection
	 * @param lineRoad the road on which to perform the work
	 * @param startOnIntersect the boolean to know if the beginning of the road is on the intersection
	 * @param folder the path toward the right sign
	 * @param scene the object containing all the elements of the scene
	 * 
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 */
	private void addSignsByRoad(int enter, LineRoad lineRoad, boolean startOnIntersect, String folder, Scene scene, Intersection intersect, double distance) throws NoSuchAuthorityCodeException, FactoryException {
		// If it is a direct driving direction
		if (enter == 1 || enter == -1){
			StreetFurniture streetFurniture = addSigns(lineRoad,startOnIntersect,this.pedestrianCrossing,scene, intersect, distance);
			addStreetFurniture(streetFurniture, lineRoad, scene);
		
		}		
		
		// If it has more than 2 lane, we had a sign on each side of the road
		else if (lineRoad.getLaneNumber() >= 2){
			//StreetFurniture streetFurniture = addSigns(lineRoad,startOnIntersect,folder,scene,intersect);
			//addStreetFurniture(streetFurniture, lineRoad, scene);
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
					System.out.println("We've placed a sign !!");
					road.addSF(streetFurniture);
					scene.addStreetFurniture(streetFurniture);
				}
			}
			else{
				System.out.println("We've placed a sign !!");
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
	 * 
	 * @return an object Coordinate that give the position of the sign from the intersection
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 */
	private StreetFurniture signPosition(LineRoad road, int position, String folder, Scene scene, Intersection intersection, double d) throws NoSuchAuthorityCodeException, FactoryException{

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
		
		sfCoord = sfPositionning(folder, x, y, d, D, theta);
		newX = sfCoord[0];
		newY = sfCoord[1];
		rotation = sfCoord[2];
		
		boolean doesIntersect = true;
		while (doesIntersect && d < d+10){
			doesIntersect = false;
			sfCoord = sfPositionning(folder, x, y, d, D, theta);
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
					d = d + 0.5;
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
	 * Determines the possible position of the signs
	 * 
	 * @param left the boolean which allow to know if the sign has to be on the right or on the left of the orad
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
			newX = x + d*Math.sin(theta) - D*Math.cos(theta);
			newY = y - d*Math.cos(theta) - D*Math.sin(theta);
		}
		// Down-Right quarter
		else if (theta> 3*Math.PI/2 && theta <= 2*Math.PI){
			newX = x - d*Math.sin(2*Math.PI - theta) - D*Math.cos(2*Math.PI - theta);
			newY = y - d*Math.cos(2*Math.PI - theta) + D*Math.sin(2*Math.PI - theta);
		}
		// Up-Left quarter
		else if (theta > Math.PI/2 && theta <= Math.PI){
			newX = x + d*Math.sin(Math.PI - theta) + D*Math.cos(Math.PI - theta);
			newY = y + d*Math.cos(Math.PI - theta) - D*Math.sin(Math.PI - theta);	
		}
		// Down-Left quarter
		else{
			newX = x - d*Math.sin(theta - Math.PI) + D*Math.cos(theta - Math.PI);
			newY = y + d*Math.cos(theta - Math.PI) + D*Math.sin(theta - Math.PI);		
		}
		
		return new double[]{newX, newY, rotation};
	}


}



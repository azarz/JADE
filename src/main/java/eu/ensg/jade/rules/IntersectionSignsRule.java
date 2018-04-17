package eu.ensg.jade.rules;

import java.util.HashMap;
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
 * IntersectionSignsRule is the class implementing the generation of intersection signs on the scene 
 * 
 * @author JADE
 */

public class IntersectionSignsRule implements RuleShape{

// ========================== ATTRIBUTES ===========================
	
	/**
	 * Path of the dead-end street sign
	 */
	private String deadEndStreet = "Models/RoadSigns/squarePlatesWithPole/DeadEndStreet/DeadEndStreet.scene"; 
	
	/**
	 * Path of the  narrow road sign
	 */
	private String roadNarrows = "Models/RoadSigns/dangerSigns/RoadNarrows/RoadNarrows.scene"; 
	
	/**
	 * Path of the one-way street sign
	 */
	private String oneWay = "Models/RoadSigns/squarePlatesWithPole/OneWayStreet2/OneWayStreet2.scene"; 
	
	/**
	 * Path of the do not enter street sign
	 */
	private String doNotEnter = "Models/RoadSigns/prohibitions/Do-not-enter/DoNotEnter.scene"; 
	
	/**
	 * Path of the traffic light
	 */
	private String trafficLight = "Models/TrafficLight/trafficlight.scene"; 
	
	/**
	 * Path of the yield sign
	 */
	private String yield = "Models/RoadSigns/otherSigns/Yield/Yield.scene"; 
	
	/**
	 * Path of the sign which warn for an intersection with right priority 
	 */
	private String intersectionAhead = "Models/RoadSigns/dangerSigns/IntersectionAhead/IntersectionAhead.scene";

	/**
	 * Path of the stop sign
	 */
	private String stop = "Models/RoadSigns/otherSigns/Stop/Stop.scene"; 
	
// ========================== METHODS ==============================
	
// ------ Interface
	/**
	 * Puts signs on intersections referring to the number of roads in the intersection
	 * 
	 * Example : stop, one way, ...
	 * 
	 * @param scene the object containing all the elements of the scene
	 * 
	 * @throws FactoryException Throws FactoryException
	 * @throws NoSuchAuthorityCodeException Throws NoSuchAuthorityCodeException
	 */
	@Override
	public void addPunctualObject(Scene scene) throws NoSuchAuthorityCodeException, FactoryException{
		
		// We get the list of existing roads and intersections in the scene
//		Map <String,Road> roads = scene.getRoads();
		IntersectionColl interColl = scene.getCollIntersect();
		
		
		
		// We go through all the intersections
		for (Intersection intersect : interColl.getMapIntersection().values()){
			
			
			// We test how many roads are attached to the intersection
			
			// If the node has 1 attached road
			if (intersect.getRoadId().size() == 1){
				/*
				 * => DeadEndStreet sign
				 */
				oneRoadIntersect(intersect, scene);	
			}
			
			else if (intersect.getRoadId().size() == 2){
				/*
				 * => Narrow signs
				 * => One-way street
				 * => Do not enter
				 */
				twoRoadIntersect(intersect, scene);
			}
			
			// If the node has 3 or 4 attached roads
			else if (intersect.getRoadId().size() == 3 || intersect.getRoadId().size() == 4){
				/*
				 * => Ramp signs
				 * => Roundabout signs
				 * => One way signs
				 * => Do not enter signs
				 * => Yield signs
				 * => Stop signs
				 * => Traffic lights
				 */
				threeFourRoadIntersect(intersect, scene);
			}
			
			// If the node has 5 or more attached nodes
			else if (intersect.getRoadId().size() >= 5){
				/* 
				 * => Direction test 
				 * => Traffic lights placed 
				 */
				fiveRoadIntersect(intersect, scene);
			}
			
			else{
//				System.out.println("There is no road in this intersection ... ");
			}
		}
	}

// ------ Interface function
	/**
	 * Creates the signs of intersections with one road
	 * 
	 * @param intersect the intersection on which to perform the work
	 * @param roads the list of roads contained by the scene
	 * @param scene the object containing all the elements of the scene
	 * 
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 */
	private void oneRoadIntersect(Intersection intersect, Scene scene) throws NoSuchAuthorityCodeException, FactoryException {
		// Roads retrieval
//		LineRoad road = (LineRoad) roads.get(intersect.getRoadId().keySet().toArray()[0]);
		LineRoad road = scene.getLineRoads().get(intersect.getRoadId().keySet().toArray()[0]);
		// We see if the road starts on the intersection
		boolean startOnIntersect = intersect.getRoadId().get(intersect.getRoadId().keySet().toArray()[0]);
		
		// DeadEndStreet Sign installation
		StreetFurniture streetFurniture = addSigns(road,startOnIntersect,this.deadEndStreet, scene,intersect);
		addStreetFurniture(streetFurniture, road, scene);
	}
	
	/**
	 * Creates the signs of intersections with two roads
	 * 
	 * @param intersect the intersection on which to perform the work
	 * @param roads the list of roads contained by the scene
	 * @param scene the object containing all the elements of the scene
	 * 
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 */
	private void twoRoadIntersect(Intersection intersect, Scene scene) throws NoSuchAuthorityCodeException, FactoryException {
		// Roads retrieval
		LineRoad[] roadsTab = new LineRoad[2];
		boolean[] startOnIntersectTab = new boolean[2];

		// We fill the two previous tabs
		roadTabFilling(roadsTab, startOnIntersectTab, intersect, scene.getLineRoads());
		
		// Narrow sign installation
		
		// We compare the width of the node 2 roads
		int larger = widthComparison(roadsTab[0],roadsTab[1]);
		
		// If they are different, we had a road narrows sign
		if ( larger != -1){
			StreetFurniture streetFurniture = addSigns(roadsTab[larger],startOnIntersectTab[larger],this.roadNarrows, scene,intersect);
			addStreetFurniture(streetFurniture, roadsTab[larger], scene);
		}
		
		// One-way sign installation
		Map<Integer,Integer> sensMap = directionVariation(roadsTab,startOnIntersectTab);
		
		// If we are facing a one way driving road, we had the appropriate sign
		if (sensMap != null){
			
			if(sensMap.containsKey(-1)){
				StreetFurniture streetFurniture = addSigns(roadsTab[sensMap.get(-1)],startOnIntersectTab[sensMap.get(-1)],this.oneWay, scene,intersect);
				addStreetFurniture(streetFurniture, roadsTab[sensMap.get(-1)], scene);
			}
			
			if(sensMap.containsKey(1)){
				StreetFurniture streetFurniture = addSigns(roadsTab[sensMap.get(1)],startOnIntersectTab[sensMap.get(1)], this.doNotEnter, scene,intersect);
				addStreetFurniture(streetFurniture, roadsTab[sensMap.get(1)], scene);
			}
		}
	}
	
	/**
	 * Creates the signs of intersections with three or four roads
	 * 
	 * @param intersect the intersection on which to perform the work
	 * @param roads the list of roads contained by the scene
	 * @param scene the object containing all the elements of the scene
	 * 
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 */
	private void threeFourRoadIntersect(Intersection intersect, Scene scene) throws NoSuchAuthorityCodeException, FactoryException {
		int size = intersect.getRoadId().size();
		
		// Roads retrieval
		LineRoad[] roadsTab = new LineRoad[size];
		boolean[] startOnIntersectTab = new boolean[size];
		
		// We fill the two previous tabs
		roadTabFilling(roadsTab, startOnIntersectTab, intersect, scene.getLineRoads());
		
		//First test for ramp, round about or "normal".
		Boolean asRamp = isRamp(roadsTab,startOnIntersectTab,intersect,size);
		Boolean asRoundAbout = isRoundAbout(roadsTab);
		
		// If it is a ramp
		if(asRamp){
			addRampSigns(roadsTab,startOnIntersectTab,intersect,size);
		}
		
		//If it is a roundabout
		else if (asRoundAbout){
			addRoundAbout(roadsTab,startOnIntersectTab,scene,intersect);
		}
		
		// "Normal" case
		else {					
			int intersectType = calcIntersectionType(roadsTab,size);
			addMultiSigns(roadsTab,startOnIntersectTab,intersectType, scene,intersect);
		}
	}
	
	/**
	 * Creates the signs of intersections with more than 4 roads
	 * 
	 * @param intersect the intersection on which to perform the work
	 * @param roads the list of roads contained by the scene
	 * @param scene the object containing all the elements of the scene
	 * 
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 */
	private void fiveRoadIntersect(Intersection intersect, Scene scene) throws NoSuchAuthorityCodeException, FactoryException {
		
		// Roads retrieval
		Map<String,LineRoad> sceneRoads = scene.getLineRoads();
		LineRoad lineRoad = new LineRoad(); 
		boolean roadBool;
		int enter = -100; 
		
		// We go through all the roads
		for (String road : intersect.getRoadId().keySet()){
			lineRoad = sceneRoads.get(road);
			roadBool = intersect.getRoadId().get(road);
			// We check the road driving direction
			enter = isEntering(lineRoad, roadBool);

			// If it is a direct driving direction, we had traffic lights and do not enter sign
			addSignsByRoad(enter, lineRoad, roadBool, this.trafficLight, scene,intersect);
		}
	}

// ------ General methods -> roads characteristics
	/**
	 * Fills the list of roads attached to the intersection and the list of boolean associated to each road that tells if the road start point is on the intersection
	 * 
	 * @param roadsTab the list of roads attached to the intersection
	 * @param startOnIntersectTab the list of boolean associated to each road that tells if the road start point is on the intersection
	 * @param intersect the intersection on which to perform the work
	 * @param roads the list of roads contained by the scene
	 */
	private void roadTabFilling(LineRoad[] roadsTab, boolean[] startOnIntersectTab, Intersection intersect, Map <String,LineRoad> roads){
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
	
// ------ General methods -> signs characteristics
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
	private StreetFurniture signPosition(LineRoad road, boolean left, int position, String folder, Scene scene, Intersection intersection) throws NoSuchAuthorityCodeException, FactoryException{

		// Variable 
		Coordinate[] coord = road.getGeom().getCoordinates();
		Map<String, LineRoad> sceneLineRoad = scene.getLineRoads();
		
		double x = coord[position].x;
		double y = coord[position].y;
		
		double newX;
		double newY;
		double rotation = 0;
		double[] sfCoord ;
		

		double d = 5; // 5 meters after the beginning of the road
		double D = road.getWidth()/2 + 0.7; // 0.70 meters after the border of the road

		double theta = JadeUtils.roadAngle(road,position); // Angle between road and horizontal line, in counter clockwise
		
		sfCoord = sfPositionning(left, folder, x, y, d, D, theta);
		newX = sfCoord[0];
		newY = sfCoord[1];
		rotation = sfCoord[2];
		
		boolean doesIntersect = true;
		while (doesIntersect && d < 15){
			doesIntersect = false;
			sfCoord = sfPositionning(left, folder, x, y, d, D, theta);
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
				
				SurfaceRoad surfaceRoad = sceneLineRoad.get(roadId).enlarge();
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
	private double[] sfPositionning (boolean left, String folder, double x, double y, double d, double D, double theta){
		
		// Initialization
		double newX;
		double newY; 
		
		double rotation = 0;

		// Determination of the position
		if(left){
			if (folder.equals(this.yield)){
				rotation = theta + Math.PI/2;
				//D = D - 2.2;
			}
			else {
				rotation = theta;
			}
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
		}
		else{
			if (folder.equals(this.doNotEnter)){
				rotation = theta + Math.PI;
			}
			else{
				rotation =  - Math.PI/2 + theta;
			}
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
		}
		return new double[]{newX, newY, rotation};
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
	private StreetFurniture addSigns(LineRoad road, boolean startOnIntersect, String folder, Scene scene, Intersection intersect) throws NoSuchAuthorityCodeException, FactoryException{
		
		// We determine if the sign has to be on the right side or the left side of the road 
		boolean left = true;
		
		if (folder.equals(this.deadEndStreet) || folder.equals(this.oneWay) || folder.equals(this.doNotEnter)){
			left = false;
		}
		
		// We create the sign 
		if (!folder.equals(this.deadEndStreet)){
			if (startOnIntersect){
				// It is possible to return the sign angle in street furniture
				return signPosition(road, left, 0, folder, scene, intersect);
			}
			else{
				return signPosition(road, left, road.getGeom().getCoordinates().length-1, folder, scene, intersect);
			}
		}
		else{
			if (startOnIntersect){
				// It is possible to return the sign angle in street furniture
				return signPosition(road, left, road.getGeom().getCoordinates().length-1, folder, scene, intersect);
			}
			else{
				return signPosition(road, left, 0, folder, scene, intersect);
			}
		}
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
	private void addSignsByRoad(int enter, LineRoad lineRoad, boolean startOnIntersect, String folder, Scene scene, Intersection intersect) throws NoSuchAuthorityCodeException, FactoryException {
		// If it is a direct driving direction, we had traffic lights and do not enter sign
		if (enter == 1){
			StreetFurniture streetFurniture = addSigns(lineRoad,startOnIntersect,this.doNotEnter,scene, intersect);
			addStreetFurniture(streetFurniture, lineRoad, scene);
			
			StreetFurniture streetFurniture2 = addSigns(lineRoad,startOnIntersect,folder,scene, intersect);
			addStreetFurniture(streetFurniture2, lineRoad, scene);
		
		}		
		
		// If it is a reverse driving direction, we had traffic lights and one way sign
		else if (enter == -1){
			StreetFurniture streetFurniture = addSigns(lineRoad,startOnIntersect,this.oneWay,scene,intersect);
			addStreetFurniture(streetFurniture, lineRoad, scene);
		}
		
		// If it is a two-way driving direction, we had traffic lights
		else if (enter == 0){
			StreetFurniture streetFurniture = addSigns(lineRoad,startOnIntersect,folder,scene,intersect);
			addStreetFurniture(streetFurniture, lineRoad, scene);
		}
	}

// ------ Methods specifics to two roads
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
	 * Compares the direction of two roads
	 * 
	 * @param roadsTab the list of roads attached to the intersection
	 * @param startOnIntersectTab the list of boolean associated to each road that tells if the road start point is on the intersection
	 * 
	 * @return the map with the way of the road and the index of it if their is a difference between both, else return null
	 */
	private Map<Integer,Integer> directionVariation(Road[] roadsTab, boolean[] startOnIntersectTab){
		
		int dir1 = isEntering(roadsTab[0],startOnIntersectTab[0]);
		int dir2 = isEntering(roadsTab[1],startOnIntersectTab[1]);
		
		Map<Integer,Integer> map= new HashMap<Integer,Integer>();
		
		if((dir1 == -1 && dir2 == 0) || (dir1 == 1 && dir2 == 0) ){
			map.put(dir1, 0);
			return map;
		}
		
		else if((dir2 == -1 && dir1 == 0) || (dir2 == 1 && dir1 == 0) ){
			map.put(dir2, 1);
			return map;
		}
		
		return null;
	}

// ------ Methods specifics to three or four roads
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
	 * Add multiple signs to the intersections.
	 * 
	 * @param roadsTab the list of roads attached to the intersection
	 * @param startOnIntersectTab the list of boolean associated to each road that tells if the road start point is on the intersection
	 * @param intersect the intersection on which to perform the work
	 * @param intersectType the type of the intersection with 3 or 4 attached roads regarding the number, importance, speed of roads
	 * @param scene the object containing all the elements of the scene
	 * 
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 */
	private void addMultiSigns(LineRoad[] roadsTab, boolean[] startOnIntersectTab, int intersectType, Scene scene, Intersection intersect) throws NoSuchAuthorityCodeException, FactoryException {
		
		// Initialization
		LineRoad lineRoad;
		boolean roadBool;
		int enter;
		int greatest;
		
		// Signs positioning
		switch (intersectType) {
		
		case 1:
			for(int i=0; i < roadsTab.length; i++){
				lineRoad = (LineRoad) roadsTab[i];
				roadBool = startOnIntersectTab[i];
				enter = isEntering(lineRoad, roadBool);
				
				addSignsByRoad(enter, lineRoad, roadBool, this.trafficLight, scene, intersect);			
			}
			break;
		case 2:
			for(int i=0; i < roadsTab.length; i++){
				lineRoad = (LineRoad) roadsTab[i];
				roadBool =startOnIntersectTab[i];
				enter = isEntering(lineRoad, roadBool);
				
				addSignsByRoad(enter, lineRoad, roadBool, this.intersectionAhead, scene, intersect);	
			}
			break;

		case 3:
			greatest = Integer.parseInt(roadsTab[0].getImportance());

			for (int i=0; i<roadsTab.length; i++){
				if (greatest > Integer.parseInt(roadsTab[i].getImportance())){
					greatest = Integer.parseInt(roadsTab[i].getImportance());
				}
			}
			for (int i=0; i<roadsTab.length; i++){
				lineRoad = (LineRoad) roadsTab[i];
				roadBool =startOnIntersectTab[i];
				enter = isEntering(lineRoad, roadBool);
				
				
				if (enter == 1){
					StreetFurniture streetFurniture = addSigns(lineRoad,roadBool,this.doNotEnter, scene,intersect);
					addStreetFurniture(streetFurniture, lineRoad, scene);
					
					if (Integer.parseInt(lineRoad.getImportance()) - greatest == 1 ){
						StreetFurniture streetFurniture2 = addSigns(lineRoad,roadBool,this.yield, scene,intersect);
						addStreetFurniture(streetFurniture2, lineRoad, scene);
					}
					else if (Integer.parseInt(lineRoad.getImportance()) - greatest > 1){
						StreetFurniture streetFurniture2 = addSigns(lineRoad,roadBool,this.stop, scene,intersect);
						addStreetFurniture(streetFurniture2, lineRoad, scene);					
					}
				}
				else if (enter == -1){
					StreetFurniture streetFurniture = addSigns(lineRoad,roadBool,this.oneWay, scene,intersect);
					addStreetFurniture(streetFurniture, lineRoad, scene);
				}
				else if (enter == 0){
					if (Integer.parseInt(lineRoad.getImportance()) - greatest == 1 ){
						StreetFurniture streetFurniture2 = addSigns(lineRoad,roadBool,this.yield, scene,intersect);
						addStreetFurniture(streetFurniture2, lineRoad, scene);
					}
					else if (Integer.parseInt(lineRoad.getImportance()) - greatest > 1){
						StreetFurniture streetFurniture2 = addSigns(lineRoad,roadBool,this.stop, scene,intersect);
						addStreetFurniture(streetFurniture2, lineRoad, scene);
					}
				}				
			}
			break;
		default:
//			System.out.println("Intersection de mauvais type");
			break;
		}
	}

// ------ Methods specifics to ramps
	/**
	 * Check if it is a ramp.
	 * 
	 * @param roadsTab the list of roads attached to the intersection
	 * @param startOnIntersectTab the list of boolean associated to each road that tells if the road start point is on the intersection
	 * @param intersect the intersection on which to perform the work
	 * @param size the number of roads in the considered intersection
	 * 
	 * @return boolean, true if the intersection is in a ramp
	 */
	private boolean isRamp(LineRoad[] roadsTab, boolean[] startOnIntersectTab, Intersection intersect, int size){
		
		// No ramp if size is different then 3:
		if (size !=3){
			return false;
		}
		
		else {
			//We check in the roads if there is at least one is of ramp nature (bretelle in French)
			for (LineRoad road : roadsTab){
				if(road.getNature().equals("Bretelle")){
					return true;
				}
			}
		}
		//Else there is no ramp
		return false;
	}

	/**
	 * Adds signs corresponding to a ramp
	 * 
	 * @param roadsTab the list of roads attached to the intersection
	 * @param startOnIntersectTab the list of boolean associated to each road that tells if the road start point is on the intersection
	 * @param intersect the intersection on which to perform the work
	 * @param size the number of roads in the considered intersection
	 */
	private void addRampSigns(LineRoad[] roadsTab, boolean[] startOnIntersectTab, Intersection intersect, int size){
		System.out.println("C'est une bretelle");
		
	}

// ------ Methods specifics to roundabout
	/**
	 * Check if it is a roundabout
	 * 
	 * @param roadsTab the list of roads attached to the intersection
	 * 
	 * @return boolean, true if the intersection is in a round about
	 */
	public boolean isRoundAbout(LineRoad[] roadsTab){
	//We go through the roads and see if there is at least one being a Round About.
		for (LineRoad road : roadsTab){
			if (road.getName().length()>2){
				if(road.getName().substring(0, 2).equals("PL") || road.getName().substring(0, 3).equals("RPT")){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Adds signs corresponding to a roundabout
	 * 
	 * @param roadsTab the list of roads attached to the intersection
	 * @param startOnIntersectTab the list of boolean associated to each road that tells if the road start point is on the intersection
	 * @param scene the object containing all the elements of the scene
	 * @param intersect the intersection on which to perform the work
	 * 
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 */
	private void addRoundAbout(LineRoad[] roadsTab, boolean[] startOnIntersectTab, Scene scene, Intersection intersect) throws NoSuchAuthorityCodeException, FactoryException{
		for (int i = 0; i < roadsTab.length; i++){
			LineRoad road = roadsTab[i];
			//We add yield signs for all roads not on the round about
			if (!road.getName().equals("")) {
				if (!((road.getName().substring(0, 2)).equals("PL") || (road.getName().substring(0, 3)).equals("RPT"))){
					addSignsByRoad(isEntering(road,startOnIntersectTab[i]), road, startOnIntersectTab[i], this.yield, scene,intersect);
				}
			}
		}
	}
}

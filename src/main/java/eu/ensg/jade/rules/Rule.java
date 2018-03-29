package eu.ensg.jade.rules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;

import eu.ensg.jade.geometricObject.Road;
import eu.ensg.jade.scene.Scene;
import eu.ensg.jade.semantic.Intersection;
import eu.ensg.jade.semantic.IntersectionColl;
import eu.ensg.jade.semantic.LineRoad;
import eu.ensg.jade.semantic.StreetFurniture;
import eu.ensg.jade.semantic.SurfaceVegetation;
import eu.ensg.jade.utils.JadeUtils;

/**
 * Rule is the class implementing the rules that are used to place punctual objects
 * 
 * @author JADE
 */

public class Rule implements IRule{
	
// ========================== METHODS ==============================

// ------------------------- INTERFACE -----------------------------
	
	/**
	 * Puts signs on intersections referring to the number of roads in the intersection
	 * 
	 * Example : stop, one way, ...
	 * 
	 * @param interColl the intersections existing in RGE data
	 * @param scene the object containing all the elements of the scene
	 */
	public void intersectSigns(Scene scene){
		
		// We get thelist of roads existing in the scene
		Map <String,Road> roads = scene.getRoads();
		IntersectionColl interColl = scene.getCollIntersect();
		Coordinate centroid = scene.getBuildingCentroid();
		
		// We go through all the intersections
		for (Intersection intersect : interColl.getMapIntersection().values()){
			
			// We test how many roads are contained by the intersection
			if (intersect.getRoadId().size() == 1){
				
				/*
				 * => DeadEndStreet sign
				 */
				
				// Roads recovery
				LineRoad road = (LineRoad) roads.get(intersect.getRoadId().keySet().toArray()[0]);
				boolean bool = intersect.getRoadId().get(intersect.getRoadId().keySet().toArray()[0]);
				
				// DeadEndStreet Sign installation
				StreetFurniture streetFurniture = addSigns(road,bool,"Models/RoadSigns/squarePlatesWithPole/DeadEndStreet/DeadEndStreet.scene", centroid);
				addStreetFurniture(streetFurniture, road, scene);
			}
			
			else if (intersect.getRoadId().size() == 2){
				/*
				 * => Narrow signs
				 * => One-way street
				 */
				
				// Roads recovery
				LineRoad[] roadsTab = new LineRoad[2];
				Boolean[] roadsBoolTab = new Boolean[2];
				int k = 0;
				
				for (String road : intersect.getRoadId().keySet()){
					roadsTab[k] = (LineRoad) roads.get(road);
					roadsBoolTab[k] = intersect.getRoadId().get(road);
					k++;
				}
				
				// Narrow sign installation
				int larger = widthComparison(roadsTab[0],roadsTab[1]);
				
				if ( larger != -1){
					StreetFurniture streetFurniture = addSigns(roadsTab[larger],roadsBoolTab[larger],"Models/RoadSigns/dangerSigns/RoadNarrows/RoadNarrows.scene",centroid);
					addStreetFurniture(streetFurniture, roadsTab[larger], scene);
				}
				
				// One-way sign installation
				Map<Integer,Integer> sensMap = directionVariation(roadsTab,roadsBoolTab);
				
				if (sensMap != null){
					
					if(sensMap.containsKey(-1)){
						StreetFurniture streetFurniture = addSigns(roadsTab[sensMap.get(-1)],roadsBoolTab[sensMap.get(-1)],"Models/RoadSigns/squarePlatesWithPole/OneWayStreet2/OneWayStreet2.scene", centroid);
						addStreetFurniture(streetFurniture, roadsTab[sensMap.get(-1)], scene);
					}
					
					if(sensMap.containsKey(1)){
						StreetFurniture streetFurniture = addSigns(roadsTab[sensMap.get(1)],roadsBoolTab[sensMap.get(1)],"Models/RoadSigns/prohibitions/Do-not-enter/DoNotEnter.scene", centroid);
						addStreetFurniture(streetFurniture, roadsTab[sensMap.get(1)], scene);
					}
				}
			}
			
			else if (intersect.getRoadId().size() == 3 || intersect.getRoadId().size() == 4){
				/*
				 * Si 3-4 => Test Bretelle
				 *        => Test Rond point
				 * 		  => Test de sens
				 * 		  => Test d'importance
				 * 		  => Test nombre de voies
				 * 		  => Algo de placement de signalisation en fonction des résultats obtenus
				 */
				
				int size = intersect.getRoadId().size();
				
				// Roads recuperation
				LineRoad[] roadsTab = new LineRoad[size];
				Boolean[] roadsBoolTab = new Boolean[size];
				
				int k = 0;
				
				for (String road : intersect.getRoadId().keySet()){
					roadsTab[k] = (LineRoad) roads.get(road);
					roadsBoolTab[k] = intersect.getRoadId().get(road);
					k++;
				}
				
				
				//First test for ramp, round about or "normal".
				Boolean asRamp = isRamp(roadsTab,roadsBoolTab,intersect,size);
				Boolean asRoundAbout = isRoundAbout(roadsTab);
				
				//Workflow following result
				
				if(asRamp){
					addRampSigns(roadsTab,roadsBoolTab,intersect,size);
				}
				else if (asRoundAbout){
					addRoundAbout(roadsTab,roadsBoolTab,scene, centroid);
				}
				else {
					//Checking Road importance for yield
					
					int intersectType = calcIntersectionType(roadsTab,size);
					addMultiSigns(roadsTab,roadsBoolTab,intersectType, scene, centroid);
				}
				
			}
			else if (intersect.getRoadId().size() >= 5){
				/*
				 * - Si 5 ou plus 
				 *        => Test de sens 
				 *        => Cédez le passage et flèches bleu pour modéliser un rond point 
				 */
				
				int size = intersect.getRoadId().size();
				
				// Roads recuperation

				LineRoad lineRoad = new LineRoad(); 
				boolean roadBool;
				int enter = -100; 
				
				for (String road : intersect.getRoadId().keySet()){
					lineRoad = (LineRoad) roads.get(road);
					roadBool = intersect.getRoadId().get(road);
					enter = isEntering(lineRoad, roadBool);
					
					if (enter == 1){
						StreetFurniture streetFurniture = addSigns(lineRoad,roadBool,"Models/RoadSigns/prohibitions/Do-not-enter/DoNotEnter.scene", centroid);
						addStreetFurniture(streetFurniture, lineRoad, scene);
						
						StreetFurniture streetFurniture2 = addSigns(lineRoad,roadBool,"Models/TrafficLight/trafficlight.scene", centroid);
						addStreetFurniture(streetFurniture2, lineRoad, scene);
					
					}
					else if (enter == -1){
						StreetFurniture streetFurniture = addSigns(lineRoad,roadBool,"Models/RoadSigns/squarePlatesWithPole/OneWayStreet2/OneWayStreet2.scene", centroid);
						addStreetFurniture(streetFurniture, lineRoad, scene);
					}
					else if (enter == 0){
						StreetFurniture streetFurniture = addSigns(lineRoad,roadBool,"Models/TrafficLight/trafficlight.scene", centroid);
						addStreetFurniture(streetFurniture, lineRoad, scene);
					}
				}
				
				
			}
			else{
				System.out.println("There is no road in this intersection ... ");
			}
		}
	}
	
	/**
	 * Puts signs on roads
	 * 
	 * Example : pedestrian crossing, speed limitation
	 * 
	 * @param roads the roads present in RGE data
	 */
	public void roadSigns(Map<String, Road> roads){
		
		// We go through all the roads
		for(Road road : roads.values()){
			
		}

	}
	
	/**
	 * Puts vegetation on vegetation area
	 * 
	 * @param vegetation the list of surface of vegetation in RGE data
	 */
	public void addVegetation(List<SurfaceVegetation> vegetation){
		
		// We go through all the surfaces
		for(int i = 0 ; i < vegetation.size() ; i++){
			// Test - ou est la surface ? (Bords de route ou parc ?) 
			//      - dimension de la surface 
			// = > On place ponctuellement les arbres a espacement réguliers le long des routes 
			//          OU on fait un fouilli d'arbre sur une grosse zone de forêt
		}

	}
	
// ------------------------- GENERALS -----------------------------
	
	/**
	 * Gives the direction of the road compared to the intersection
	 * 
	 * @param intersection the intersection
	 * @param i the index of the road
	 * 
	 * @return -1 if leaving, 0 if double-way, +1 if entering 
	 */
	private int isEntering(Road road, Boolean direction){
		
		if(road.getDirection()=="Double"){return 0;}
		int modDirection=1;
		if (road.getDirection()=="Inverse"){modDirection=-1;}
		else if(direction) {return -1*modDirection;}
		else if(!direction) {return 1*modDirection;}
		
		return 0;
	}
	
	/**
	 * Adds street furniture to the associated road and the scene
	 * 
	 * @param streetFurniture the furniture to add
	 * @param road the road on which the furniture is added
	 * @param scene the object which contains all the elements to add to the simulator
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
	 * @param road the road on which the furniture has to be added
	 * @param left the boolean which allow to know if the sign has to be on the right or on the left of the orad
	 * @param position the position in the table of coordinate for the point we need to use
	 * 
	 * @return an object Coordinate that give the position of the sign from the intersection
	 */
	private StreetFurniture signPosition(LineRoad road, boolean left, int position, String folder,Coordinate centroid){

		// Variable 
		Coordinate[] coord = road.getGeom().getCoordinates();
		
		double x = coord[position].x;
		double y = coord[position].y;
		
		double newX;
		double newZ;
		
		double d = 5; // 5 meters after the beginning of the road
		double D = road.getWidth()/2 + 0.7; // 0.70 meters after the border of the road

		double theta = JadeUtils.roadAngle(road); // Angle between road and horizontal line, in counter clockwise
		double rotation = 0;
		
		if (position != 0){ // if the end of the road is on the intersection
			theta = theta - Math.PI;
		}
		
		// Determination of the position
		if(left){
			rotation = theta;

			// Up-Right quarter
			if (0<= theta && theta <= Math.PI/2){
				newX = x + d*Math.cos(theta) - D*Math.sin(theta);
				newZ = y + d*Math.sin(theta) + D*Math.cos(theta);
			}
			// Down-Right quarter
			else if (theta> 3*Math.PI/2 && theta <= 2*Math.PI){
				newX = x + d*Math.cos(2*Math.PI - theta) + D*Math.sin(2*Math.PI - theta);
				newZ = y - d*Math.sin(2*Math.PI - theta) + D*Math.cos(2*Math.PI - theta);
			}
			// Up-Left quarter
			else if (theta > Math.PI/2 && theta <= Math.PI){
				newX = x - d*Math.cos(Math.PI - theta) - D*Math.sin(Math.PI - theta);
				newZ = y + d*Math.sin(Math.PI - theta) - D*Math.cos(Math.PI - theta);
			}
			// Down-Left quarter
			else{
				newX = x - d*Math.cos(theta - Math.PI) + D*Math.sin(theta - Math.PI);
				newZ = y - d*Math.sin(theta - Math.PI) - D*Math.cos(theta - Math.PI);
			}
		}
		else{
			rotation = Math.PI + theta;

			// Up-Right quarter
			if (0<= theta && theta <= Math.PI/2){
				newX = x + d*Math.cos(theta) + D*Math.sin(theta);
				newZ = y + d*Math.sin(theta) - D*Math.cos(theta);
			}
			// Down-Right quarter
			else if (theta> 3*Math.PI/2 && theta <= 2*Math.PI){
				newX = x + d*Math.cos(2*Math.PI - theta) - D*Math.sin(2*Math.PI - theta);
				newZ = y - d*Math.sin(2*Math.PI - theta) - D*Math.cos(2*Math.PI - theta);
			}
			// Up-Left quarter
			else if (theta > Math.PI/2 && theta <= Math.PI){
				newX = x - d*Math.cos(Math.PI - theta) + D*Math.sin(Math.PI - theta);
				newZ = y + d*Math.sin(Math.PI - theta) + D*Math.cos(Math.PI - theta);
			}
			// Down-Left quarter
			else{
				newX = x - d*Math.cos(theta - Math.PI) - D*Math.sin(theta - Math.PI);
				newZ = y - d*Math.sin(theta - Math.PI) + D*Math.cos(theta - Math.PI);
			}
		}
		
		// Be careful y is the vertical axis in OpenDS 
		Coordinate newCoord = new Coordinate(newX - centroid.x, newZ - centroid.y, road.getZ_ini());
		return new StreetFurniture(folder, newCoord, rotation);
	}
	
	/**
	 * Creates new street furniture
	 * 
	 * @param road the road on which the furniture has to be created
	 * @param init the boolean to know if the beginning of the road is on the intersection
	 * @param folder the path toward the right sign
	 * 
	 * @return a street furniture object 
	 */
	private StreetFurniture addSigns(LineRoad road, boolean init, String folder, Coordinate centroid){
		
		// The signs which has to be on the right of the road 
		String deadEndStreet = "Models/RoadSigns/squarePlatesWithPole/DeadEndStreet/DeadEndStreet.scene";
		String oneWayStreet = "Models/RoadSigns/squarePlatesWithPole/OneWayStreet2/OneWayStreet2.scene";
		String doNotEnter = "Models/RoadSigns/prohibitions/Do-not-enter/DoNotEnter.scene";
		
		// We determine if the sign has to be on the right side or the left side of the road 
		boolean left = true;
		
		if (folder == deadEndStreet || folder == oneWayStreet || folder == doNotEnter){
			left = false;
		}
		
		// We create the sign 
		if (init){
			// It is possible to return the sign angle in street furniture
			return signPosition(road, left, 0, folder, centroid);
				
		}
		else{
			return signPosition(road, left, road.getGeom().getCoordinates().length-1, folder, centroid);
		}
	}
	
// -------------------------- 2-SPECIFIC ---------------------------
	
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
	 * @param roadsTab the table of roads
	 * @param roadsBoolTab the table of boolean to say if the initial point of the orad is on the intersection
	 * 
	 * @return the map with the way of the road and the index of it if their is a difference between both, else return null
	 */
	private Map<Integer,Integer> directionVariation(Road[] roadsTab, Boolean[] roadsBoolTab){
		
		int dir1 = isEntering(roadsTab[0],roadsBoolTab[0]);
		int dir2 = isEntering(roadsTab[1],roadsBoolTab[1]);
		
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


// -------------------------- 3/4-SPECIFIC ---------------------------
	/**
	 * Check if it is a ramp.
	 * 
	 * @param roadsTab the table containing the roads
	 * @param roadsBoolTab the table containing the roads boolean descritor of direction
	 * @param intersect the intersection considered
	 * @param size Intersection's size
	 * 
	 * @return boolean, true if the intersection is in a ramp
	 */
	private boolean isRamp(LineRoad[] roadsTab, Boolean[] roadsBoolTab, Intersection intersect, int size){
		// No ramp if size is different then 3:
		if (size !=3){return false;}
		else {
			//We check in the roads if there is at least one is of ramp nature (bretelle in French)
			for (LineRoad road : roadsTab){
				if(road.getNature() == "Bretelle")
				{return true;}
			}
		}
		//Else there is no ramp
		return false;
	}

	/**
	 * Check if it is a roundabout
	 * 
	 * @param roadsTab the table containing the roads
	 * @param roadsBoolTab the table containing the roads boolean descriptor of direction
	 * @param intersect the intersection considered
	 * @param size Intersection's size
	 * 
	 * @return boolean, true if the intersection is in a round about
	 */
	private boolean isRoundAbout(LineRoad[] roadsTab){
	//We go through the roads and see if there is at least one being a Round About.
		for (LineRoad road : roadsTab){
			if (road.getName().length()>2){
				if(road.getName().substring(0, 2)==("PL") || road.getName().substring(0, 3) == "RPT")
				{return true;}
			}
		}
		return false;
	}
	
	/**
	 * Adds signs corresponding to a ramp
	 * 
	 * @param roadsTab the table containing the roads
	 * @param roadsBoolTab the table containing the roads boolean descritor of direction
	 * @param intersect the intersection considered
	 * @param size Intersection's size
	 */
	private void addRampSigns(LineRoad[] roadsTab, Boolean[] roadsBoolTab, Intersection intersect, int size){
		System.out.println("C'est une bretelle");
		
	}
	
	/**
	 * Adds signs corresponding to a roundabout
	 * 
	 * @param roadsTab the table containing the roads
	 * @param roadsBoolTab the table containing the roads boolean descritor of direction
	 * @param intersect the intersection considered
	 * @param size Intersection's size
	 */
	private void addRoundAbout(LineRoad[] roadsTab, Boolean[] roadsBoolTab, Scene scene, Coordinate centroid){
		for (int i = 0; i < roadsTab.length; i++){
			LineRoad road = roadsTab[i];

			//We add yeild signs for all roads not on the round about
			if (!(road.getName().substring(0, 2)==("PL") || road.getName().substring(0, 3) == "RPT")){
				StreetFurniture lightRoad = addSigns(road, roadsBoolTab[i], "Models/RoasSings/otherSigns/Yield/Yield.scene", centroid);
				addStreetFurniture(lightRoad, road, scene);
			}
		}
	}
	
	/**
	 * Check if there is an importance change between roads
	 * 
	 * @param roadsTab the table containing the roads
	 * @param size
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
	 * @param roadsTab the table containing the roads
	 * @param roadsBoolTab the table containing the roads boolean descritor of direction
	 * @param intersect the intersection considered
	 * @param size Intersection's size
	 * @param importTab the array of importance
	 * 
	 * @return int the type of intersection: 0: Traffic Lights, 1: Yield, 2: Break; 3: Priority to the right.
	 */
	private int calcIntersectionType(LineRoad[] roadsTab, int size) {
		int diff = hasDiffImportance(roadsTab, size);
		
		if (diff != -1){
			for(int i=0; i < size; i++){
				if ((roadsTab[i].getDirection() == "Double" && roadsTab[i].getLaneNumber() >= 3) 
						|| ((roadsTab[i].getDirection() == "Inverse" || roadsTab[i].getDirection() == "Direct") && roadsTab[i].getLaneNumber() >= 2)
						){
					return 1;
				}
				else if(diff == 0){
					if (roadsTab[i].getSpeed() != ""){
						return 2;
					}
					return 1;
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
	 * @param roadsTab the table containing the roads
	 * @param roadsBoolTab the table containing the roads boolean descritor of direction
	 * @param intersect the intersection considered
	 * @param size Intersection's size
	 * @param intersectType the type of the intersection
	 */
	private void addMultiSigns(LineRoad[] roadsTab, Boolean[] roadsBoolTab, int intersectType, Scene scene, Coordinate centroid) {
		LineRoad lineRoad;
		boolean roadBool;
		int enter;
		int greatest;
		
		switch (intersectType) {
		
		case 1:
			
			for(int i=0; i < roadsTab.length; i++){
				lineRoad = (LineRoad) roadsTab[i];
				roadBool =roadsBoolTab[i];
				enter = isEntering(lineRoad, roadBool);
				
				if (enter == 1){
					StreetFurniture streetFurniture = addSigns(lineRoad,roadBool,"Models/RoadSigns/prohibitions/Do-not-enter/DoNotEnter.scene", centroid);
					addStreetFurniture(streetFurniture, lineRoad, scene);
					
					StreetFurniture streetFurniture2 = addSigns(lineRoad,roadBool,"Models/TrafficLight/trafficlight.scene", centroid);
					addStreetFurniture(streetFurniture2, lineRoad, scene);
				
				}
				else if (enter == -1){
					StreetFurniture streetFurniture = addSigns(lineRoad,roadBool,"Models/RoadSigns/squarePlatesWithPole/OneWayStreet2/OneWayStreet2.scene", centroid);
					addStreetFurniture(streetFurniture, lineRoad, scene);
				}
				else if (enter == 0){
					StreetFurniture streetFurniture = addSigns(lineRoad,roadBool,"Models/TrafficLight/trafficlight.scene", centroid);
					addStreetFurniture(streetFurniture, lineRoad, scene);
				}
			
			}
			break;
		case 2:
			
			for(int i=0; i < roadsTab.length; i++){
				lineRoad = (LineRoad) roadsTab[i];
				roadBool =roadsBoolTab[i];
				enter = isEntering(lineRoad, roadBool);
				
				if (enter == 1){
					StreetFurniture streetFurniture = addSigns(lineRoad,roadBool,"Models/RoadSigns/prohibitions/Do-not-enter/DoNotEnter.scene", centroid);
					addStreetFurniture(streetFurniture, lineRoad, scene);
					
					StreetFurniture streetFurniture2 = addSigns(lineRoad,roadBool,"Models/RoadSigns/dangerSigns/IntersectionAhead/IntersectionAhead.scene", centroid);
					addStreetFurniture(streetFurniture2, lineRoad, scene);
				
				}
				else if (enter == -1){
					StreetFurniture streetFurniture = addSigns(lineRoad,roadBool,"Models/RoadSigns/squarePlatesWithPole/OneWayStreet2/OneWayStreet2.scene", centroid);
					addStreetFurniture(streetFurniture, lineRoad, scene);
				}
				else if (enter == 0){
					StreetFurniture streetFurniture = addSigns(lineRoad,roadBool,"Models/RoadSigns/dangerSigns/IntersectionAhead/IntersectionAhead.scene", centroid);
					addStreetFurniture(streetFurniture, lineRoad, scene);
				}	
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
				roadBool =roadsBoolTab[i];
				enter = isEntering(lineRoad, roadBool);
				
				if (enter == 1){
					StreetFurniture streetFurniture = addSigns(lineRoad,roadBool,"Models/RoadSigns/prohibitions/Do-not-enter/DoNotEnter.scene", centroid);
					addStreetFurniture(streetFurniture, lineRoad, scene);
					
					if (Integer.parseInt(lineRoad.getImportance()) - greatest == 1 ){
						StreetFurniture streetFurniture2 = addSigns(lineRoad,roadBool,"Models/RoadSigns/otherSigns/Yield/Yield.scene", centroid);
						addStreetFurniture(streetFurniture2, lineRoad, scene);
					}
					else if (Integer.parseInt(lineRoad.getImportance()) - greatest > 1){
						StreetFurniture streetFurniture2 = addSigns(lineRoad,roadBool,"Models/RoadSigns/otherSigns/Stop/Stop.scene", centroid);
						addStreetFurniture(streetFurniture2, lineRoad, scene);
					
					}
				
				}
				else if (enter == -1){
					StreetFurniture streetFurniture = addSigns(lineRoad,roadBool,"Models/RoadSigns/squarePlatesWithPole/OneWayStreet2/OneWayStreet2.scene", centroid);
					addStreetFurniture(streetFurniture, lineRoad, scene);
				}
				else if (enter == 0){
					if (Integer.parseInt(lineRoad.getImportance()) - greatest == 1 ){
						StreetFurniture streetFurniture2 = addSigns(lineRoad,roadBool,"Models/RoadSigns/otherSigns/Yield/Yield.scene", centroid);
						addStreetFurniture(streetFurniture2, lineRoad, scene);
					}
					else if (Integer.parseInt(lineRoad.getImportance()) - greatest > 1){
						StreetFurniture streetFurniture2 = addSigns(lineRoad,roadBool,"Models/RoadSigns/otherSigns/Stop/Stop.scene", centroid);
						addStreetFurniture(streetFurniture2, lineRoad, scene);
					
					}
				}				
			}
			
			break;
			
		default:
			System.out.println("Intersection de mauvais type");
			break;
		}
	
	
	}
}

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
	public void intersectSigns(IntersectionColl interColl, Scene scene){
		
		// We get thelist of roads existing in the scene
		Map <String,Road> roads = scene.getRoads();
		
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
				StreetFurniture streetFurniture = addSigns(road,bool,"Models/RoadSigns/squarePlatesWithPole/DeadEndStreet/DeadEndStreet.scene");
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
					StreetFurniture streetFurniture = addSigns(roadsTab[larger],roadsBoolTab[larger],"Models/RoadSigns/dangerSigns/RoadNarrows/roadNarrows.scene");
					addStreetFurniture(streetFurniture, roadsTab[larger], scene);
				}
				
				// One-way sign installation
				Map<Integer,Integer> sensMap = directionVariation(roadsTab,roadsBoolTab);
				
				if (sensMap != null){
					
					if(sensMap.containsKey(-1)){
						StreetFurniture streetFurniture = addSigns(roadsTab[sensMap.get(-1)],roadsBoolTab[sensMap.get(-1)],"Models/RoadSigns/squarePlatesWithPole/OneWayStreet2/OneWayStreet2.scene");
						addStreetFurniture(streetFurniture, roadsTab[sensMap.get(-1)], scene);
					}
					
					if(sensMap.containsKey(1)){
						StreetFurniture streetFurniture = addSigns(roadsTab[sensMap.get(1)],roadsBoolTab[sensMap.get(1)],"Models/RoadSigns/prohibitions/Do-not-enter/Do-not-enter.scene");
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
				Boolean asRoundAbout = isRoundAbout(roadsTab,roadsBoolTab,intersect,size);
				
				//Workflow following result
				
				if(asRamp){
					addRampSigns(roadsTab,roadsBoolTab,intersect,size);
				}
				else if (asRoundAbout){
					addRoundAbout(roadsTab,roadsBoolTab,intersect,size);
				}
				else {
					//Checking Road importance for yield
					int[] importTab = checkImportance(roadsTab,size);
					int intersectType = calcIntersectionType(roadsTab,roadsBoolTab,intersect,size,importTab);
					addMultiSigns(roadsTab,roadsBoolTab,intersect,importTab,size,intersectType, scene);
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

				LineRoad lineRoad; 
				boolean roadBool;
				int enter = -100; 
				
				for (String road : intersect.getRoadId().keySet()){
					lineRoad = (LineRoad) roads.get(road);
					roadBool = intersect.getRoadId().get(road);
					enter = isEntering(lineRoad, roadBool);
				}
				
				if (enter == 1){
					
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
			if (road.getSF().size() != 0){
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
	private StreetFurniture signPosition(LineRoad road, boolean left, int position, String folder){

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
		Coordinate newCoord = new Coordinate(newX, newZ, road.getZ_ini());
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
	private StreetFurniture addSigns(LineRoad road, boolean init, String folder){
		
		// The signs which has to be on the right of the road 
		String deadEndStreet = "Models/RoadSigns/squarePlatesWithPole/DeadEndStreet/DeadEndStreet.scene";
		String oneWayStreet = "Models/RoadSigns/squarePlatesWithPole/OneWayStreet2/OneWayStreet2.scene";
		String doNotEnter = "Models/RoadSigns/prohibitions/Do-not-enter/Do-not-enter.scene";
		
		// We determine if the sign has to be on the right side or the left side of the road 
		boolean left = true;
		
		if (folder == deadEndStreet || folder == oneWayStreet || folder == doNotEnter){
			left = false;
		}
		
		// We create the sign 
		if (init){
			// It is possible to return the sign angle in street furniture
			return signPosition(road, left, 0, folder);
				
		}
		else{
			return signPosition(road, left, road.getGeom().getCoordinates().length-1, folder);
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
		// Si size = 3, on peut faire un test pour retourner autre que false 
		if (size !=3){return false;}
		else {
			for (LineRoad road : roadsTab){
				if(road.getNature() == "Bretelle")
				{return true;}
			}
		}
		return false;
	}

	/**
	 * Check if it is a roundabout
	 * 
	 * @param roadsTab the table containing the roads
	 * @param roadsBoolTab the table containing the roads boolean descritor of direction
	 * @param intersect the intersection considered
	 * @param size Intersection's size
	 * 
	 * @return boolean, true if the intersection is in a roundabout
	 */
	private boolean isRoundAbout(LineRoad[] roadsTab, Boolean[] roadsBoolTab, Intersection intersect, int size){
		for (LineRoad road : roadsTab){
			if(road.getName().substring(0, 1)==("PL") || road.getName().substring(0, 3) == "RPT")
			{return true;}
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
		
	}
	
	/**
	 * Adds signs corresponding to a roundabout
	 * 
	 * @param roadsTab the table containing the roads
	 * @param roadsBoolTab the table containing the roads boolean descritor of direction
	 * @param intersect the intersection considered
	 * @param size Intersection's size
	 */
	private void addRoundAbout(LineRoad[] roadsTab, Boolean[] roadsBoolTab, Intersection intersect, int size){
		
	}
	
	/**
	 * Check if there is an importance change between roads
	 * 
	 * @param roadsTab the table containing the roads
	 * @param size
	 * 
	 * @return table of int, 1 for bigger, 0 for lesser importance, null if same importance.
	 */
	private int[] checkImportance(LineRoad[] roadsTab, int size) {
		boolean difference = false;
		int greatest = Integer.parseInt(roadsTab[0].getImportance());
		for (int i=0; i<size; i++){
				if (greatest < Integer.parseInt(roadsTab[i].getImportance())){
					greatest = Integer.parseInt(roadsTab[i].getImportance());
					difference = true;
				}
			}
		if (difference){
			int[] tabImp= new int[size];
			for (int i=0; i<size; i++){
					if (Integer.parseInt(roadsTab[i].getImportance()) == greatest){
						tabImp[i]=1;
					}
					else{
						tabImp[i]=0;
						}
				}
			return tabImp;
			}
		return null;
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
	private int calcIntersectionType(LineRoad[] roadsTab, Boolean[] roadsBoolTab,
									 Intersection intersect, int size,
									 int[] importTab) {
		//Si plus de 3 voies à une route => feux.
		for(int i=0; i < size; i++){
			if (roadsTab[i].getLaneNumber()>3){
				return 0;
			}
		}
		return 0;
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
	private void addMultiSigns(LineRoad[] roadsTab, Boolean[] roadsBoolTab,
												Intersection intersect,
												int[] importance,
												int size, int intersectType, Scene scene) {
		switch (intersectType) {
		case 0:
			for (int i=0; i < size; i++){
				if (isEntering(roadsTab[i], roadsBoolTab[i]) != -1){
					StreetFurniture lightRoad = addSigns(roadsTab[i], roadsBoolTab[i], "");
					addStreetFurniture(lightRoad, roadsTab[i], scene);
				}
			}
			break;
		case 1:
			
			break;
		case 2:
				
			break;
		case 3:
			
			break;
		default:
			System.out.println("Intersection de mauvias type");
			break;
		}
	}
	

}

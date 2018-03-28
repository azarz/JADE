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
 * Rule is the class implementing the rules that are used to place a punctual object
 * 
 * @author JADE
 */

public class Rule implements IRule{
	
	/*
	 * CAS PARTICULIERS : BRETELLE, ROND POINT (ATTENTION,toujours test de sens)
	 */	

// ========================== ATTRIBUTES ===========================


// ========================== CONSTRUCTORS =========================	


// ========================== GETTERS/SETTERS ======================


// ========================== METHODS ==============================

// ------------------------- INTERFACE -----------------------------
	
	/**
	 * Puts signs on intersections referring to the number of roads in the intersection
	 * 
	 * Example : stop, one way, ...
	 * 
	 * @param interColl the intersections presents in RGE data
	 */
	public void intersectSigns(IntersectionColl interColl, Scene scene){
		
		Map <String,Road> roads = scene.getRoads();
		// We go through all the intersections
		for (Intersection intersect : interColl.getMapIntersection().values()){
			
			// We test how many roads are contained by the intersection
			if (intersect.getRoadId().size() == 1){
				/*
				 * => DeadEndStreet sign
				 */
				
				// Roads recuperation
				LineRoad road = (LineRoad) roads.get(intersect.getRoadId().keySet().toArray()[0]);
				boolean bool = intersect.getRoadId().get(intersect.getRoadId().keySet().toArray()[0]);
				
				// DeadEndStreet Sign installation
				StreetFurniture streetFurniture = addSigns(road,bool,"Models/RoadSigns/squarePlatesWithPole/DeadEndStreet/DeadEndStreet.jpg");
				addStreetFurniture(streetFurniture, road, scene);
			}
			
			else if (intersect.getRoadId().size() == 2){
				/*
				 * => Rétrécissement
				 * => Test de sens
				 * => ou rien
				 */
				
				// Roads recuperation
				LineRoad[] roadsTab = new LineRoad[2];
				Boolean[] roadsBoolTab = new Boolean[2];
				int k = 0;
				
				for (String road : intersect.getRoadId().keySet()){
					roadsTab[k] = (LineRoad) roads.get(road);
					roadsBoolTab[k] = intersect.getRoadId().get(road);
					k++;
				}
				
				// "Diminution largeur" sign installation
				int larger = widthComparison(roadsTab[0],roadsTab[1]);
				
				if ( larger != -1){
					StreetFurniture streetFurniture = addSigns(roadsTab[larger],roadsBoolTab[larger],"Models/RoadSigns/dangerSigns/RoadNarrows/roadNarrows.jpg");
					addStreetFurniture(streetFurniture, roadsTab[larger], scene);

				}
				
				// "Sens unique/Sens interdit" sign installation
				Map<Integer,Integer> sensMap = directionVariation(roadsTab,roadsBoolTab);
				
				if (sensMap != null){
					
					if(sensMap.containsKey(-1)){
						StreetFurniture streetFurniture = addSigns(roadsTab[sensMap.get(-1)],roadsBoolTab[sensMap.get(-1)],"Models/RoadSigns/squarePlatesWithPole/OneWayStreet2/OneWayStreet2.jpg");
						addStreetFurniture(streetFurniture, roadsTab[sensMap.get(-1)], scene);
					}
					
					if(sensMap.containsKey(1)){
						StreetFurniture streetFurniture = addSigns(roadsTab[sensMap.get(1)],roadsBoolTab[sensMap.get(1)],"Models/RoadSigns/prohibitions/Do-not-enter/Do-not-enter.jpg");
						addStreetFurniture(streetFurniture, roadsTab[sensMap.get(1)], scene);
					}
				}
			}
			else if (intersect.getRoadId().size() == 3 || intersect.getRoadId().size() == 4){
				/*
				 * - Si 3-4 => Test Bretelle
				 *        => Test Rond point
				 * 		  => Test de sens
				 * 		  => Test d'importance
				 * 		  => Test nombre de voies
				 * 		  => Algo de placement de signalisation en fonction des résultats obtenus
				 */
				
			}
			else if (intersect.getRoadId().size() >= 5){
				/*
				 * - Si 5 ou plus 
				 *        => Test de sens 
				 *        => Cédez le passage et flèches bleu pour modéliser un rond point 
				 */
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
	
//	------------------------- GENERALS -----------------------------
	
	/**
	 * Gives the direction of the road compared to the intersection
	 * 
	 * @param intersection the intersection
	 * @param i the index of the road
	 * 
	 * @return -1 if leaving, 0 if double-way, +1 if entering 
	 */
	private int getDirection(Road road, Boolean direction){
		
		if(road.getDirection()=="Double"){return 0;}
		int modDirection=1;
		if (road.getDirection()=="Inverse"){modDirection=-1;}
		else if(direction) {return -1*modDirection;}
		else if(!direction) {return 1*modDirection;}
		
		return 0;
	}
	
	/**
	 * 
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
	 * 
	 * @param road
	 * @param left
	 * 
	 * @return
	 */
	private Coordinate signPosition(LineRoad road, boolean left){

		Coordinate[] coord = road.getGeom().getCoordinates();
		
		double x = coord[0].x;
		double y = coord[0].y;
		
		// 5meters after the beginning of the road
		double d = 5;
		// 0.70 meters after the border of the road
		double D = road.getWidth()/2 + 0.7;
		
		double newX;
		double newZ;

		double theta = JadeUtils.roadAngle(road);
		
		if(left){
			if (0<= theta && theta <= Math.PI/2){
				newX = x + d*Math.cos(theta) - D*Math.sin(theta);
				newZ = y + d*Math.sin(theta) + D*Math.cos(theta);
			}
			else if (theta> 3*Math.PI/2 && theta <= 2*Math.PI){
				newX = x + d*Math.cos(2*Math.PI - theta) + D*Math.sin(2*Math.PI - theta);
				newZ = y - d*Math.sin(2*Math.PI - theta) + D*Math.cos(2*Math.PI - theta);
			}
			else if (theta > Math.PI/2 && theta <= Math.PI){
				newX = x - d*Math.cos(Math.PI - theta) - D*Math.sin(Math.PI - theta);
				newZ = y + d*Math.sin(Math.PI - theta) - D*Math.cos(Math.PI - theta);
			}
			else{
				newX = x - d*Math.cos(theta - Math.PI) + D*Math.sin(theta - Math.PI);
				newZ = y - d*Math.sin(theta - Math.PI) - D*Math.cos(theta - Math.PI);
			}
		}
		else{
			if (0<= theta && theta <= Math.PI/2){
				newX = x + d*Math.cos(theta) + D*Math.sin(theta);
				newZ = y + d*Math.sin(theta) - D*Math.cos(theta);
			}
			else if (theta> 3*Math.PI/2 && theta <= 2*Math.PI){
				newX = x + d*Math.cos(2*Math.PI - theta) - D*Math.sin(2*Math.PI - theta);
				newZ = y - d*Math.sin(2*Math.PI - theta) - D*Math.cos(2*Math.PI - theta);
			}
			else if (theta > Math.PI/2 && theta <= Math.PI){
				newX = x - d*Math.cos(Math.PI - theta) + D*Math.sin(Math.PI - theta);
				newZ = y + d*Math.sin(Math.PI - theta) + D*Math.cos(Math.PI - theta);
			}
			else{
				newX = x - d*Math.cos(theta - Math.PI) - D*Math.sin(theta - Math.PI);
				newZ = y - d*Math.sin(theta - Math.PI) + D*Math.cos(theta - Math.PI);
			}
		}
		
		return new Coordinate(newX, newZ, road.getZ_ini());
	}
	
	/**
	 * 
	 * @param road
	 * @param init
	 * @param folder
	 * 
	 * @return
	 */
	private StreetFurniture addSigns(LineRoad road, boolean init, String folder){
		
		if (init){
			boolean left = false;
			// test folder .. pour droite ou gauche ! 
			
			Coordinate coord = signPosition(road, left);
			// It is possible to return the sign angle in street furniture
			return new StreetFurniture(folder, coord);
		}
		
		else{
			return null;
		}
	}
	
// -------------------------- 2-SPECIFIC ---------------------------
	
	/**
	 * Compares the width of two roads and returns the largest one,
	 * and null if the roads have the same width
	 * 
	 * @param road1 first road
	 * @param road2 second road
	 * @return the largest road of them both
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
	 * @return the map with intersections
	 */
	private Map<Integer,Integer> directionVariation(Road[] roadsTab, Boolean[] roadsBoolTab){
		
		int dir1 = getDirection(roadsTab[0],roadsBoolTab[0]);
		int dir2 = getDirection(roadsTab[1],roadsBoolTab[1]);
		
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

}

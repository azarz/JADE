package eu.ensg.jade.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;

import eu.ensg.jade.geometricObject.Road;
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
	public void intersectSigns(IntersectionColl interColl,Map <String,Road> roads){
		
		// We go through all the intersections
		for (Intersection intersect : interColl.getMapIntersection().values()){
			
			// We test how many roads are contained by the intersection
			if (intersect.getRoadId().size() == 1){
				/*
				 * - Si 1 => cul de sac 
				 * 
				 * On ajoute le panneau à la route (ID) ? 
				 * 		OUI car on a besoin pour verifier les conflits
				 * On ajoute le panneau à la scène (OBject) ? 
				 * 		OUI car on a besoin pour la creation du xml ?? 
				 * 
				 * Fct => addDeadEndSign()
				 * 
				 */
				LineRoad road = (LineRoad) roads.get(intersect.getRoadId().keySet().toArray()[0]);
				boolean bool = intersect.getRoadId().get(intersect.getRoadId().keySet().toArray()[0]);
				
				StreetFurniture streetFurniture = addSigns(road,bool,"Models/RoadSigns/squarePlatesWithPole/DeadEndStreet/DeadEndStreet.jpg");
				addStreetFurniture(streetFurniture);
			}
			else if (intersect.getRoadId().size() == 2){
				/*
				 * - Si 2 => Rétrécissement
				 * 		  => Test de sens
				 *        => ou rien
				 */
//				List<Integer> directions = new ArrayList<Integer>();
//				for(int i = 0; i < intersect.getRoadId().size(); i++){
//					directions.add(getDirection(intersect, i));						
//					}
//				if(Collections.frequency(directions,-1) == 1){
//					directions.indexOf(-1);
//					StreetFurniture streetFurniture = addOneWaySign();
//					addStreetFurniture(streetFurniture);
//				}
//				else if(Collections.frequency(directions,1) == 1){
//					directions.indexOf(1);
//					StreetFurniture streetFurnitureOw = addOneWaySign();
//					addStreetFurniture(streetFurnitureOw);
//					StreetFurniture streetFurnitureFw = addForbiddenWaySign();
//					addStreetFurniture(streetFurnitureFw);
//				}
				Road road1 = roads.get(intersect.getRoadId().get(0));
				Road road2 = roads.get(intersect.getRoadId().get(1));
				Road larger = widthComparison(road1,road2);
				if ( larger != null){
					addSigns(larger,"");
				}
				Map<Integer,Road> sensMap = sensComparison(road1,road2);
				if (sensMap != null){
					if(sensMap.containsKey(-1)){
						addSigns(sensMap.get(-1),"");
					}
					if(sensMap.containsKey(1)){
						addSigns(sensMap.get(1),"");
					}
				}
			}
			else if (intersect.getRoadId().size() == 3 || intersect.getRoadId().size() == 4){
				/*
				 * - Si 3 => Test Bretelle
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
	 * 
	 * @param intersection
	 * @param i
	 * @return -1 if leaving, 0 if double-way, +1 if entering 
	 */
	private int getDirection(Intersection intersection, int i){
		//TODO
		return 0;
	}
	
	/**
	 * 
	 */
	private void addStreetFurniture(StreetFurniture streetFurniture){
		//if(not nul..)
		// Test si null
		// Test si sur la route
		// Ajoute sur la route si non
		// Ajoute sur la scène
	}
	
	/**
	 * 
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
	private Road widthComparison(Road road1, Road road2){
		return null;
	}
	
	private Map<Integer,Road> sensComparison(Road road1, Road road2){
		return null;	
	}
	
	
}

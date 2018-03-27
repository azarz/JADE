package eu.ensg.jade.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;

import eu.ensg.jade.geometricObject.Road;
import eu.ensg.jade.semantic.Intersection;
import eu.ensg.jade.semantic.IntersectionColl;
import eu.ensg.jade.semantic.StreetFurniture;
import eu.ensg.jade.semantic.SurfaceVegetation;

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
				StreetFurniture streetFurniture = addSigns(roads.get(intersect.getRoadId().get(0)),"");
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
		
	}
	
	/**
	 * 
	 */
	private Coordinate signPosition(){
		// Calcul de la position du panneau sur le bord de route par rapport au DTM et au buffer
		// return un objet Coordinate ?
		
		
		return null;
	}
	
	
	
	private StreetFurniture addSigns(Road road, String folder){
		Coordinate coord = null;
		return new StreetFurniture(folder, coord);
	}
// -------------------------- 2-SPECIFIC ---------------------------
	private Road widthComparison(Road road1, Road road2){
		return null;
	}
	
	private Map<Integer,Road> sensComparison(Road road1, Road road2){
		return null;	
	}
	
	
}

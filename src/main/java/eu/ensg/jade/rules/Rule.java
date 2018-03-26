package eu.ensg.jade.rules;

import java.util.List;
import java.util.Map;

import eu.ensg.jade.geometricObject.Road;
import eu.ensg.jade.semantic.Intersection;
import eu.ensg.jade.semantic.IntersectionColl;
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

	/**
	 * Puts signs on intersections referring to the number of roads in the intersection
	 * 
	 * Example : stop, one way, ...
	 * 
	 * @param interColl the intersections presents in RGE data
	 */
	public void intersectSigns(IntersectionColl interColl){
		
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
				 */
			}
			else if (intersect.getRoadId().size() == 2){
				/*
				 * - Si 2 => Rétrécissement
				 * 		  => Test de sens
				 *        => ou rien
				 */
			}
			else if (intersect.getRoadId().size() == 3){
				/*
				 * - Si 3 => Test Bretelle
				 *        => Test Rond point
				 * 		  => Test de sens
				 * 		  => Test d'importance
				 * 		  => Test nombre de voies
				 * 		  => Algo de placement de signalisation en fonction des résultats obtenus
				 */
			}
			else if (intersect.getRoadId().size() == 4){
				/*
				 * 	- Si 4 => Test Rond point
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
	
	/**
	 * 
	 */
	public void signPosition(){
		// Calcul de la position du panneau sur le bord de route par rapport au DTM et au buffer
		// return un objet Coordinate ? 
	}
	
	/**
	 * 
	 */
	public void addDeadEndSign(){
		// Ajout du panneau par rapport au début de la route
		// A quelle distance ? 
		// NOTE : on est forcement à l'intersection de fin de route 
		// ==> On doit placer le panneau au debut .. 
		// ie on doit tester si il y a déjà un panneau pour ne pas causer de conflit ! 
	}
}

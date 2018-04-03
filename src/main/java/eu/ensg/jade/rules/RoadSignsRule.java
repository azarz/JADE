package eu.ensg.jade.rules;

import java.util.Map;

import eu.ensg.jade.geometricObject.Road;
import eu.ensg.jade.scene.Scene;

/**
 * 
 * @author JADE
 */

public class RoadSignsRule implements RuleShape {
	
// ========================== METHODS ==============================

	/**
	 * Puts signs on roads
	 * 
	 * Example : pedestrian crossing, speed limitation
	 * 
	 * @param roads the roads present in RGE data
	 */	
	@Override
	public void addPunctualObject(Scene scene) {
			Map<String, Road> roads = scene.getRoads();
			// We go through all the roads
			for(Road road : roads.values()){
				
			}

		}		
	}



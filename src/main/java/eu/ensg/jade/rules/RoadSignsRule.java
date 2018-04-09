package eu.ensg.jade.rules;

import java.util.Map;

import eu.ensg.jade.scene.Scene;
import eu.ensg.jade.semantic.SurfaceRoad;

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
			Map<String, SurfaceRoad> roads = scene.getSurfaceRoads();
			// We go through all the roads
			for(SurfaceRoad road : roads.values()){
				
			}

		}		
	}



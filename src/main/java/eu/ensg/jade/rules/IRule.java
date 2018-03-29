package eu.ensg.jade.rules;

import java.util.List;
import java.util.Map;

import eu.ensg.jade.geometricObject.Road;
import eu.ensg.jade.scene.Scene;
import eu.ensg.jade.semantic.IntersectionColl;
import eu.ensg.jade.semantic.SurfaceVegetation;

/**
 * Rule is the interface proposing the rules that are used to place a punctual object
 * 
 * @author JADE
 */

public interface IRule {
	
// ========================== METHODS ==============================

	/**
	 * Puts signs on intersections referring to the number of roads in the intersection
	 * 
	 * Example : stop, one way, ...
	 * 
	 * @param interColl the intersections presents in RGE data
	 */
	public void intersectSigns(Scene scene);
	
	/**
	 * Puts signs on roads
	 * 
	 * Example : pedestrian crossing, speed limitation
	 * 
	 * @param roads the roads present in RGE data
	 */
	public void roadSigns(Map<String, Road> roads);
	
	/**
	 * Puts vegetation on vegetation area
	 * 
	 * @param vegetation the list of surface of vegetation in RGE data
	 */
	public void addVegetation(List<SurfaceVegetation> vegetation);
	

}

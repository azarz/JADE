package eu.ensg.jade.rules;

import java.util.Map;

import eu.ensg.jade.geometricObject.Road;
import eu.ensg.jade.semantic.IntersectionColl;

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
	public void intersectSigns(IntersectionColl interColl);
	
	/**
	 * Puts signs on roads
	 * 
	 * Example : pedestrian crossing, speed limitation
	 * 
	 * @param interColl the intersections presents in RGE data
	 */
	public void roadSigns(Map<String, Road> roads);
	

}

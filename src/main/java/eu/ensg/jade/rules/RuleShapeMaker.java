package eu.ensg.jade.rules;

import eu.ensg.jade.scene.Scene;

/**
 * 
 * @author JADE
 */

public class RuleShapeMaker {
	
// ========================== ATTRIBUTES ===========================
	
	/**
	 * 
	 */
	private RuleShape intersectionSigns;
	
	/**
	 * 
	 */
	private RuleShape roadSigns;
	
	/**
	 * 
	 */
	private RuleShape vegetation;
	
// ========================== CONSTRUCTORS =========================
	
	/**
	 * Default constructor
	 */
	public RuleShapeMaker(){
		intersectionSigns = new IntersectionSignsRule();
		roadSigns = new RoadSignsRule();
		vegetation = new VegetationRule();
	}
	
// ========================== METHODS ==============================

	/**
	 * 
	 * @param scene
	 */
	public void addIntersectionSigns(Scene scene){
		intersectionSigns.addPunctualObject(scene);
	}
	
	/**
	 * 
	 * @param scene
	 */
	public void addRoadSigns(Scene scene){
		roadSigns.addPunctualObject(scene);
	}
	
	/**
	 * 
	 * @param scene
	 */
	public void addVegetation(Scene scene){
		vegetation.addPunctualObject(scene);
	}
	
}

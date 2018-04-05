package eu.ensg.jade.rules;

import java.io.IOException;

import org.geotools.feature.SchemaException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import eu.ensg.jade.scene.Scene;

/**
 * 
 * @author JADE
 */

public class RuleShapeMaker {
	
// ========================== ATTRIBUTES ===========================
	
	/**
	 * Every road signs that are not human crossing and speed limits
	 */
	private RuleShape intersectionSigns;
	
	/**
	 * Human crossing and speed limits
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
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 * @throws IOException 
	 * @throws SchemaException 
	 */
	public void addIntersectionSigns(Scene scene) throws NoSuchAuthorityCodeException, FactoryException, SchemaException, IOException{
		intersectionSigns.addPunctualObject(scene);
	}
	
	/**
	 * 
	 * @param scene
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 * @throws IOException 
	 * @throws SchemaException 
	 */
	public void addRoadSigns(Scene scene) throws NoSuchAuthorityCodeException, FactoryException, SchemaException, IOException{
		roadSigns.addPunctualObject(scene);
	}
	
	/**
	 * 
	 * @param scene
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 * @throws IOException 
	 * @throws SchemaException 
	 */
	public void addVegetation(Scene scene) throws NoSuchAuthorityCodeException, FactoryException, SchemaException, IOException{
		vegetation.addPunctualObject(scene);
	}
	
}

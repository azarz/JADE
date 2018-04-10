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
	 * Adds intersection signs to a scene
	 * 
	 * @param scene The scene where to add intersection signs
	 * @throws FactoryException Throws FactoryException
	 * @throws NoSuchAuthorityCodeException Throws NoSuchAuthorityCodeException
	 * @throws IOException Throws IOException
	 * @throws SchemaException Throws SchemaException
	 */
	public void addIntersectionSigns(Scene scene) throws NoSuchAuthorityCodeException, FactoryException, SchemaException, IOException{
		intersectionSigns.addPunctualObject(scene);
	}
	
	/**
	 * Adds Road Signs to a scene
	 * 
	 * @param scene The scene where to add intersection signs
	 * @throws FactoryException Throws FactoryException
	 * @throws NoSuchAuthorityCodeException Throws NoSuchAuthorityCodeException
	 * @throws IOException Throws IOException
	 * @throws SchemaException Throws SchemaException
	 */
	public void addRoadSigns(Scene scene) throws NoSuchAuthorityCodeException, FactoryException, SchemaException, IOException{
		roadSigns.addPunctualObject(scene);
	}
	
	/**
	 * Adds vegetation to the scene
	 * 
	 * @param scene The scene where to add intersection signs
	 * @throws FactoryException Throws FactoryException
	 * @throws NoSuchAuthorityCodeException Throws NoSuchAuthorityCodeException
	 * @throws IOException Throws IOException
	 * @throws SchemaException Throws SchemaException
	 */
	public void addVegetation(Scene scene) throws NoSuchAuthorityCodeException, FactoryException, SchemaException, IOException{
		vegetation.addPunctualObject(scene);
	}
	
}

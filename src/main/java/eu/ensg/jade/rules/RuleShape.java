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

public interface RuleShape {
	
// ========================== METHODS ==============================

	/**
	 * Adds punctual objects to the scene
	 * 
	 * @param scene The scene where to add punctual object
	 * 
	 * @throws FactoryException Throws FactoryException
	 * @throws NoSuchAuthorityCodeException Throws NoSuchAuthorityCodeException
	 * @throws IOException Throws IOException
	 * @throws SchemaException Throws SchemaException
	 * 
	 */
	public void addPunctualObject(Scene scene) throws NoSuchAuthorityCodeException, FactoryException, SchemaException, IOException;
}

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
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 * @throws IOException 
	 * @throws SchemaException 
	 * 
	 */
	public void addPunctualObject(Scene scene) throws NoSuchAuthorityCodeException, FactoryException, SchemaException, IOException;
}

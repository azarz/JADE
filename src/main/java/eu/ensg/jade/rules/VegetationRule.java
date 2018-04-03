package eu.ensg.jade.rules;

import java.util.List;

import eu.ensg.jade.scene.Scene;
import eu.ensg.jade.semantic.SurfaceVegetation;

/**
 * 
 * @author JADE
 */

public class VegetationRule implements RuleShape {

// ========================== METHODS ==============================
	/**
	 * Puts vegetation on vegetation area
	 * 
	 * @param vegetation the list of surface of vegetation in RGE data
	 */
	@Override
	public void addPunctualObject(Scene scene) {
		/*			
		// We go through all the surfaces
		for(int i = 0 ; i < vegetation.size() ; i++){
			// Test - ou est la surface ? (Bords de route ou parc ?) 
			//      - dimension de la surface 
			// = > On place ponctuellement les arbres a espacement réguliers le long des routes 
			//          OU on fait un fouilli d'arbre sur une grosse zone de forêt
		}*/
	}		
}

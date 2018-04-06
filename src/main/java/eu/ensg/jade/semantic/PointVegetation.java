package eu.ensg.jade.semantic;

import com.vividsolutions.jts.geom.Coordinate;

import eu.ensg.jade.geometricObject.PunctualObject;
import eu.ensg.jade.rules.VegetationRule.TREE;

/**
 * Vegetation is the class implementing the vegetation to be added to the scene
 * 
 * @author JADE
 */

public class PointVegetation extends PunctualObject {
	
// ========================== ATTRIBUTES ===========================

	/**
	 * The tree nature path 
	 */
	private String nature;
	
// ========================== CONSTRUCTORS ===========================

	/**
	 * 
	 * @param coord
	 * @param nature
	 */
	public PointVegetation(Coordinate coord,TREE tree) {
		super(coord);
		choiceTree(tree);
	}
	
// ========================== GETTERS/SETTERS ======================
	
	/**
	 * 
	 * @return
	 */
	public String getNature() {
		return nature;
	}

	/**
	 * 
	 * @param nature the tree nature path
	 */
	public void setNature(String nature) {
		this.nature = nature;
	}
	
// ========================== METHODS ==============================
	
	/**
	 * Gives the right path for the called tree
	 * 
	 * @param choice the id of the tree
	 * 
	 * @return the path of the chosen tree
	 */
	private String choiceTree(TREE tree){
		
		switch(tree)
		{
			case CHESTNUT: return "";
			case PINE: return "";
			case SASSAFRA: return "";
			case SYCAMORE: return "";
			default:
				return null;
		}		
	}
}

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
	 * Constructor using all fields
	 *  
	 * @param coord The coordinate of the vegetation
	 * @param tree The tree for this vegetation point
	 */
	public PointVegetation(Coordinate coord,TREE tree) {
		super(coord);
		choiceTree(tree);
	}
	
// ========================== GETTERS/SETTERS ======================
	
	/**
	 * 
	 * @return Then nature of the vegetation
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
	private void choiceTree(TREE tree){
		
		switch(tree)
		{
			case DECIDUOUS:
				this.nature = "Models/Trees/deciduousTree/tree.obj";
				break;
			default:
				this.nature = "";
		}		
	}
}

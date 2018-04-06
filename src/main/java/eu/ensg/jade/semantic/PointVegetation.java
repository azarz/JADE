package eu.ensg.jade.semantic;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

import eu.ensg.jade.geometricObject.PunctualObject;
import eu.ensg.jade.input.BuildingSHP;
import eu.ensg.jade.input.DTMReader;
import eu.ensg.jade.input.HydrographySHP;
import eu.ensg.jade.input.LineRoadSHP;
import eu.ensg.jade.input.VegetationSHP;
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
	private void choiceTree(TREE tree){
		
		switch(tree)
		{
			case DECIDUOUS: this.nature = "Models/Trees/deciduousTree/tree.obj";
			default:
				this.nature = "";
		}		
	}
	
	/**
	 * @see eu.ensg.jade.geometricObject.Point#toOBJ(java.util.List, double, double)
	 */
	@Override
	public String toOBJ(List<Integer> indexOffsets, double xOffset, double yOffset) {
		// TODO Auto-generated method stub
		return null;
	}
}

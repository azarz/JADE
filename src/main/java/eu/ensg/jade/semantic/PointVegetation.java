package eu.ensg.jade.semantic;

import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;

import eu.ensg.jade.geometricObject.PunctualObject;

/**
 * Vegetation is the class implementing the vegetation to be added to the scene
 * 
 * @author JADE
 */

public class PointVegetation extends PunctualObject {
	
// ========================== ATTRIBUTES ===========================

	/**
	 * The furniture nature 
	 */
	private String nature;
	
// ========================== CONSTRUCTORS ===========================

	public PointVegetation(Coordinate coord) {
		super(coord);
	}
	
// ========================== GETTERS/SETTERS ======================
	public String getNature() {
		return nature;
	}


	public void setNature(String nature) {
		this.nature = nature;
	}
	
	
// ========================== METHODS ==============================
	
	
	/**
	 * @see eu.ensg.jade.geometricObject.Point#toOBJ(java.util.List, double, double)
	 */
	@Override
	public String toOBJ(List<Integer> indexOffsets, double xOffset, double yOffset) {
		// TODO Auto-generated method stub
		return null;
	}


	

}

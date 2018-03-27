package eu.ensg.jade.geometricObject;

import java.util.List;

import com.jme3.app.R.string;

/**
 * Point is the class implementing the punctual objects to be added to the scene
 * 
 * @author JADE
 */

public abstract class PunctualObject extends WorldObject {

// ========================== ATTRIBUTES ===========================
	
	/** 
	 * The punctual object nature 
	 */
	private string nature;
	
	/**
	 * This method will have a return that will soon be specified
	 * 
	 * @see eu.ensg.jade.geometricObject.WorldObject#toOBJ(java.util.List)
	 */
	@Override
	public abstract String toOBJ(List<Integer> indexOffsets, double xOffset, double yOffset);
}

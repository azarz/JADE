package eu.ensg.jade.geometricObject;

import java.util.List;

import com.jme3.app.R.string;

import eu.ensg.jade.output.ObjectVisitor;

/**
 * Point is the class implementing the punctual objects to be added to the scene
 * 
 * @author JADE
 */

public abstract class Point extends WorldObject {

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
	public String toOBJ(List<Integer> indexOffsets) {
		// TODO Auto-generated method stub
		return null;
	}

}

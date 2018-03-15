package eu.ensg.jade.semantic;

import java.util.List;

import eu.ensg.jade.geometricObject.ISurfacicObject;

/**
 * DTM is the class implementing a DTM
 * 
 * @author JADE
 */

public class DTM implements ISurfacicObject {
	
// ========================== ATTRIBUTES ===========================
	
	/**
	 * The table associates to the DTM
	 */
	private double[][] tabDTM;
	
// ========================== CONSTRUCTORS =========================	

	/**
	 * Constructor using all fields
	 * 
	 * @param tabDTM the table associates to the DTM
	 */
	public DTM(double[][] tabDTM) {
		this.tabDTM = tabDTM;
	}

// ========================== METHODS ==============================

	/**
	 * Allows to add an elevation to a flat ground
	 */
	@Override
	public void addHeight() {
		
	}
	
	/**
	 * This method will have a return that will soon be specified
	 */
	public void toOBJ(){
		
	}

}

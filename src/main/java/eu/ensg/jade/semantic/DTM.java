package eu.ensg.jade.semantic;

import java.util.Map;

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
	
	/**
	 * The header containing the metadata of the DTM (same order as in the ascii file)*
	 * 
	 * Header Keys : ncols - nrows - xllcorner - yllcorner - cellsize
	 */
	private Map<String,Double> headerDTM;
	
	
// ========================== CONSTRUCTORS =========================	

	/**
	 * Constructor using all fields
	 * 
	 * @param tabDTM the table associates to the DTM
	 */
	public DTM(double[][] tabDTM,Map<String,Double> headerDTM) {
		this.tabDTM = tabDTM;
		this.headerDTM = headerDTM;
	}
	
// ========================== GETTERS/SETTERS ======================
	
	/**
	 * Gets the DTM table
	 * 
	 * @return table of elevations
	 */
	public double[][] getTabDTM() {
		return tabDTM;
	}
	
	/**
	 * Gets the DTM header
	 * 
	 * @return header of the DTM as a KEY/VALUE map
	 */
	public Map<String,Double> getHeaderDTM() {
		return headerDTM;
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

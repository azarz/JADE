package eu.ensg.jade.exception;

import eu.ensg.jade.input.ReaderFactory.READER_METHOD;

/**
 * MethodTypeNotFoundException is the class implementing an exception when a method is not available
 * 
 * @author JADE 
 */

public class MethodTypeNotFoundException extends Exception{
	
// ========================== ATTRIBUTES ===========================

	/**
	 * The serialization ID number
	 */
	private static final long serialVersionUID = 1L;
	
// ========================== METHODS ==============================
	
	/**
	 * Raises an exception when the proposed method does not exist
	 * 
	 * @param method the method to verify
	 */
	public MethodTypeNotFoundException(READER_METHOD method){
		System.out.println("The method "+method+" is not implemented yet");
	}
	
	

}

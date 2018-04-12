package eu.ensg.jade.output;

import java.util.List;

public interface IObjExport {
	
	/**
	 * Creates the corresponding obj string
	 * 
	 * @param indexOffsets The index offset
	 * @param xOffset The Offset along x axis
	 * @param yOffset The Offset along y axis
	 * 
	 * @return The obj string
	 */
	String toOBJ(List<Integer> indexOffsets, double xOffset, double yOffset);

}
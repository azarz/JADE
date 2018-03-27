package eu.ensg.jade.geometricObject;

import java.util.List;



/**
 * WorldObject is an abstract class representing any physical object of the Scene
 * 
 * @author JADE
 *
 */
public abstract class WorldObject {
	
	public abstract String toOBJ(List<Integer> indexOffsets, double xOffset, double yOffset);
	
}

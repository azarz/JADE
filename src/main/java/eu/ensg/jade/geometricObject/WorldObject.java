package eu.ensg.jade.geometricObject;

import eu.ensg.jade.output.ObjectVisitor;

public abstract class WorldObject {
	
	public void acceptVisitor(ObjectVisitor visitor){
		visitor.visit(this);
	}
}

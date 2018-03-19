package eu.ensg.jade.output;

import eu.ensg.jade.geometricObject.WorldObject;

public abstract class ObjectVisitor {
	/*
	 *  Resource for the 'Visitor' design pattern:	https://sourcemaking.com/design_patterns/visitor
	 */
	public abstract void visit(WorldObject e);

}

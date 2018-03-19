package eu.ensg.jade.output;

import java.util.List;

import eu.ensg.jade.geometricObject.WorldObject;
import eu.ensg.jade.semantic.Building;
import eu.ensg.jade.semantic.Hydrography;
import eu.ensg.jade.semantic.PointVegetation;
import eu.ensg.jade.semantic.StreetFurniture;
import eu.ensg.jade.semantic.SurfacicRoad;
import eu.ensg.jade.semantic.SurfacicVegetation;

public abstract class ObjectVisitor {
	/*
	 *  Resource for the 'Visitor' design pattern:	https://sourcemaking.com/design_patterns/visitor
	 */
	public abstract void visit(List<WorldObject> objList);

}

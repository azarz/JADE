package eu.ensg.jade.output;

import java.util.List;

import eu.ensg.jade.geometricObject.WorldObject;
import eu.ensg.jade.semantic.Building;
import eu.ensg.jade.semantic.Hydrography;
import eu.ensg.jade.semantic.VegetationPoint;
import eu.ensg.jade.semantic.StreetFurniture;
import eu.ensg.jade.semantic.ArealRoad;
import eu.ensg.jade.semantic.VegetationArea;

public abstract class ObjectVisitor {
	/*
	 *  Resource for the 'Visitor' design pattern:	https://sourcemaking.com/design_patterns/visitor
	 */
	public abstract void visit(List<WorldObject> objList);

}

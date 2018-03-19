package eu.ensg.jade.output;

import java.util.List;

import eu.ensg.jade.geometricObject.WorldObject;
import eu.ensg.jade.semantic.Building;
import eu.ensg.jade.semantic.Hydrography;
import eu.ensg.jade.semantic.PointVegetation;
import eu.ensg.jade.semantic.StreetFurniture;
import eu.ensg.jade.semantic.SurfacicRoad;
import eu.ensg.jade.semantic.SurfacicVegetation;

/**
 * OBJCreator is the class implementing the creation of obj files for the objects to be added in the scene
 * 
 * @author JADE
 */

public class OBJCreator extends ObjectVisitor {
	
// ========================== METHODS ==============================	
	
	/**
	 * Generates an obj file depending on the object type
	 * 
	 */
	public void objCreator(){
		// TO DO: add a parameter to this function (object type)
	}
	

	@Override
	public void visit(Building building) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void visit(SurfacicRoad road) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void visit(Hydrography hydro) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void visit(StreetFurniture object) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void visit(SurfacicVegetation vegetation) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void visit(PointVegetation vegetation) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void visit(List<WorldObject> objList) {
		// TODO Auto-generated method stub
		
	}

}

package eu.ensg.jade.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import eu.ensg.jade.geometricObject.WorldObject;

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
	public void visit(List<WorldObject> objList) {
		// TODO Auto-generated method stub
		
		List<Integer> offsets = new ArrayList<Integer>();
		offsets.add(1);
		offsets.add(1);
		offsets.add(1);
		
		File file = new File("paris.obj");
		try {
			Files.deleteIfExists(file.toPath());
			
			FileWriter fw = new FileWriter("paris.obj", true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);
			
			
			for (int i = 0; i < objList.size(); i++) {
//				System.out.println(100*i/objList.size() + "%");
				objList.get(i).toOBJ(offsets);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}

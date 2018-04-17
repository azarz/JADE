package eu.ensg.jade.semantic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


import com.vividsolutions.jts.geom.Coordinate;

import eu.ensg.jade.geometricObject.PunctualObject;
import eu.ensg.jade.output.IObjExport;
import eu.ensg.jade.rules.VegetationRule.TREE;

/**
 * Vegetation is the class implementing the vegetation to be added to the scene
 * 
 * @author JADE
 */

public class PointVegetation extends PunctualObject implements IObjExport {
	
// ========================== ATTRIBUTES ===========================

	/**
	 * The tree nature path 
	 */
	private String nature;
	
// ========================== CONSTRUCTORS ===========================

	/**
	 * Constructor using all fields
	 *  
	 * @param coord The coordinate of the vegetation
	 * @param tree The tree for this vegetation point
	 */
	public PointVegetation(Coordinate coord,TREE tree) {
		super(coord);
		choiceTree(tree);
	}
	
// ========================== GETTERS/SETTERS ======================
	
	/**
	 * 
	 * @return Then nature of the vegetation
	 */
	public String getNature() {
		return nature;
	}

// ========================== METHODS ==============================
	
	/**
	 * Gives the right path for the called tree
	 * 
	 * @param choice the id of the tree
	 * 
	 * @return the path of the chosen tree
	 */
	private void choiceTree(TREE tree){
		
		switch(tree)
		{
			case DECIDUOUS:
				this.nature = "Models/Trees/deciduousTree/tree.obj";
				break;
			default:
				this.nature = "";
		}		
	}
	
	/**
	 * Converts a PointVegetation into a string corresponding to the .obj description of its tree
	 * 
	 * @param indexOffsets a list of 3 integers wich correspond to the offset of
	 * 			- vertex index
	 * 			- uv coordinates index
	 * 			- normal coordinates indexs
	 * in the file
	 * @return A string corresponding to the .obj description of the tree
	 */
	public String toOBJ(List<Integer> indexOffsets, double xOffset, double yOffset) {
		
		// Defining a new decimal format in order to have smaller obj files
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		DecimalFormat format = (DecimalFormat)nf;
		format.applyPattern("#.###");
				
		// Fetching the offsets from the offsets parameter (initial it's 0,0,0)
		int vertexIndexOffset  = indexOffsets.get(0);
		int textureIndexOffset = indexOffsets.get(1);
		int normalIndexOffset  = indexOffsets.get(2);
		
		String outputString = "";
		
		// Open the file
        try(FileReader fileOBJ = new FileReader("assets/" + this.nature)){
	        BufferedReader bufferOBJ = new BufferedReader(fileOBJ);
	        String line = bufferOBJ.readLine();
	        String[] lineDecomp, tuple;
	        
	        String newX, newY, newZ, newLine, newVertexIndex, newTextureIndex, newNormalIndex;
	        
			while (line != null) {
			        	
	            lineDecomp = line.split(" ");
	            
	            switch (lineDecomp[0]) {
	            
	            case "mtllib":				
					//set by OBJWriter for all the trees
					break;
	
				case "v":
					newX = format.format(Double.parseDouble(lineDecomp[1]) + this.coord.x);
					newY = format.format(Double.parseDouble(lineDecomp[2]) + this.coord.z);
					newZ = format.format(Double.parseDouble(lineDecomp[3]) + this.coord.y);
					
					newLine = "v" + " " + newX + " " + newY + " " + newZ + "\n";
					outputString += newLine;
					vertexIndexOffset++;
					break;
					
				case "vt":				
					outputString += line + "\n";//no changes here just need to count the number of textures to set offset later
					textureIndexOffset++;
					break;
					
				case "vn":								
					outputString += line + "\n";//no changes here just need to count the number of normals to set offset later
					normalIndexOffset++;
					break;
					
				case "f":
					newLine = "f";
					
					//add offset for each tuple
					for (int i = 1; i < lineDecomp.length; i++) {
						tuple = lineDecomp[i].split("/");
						newVertexIndex = (Integer.parseInt(tuple[0]) + indexOffsets.get(0)) + "";
						newTextureIndex = (Integer.parseInt(tuple[1]) + indexOffsets.get(1)) + "";
						newNormalIndex = (Integer.parseInt(tuple[2]) + indexOffsets.get(2)) + "";
	
						newLine += " " + newVertexIndex + "/" + newTextureIndex + "/" + newNormalIndex;
						
					}
					newLine += "\n";
					
					outputString += newLine;
					break;
	
				default:
					outputString += line + "\n"; //no changes here
					break;
				}
	            
	            line = bufferOBJ.readLine();
			}
			fileOBJ.close();
        } catch(IOException e) {
        	System.out.println("Tree model not found");
        }
		
		// Updating the offsets
		indexOffsets.set(0, vertexIndexOffset);
		indexOffsets.set(1, textureIndexOffset);
		indexOffsets.set(2, normalIndexOffset);
		
		return outputString;
	}
}

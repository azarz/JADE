package eu.ensg.jade.semantic;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

import eu.ensg.jade.output.IObjExport;
import eu.ensg.jade.utils.JadeUtils;

/**
 * PedestrianCrossing is the class implementing a surfacic PedestrianCossing object
 * 
 * @author JADE
 */


public class PedestrianCrossing implements IObjExport{
	
// ========================== ATTRIBUTES ===========================

	/**
	 * the attribute containing the geometry of the road
	 */
	Coordinate[] geometry;
	
// ========================== CONSTRUCTORS =========================
	
	/**
	 * Default constructor
	 * 
	 * @param geometry the geomtry of the pedestrian crossing
	 */
	public PedestrianCrossing(Coordinate[] geometry) {
		this.geometry = geometry;
	}
	
// ========================== METHODS ==============================

	@Override
	public String toOBJ(List<Integer> indexOffsets, double xOffset, double yOffset) {
		// Defining a new decimal format in order to have smaller obj files
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
		DecimalFormat format = (DecimalFormat)nf;
		format.applyPattern("#.###");				
		
		// Fetching the offsets from the offsets parameter
		int vertexIndexOffset  = indexOffsets.get(0);
		int textureIndexOffset = indexOffsets.get(1);
		int normalIndexOffset  = indexOffsets.get(2);
		
		String vertexCoords = "";
		String uvCoords     = "";
		String normalCoords = "";
		String faces;
		String outPutString = "";
		
		faces = "usemtl PedestrianCrossing\n";
		
		for (int i = 0; i < geometry.length ; i++) {
			vertexCoords += "v "+ format.format(geometry[i].x)+" "+ format.format(geometry[i].z)+" "+format.format(geometry[i].y)+"\n";
			vertexIndexOffset++;
		}
		
		uvCoords += "vt 0 0" + "\n";
		uvCoords += "vt " + format.format(JadeUtils.getDistance(geometry[0], geometry[1])) + " 0" + "\n";
		uvCoords += "vt " + format.format(JadeUtils.getDistance(geometry[0], geometry[1])) + " " + format.format(JadeUtils.getDistance(geometry[0], geometry[3])) + "\n";
		uvCoords += "vt 0 " + format.format(JadeUtils.getDistance(geometry[0], geometry[3])) + "\n";
		
		textureIndexOffset += 4;
		
		normalCoords = "vn 0 1 0 \n";
		
		normalIndexOffset += 1;
		
		faces += "f "+(1+indexOffsets.get(0))+"/"+(1+indexOffsets.get(1))+"/"+(1+indexOffsets.get(2))+" "+ 
				(2+indexOffsets.get(0))+"/"+(2+indexOffsets.get(1))+"/"+(1+indexOffsets.get(2))+" "+
				(3+indexOffsets.get(0))+"/"+(3+indexOffsets.get(1))+"/"+(1+indexOffsets.get(2))+" "+
				(4+indexOffsets.get(0))+"/"+(4+indexOffsets.get(1))+"/"+(1+indexOffsets.get(2))+"\n";
		
		indexOffsets.set(0, vertexIndexOffset);
		indexOffsets.set(1, textureIndexOffset);
		indexOffsets.set(2, normalIndexOffset);
		
		outPutString = vertexCoords + uvCoords + normalCoords + faces;
		
		return outPutString;
	}



}

package eu.ensg.jade.main;

import java.io.IOException;

import org.geotools.feature.SchemaException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import eu.ensg.jade.scene.SceneBuilder;
import eu.opends.main.Simulator;

/**
 * Main class, entry point for both jade's SceneBuilder and opends' Simulator
 * @author JADE
 *
 */
public class Jade {

	/**
	 * Main method, launches either the SceneBuilder, the Simulator, or both
	 * @param args
	 * @throws IOException 
	 * @throws SchemaException 
	 * @throws NoSuchAuthorityCodeException 
	 * @throws FactoryException 
	 */
	public static void main(String[] args) throws SchemaException, IOException, NoSuchAuthorityCodeException, FactoryException {
		if (args.length == 0 || args[0].equals("sim")) {
			Simulator.main(new String[0]);
			return;
		} else if (args[0].equals("build")) {
			SceneBuilder.main(args);
			return;
		} else if (args[0].equals("both")) {
			SceneBuilder.main(args);
			Simulator.main(new String[0]);
			return;
		} else {
			System.out.println("Invalid parameters. See the documentation for further details");
			return;
		}
	}
}

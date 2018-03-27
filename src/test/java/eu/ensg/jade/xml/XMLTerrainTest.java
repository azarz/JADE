package eu.ensg.jade.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * XMLTerrainTest is the class testing the {@link XMLTerrain} class
 * 
 * @author JADE
 */
public class XMLTerrainTest {
	
	private static final double DELTA = 10e-15;
	
	/**
	 * Test constructor
	 */
	@Test
	public void testConstructor(){
		
		XMLTerrain terrain = new XMLTerrain("id", "path/to/heightmap.png");
		assertNotNull(terrain);		
	}
	
	/**
	 * Test getter and setter
	 */
	@Test
	public void testGetterSetter() {
		XMLTerrain terrain = new XMLTerrain("anything", "anything else");
		
		
		String id = "id2";
		String imageHeightmap = "path/to/heightmap.png";
		
		double heightScale = 0.1;
		
		int patchSize = 129;
		int totalSize = 1025;
		double distanceFactor = 2.5;
		
		
		terrain.setId(id);
		assertEquals(terrain.getId(), id);
		
		terrain.setImageHeightmap(imageHeightmap);
		assertEquals(terrain.getImageHeightmap(), imageHeightmap);
		
		terrain.setHeightScale(heightScale);
		assertEquals(terrain.getHeightScale(), heightScale, DELTA);
		
		terrain.setPatchSize(patchSize);
		assertEquals(terrain.getPatchSize(), patchSize);
		
		terrain.setTotalSize(totalSize);
		assertEquals(terrain.getTotalSize(), totalSize);
		
		terrain.setDistanceFactor(distanceFactor);
		assertEquals(terrain.getDistanceFactor(), distanceFactor, DELTA);
	}
	
	/**
	 * Test XML export
	 */
	@Test
	public void testXMLElement() {
		String id = "SuperCoolID";
		String imageHeightmap = "starway/to/heaven.png";

		XMLTerrain terrain = new XMLTerrain(id, imageHeightmap);
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			
			Element element = terrain.toXMLElement(doc);
			
			assertNotNull(element.getElementsByTagName("imageBasedHeightMap"));
			assertNotNull(element.getElementsByTagName("smoothing"));
			assertNotNull(element.getElementsByTagName("lod"));
			
			Node imageNode = element.getElementsByTagName("imageBasedHeightMap").item(0);
			Node lodNode = element.getElementsByTagName("lod").item(0);
			
			
			assertEquals(
					imageNode.getAttributes().item(0).getTextContent(),
					terrain.getImageHeightmap(), DELTA);
			
			assertEquals(
					Double.parseDouble(lodNode.getAttributes().item(2).getTextContent()),
					terrain.getDistanceFactor(), DELTA);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
}

package eu.ensg.jade.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * XMLVectorTest is the class testing the {@link XMLVector} class
 * 
 * @author JADE
 */
public class XMLVectorTest {
	
	private static final double DELTA = 10e-15;
	
	/**
	 * Test constructor
	 */
	@Test
	public void testConstructor(){
		
		XMLVector vector = new XMLVector();
		assertNotNull(vector);
		
		vector = new XMLVector(new double[]{1,1,1}, new double[]{0,0,0}, new double[]{-1,0,1});
		assertNotNull(vector);
	}
	
	/**
	 * Test getter and setter
	 */
	@Test
	public void testGetterSetter() {
		XMLVector vector = new XMLVector();
		
		double[] scale = {1, 2, 1};
		double[] translation = {-1, 0.5, 0};
		double[] rotation = {0, 90, 0};
		
		vector.setScale(scale);
		assertEquals(vector.getScale(), scale);
		
		vector.setTranslation(translation);
		assertEquals(vector.getTranslation(), translation);
		
		vector.setRotation(rotation);
		assertEquals(vector.getRotation(), rotation);
	}
	
	/**
	 * Test XML export
	 */
	@Test
	public void testXMLElement() {		
		double[] scale = {1, 2, 1};
		double[] translation = {-1, 0.5, 0};
		double[] rotation = {0, 90, 0};

		XMLVector vector = new XMLVector(scale, rotation, translation);
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			
			Element element = vector.toXMLElement(doc);
			
			assertNotNull(element.getElementsByTagName("scale"));
			assertNotNull(element.getElementsByTagName("rotation"));
			assertNotNull(element.getElementsByTagName("translation"));
			
			Node scaleNode = element.getElementsByTagName("scale").item(0);
			Node rotationNode = element.getElementsByTagName("rotation").item(0);
			Node translationNode = element.getElementsByTagName("translation").item(0);
			
			
			assertEquals(
					Double.parseDouble(scaleNode.getChildNodes().item(0).getTextContent()), 
					vector.getScale()[0], DELTA);
			
			assertEquals(
					Double.parseDouble(scaleNode.getChildNodes().item(2).getTextContent()),
					vector.getScale()[2], DELTA);
			
			assertEquals(
					Double.parseDouble(rotationNode.getChildNodes().item(1).getTextContent()),
					vector.getRotation()[1], DELTA);
			
			assertEquals(
					Double.parseDouble(translationNode.getChildNodes().item(0).getTextContent()),
					vector.getTranslation()[0], DELTA);
			
			assertNull(translationNode.getChildNodes().item(15));			
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

}

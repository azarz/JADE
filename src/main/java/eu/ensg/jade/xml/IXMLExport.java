package eu.ensg.jade.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Interface for the conversion of an XML related class to a W3C Element class
 * 
 * @author JADE
 *
 */
public interface IXMLExport {
	/**
	 * Convert a set of data, stored in class parameters, to an XML Element.
	 * This method is used to write the content of the class to an XML file with the correct format
	 * @param doc the XML document
	 * @return a new element that can be added to the XML file
	 */
	public abstract Element toXMLElement(Document doc);
}

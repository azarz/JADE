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
	public abstract Element toXMLElement(Document doc);
}

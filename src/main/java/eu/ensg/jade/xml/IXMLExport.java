package eu.ensg.jade.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface IXMLExport {
	public abstract Element toXMLElement(Document doc);
}

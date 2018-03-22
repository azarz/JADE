package eu.ensg.jade.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface INodeExport {
	public abstract Element toNode(Document doc);
}

package eu.ensg.jade.output;

import java.util.List;

public interface IObjExport {

	String toOBJ(List<Integer> indexOffsets, double xOffset, double yOffset);

}
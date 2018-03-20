package eu.ensg.jade.semantic;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * DTM is the class implementing a DTM
 * 
 * @author JADE
 */

public class DTM {
	
// ========================== ATTRIBUTES ===========================
	
	/**
	 * The table associates to the DTM
	 */
	private double[][] tabDTM;
	
	/**
	 * The header containing the metadata of the DTM (same order as in the ascii file)*
	 * 
	 * Header Keys : ncols - nrows - xllcorner - yllcorner - cellsize
	 */
	private Map<String,Double> headerDTM;
	
	
// ========================== CONSTRUCTORS =========================	

	/**
	 * Constructor using all fields
	 * 
	 * @param tabDTM the table associates to the DTM
	 */
	public DTM(double[][] tabDTM,Map<String,Double> headerDTM) {
		this.tabDTM = tabDTM;
		this.headerDTM = headerDTM;
	}
	
// ========================== GETTERS/SETTERS ======================
	
	/**
	 * Gets the DTM table
	 * 
	 * @return table of elevations
	 */
	public double[][] getTabDTM() {
		return tabDTM;
	}
	
	/**
	 * Gets the DTM header
	 * 
	 * @return header of the DTM as a KEY/VALUE map
	 */
	public Map<String,Double> getHeaderDTM() {
		return headerDTM;
	}


// ========================== METHODS ==============================

	/**
	 * Transforms a DTM table into a PNG file 
	 * 
	 * @throws IOException 
	 */
	public void toPNG(String path) throws IOException{
		
		
		int nrows = this.headerDTM.get("nrows").intValue();
		int ncols =  this.headerDTM.get("ncols").intValue();
		BufferedImage bufferImageDTM;
		
		if (ncols < nrows) {
			bufferImageDTM = new BufferedImage(nrows,nrows, BufferedImage.TYPE_BYTE_GRAY);
			WritableRaster wr = bufferImageDTM.getRaster();
			for(int y = 0; y < nrows ; y++){
		        for(int x = 0; x < nrows ; x++){
		        	wr.setSample(x, y, 0, 0);
		        }
		    }
		} else if (ncols > nrows) {
			bufferImageDTM = new BufferedImage(ncols,ncols, BufferedImage.TYPE_BYTE_GRAY);
			WritableRaster wr = bufferImageDTM.getRaster();
			for(int y = 0; y < ncols ; y++){
		        for(int x = 0; x < ncols ; x++){
		        	wr.setSample(x, y, 0, 0);
		        }
		    }
		} else {
			bufferImageDTM = new BufferedImage(ncols,nrows, BufferedImage.TYPE_BYTE_GRAY);
		}
		
		WritableRaster wr = bufferImageDTM.getRaster();
		for(int y = 0; y < nrows ; y++){
	        for(int x = 0; x < ncols ; x++){
	        	wr.setSample(x, nrows-1 - y, 0, (int) this.tabDTM[y][x]);
	        }
	    }
		
	    File outputfile = new File(path);
	    ImageIO.write(bufferImageDTM, "png", outputfile);
	}

}

package eu.ensg.jade.semantic;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
	 * Number of colons
	 */
	private int ncols;
	
	/**
	 * Number of rows
	 */
	private int nrows;
	
	/**
	 * X coordinate of the leftmost low corner
	 */
	private double xllcorner;
	
	/**
	 * Y coordinate of the leftmost low corner
	 */
	private double yllcorner;
	
	/**
	 * Size of a cell in meters
	 */
	private double cellsize;
	
	
// ========================== CONSTRUCTORS =========================	

	/**
	 * Constructor using all fields
	 * 
	 * @param tabDTM the table associates to the DTM
	 */
	public DTM(double[][] tabDTM, Map<String,Double> headerDTM) {
		this.tabDTM = tabDTM;
		// Getting all the data from the hashmap and putting default values if they are not in the input map
		try {
			this.ncols = headerDTM.get("ncols").intValue();
		} catch (NullPointerException e) {
			this.ncols = tabDTM[0].length;
		}
		try {
			this.nrows = headerDTM.get("nrows").intValue();
		} catch (NullPointerException e) {
			this.nrows = tabDTM.length;
		}
		try {
			this.xllcorner = headerDTM.get("xllcorner");
		} catch (NullPointerException e) {	
			this.xllcorner = 0;
		}
		try {
			this.yllcorner = headerDTM.get("yllcorner");
		} catch (NullPointerException e) {
			this.yllcorner = 0;
		}
		try {
			this.cellsize = headerDTM.get("cellsize");
		} catch (NullPointerException e) {
			this.cellsize = 1;
		}
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
	 * Gets the number of colons
	 * @return number of colons
	 */
	public int getNcols() {
		return ncols;
	}

	/**
	 * Gets the number of rows
	 * @return number of rows
	 */
	public int getNrows() {
		return nrows;
	}

	/**
	 * Gets the X coordinate of the leftmost low corner
	 * @return X coordinate of the leftmost low corner
	 */
	public double getXllcorner() {
		return xllcorner;
	}

	/**
	 * Gets the Y coordinate of the leftmost low corner
	 * @return Y coordinate of the leftmost low corner
	 */
	public double getYllcorner() {
		return yllcorner;
	}

	/**
	 * Gets the size of a cell in meters
	 * @return Size of a cell in meters
	 */
	public double getCellsize() {
		return cellsize;
	}


// ========================== METHODS ==============================


	/**
	 * Transforms a DTM table into a PNG file 
	 * 
	 * @throws IOException 
	 */
	public void toPNG(String path) {
		
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
		
		try {
			Files.deleteIfExists(outputfile.toPath());
			ImageIO.write(bufferImageDTM, "png", outputfile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}

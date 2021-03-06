package eu.ensg.jade.semantic;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import javax.imageio.ImageIO;

import eu.ensg.jade.utils.JadeUtils;

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
	 * The DTM mocking JMonkey smooth
	 */
	private double[][] smoothDTM;
	
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
	
	/**
	 * Maximum height value in meters
	 */
	private double maxvalue;
	/**
	 * Maximum height value in meters
	 */
	private double minvalue;
	
// ========================== CONSTRUCTORS =========================
	
	/**
	 * Empty constructor 
	 */
	public DTM() {
		this.tabDTM = new double[0][0];
		this.ncols = 0;
		this.nrows = 0;
		this.xllcorner = 0;
		this.yllcorner = 0;
		this.cellsize = 1;
		this.maxvalue = 255;
		this.minvalue = 0;
	}

	/**
	 * Constructor using all fields
	 * 
	 * @param tabDTM the table associates to the DTM
	 * @param headerDTM the header Map of the DTM
	 */
	public DTM(double[][] tabDTM, Map<String,Double> headerDTM) {
		this.tabDTM = tabDTM;
		this.ncols = tabDTM[0].length;
		this.nrows = tabDTM.length;
		this.xllcorner = 0;
		this.yllcorner = 0;
		this.cellsize = 1;
		this.maxvalue = 255;
		
		// Getting all the data from the hashmap and putting default values if they are not in the input map
		try {
			this.ncols = headerDTM.get("ncols").intValue();
			this.nrows = headerDTM.get("nrows").intValue();
			this.xllcorner = headerDTM.get("xllcorner");
			this.yllcorner = headerDTM.get("yllcorner");
			this.cellsize = headerDTM.get("cellsize");
			this.maxvalue = headerDTM.get("maxvalue");
			
		} catch (NullPointerException e) {
			System.out.println("Incomplete DTM; putting default values");
		}
		
		this.createSmoothDTM();
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
	 * Gets the number of columns
	 * 
	 * @return number of columns
	 */
	public int getNcols() {
		return ncols;
	}

	/**
	 * Gets the number of rows
	 * 
	 * @return number of rows
	 */
	public int getNrows() {
		return nrows;
	}

	/**
	 * Gets the X coordinate of the leftmost low corner
	 * 
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
	 * 
	 * @return Size of a cell in meters
	 */
	public double getCellsize() {
		return cellsize;
	}
	
	/**
	 * Gets the maximum altitude value
	 * 
	 * @return Maximum altitude value in meters
	 */
	public double getMaxvalue() {
		return maxvalue;
	}
	
	/**
	 * Gets the minimum altitude value
	 * 
	 * @return Minimuim altitude value in meters
	 */
	public double getMinvalue() {
		return minvalue;
	}


// ========================== METHODS ==============================


	/**
	 * Transforms a DTM table into a PNG file 
	 * 
	 * @param path path to the DTM
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
	        	int sampleValue = (int) this.tabDTM[y][x];
	        	if (maxvalue > 255) {
	        		sampleValue -= (int) minvalue;
	        		sampleValue *= 255/maxvalue;
	        		
	        	} 
	        	wr.setSample(x, nrows-1 - y, 0, sampleValue);
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
	
	/**
	 * Gets the height of a point, given its coordinates
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * 
	 * @return The height
	 */
	public double getHeightAtPoint(double x, double y){
		double xFraction = (x - xllcorner) / cellsize;
		double yFraction = nrows - ((y - yllcorner) / cellsize);
		
		int column = (int) xFraction;
		int row = (int) yFraction;
		
		xFraction -= column;
		yFraction -= row;
		
		double northWest;
		double northEast;
		double southWest;
		double southEast;
		try {
			northWest = smoothDTM[row][column];
			northEast = smoothDTM[row][column+1];
			southWest = smoothDTM[row+1][column];
			southEast = smoothDTM[row+1][column+1];
		} catch (ArrayIndexOutOfBoundsException e) {
			return 0;
		}
		
		double height = 0;
		if ((column == 0 && row == 0) || (column == ncols - 1 && row == nrows - 1)) {
            if (xFraction < yFraction)
                height = northWest + xFraction*(southEast-southWest) + yFraction*(southWest-northWest);
            else
            	height = northWest + xFraction*(northEast-northWest) + yFraction*(southEast-northEast);
            
        }
		else {
            if (xFraction < (1-yFraction))
            	height = southWest + (xFraction)*(northEast-northWest) + (1-yFraction)*(northWest-southWest);
            else
                height = southWest + (xFraction)*(southEast-southWest) + (1-yFraction)*(northEast-southEast);
        }
		if (maxvalue > 255) {
			height *= maxvalue/255;
			height -= minvalue;
		}
		return height + 0.03;
	}
	
	/**
	 * Smooth the DTM
	 * 
	 * @param np Smoothing coefficient
	 * @param radius Radius of smoothing
	 */
	public void smooth(double np, int radius) {
		if(smoothDTM == null) this.createSmoothDTM();
        if (np < 0 || np > 1) np = 0.9;
        if (radius == 0) radius = 10;
    
        int number = 0;
        double average = 0;
        for (int x = 0; x < ncols; x++) {
            for (int y = 0; y < nrows; y++) {
            	number = 0;
            	average = 0;
                for (int rx = -radius; rx <= radius; rx++) {
                    for (int ry = -radius; ry <= radius; ry++) {
        				if(x+rx >= 0 && x+rx < ncols && y+ry >= 0 && y+ry < nrows) {
        					number++;
        					average += smoothDTM[y+ry][x+rx];
        				}
                    }
                }
                average /= number;
                smoothDTM[y][x] = JadeUtils.lerp(smoothDTM[y][x], average, np);
            }
        }
    }
	
	/**
	 * Creates a clone of the DTM table, to keep the original in memory
	 */
	private void createSmoothDTM(){
		// Copy of tabDTM into smoothDTM
		if(this.tabDTM == null) return;
		
		this.smoothDTM = new double[nrows][ncols];
		for(int i=0; i<nrows; i++){
			for(int j=0; j<ncols; j++) {
				int newvalue = (int) this.tabDTM[i][j];
				if (maxvalue > 255) {
					newvalue *= 255/maxvalue;
				}
				this.smoothDTM[i][j] = newvalue;
			}
		}
	}

}

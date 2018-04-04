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
	 * Empty constructor 
	 * 
	 * @param tabDTM the table associates to the DTM
	 */
	public DTM() {
		this.tabDTM = new double[0][0];
		this.ncols = 0;
		this.nrows = 0;
		this.xllcorner = 0;
		this.yllcorner = 0;
		this.cellsize = 1;
	}

	/**
	 * Constructor using all fields
	 * 
	 * @param tabDTM the table associates to the DTM
	 */
	public DTM(double[][] tabDTM, Map<String,Double> headerDTM) {
		this.tabDTM = tabDTM;
		this.ncols = tabDTM[0].length;
		this.nrows = tabDTM.length;
		this.xllcorner = 0;
		this.yllcorner = 0;
		this.cellsize = 1;
		
		// Getting all the data from the hashmap and putting default values if they are not in the input map
		try {
			this.ncols = headerDTM.get("ncols").intValue();
			this.nrows = headerDTM.get("nrows").intValue();
			this.xllcorner = headerDTM.get("xllcorner");
			this.yllcorner = headerDTM.get("yllcorner");
			this.cellsize = headerDTM.get("cellsize");
			
		} catch (NullPointerException e) {
			e.printStackTrace();
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
	 * Gets the number of columns
	 * @return number of columns
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
	
	public double getHeightAtPoint(double x, double y){
		double xFraction = (x - xllcorner) / cellsize;
		double yFraction = nrows - ((y - yllcorner) / cellsize);
		
		int column = (int) xFraction;
		int row = (int) yFraction;
		
		xFraction -= column;
		yFraction -= row;
		
		double northWest = mockJMonkeySmooth(column, row, 0.9, 1);
		double northEast = mockJMonkeySmooth(column+1, row, 0.9, 1);
		double southWest = mockJMonkeySmooth(column, row+1, 0.9, 1);
		double southEast = mockJMonkeySmooth(column+1, row+1, 0.9, 1);
		
		if ((column == 0 && row == 0) || (column == ncols - 2 && row == nrows - 2)) {
            if (xFraction < yFraction)
                return 0.01 + northWest + xFraction*(southEast-southWest) + yFraction*(southWest-northWest);
            else
                return 0.01 + northWest + xFraction*(northEast-northWest) + yFraction*(southEast-northEast);
            
        }
		else {
            if (xFraction < (1-yFraction))
            	return 0.01 + southWest + (xFraction)*(northEast-northWest) + (1-yFraction)*(northWest-southWest);
            else
                return 0.01 + southWest + (xFraction)*(southEast-southWest) + (1-yFraction)*(northEast-southEast);
        }

		
		
//		return JadeUtils.lerp(
//				JadeUtils.lerp(northWestValue, northEastValue, columnFraction),
//				JadeUtils.lerp(southWestValue, southEastValue, columnFraction),
//				rowFraction) + 0.01;
	}
	
	
	private double mockJMonkeySmooth(int x, int y, double np, int radius) {
		if (np < 0 || np > 1) {
			System.out.println("Percent not valid");
			np = 0.9;
		}
		if (radius == 0){
			radius = 1;
		}
		
		int number = 0;
		float average = 0;
		for (int rx = -radius; rx <= radius; rx++) {
			for (int ry = -radius; ry <= radius; ry++) {
				if(x+rx >= 0 && x+rx < ncols && y+ry >= 0 && y+ry < nrows) {
					number++;
					average += tabDTM[y+ry][x+rx];
				}
			}
		}
		average /= number;
		return JadeUtils.lerp(tabDTM[y][x], average, np);
	}

}

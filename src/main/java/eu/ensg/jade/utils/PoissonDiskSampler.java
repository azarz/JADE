package eu.ensg.jade.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * PoissonDiskSampler is the class implementing the Poisson disk sampling algorithm
 * 
 * The algorithm allows to create an homogeneous distribution of points in 2D by granting a certain distance between points.
 * 
 * @author JADE
 */

public class PoissonDiskSampler {

// ========================== ATTRIBUTES ===========================	
	
	/**
	 * The upper left X coordinate of the concerned area
	 */
	private double startX;
	
	/**
	 * The upper left Y coordinate of the concerned area
	 */
	private double startY;
	
	/**
	 * The width of the sampled space
	 */
	private double width;
	
	/**
	 * The height of the sampled space
	 */
	private double height;
	
	/**
	 * The minimum distance between each point
	 */
	private double radius;
	
	/**
	 * The number of repetitions to create a point
	 */
	private int k;
	
	/**
	 * The random number generator
	 */
	private Random rng;
	
	/**
	 * The grid optimizing the placement of new points
	 */
	private double[][] grid;
	
	/**
	 * The size of each cell from the grid
	 */
	private double cellSize;
	
	/**
	 * The number of columns of the grid
	 */
	private int gridColumns;
	
	/**
	 * The number of rows of the grid
	 */
	private int gridRows;
	
	/**
	 * The number of point generated
	 */
	private int pointNumber;
	
	/**
	 * The queue containing the index of point with potential neighbor
	 */
	private List<Integer> queue;

// ========================== CONSTRUCTORS =========================
	
	/**
	 * Class constructor based on on start point and end point to define the extent, and all other parameters
	 * 
	 * @param startX the upper left X coordinate of the concerned area
	 * @param startY the upper left Y coordinate of the concerned area
	 * @param endX the down right X coordinate of the concerned area
	 * @param endY the down right Y coordinate of the concerned area
	 * @param radius the minimum distance between each point
	 * @param k the number of repetitions to create a point
	 */
	public PoissonDiskSampler(double startX, double startY, double endX, double endY, double radius, int k) {
		this.startX = Math.min(startX, endX);
		this.startY = Math.min(startY, endY);
		this.width = Math.abs(endX - startX);
		this.height = Math.abs(endY - startY);
		this.radius = Math.abs(radius);
		this.k = k > 0 ? k : 30;
		
		this.rng = new Random();
		
		this.initGrid();
	}
	
	/**
	 * Class constructor based on on start point and end point to define the extent
	 * 
	 * @param startX the upper left X coordinate of the concerned area
	 * @param startY the upper left Y coordinate of the concerned area
	 * @param endX the down right X coordinate of the concerned area
	 * @param endY the down right Y coordinate of the concerned area
	 * @param radius the minimum distance between each point
	 */
	public PoissonDiskSampler(double startX, double startY, double endX, double endY, double radius) {
		this(startX, startY, endX, endY, radius, 30);
	}
	
// ========================== METHODS ==============================
	
	/**
	 * This method fills the space with randomly placed point. It compute all points, and leave not space empty
	 * 
	 * @return The list of points coordinates: [x, y]
	 */
	public List<double[]> compute(){
		List<double[]> pointList = new ArrayList<double[]>();
		double[] point = new double[2];
		while(point != null) {
			point = next();
			if(point != null) pointList.add(point);
		}
		return pointList;
	}
	
	/**
	 * This method compute only the next point in the sample
	 * 
	 * @return A single point coordinates [x, y]
	 */
	public double[] next() {
		double x = 0, y = 0;
		
		if(this.pointNumber == 0) {
			x = startX + rng.nextDouble() * width;
			y = startY + rng.nextDouble() * height;
			return insertPoint(x, y);
		}
		
		while(queue.size() > 0) {
	      int idx = (int) (queue.size() * rng.nextDouble()) ;
	      double[] p = grid[queue.get(idx)];

	      double rad = 0, angle = 0;
	      for (int i = 0; i < k; i++) {
	        rad = radius * (1 + rng.nextDouble());
	        angle = rng.nextDouble() * 2*Math.PI;

	        x = p[0] + rad*Math.cos(angle);
	        y = p[1] + rad*Math.sin(angle);

	        if(validPoint(x,y)) {
	          return insertPoint(x,y);
	        }
	      }
	      queue.remove(idx);
	    }
		
	    return null;
	}
	
	/**
	 * Insert a new point in the grid, and update the queue
	 * 
	 * @param x The X coordinate of the new point
	 * @param y The Y coordinate of the new point
	 * 
	 * @return A structure containing the point
	 */
	private double[] insertPoint(double x, double y) {
		double[] point = new double[]{x, y};
		
		int idx = (int)((x-startX)/cellSize) + gridColumns * (int)((y-startY)/cellSize);
	    grid[idx] = point;
	    pointNumber++;
	    queue.add(idx);
	    
		return point;
	}
	
	/**
	 * Tests if a point is valid, with the following conditions:
	 * <ul>
	 * <li>The point is inside the sample extent</li>
	 * <li>The point is not too close to any other previously generated point</li>
	 * </ul>
	 * 
	 * @param x The X coordinate of the point
	 * @param y The Y coordinate of the point
	 * 
	 * @return True is the point is valid, false otherwise
	 */
	private boolean validPoint(double x, double y) {
		if(x < startX || x >= (startX+width) || y < startY || y >= (startY+height)){
			return false;
		}

		int col = (int) ((x-startX)/cellSize);
		int row = (int) ((y-startY)/cellSize);
		
		int idx = 0;
		for (int i = col-2; i < col+3; i++) {
		  for(int j = row-2; j < row+3; j++) {
		    if(i < 0 || i >= gridColumns || j < 0 || j >= gridRows){ continue; }
		    idx = i + (j*gridColumns);
		
		    if(grid[idx] != null) {
		      if(this.squareDistance(x, y, grid[idx][0], grid[idx][1]) <= (radius*radius) ) {
		        return false;
		      }
		    }
		  }
		}
		
		return true;
	}
	
	/**
	 * Compute the squared distance between two points. It avoids using sqrt for performance boost
	 * 
	 * @param x1 the x coordinate of the first point
	 * @param y1 the y coordinate of the first point
	 * @param x2 the x coordinate of the second point
	 * @param y2 the y coordinate of the second point
	 * 
	 * @return The squared distance between two points
	 */
	private double squareDistance(double x1, double y1, double x2, double y2){
	    return (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
	}
	
	
	/**
	 * Utility method to initialize the grid containing all generated points
	 */
	private void initGrid(){
		this.cellSize = this.radius * Math.sqrt(0.5);
	    this.gridColumns = (int) Math.ceil(this.width / this.cellSize);
	    this.gridRows = (int) Math.ceil(this.height / this.cellSize);
	    this.grid = new double[this.gridColumns * this.gridRows][2];
	    for (int i = 0; i < this.grid.length; i++) { this.grid[i] = null; }

	    this.queue = new ArrayList<>();
	    this.pointNumber = 0;
	}
}

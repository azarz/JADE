package eu.ensg.jade.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PoissonDiskSampler {
	
	private double startX;
	
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
	
	/**
	 * Class constructor specifying each parameters
	 * 
	 * @param width The width of the sampled space
	 * @param height The height of the sampled space
	 * @param radius The minimum distance between each point
	 * @param k The number of repetitions to create a point
	 */
	public PoissonDiskSampler(double width, double height, double radius, int k) {
		this.startX = 0;
		this.startY = 0;
		this.width = Math.abs(width);
		this.height = Math.abs(height);
		this.radius = Math.abs(radius);
		this.k = k > 0 ? k : 30;
		
		this.initGrid();
	}
	
	/**
	 * Class constructor with only useful parameters
	 * 
	 * @param width The width of the sampled space
	 * @param height The height of the sampled space
	 * @param radius The minimum distance between each point
	 */
	public PoissonDiskSampler(double width, double height, double radius) {
		this(width, height, radius, 30);
	}
	
	/**
	 * Class constructor defining only the extent of the sampling
	 * 
	 * @param width The width of the sampled space
	 * @param height The height of the sampled space
	 */
	public PoissonDiskSampler(double width, double height) {
		this(width, height, 10, 30);
	}
	
	/**
	 * Class constructor based on on start point and end point to define the extent, and all other parameters
	 * 
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param radius
	 * @param k
	 */
	public PoissonDiskSampler(double startX, double startY, double endX, double endY, double radius, int k) {
		this.startX = Math.min(startX, endX);
		this.startY = Math.min(startY, endY);
		this.width = Math.abs(endX - startX);
		this.height = Math.abs(endY - startY);
		this.radius = Math.abs(radius);
		this.k = k > 0 ? k : 30;
		
		this.initGrid();
	}
	
	/**
	 * Class constructor based on on start point and end point to define the extent
	 * 
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param radius
	 */
	public PoissonDiskSampler(double startX, double startY, double endX, double endY, double radius) {
		this(startX, startY, endX, endY, radius, 30);
	}
	
	/**
	 * Class constructor based on on start point and end point to define the extent, and leaving all parameters
	 * as default
	 * 
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 */
	public PoissonDiskSampler(double startX, double startY, double endX, double endY) {
		this(startX, startY, endX, endY, 10, 30);
	}
	
	
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
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return The distance
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

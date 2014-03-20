package logic;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class RushSolver extends Thread{
	private static int[] dirs;
	private static HashMap<GridState, GridState> states;//k = curSate, v = Hint
	private static LinkedList<GridState> toProcess;
	
	private static class GridState {
		private int [][] daMap;
		private HashMap<Point, GridState> neighbors;
		private int stepsToEnd;

		public GridState(int [][] gr){
			daMap = gr.clone();
			neighbors = new HashMap<Point, GridState>();
		}

		@Override
		public boolean equals(Object other){
			if (!(other instanceof GridState))
				return false;
			if (this  == other)//same object
				return true;
			GridState o = (GridState)other;
			for (int i = 0; i < daMap.length; i++) {
				for (int j = 0; j < daMap[0].length; j++) {
					if (o.daMap[i][j] == daMap[i][j])
						return false;
				}
			}
			return true;
		}

		public int[][] getGrid(){
			return daMap.clone();//clone does deep copy (on primitives)
		}
	}
	
	private static int[][] clone2D(int[][] tmp){
		int[][] asd = new int[tmp.length][tmp[0].length];
		for (int i = 0; i < asd.length; ++i){
			for (int j = 0; j < asd[0].length; ++j){
				asd[i][j] = tmp[i][j];
			}
		}
		return asd;
	}

	private static void printSectors(int[][] sector){
		System.out.println();
		for (int i = 0; i < sector.length; ++i){
			if(i>0)
				System.out.println();
			for (int j = 0; j < sector[0].length; ++j){
				System.out.print(sector[j][i] + " ");
			}
		}
		System.out.println();
	}

	/**
	 * Assume there all pieces has a side
	 * that is of length 1 (working on any size)
	 * 
	 * 
	 * Array of carDirs corresponds to
	 * the grids (positionValue - 1)
	 * 
	 * @param initGrid Initial grid
	 * @param carDirs Array of cars dirs
	 */
	public RushSolver(int [][] initGrid, int []carDirs) {
		//TODO Make it threaded possibly, private constructor?
		dirs = new int[carDirs.length + 1];
		for (int i = 1; i < dirs.length; ++i){
			dirs[i] = carDirs[i - 1];
		}
		if (dirs[1] == 1)//sanity check
			throw new IllegalArgumentException("First block must be able to move right");
		toProcess = new LinkedList<GridState>();
		toProcess.addFirst(new GridState(initGrid));
	}

	@Override
	public void run(){
		while(!toProcess.isEmpty()){
			enumerate(toProcess.removeLast().getGrid());
		}
	}
	

	/**
	 * Provided the top lect sector of the value 'val',
	 * decides if piece can move 'num' spaces 
	 * (Should support any size rectangles)
	 * 
	 * @param gr Grid to check
	 * @param val Value of piece in grid
	 * @param x x coordinate in grid[x][y]
	 * @param y y coordinate in grid[x][y]
	 * @param num Number of spaces to attempt move right
	 * @return
	 */
	private boolean canMoveLeft(int[][] gr, int val, int x, int y, int num){
		//assumes top left provided
		//assumes lower "nums" checked
		if (num < 1)
			return false;//sanity check
		if (x - num < 0)//too long a stride
			return false;
		if (gr[x - num][y] != 0){//check left
			return false;
		}
		// gr[x - num][y] must be zero here
		if (y + 1 == gr[0].length || gr[x][y + 1] != val){
			return true;
		}
		if (gr[x][y + 1] == val){// check next row
			return canMoveLeft(gr, val, x, y + 1, num);
		}
		return false;
	}
	
	private boolean canMoveUp(int[][] gr, int val, int x, int y, int num){
		//assumes top left provided
		//assumes lower "nums" checked
		if (num < 1)
			return false;//sanity check
		if (y - num < 0)//too long a stride
			return false;
		if (gr[x][y - num] != 0){//check left
			return false;
		}
		// gr[x - num][y] must be zero here
		if (x + 1 == gr.length || gr[x + 1][y] != val){
			return true;
		}
		if (gr[x + 1][y] == val){// check next row
			return canMoveUp(gr, val, x + 1, y, num);
		}
		return false;
	}
	
	private boolean canMoveRight(int[][] gr, int val, int x, int y, int num){
		//assumes top left provided
		//assumes lower "nums" checked
		if (num < 1)
			return false;//sanity check
		if (x + 1 >= gr.length)//starting at edge
			return false;
		while (gr[x + 1][y] == val){ // too far left in block
			++x;// traverse right
			if (x == gr.length - 1)//reached edge
				break;
		}
		if (x + num >= gr.length)//too long a stride
			return false;
		//x + num in bound

		if (y == gr[0].length - 1)
			return gr[x + num][y] == 0;
		while (gr[x][y + 1] == val){//there is another row
			y++;
			if (gr[x + num][y] != 0)
				return false;
			if (y == gr[0].length - 1)
				break;
		}
		return true;//else can move, base case 2
	}
	
	private boolean canMoveDown(int[][] gr, int val, int x, int y, int num){
		//assumes top left provided
		//assumes lower "nums" checked
		if (num < 1)
			return false;//sanity check
		if (y + 1 >= gr[0].length)//starting at edge
			return false;
		while (gr[x][y + 1] == val){ // too far left in block
			++y;// traverse right
			if (y == gr[0].length - 1)//reached edge
				break;
		}
		if (y + num >= gr[0].length)//too long a stride
			return false;
		//x + num in bound

		if (x == gr.length - 1)
			return gr[x][y + num] == 0;
		while (gr[x + 1][y] == val){//there is another row
			x++;
			if (gr[x][y + num] != 0)
				return false;
			if (x == gr.length - 1)
				break;
		}
		return true;//else can't move, base case 2
	}

	private void tryMoveLeft(int[][] gr, int val, int x, int y, int dir){
		if (dir == 1)//vert
			return;
		System.out.println("In tryMoveLeft ");
		int numSpaces = 1;
		int height = gr.length, width = gr[0].length;
		int [][] temp = clone2D(gr);
		while(canMoveLeft(gr, val, x, y, numSpaces)){
			for (int tX = 0; tX < width; ++tX){
				for (int tY = 0; tY < height; ++tY){//left right to prevent overwrite
					if (gr[tX][tY] == val){//"map" original into temp transformed
						temp[tX][tY] = 0;
						temp[tX - numSpaces][tY] = gr[tX][tY];
					}
				}
			}
			printSectors(temp);
			temp = clone2D(gr);
			//add new grid
			
			++numSpaces;
		}

		// move down to check width
	}

	private void tryMoveRight(int[][] gr, int val, int x, int y, int dir){
		if (dir == 1)
			return;
		System.out.println("In tryMoveRight...\nGR: ");
		int numSpaces = 1;
		int height = gr.length, width = gr[0].length;
		int [][] temp = clone2D(gr);
		while(canMoveRight(gr, val, x, y, numSpaces)){
			for (int tX = width - 1; tX >= 0; --tX){
				for (int tY = 0; tY < height; ++tY){//left right to prevent overwrite
					if (gr[tX][tY] == val){//"map" original into temp transformed
						temp[tX][tY] = 0;
						temp[tX + numSpaces][tY] = gr[tX][tY];
					}
				}
			}
			//add new grid
			printSectors(temp);
			temp = clone2D(gr);
			
			++numSpaces;
		}
		// parse right
		// move down to check width
	}

	private void tryMoveUp(int[][] gr, int val, int x, int y, int dir){
		if (dir == 0)
			return;
		System.out.println("In tryMoveUp...");
		int numSpaces = 1;
		int height = gr.length, width = gr[0].length;
		int [][] temp = clone2D(gr);
		while(canMoveUp(gr, val, x, y, numSpaces)){
			for (int tX = 0; tX < width; ++tX){
				for (int tY = 0; tY < height; ++tY){//left right to prevent overwrite
					if (gr[tX][tY] == val){//"map" original into temp transformed
						temp[tX][tY] = 0;
						temp[tX][tY - numSpaces] = gr[tX][tY];
					}
				}
			}
			printSectors(temp);
			temp = clone2D(gr);
			//add new grid
			
			++numSpaces;
		}

		// move right to check width
	}

	private void tryMoveDown(int[][] gr, int val, int x, int y, int dir){
		if (dir == 0)
			return;
		System.out.println("In tryMoveDown...");
		int numSpaces = 1;
		int height = gr.length, width = gr[0].length;
		int [][] temp = clone2D(gr);
		while(canMoveDown(gr, val, x, y, numSpaces)){
			for (int tX = width - 1; tX >= 0; --tX){
				for (int tY = 0; tY < height; ++tY){//left right to prevent overwrite
					if (gr[tX][tY] == val){//"map" original into temp transformed
						temp[tX][tY] = 0;
						temp[tX][tY + numSpaces] = gr[tX][tY];
					}
				}
			}
			//add new grid
			printSectors(temp);
			temp = clone2D(gr);
			
			++numSpaces;
		}
		// parse down
		// move right to check width
	}

	private void enumerate(int[][] gr) {
		boolean [] indexIsDone = new boolean[dirs.length];//initializes to false
		int width = gr[0].length, height = gr.length;
		int val;
		System.out.println("Print B4 All: ");
		printSectors(gr);
		for (int j = 0; j < height; ++j){
			for (int k = 0; k < width; ++k){
				val = gr[j][k];
				if (val > 0 && !indexIsDone[val]){
					tryMoveLeft(gr, val, j, k, dirs[val]);
					tryMoveRight(gr, val, j, k, dirs[val]);
					tryMoveUp(gr, val, j, k, dirs[val]);
					tryMoveDown(gr, val, j, k, dirs[val]);
					indexIsDone[val] = true;
				}
			}
		}

	}

}

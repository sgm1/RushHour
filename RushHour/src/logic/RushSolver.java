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
		private final int [][] daMap;
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
		if (dirs[1] == 2)
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
	
	private boolean canMoveLeft(int[][] gr, int val, int curRow, int curCol, int num){
		//assumes top left provided
		//assumes lower "nums" checked
		if (num < 1)
			return false;//sanity check
		if (curCol - num < 0)//too long a stride
			return false;
		if (gr[curRow][curCol - num] != 0){//check left
			return false;
		}
		// gr[curRow][curCol - num] must be zero here
		if (curRow + 1 == gr[0].length || gr[curRow + 1][curCol] != val){
			return true;
		}
		while(gr[curRow + 1][curCol] == val){// check next row
			return canMoveLeft(gr, val, curRow + 1, curCol, num);
		}
		return false;
	}
	/**
	 * Provided the top lect sector of the value 'val',
	 * decides if piece can move 'num' spaces 
	 * (Should support any size rectangles)
	 * 
	 * @param gr Grid to check
	 * @param val Value of piece in grid
	 * @param curRow R top coordinate in grid[r][c]
	 * @param curCol C far right coordinate in grid[r][c]
	 * @param num Number of spaces to attempt move right
	 * @return
	 */
	private boolean canMoveRight(int[][] gr, int val, int curRow, int curCol, int num){
		//assumes top left provided
		//assumes lower "nums" checked
		if (num < 1)
			return false;//sanity check
		while (curCol + 1 < gr[0].length &&
				gr[curRow][curCol + 1] == val){ // too far left in block
			++curCol;// traverse right
			//TODO re-check
		}
		if (curCol + num >= gr[0].length)//too long a stride
			return false;
		//base case
		if (gr[curRow][curCol + num] == 0 && // free to move
				(curRow + 1 == gr[0].length //at bot edge
				|| gr[curRow + 1][curCol] != val)){// at bot right
			return true;//return can move
		}//TODO Make these V and ^ loop? 
		if (gr[curRow][curCol + num] == 0 &&// free to move
				(gr[curRow + 1][curCol] == val)){//there is another row
			return canMoveRight(gr, val, curRow + 1, curCol, num);//curRow free
																//check next level
		}
		return false;//else can't move, base case 2
	}

	private void tryMoveLeft(int[][] gr, int val, int x, int y, int dir){
		if (dir == 1)//vert
			return;

		// move down to check width
	}

	private void tryMoveRight(int[][] gr, int val, int x, int y, int dir){
		if (dir == 1)
			return;
		int numSpaces = 1;
		int height = gr.length, width = gr[0].length;
		int [][] temp;
		while(canMoveRight(gr, val, x, y, numSpaces)){
			temp = gr.clone();
			for (int j = 0; j < height; ++j){
				for (int k = width - 1; k >= 0; --k){//right to left to prevent overwrite
					if (gr[j][k] == val){//"map" original into temp transformed
						temp[j][k] = 0;
						temp[j][k + numSpaces] = gr[j][k];
					}
				}
			}
			//add new grid
			
			++numSpaces;
		}
		// parse right
		// move down to check width
	}

	private void tryMoveUp(int[][] gr, int val, int x, int y, int dir){
		if (dir == 0)
			return;
		// move right to check width
	}

	private void tryMoveDown(int[][] gr, int val, int x, int y, int dir){
		if (dir == 0)
			return;
		// parse down
		// move right to check width
	}

	private void enumerate(int[][] gr) {
		boolean [] indexIsDone = new boolean[dirs.length];//initializes to false
		int width = gr[0].length, height = gr.length;
		int val;
		for (int j = 0; j < height; ++j){
			for (int k = 0; k < width; ++k){
				val = gr[j][k];
				if (val > 0 && !indexIsDone[val]){
					indexIsDone[val] = true;
					tryMoveLeft(gr.clone(), val, j, k, dirs[val]);
					tryMoveRight(gr.clone(), val, j, k, dirs[val]);
					tryMoveUp(gr.clone(), val, j, k, dirs[val]);
					tryMoveDown(gr.clone(), val, j, k, dirs[val]);
				}
			}
		}

	}

}

package logic;

import java.util.HashMap;
import java.util.LinkedList;

public class RushSolver extends Thread{
	private static int[] dirs;
	private static HashMap<GridState, GridState> states;//k = curSate, v = Hint
	private static LinkedList<GridState> toProcess;

	private static class GridState{//int[][] wrapper
		private final int [][] daMap;
		private int stepsToEnd;

		public GridState(int [][] gr){
			daMap = gr.clone();
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
			return daMap.clone();
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
			enumerate(toProcess.removeLast().getGrid());//clone does deep copy
		}
	}

	/**
	 * Provided the top right sector of the value 'val',
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
		if (curRow + 1 == gr.length)//already at edge
			return false;
		if (gr[curRow + 1][curCol] == val)// to far left in block
			return canMoveRight(gr, val, curRow + 1, curCol, num);
		//base case
		if (gr[curRow + 1][curCol] == 0 &&
				(curCol + 1 == gr[0].length //at bot edge
				|| gr[curRow][curCol + 1] != val)){// at bot right
			return gr[curRow + num][curCol] == 0;//return can move
		}
		if (gr[curRow + 1][curCol] == 0 &&// at the right side
				gr[curRow + num][curCol] == 0//can move that 'num' spaces
				&& ( gr[curRow][curCol + 1] == val)){//there is another row
			return canMoveRight(gr, val, curRow, curCol + 1, num);//curRow free, check next level
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
		for (int j = 0; j < height; ++j){//fix the 2 for loops positions
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

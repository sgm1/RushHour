package logic;

import java.util.HashMap;
import java.util.LinkedList;

public class RushSolver extends Thread{
	private static int[] dirs;
	private static HashMap<GridState, GridState> states;//k = curSate, v = Hint
	private static LinkedList<GridState> toProcess;
	
	private static class GridState{//int[][] wrapper
		private final int [][] daMap;
		
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
		dirs = carDirs.clone();
		toProcess = new LinkedList<GridState>();
		toProcess.addFirst(new GridState(initGrid));
	}
	
	@Override
	public void run(){
		while(!toProcess.isEmpty()){
			enumerate(toProcess.removeLast().getGrid());//clone does deep copy
		}
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
		int val;
		for (int j = 0; j < gr.length; ++j){//fix the 2 for loops positions
			for (int k = 0; k < gr[0].length; ++k){
				val = gr[j][k];
				if (val > 0 && !indexIsDone[val - 1]){
					indexIsDone[val - 1] = true;
					tryMoveLeft(gr.clone(), val, j, k, dirs[val - 1]);
					tryMoveRight(gr.clone(), val, j, k, dirs[val - 1]);
					tryMoveUp(gr.clone(), val, j, k, dirs[val - 1]);
					tryMoveDown(gr.clone(), val, j, k, dirs[val - 1]);
				}
			}
		}
		
	}

}

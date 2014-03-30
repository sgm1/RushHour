package logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.LinkedList;

public class RushSolver extends Thread{
	private int[] dirsAllowed;
	private boolean isSolved;
	private int solveXSide;
	private ActionListener eventHandler;
	private HashSet<GridState> visited;
	private GridState lastUsed;
	private GridState lastStep;//just in case it isn't lastUsed
	private LinkedList<GridState> toProcess;
	private LinkedList<Triple<Integer,Integer,Integer>> solMoves;

	private class GridState {
		private int [][] daMap;
		private int hashVal;
		private GridState fromState;
		private Triple<Integer, Integer, Integer>  source;

		public GridState(int [][] gr, GridState numSteps, Triple<Integer, Integer, Integer>  fromDat){
			daMap = clone2D(gr);
			int primes[] = {401, 919};//good hash distribution?
			fromState = numSteps;
			source = fromDat;

			int count = 0;
			for (int i = 0; i < daMap.length; ++i){
				for (int j = 0; j < daMap[0].length; ++j){
					hashVal += count * primes[j % 2] * daMap[i][j];
					count++;
					hashVal %= Integer.MAX_VALUE / 4;
				}
			}
		}

		public GridState getFromState(){
			return fromState;
		}

		public Triple<Integer, Integer, Integer> getFromTransition(){
			return source;
		}

		@Override
		public boolean equals(Object other){
			if (!(other instanceof GridState))
				return false;
			GridState o = (GridState)other;
			if (this  == o)//same object
				return true;
			for (int i = 0; i < daMap.length; i++) {
				for (int j = 0; j < daMap[0].length; j++) {
					if (o.daMap[i][j] != this.daMap[i][j]){
						return false;
					}
				}
			}
			return true;
		}

		@Override
		public int hashCode(){
			return hashVal;
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
		printSectors(initGrid);
		lastUsed = null;
		dirsAllowed = new int[carDirs.length + 1];
		for (int i = 1; i < dirsAllowed.length; ++i){
			dirsAllowed[i] = carDirs[i - 1];
		}
		solveXSide = initGrid.length - 1;// left
		//solveXSide = 0;// right side
		if (dirsAllowed[1] == 1){//sanity check
			boolean throwIll = true;
			for (int i = 0; i < initGrid[0].length; ++i){
				if (initGrid[solveXSide][i] == 1){
					throwIll = false;
					break;
				}
			}
			if (throwIll)
				throw new IllegalArgumentException("First block must be able to move right");
		}

		eventHandler = null;
		toProcess = new LinkedList<GridState>();
		visited = new HashSet<GridState>();
		solMoves = new LinkedList<Triple<Integer,Integer,Integer>>();
		toProcess.addFirst(new GridState(initGrid, null, null));
	}

	private void backTracePath(GridState end){
		if (end != null){
			backTracePath(end.getFromState());
			solMoves.add(end.getFromTransition());
			//printSectors(end.getGrid());
		}
	}


	public boolean isSolvable(){
		return isSolved;
	}

	public LinkedList<Triple<Integer,Integer,Integer>> getMoves(){
		return solMoves;
	}

	@Override
	public void run(){
		int steps = 0;
		while(!toProcess.isEmpty()){
			GridState temp = toProcess.getLast();
			lastUsed = temp;
			//printSectors(temp.getGrid());
			checkIsSolution(temp);
			if (isSolved){
				sendEvent();
				return;
			}
			enumerate(steps, temp.getGrid());
			lastUsed = temp;
			visited.add(temp);
			if (!toProcess.isEmpty()){
				toProcess.removeLast();
			}
		}
		if (!isSolved){
			sendEvent();
			return;
		}

		backTracePath(lastStep);
		System.out.println("BT done");
		if(eventHandler == null){
			throw new IllegalArgumentException("Needs an RushHourGame");
		}
		sendEvent();
	}

	private void sendEvent(){
		ActionEvent message = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null);
		eventHandler.actionPerformed(message);
	}

	public void addActionListioner(ActionListener e){
		eventHandler = e;
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
				return false;
		}
		if (x + num >= gr.length)//too long a stride
			return false;
		//x + num in bound

		if (y == gr[0].length - 1)
			return gr[x + num][y] == 0;
		if (gr[x + num][y] != 0)
			return false;
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
		while (gr[x][y + 1] == val){ // too far up in block
			++y;// traverse right
			if (y == gr[0].length - 1)//reached edge
				return false;
		}
		if (y + num >= gr[0].length)//too long a stride
			return false;
		//x + num in bound

		if (x == gr.length - 1)
			return gr[x][y + num] == 0;
		if (gr[x][y + num] != 0)
			return false;
		while (gr[x + 1][y] == val){//there is another row
			x++;
			if (gr[x][y + num] != 0)
				return false;
			if (x == gr.length - 1)
				break;
		}
		return true;//else can't move, base case 2
	}


	private void checkIsSolution(GridState gr) {
		int [][] grid = gr.getGrid();
		//int lx = 0;
		for (int y = 0; y < grid[0].length; ++y){
			if (grid[solveXSide][y] == 1){// if last x has value 1, assume right side
				isSolved = true;
				lastStep = gr;
				visited.add(gr);
				toProcess.clear();
			}
		}
	}

	private void tryMoveLeft(int steps, int[][] gr, int val, int x, int y, int allowedDir){
		if (allowedDir == 1)//vert
			return;
		//System.out.println("In tryMoveLeft ");
		int numSpaces = 1;
		int height = gr.length, width = gr[0].length;
		int [][] temp;
		while(canMoveLeft(gr, val, x, y, numSpaces)){
			temp = clone2D(gr);
			for (int tX = 0; tX < width; ++tX){
				for (int tY = 0; tY < height; ++tY){//left right to prevent overwrite
					if (gr[tX][tY] == val){//"map" original into temp transformed
						temp[tX][tY] = 0;
						temp[tX - numSpaces][tY] = gr[tX][tY];
					}
				}
			}
			Triple<Integer, Integer, Integer>  transitionInfo =
					new Triple<Integer, Integer, Integer> (val, 4, numSpaces);
			GridState newState = new GridState(temp, lastUsed, transitionInfo);
			checkIsSolution(newState);
			if (!isSolved && !visited.contains(newState)){
				//System.out.println("Hi thr l");
				visited.add(newState);
				toProcess.addFirst(newState);
			}
			//add new grid

			++numSpaces;
		}

		// move down to check width
	}

	private void tryMoveRight(int steps, int[][] gr, int val, int x, int y, int allowedDir){
		if (allowedDir == 1)
			return;
		//System.out.println("In tryMoveRight...");
		int numSpaces = 1;
		int height = gr.length, width = gr[0].length;
		int [][] temp;
		while(canMoveRight(gr, val, x, y, numSpaces)){
			temp = clone2D(gr);
			for (int tX = width - 1; tX >= 0; --tX){
				for (int tY = 0; tY < height; ++tY){//left right to prevent overwrite
					if (gr[tX][tY] == val){//"map" original into temp transformed
						temp[tX][tY] = 0;
						temp[tX + numSpaces][tY] = gr[tX][tY];
					}
				}
			}
			Triple<Integer, Integer, Integer>  transitionInfo =
					new Triple<Integer, Integer, Integer> (val, 6, numSpaces);
			GridState newState = new GridState(temp, lastUsed, transitionInfo);
			checkIsSolution(newState);
			if (!isSolved && !visited.contains(newState)){
				//System.out.println("Hi thr r");
				visited.add(newState);
				toProcess.addFirst(newState);
			}

			++numSpaces;
		}
		// parse right
		// move down to check width
	}

	private void tryMoveUp(int steps, int[][] gr, int val, int x, int y, int allowedDir){
		if (allowedDir == 0)
			return;
		//System.out.println("In tryMoveUp...");
		int numSpaces = 1;
		int height = gr.length, width = gr[0].length;
		int [][] temp;
		while(canMoveUp(gr, val, x, y, numSpaces)){
			temp = clone2D(gr);
			for (int tX = 0; tX < width; ++tX){
				for (int tY = 0; tY < height; ++tY){//left right to prevent overwrite
					if (gr[tX][tY] == val){//"map" original into temp transformed
						temp[tX][tY] = 0;
						temp[tX][tY - numSpaces] = gr[tX][tY];
					}
				}
			}
			Triple<Integer, Integer, Integer>  transitionInfo =
					new Triple<Integer, Integer, Integer> (val, 8, numSpaces);
			GridState newState = new GridState(temp, lastUsed, transitionInfo);
			checkIsSolution(newState);
			if (!isSolved && !visited.contains(newState)){
				//System.out.println("Hi thr u");
				visited.add(newState);
				toProcess.addFirst(newState);
			}
			//add new grid

			++numSpaces;
		}

		// move right to check width
	}

	private void tryMoveDown(int steps, int[][] gr, int val, int x, int y, int allowedDir){
		if (allowedDir == 0)
			return;
		//System.out.println("In tryMoveDown...");
		int numSpaces = 1;
		int height = gr.length, width = gr[0].length;
		int [][] temp;
		while(canMoveDown(gr, val, x, y, numSpaces)){
			temp = clone2D(gr);
			for (int tX = x; tX < width; ++tX){
				for (int tY = height - 1; tY >= 0; --tY){//bot to top to prevent overwrite
					if (gr[tX][tY] == val){//"map" original into temp transformed
						temp[tX][tY] = 0;
						temp[tX][tY + numSpaces] = gr[tX][tY];
					}
				}
			}
			Triple<Integer, Integer, Integer>  transitionInfo =
					new Triple<Integer, Integer, Integer> (val, 2, numSpaces);
			GridState newState = new GridState(temp, lastUsed, transitionInfo);
			checkIsSolution(newState);
			if (!isSolved && !visited.contains(newState)){
				//System.out.println("Hi thr d");
				visited.add(newState);
				toProcess.addFirst(newState);
			}

			++numSpaces;
		}
		// parse down
		// move right to check width
	}

	private void enumerate(int steps, int[][] gr) {
		boolean [] indexIsDone = new boolean[dirsAllowed.length];//initializes to false
		for (int i = 0; i < indexIsDone.length; ++i){
			indexIsDone[i] = false;//safe measure
		}
		int width = gr[0].length, height = gr.length;
		int val;
		//System.out.println("CurState: ");
		//printSectors(curState.getGrid());
		for (int j = 0; j < height; ++j) {
			for (int k = 0; k < width; ++k) {
				val = gr[j][k];
				if (val > 0 && !indexIsDone[val]) {
					tryMoveLeft(steps, gr, val, j, k, dirsAllowed[val]);
					tryMoveRight(steps, gr, val, j, k, dirsAllowed[val]);
					tryMoveUp(steps, gr, val, j, k, dirsAllowed[val]);
					tryMoveDown(steps, gr, val, j, k, dirsAllowed[val]);
					indexIsDone[val] = true;
				}
			}
		}

	}

}

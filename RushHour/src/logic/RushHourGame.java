package logic;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.Timer;

import launcher.CarRect;
import launcher.GameMenu;
import launcher.GamePanel;
import launcher.MainFrame;

/**
 * Creates the GUI for the game to
 * use and links to the solver
 * 
 * @author Shanon Mathai
 * @author Mike Albanese
 */
public class RushHourGame implements ActionListener{
	private final int secWidth, secHeight;
	private int isSovling = 0;
	//TODO Lock other until done (2), unless not solving (0)
	private RushSolver solver;
	private ArrayList<CarRect> cars = new ArrayList<CarRect>();
	private GamePanel GUIPanel;
	private int stepsPerMove;
	private int stepsInMove;
	private int carNum;
	private Point curfrom;
	private Point curTo;
	private int[]dirs;//quick look up, initializes to zero
	private int[][] sector;//quick look up, initializes to zero
	private LinkedList<Triple<Integer, Integer, Integer>> movesToSolve;
	private Timer solveAniTimer;
	private boolean solveActivated;
	private boolean isHint;

	/**
	 * Creates the game and the GUI for the game aspect
	 * 
	 * @param carlist "Cars" read in from file
	 */
	public RushHourGame(ArrayList<CarRect> carlist){
		solveActivated = false;
		isSovling = 0;
		secWidth = CarRect.getTileSize();
		secHeight = CarRect.getTileSize();
		stepsPerMove = 20;
		stepsInMove = 0;
		cars = carlist;
		dirs = new int[carlist.size()];
		for (int i = 0; i < carlist.size(); ++i){
			dirs[i] = carlist.get(i).getDir();
		}
		sector = new int[GamePanel.getTileWidth()][GamePanel.getTileHeight()];
		setSectors();
		printSectors();

		GUIPanel = new GamePanel(MainFrame.getWinWidth(), MainFrame.getWinHeight(), this);
		GUIPanel.setCars(cars);
	}

	/**
	 * Sets the sector representation of the current board
	 */
	private void setSectors(){//TODO Change to support oversized blocks? 
		int x, y, w, h;

		sector = new int[GamePanel.getTileWidth()][GamePanel.getTileHeight()];
		for (int i = 0; i < cars.size(); i++){
			x = cars.get(i).x / CarRect.getTileSize();
			y = cars.get(i).y / CarRect.getTileSize();
			w = cars.get(i).width / CarRect.getTileSize();// -1 for the int div round off
			h = cars.get(i).height / CarRect.getTileSize();// -1 for the int div round off
			for (int j = 0; j < w; ++j){
				for (int k = 0; k < h; ++k){
					sector[x + j][y + k] = i + 1;
				}
			}
		}
	}

	/**
	 * Initiates the thread to handle the
	 * computation for the solving of the 
	 * current board
	 */
	public void solve(){
		isHint = false;
		if (isSovling == 1){
			System.out.println("Is solving solve");
			return;
		}
		isSovling = 1;
		setSectors();
		System.out.println("solving..");
		try{
			solver = new RushSolver(sector, dirs);
		}catch(IllegalArgumentException e){
			System.out.println("Z piece canot move horiziontal, unsolveable");
			GameMenu.skipLevel();
			return;
		}
		solver.addActionListioner(this);
		solver.start();
	}

	/**
	 * Prints the array representation of the 
	 * current grid
	 */
	private void printSectors(){
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
	 * Returns the GamePanel that is currently being used
	 * in this instance
	 * @return
	 */
	public GamePanel getPanel(){
		return GUIPanel;
	}

	/**
	 * Handles the threaded events
	 * 
	 */ @Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == solver){
			//DONE Prevent solver before this is called
			movesToSolve = solver.getMoves();
			if (!solver.isSolvable()){
				System.out.println("***No solution***");
				GameMenu.skipLevel();
				return;
			}
			if (solver.isSolvable()){
				System.out.println("***Solvable***");
			}
			System.out.println("Solve activated.");
			solveActivated=false;
			//GamePanel.stopGame();
			startSolveAnimation();
		} 
		else if (e.getSource() == solveAniTimer){
			if (MainFrame.isSolved()){
				isSovling = 0;
				return;
			}
			//TODO Prevent another solve click?
			if (movesToSolve.isEmpty()){
				isSovling = 0;
				solveAniTimer.stop();
				return;
			}
			Triple<Integer, Integer, Integer> curMove = movesToSolve.remove();
			GUIPanel.movePiece(curMove);
			if (movesToSolve.isEmpty()){
				isSovling = 0;
				solveAniTimer.stop();
				return;
			}
			if (isHint && curMove != null){
				isSovling = 0;
				solveAniTimer.stop();
				return;
			}
			if (isHint)
				actionPerformed(e);
		}
	}

	/**
	 * Launches the animation for the solve
	 */
	private void startSolveAnimation(){
		if (movesToSolve.isEmpty()){
			isSovling = 0;
			GameMenu.nextLevel();
			return;
		}
		solveAniTimer = new Timer(1000, this);
		solveAniTimer.setInitialDelay(0);//fire first move right away (null)
		solveAniTimer.start();
	}

	/**
	 * Launches the hint event
	 */
	public void hint() {
		isHint = true;
		if (isSovling == 1){
			System.out.println("Is solving hint");
			return;
		}
		isSovling = 1;
		setSectors();
		System.out.println("solving..");
		try{
			solver = new RushSolver(sector, dirs);
		}catch(IllegalArgumentException e){
			System.out.println("Z piece canot move horiziontal, unsolveable");
			isSovling = 0;
			GameMenu.skipLevel();
			return;
		}
		solver.addActionListioner(this);
		solver.start();
	}


}

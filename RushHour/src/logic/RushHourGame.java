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

	public RushHourGame(ArrayList<CarRect> carlist){
		solveActivated = false;
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

		//solve();
	}

	private void setSectors(){//TODO Change to support oversized blocks? 
		int x, y, w, h;
		for (int i = 0; i < cars.size(); i++){
			x = cars.get(i).x;
			y = cars.get(i).y;
			w = cars.get(i).width - 1;// -1 for the int div round off
			h = cars.get(i).height - 1;// -1 for the int div round off

			if (w  < secWidth && h < secWidth){
				sector[(x + w) / secWidth][(y + h) / secHeight] = i + 1;
				w -= secWidth;
			}

			//following assumes 1 side will be of width 1
			while(w > secWidth){
				sector[(x + w) / secWidth][(y + h) / secHeight] = i + 1;
				w -= secWidth;
				if (w  < secWidth){
					sector[(x + w) / secWidth][(y + h) / secHeight] = i + 1;
					w -= secWidth;
				}
			}
			//following assumes 1 side will be of width 1
			while(h > secHeight){
				sector[(x + w) / secWidth][(y + h) / secHeight] = i + 1;
				h -= secHeight;
				if (h < secHeight){
					sector[(x + w) / secWidth][(y + h) / secHeight] = i + 1;
					h -= secHeight;
				}
			}
		}
	}

	public void solve(){
		isSovling = 1;
		setSectors();
		try{
			solver = new RushSolver(sector, dirs);
		}catch(IllegalArgumentException e){
			System.out.println("Z piece canot move horiziontal, unsolveable");
			GameMenu.skipLevel();
			return;
		}
		System.out.println("solving..");
		solver.addActionListioner(this);
		solver.start();


	}

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

	public GamePanel getPanel(){
		return GUIPanel;
	}

	@Override
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
			//TODO when movesToSolve is empty, show dialog message and return control back to the user
			//TODO when dialog window is closed, open up next level
			if (movesToSolve.isEmpty()){
				solveAniTimer.stop();
				//MainFrame.puzzleSolved();
				//GameMenu.nextLevel();
				return;
			}
			Triple<Integer, Integer, Integer> curMove = movesToSolve.remove();
			GUIPanel.movePiece(curMove);
			/*
			if (curMove != null){
				
				System.out.println("Move piece " + curMove.from +
						" in dir " + curMove.dir +
						" by " + curMove.spaces + " space(s)");
						
			}
			*/
			if (movesToSolve.isEmpty()){
				solveAniTimer.stop();
				//MainFrame.puzzleSolved();
				//GameMenu.nextLevel();
				return;
			}
		}
	}

	private void startSolveAnimation(){
		solveAniTimer = new Timer(1000, this);
		solveAniTimer.setInitialDelay(0);//fire first move right away (null)
		solveAniTimer.start();
	}


}

package logic;

import java.util.ArrayList;
import java.util.LinkedList;

import launcher.CarRect;
import launcher.GamePanel;
import launcher.MainFrame;

public class RushHourGame {
	private final int secWidth, secHeight;
	private static int isSovling = 0;
	//TODO Lock other until done (2), unless not solving (0)
	private ArrayList<CarRect> cars = new ArrayList<CarRect>();
	private GamePanel GUIPanel;
	private int[]dirs;//quick look up, initializes to zero
	private int[][] sector;//quick look up, initializes to zero
	private LinkedList<Triple<Integer, Integer, Integer>> movesToSolve;

	public RushHourGame(ArrayList<CarRect> carlist){
		secWidth = CarRect.getTileSize();
		secHeight = CarRect.getTileSize();
		cars = carlist;
		dirs = new int[carlist.size()];
		for (int i = 0; i < carlist.size(); ++i){
			dirs[i] = carlist.get(i).getDir();
		}
		sector = new int[GamePanel.getTileWidth()][GamePanel.getTileHeight()];
		setSectors();
		printSectors();

		//solver(sector);//TODO returns steps

		GUIPanel = new GamePanel(MainFrame.getWinWidth(), MainFrame.getWinHeight(), this);
		GUIPanel.setCars(cars);	
		solve();
	}
	
	private void setSectors(){
		int x, y, w, h;
		for (int i = 0; i < cars.size(); i++){//TODO Make this cleaner?
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
		//TODO need to check if already won in start state
		setSectors();
		RushSolver temp = new RushSolver(sector,dirs);
		temp.start();
		while(temp.isAlive()){}//stall current thread?
		movesToSolve = temp.getMoves();
		if (!temp.isSolvable()){
			System.out.println("***No solution***");
			return;
		}
		if (temp.isSolvable()){
			System.out.println("***Solvable***");
			if (!movesToSolve.isEmpty())
				movesToSolve.remove();
			for (int i = 0; i < movesToSolve.size(); ++i){
				System.out.println(movesToSolve.get(0).dir + " " +
						movesToSolve.get(0).from + " " +
						movesToSolve.get(0).spaces);
				//TODO Use this information to finally move the pieces
				//-dir is direction
				//		think numpad 2 = down, 8 = up, 4 = left, 6 = right
				//-from is the piece to move
				//-space tell you how many
			}
		}
		
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

}

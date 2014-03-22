package logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import launcher.CarRect;
import launcher.GamePanel;
import launcher.MainFrame;

public class RushHourGame implements ActionListener{
	private final int secWidth, secHeight;
	private int isSovling = 0;
	private RushSolver solver;
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

		GUIPanel = new GamePanel(MainFrame.getWinWidth(), MainFrame.getWinHeight(), this);
		GUIPanel.setCars(cars);
		
		solve();
	}
	
	private void setSectors(){
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
		}catch(Exception e){
			System.out.println("Z peive canot move horiziontal, unsolveable");
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
		System.out.println("ASDASD");
		if (e.getSource() == solver){
			//TODO Prevent solver before this is called
			movesToSolve = solver.getMoves();
			if (!solver.isSolvable()){
				System.out.println("***No solution***");
				return;
			}
			if (solver.isSolvable()){
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
		
	}

}

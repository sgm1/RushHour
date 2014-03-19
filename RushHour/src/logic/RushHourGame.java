package logic;

import java.util.ArrayList;

import launcher.CarRect;
import launcher.GamePanel;
import launcher.MainFrame;

public class RushHourGame {
	private final int secWidth, secHeight;
	private ArrayList<CarRect> cars = new ArrayList<CarRect>();
	private GamePanel GUIPanel;
	private int[][] sector;//quick look up, initializes to zero

	public RushHourGame(ArrayList<CarRect> carlist){
		secWidth = CarRect.getTileSize();
		secHeight = CarRect.getTileSize();
		cars = carlist;
		sector = new int[GamePanel.getTileWidth()][GamePanel.getTileHeight()];
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
		printSectors();

		//solver(sector);//TODO returns steps

		GUIPanel = new GamePanel(MainFrame.getWinWidth(), MainFrame.getWinHeight(), this);
		GUIPanel.setCars(cars);
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

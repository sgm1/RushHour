package logic;

import launcher.CarRect;
import launcher.GamePanel;

public class RushHourGame {
	private final int secWidth, secHeight;
	private CarRect[] cars = new CarRect[20];
	private int len;
	private GamePanel GUIPanel;
	private int[][] sector = new int[6][6];//quick look up, initializes to zero

	public RushHourGame(int width, int height){
		secWidth = width / 6;
		secHeight = height /6;
		cars[0] = new CarRect(0, 0, secWidth, 2 * secHeight);// generate rects
		cars[1] = new CarRect(secWidth * 2, 0, secWidth, 2 * secHeight);// generate rects
		
		len = 2;//set size of list
		int x, y, w, h;
		for (int i = 0; i < len; i++){
			x = cars[i].x;
			y = cars[i].y;
			w = cars[i].width - 1;// -1 for the int div round off
			h = cars[i].height - 1;// -1 for the int div round off
			
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

		GUIPanel = new GamePanel(width, height, this);//generate GUI
		GUIPanel.setCars(cars, len);// make this a constructor param?
		//pass "cars" and "len" to the GamePanel
		// update 
	}
	
	private void printSectors(){
		for (int i = 0; i < 6; ++i){
			System.out.println();
			for (int j = 0; j < 6; ++j){
				System.out.print(sector[j][i] + " ");
			}
		}
	}
	
	public GamePanel getPanel(){
		return GUIPanel;
	}

}

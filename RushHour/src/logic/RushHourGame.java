package logic;

import java.util.ArrayList;

import launcher.CarRect;
import launcher.GamePanel;

public class RushHourGame {
	private final int secWidth, secHeight;
	private ArrayList<CarRect> cars = new ArrayList<CarRect>();
	private GamePanel GUIPanel;
	private int[][] sector = new int[6][6];//quick look up, initializes to zero

	public RushHourGame(int width, int height){
		secWidth = width / 6;
		secHeight = height /6;
		CarRect car1 = new CarRect(0,0,3,1,'1');
		cars.add(car1);
		CarRect car2 = new CarRect(2,2,1,2,'2');
		cars.add(car2);
		CarRect car3 = new CarRect(3,1,3,1,'3');
		cars.add(car3);
		CarRect car4 = new CarRect(5,0,1,1,'4');
		cars.add(car4);
		int x, y, w, h;
		for (int i = 0; i < cars.size(); i++){
			x = cars.get(i).x;
			y = cars.get(i).y;
			w = cars.get(i).width - 1;// -1 for the int div round off
			h = cars.get(i).height - 1;// -1 for the int div round off
			
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
		GUIPanel.setCars(cars);// make this a constructor param?
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

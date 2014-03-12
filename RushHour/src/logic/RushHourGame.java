package logic;

import launcher.CarRect;
import launcher.GamePanel;

public class RushHourGame {
	CarRect[] cars = new CarRect[20];
	GamePanel GUIPanel;
	int[] sector = new int[36];//quick look up, initializes to zero
	private final int secWidth, secHeight;
	int len;

	public RushHourGame(int width, int height){
		GUIPanel = new GamePanel(width, height);//generate GUI
		secWidth = width / 6;
		secHeight = height /6;
		cars[0] = new CarRect(0, 0, secWidth, 2 * secHeight);// generate rects
		cars[1] = new CarRect(secWidth * 2, 0, secWidth, 2 * secHeight);// generate rects
		
		//TODO MARK the SECTORS!!!
		
		len = 2;//set size of list
		GUIPanel.setCars(cars, len);
		//pass "cars" and "len" to the GamePanel
		// update 
	}
	
	public GamePanel getPanel(){
		return GUIPanel;
	}

}

package logic;

import launcher.CarRect;

public class RushHourGame {
	CarRect[] cars = new CarRect[20];
	int[] sector = new int[36];//quick look up, initializes to zero
	int len;

	public RushHourGame(int w, int h){
		len = 0;
		cars[0] = new CarRect(w / 6, h * 2 / 6);
	}

}

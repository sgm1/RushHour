package launcher;

import java.awt.Rectangle;
import java.util.Random;
import java.awt.Graphics;
import java.awt.Color;

@SuppressWarnings("serial")
public class CarRect extends Rectangle {
	
	private static int size = 50;
	private static Random temp = new Random();
	private Color color;
	private char symbol;
	
	public CarRect(int x, int y, int width, int height, char text) {
		super(x*size, y*size, width*size, height*size);
		color = new Color(temp.nextInt(255), temp.nextInt(255), temp.nextInt(255));
		symbol = text; 
	}
	
	public Color getColor(){
		return this.color;
	}
	
	public char getSymbol(){
		return this.symbol;
	}
	
	
}

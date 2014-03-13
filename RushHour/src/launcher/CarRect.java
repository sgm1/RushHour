package launcher;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

public class CarRect extends Rectangle {
	private static int size = 50;
	private static Random temp = new Random();
	private Color col;
	private boolean moveInHorizontal;
	private char symbol;
	
	public CarRect(int x, int y, int width, int height, char text) {
		super(x*size, y*size, width*size, height*size);
		moveInHorizontal = width > height;// move horizontal if wide
		col = new Color(temp.nextInt(255), temp.nextInt(255), temp.nextInt(255));
		symbol = text; 
	}
	
	public void draw(Graphics g){
		//g.setColor(col);
		//g.fillRect(this.x, this.y, this.width, this.height);
		g.drawRect(this.x, this.y, this.width, this.height);//why do this?
		g.setColor(col);
		g.fillRect(this.x, this.y, this.width, this.height);
		g.setColor(new Color(255-col.getRed(),255-col.getGreen(),255-col.getBlue()));
		g.drawString("" + this.getSymbol(),this.x + this.width/2 - 4, this.y + this.height/2 + 5);
		g.setColor(Color.black);
		
	}

	public void moveByPoint(Point point) {
		// TODO Auto-generated method stub
		if (moveInHorizontal)
			x = point.x;
		else
			y = point.y;
	}
	
	public char getSymbol(){
		return this.symbol;
	}
}

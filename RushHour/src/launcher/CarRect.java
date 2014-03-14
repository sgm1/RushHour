package launcher;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

public class CarRect extends Rectangle {
	private static int tileDim;
	private static Random temp = new Random();
	private boolean isGhosting;
	private Point realPoint;// for ghost
	private Color col;
	private int moveCapabilites;
	private int xS, yS;//xSector, ySector
	private char symbol;

	public CarRect(int x, int y, int width, int height, int dir, char text) {
		super(x * tileDim, y * tileDim, width * tileDim, height * tileDim);
		isGhosting = false;
		xS = x;
		yS = y;
		moveCapabilites = dir;// 0 = horizontal, 1 = vert, anything else = both
		col = new Color(temp.nextInt(255), temp.nextInt(255), temp.nextInt(255));
		symbol = text;
	}

	public void draw(Graphics g) {
		// g.setColor(col);
		// g.fillRect(this.x, this.y, this.width, this.height);
		g.drawRect(this.x, this.y, this.width, this.height);// why do this?
		g.setColor(col);
		g.fillRect(this.x, this.y, this.width, this.height);
		if((col.getRed()+col.getGreen()+col.getBlue())/3 > 127)
				g.setColor(Color.black);
			else
				g.setColor(Color.white);
		g.drawString("" + this.symbol, this.x + this.width / 2 - 4, this.y
				+ this.height / 2 + 5);
		g.setColor(Color.black);

	}

	public void dropByPoint(Point p) {
		moveByPoint(p);
		realPoint = null;
		isGhosting = false;
	}

	public void moveByPoint(Point p) {
		// TODO Auto-generated method stub
		if (!isGhosting) {
			realPoint = this.getLocation();
			isGhosting = true;
		}
		if (moveCapabilites == 0)
			x = p.x;
		else if (moveCapabilites == 1)
			y = p.y;
		else if (Math.abs(realPoint.x - p.x) > Math.abs(realPoint.y - p.y)) {
			x = p.x;
			y = realPoint.y;
		} else {
			x = realPoint.x;
			y = p.y;
		}
	}

	public char getSymbol() {
		return this.symbol;
	}
	
	public static void setTileSize(int width, int height){
		if(width>height)
			CarRect.tileDim = (MainFrame.getWinWidth()-2)/GamePanel.getTileWidth();
		else
			CarRect.tileDim = (MainFrame.getWinHeight()-2)/GamePanel.getTileHeight();
	}
	
	public static int getTileSize(){
		return tileDim;
	}
}

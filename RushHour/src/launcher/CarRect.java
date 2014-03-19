package launcher;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

public class CarRect extends Rectangle {
	private static int tileDim;
	private static Rectangle[] borders = new Rectangle[4]; 
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

	public void dropByPoint(Point p, CarRect[] cars) {
		moveByPoint(p, cars);
		x = ((x + tileDim / 2) / tileDim) * tileDim;
		y = ((y + tileDim / 2) / tileDim) * tileDim;
		realPoint = null;
		isGhosting = false;
	}

	public void moveByPoint(Point p, CarRect[] cars) {
		if (!isGhosting) {
			realPoint = this.getLocation();
			isGhosting = true;
		}
		int tempX = x;
		int tempY = y;
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
		double dis = Math.sqrt((tempX - x)*(tempX - x)+(tempY - y)*(tempY - y));

		for(CarRect t: cars){
			if ((t != this && this.intersects(t)) || dis > tileDim * 1.5){
				x = tempX;
				y = tempY;
			}
		}
		for(Rectangle t: borders){
			if ((t != this && this.intersects(t)) || dis > tileDim * 1.5){
				x = tempX;
				y = tempY;
			}
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
		borders[0] = new Rectangle(-tileDim, 0, tileDim, MainFrame.getWinHeight());
		borders[1] = new Rectangle(MainFrame.getWinWidth(), 0, tileDim, MainFrame.getWinHeight());
		borders[2] = new Rectangle(0, -tileDim, MainFrame.getWinWidth(), tileDim);
		borders[3] = new Rectangle(0, MainFrame.getWinHeight(), MainFrame.getWinWidth(), tileDim);
	}

	public static int getTileSize(){
		return tileDim;
	}
	
	public static Rectangle[] getBorders(){
		return borders;
	}
}

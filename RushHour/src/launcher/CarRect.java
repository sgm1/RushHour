package launcher;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

public class CarRect extends Rectangle {
	public final static int size = 50;
	private static Random temp = new Random();
	private boolean isGhosting;
	private Point realPoint;// for ghost
	private Color col;
	private int moveCapabilites;
	private int xS, yS;//xSector, ySector
	private char symbol;

	public CarRect(int x, int y, int width, int height, int dir, char text) {
		super(x * size, y * size, width * size, height * size);
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
		g.setColor(new Color(255 - col.getRed(), 255 - col.getGreen(),
				255 - col.getBlue()));
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
}

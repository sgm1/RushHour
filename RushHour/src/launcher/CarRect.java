package launcher;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class CarRect extends Rectangle {
	private Color col;
	private boolean moveInHorizontal;
	
	public CarRect(int x, int y, int w, int h) {
		super(x, y, w, h);
		moveInHorizontal = w > h;// move horizontal if wide
			
		col = new Color((int)(Math.random() * 256),
				(int)(Math.random() * 256),
				(int)(Math.random() * 256));
		// TODO Auto-generated constructor stub
	}
	
	public void draw(Graphics g){
		g.setColor(col);
		g.fillRect(this.x, this.y, this.width, this.height);
	}

	public void moveByPoint(Point point) {
		// TODO Auto-generated method stub
		if (moveInHorizontal)
			x = point.x;
		else
			y = point.y;
	}
}

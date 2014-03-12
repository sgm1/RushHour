package launcher;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class CarRect extends Rectangle {
	Color col;
	
	public CarRect(int x, int y, int w, int h) {
		super(x, y, w, h);
		col = new Color((int)(Math.random() * 256),
				(int)(Math.random() * 256),
				(int)(Math.random() * 256));
		// TODO Auto-generated constructor stub
	}
	
	public void draw(Graphics g){
		g.setColor(col);
		g.fillRect(this.x, this.y, this.width, this.height);
	}
}

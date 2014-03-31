package launcher;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

/**
 * Rectangle to represent the
 * cars in the GamePanel
 *  
 * @author Shanon Mathai
 * @author Mike Albanese
 */
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

	/**
	 * Get the movement capabilities
	 * where 0 means horizontal movement only,
	 * 1 means vertical only, 2 means both
	 * 
	 * @return 0, 1 or 2, as described above
	 */
	public int getDir(){
		return moveCapabilites;
	}

	/**
	 * Draw this Rectangle on the
	 * Graphic component
	 * @param g Graphics to draw on
	 */
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

	/**
	 * Handles the mouse release event
	 * if the CarRect was in use
	 * @param p Point to drop by
	 * @param cars All CarRects on the board
	 * @param fromSolver If Being called form the solver
	 */
	public void dropByPoint(Point p, CarRect[] cars, boolean fromSolver) {
		moveByPoint(p, cars, fromSolver);
		//System.out.println("Droped by: " + p.toString());
		x = ((x + tileDim / 2) / tileDim) * tileDim;
		y = ((y + tileDim / 2) / tileDim) * tileDim;
		realPoint = null;
		isGhosting = false;
	}

	/**
	 * Handles the drag event
	 * 
	 * @param p Point to move towards
	 * @param cars All CarRects on the board
	 * @param fromSolver If Being called form the solver
	 */
	public void moveByPoint(Point p, CarRect[] cars, boolean fromSolver) {
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
		
		if (fromSolver)
			dis = 0;

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

	/**
	 * Get the character representation of this
	 * block
	 * 
	 * @return Character used on this block
	 */
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

	/**
	 * Get the tileSize, should be same as GamePanel
	 * @return tileSize of the 
	 */
	public static int getTileSize(){
		return tileDim;
	}

	/**
	 * Get the border Rectangles to check out
	 * of bounds
	 * @return Rectangle array of the borders
	 */
	public static Rectangle[] getBorders(){
		return borders;
	}
}

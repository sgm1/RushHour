package launcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import logic.RushHourGame;
import logic.Triple;

/**
 * Is the GUI for the game aspect of the 
 * on the MainFrame
 * 
 * @author Shanon Mathai
 * @author Mike Albanese
 *
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel implements MouseInputListener {

	private int numCars = 4;
	//private List<CarRect> cars = new ArrayList<CarRect>();
	private CarRect[] cars = new CarRect[0];
	private Point lastPressOffset;
	private RushHourGame game;//to communicate key events
	//should communicate on release only
	private CarRect carPressed;
	private static int tileWidth = 10;//uninitialized once we read in files
	private static int tileHeight = 10;//
	private static double savedX = -1;
	private static double savedY = -1;
	private static int numMoves;
	private static boolean gameRunning = true;

	/**
	 * Creates the GUI for the blocks to be put
	 * 
	 * @param w Width of this panel
	 * @param h Height of this panel
	 * @param g Game to attach to this GUI
	 */
	public GamePanel(int w, int h, RushHourGame g) {
		super();
		game = g;
		gameRunning = true;
		numMoves = 0; savedX = -1; savedY = -1;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.white);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	/**
	 * Handles the initial click for the mouse
	 * dragged
	 * @param e MouseEvent for the location
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if(gameRunning){
			CarRect t = onCar(e.getPoint());
			if (t != null){
				lastPressOffset = new Point(t.x - e.getPoint().x, t.y - e.getPoint().y);
				savedX = t.getX();
				savedY = t.getY();
				carPressed = t;
			}
			else {
				lastPressOffset = null;
				carPressed = null;
			}
		}
	}
	
	public void movePiece(Triple<Integer,Integer,Integer> curMove){
		if (curMove == null) return;
		
		System.out.println("Move piece " + curMove.from +
				" in dir " + curMove.dir +
				" by " + curMove.spaces + " space(s)");
		
		if(gameRunning){
			CarRect t = cars[curMove.from - 1];
			System.out.println("from (x, y): (" + t.x + ", " + t.y + ")");
			//System.out.println(t.getLocation().toString());
			if (t != null){
				savedX = t.getX();
				savedY = t.getY();
				carPressed = t;
			}
			

			if (carPressed != null){
				int dx = 0,
					dy = 0;
				int direction = curMove.dir.intValue();
				int spaces = curMove.spaces.intValue();
				if (direction == 2){
					dx = 0;
					dy = spaces;
				} else if (direction == 8){
					dx = 0;
					dy = -spaces;
				} else if (direction == 6){
					dx = spaces;
					dy = 0;
				} else {
					dx = -spaces;
					dy = 0;
				}
				int x =  t.x + dx * CarRect.getTileSize();
				int y =  t.y + dy * CarRect.getTileSize();
				System.out.println("to (x, y): (" + x + ", " + y + ")");
				t.x = x;
				t.y = y;
				carPressed.dropByPoint(new Point(x, y),cars, true);
				if(carPressed.getX() != savedX || carPressed.getY() != savedY)
					numMoves++;
				repaint();
			}
			if((cars[0]==carPressed) && (cars[0].getX() + cars[0].getWidth() == CarRect.getTileSize() * tileWidth)){
				gameRunning = false;
				GameMenu.nextLevel();
			}
			lastPressOffset = null;
			carPressed = null;

		}
	}

	/**
	 * Checks to see if point is on 
	 * any CarRect
	 * @param p Point to check
	 * @return CarRect at point, or null if none exist at point
	 */
	private CarRect onCar(Point p){
		for (CarRect t: cars){
			if (t.contains(p))
				return t;
		}
		return null;
	}

	/**
	 * Handles the mouse release when dragged
	 * 
	 * @param e MouseEvent for position
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		if(gameRunning){
			if (carPressed != null){
				int x =  lastPressOffset.x + e.getPoint().x;
				int y =  lastPressOffset.y + e.getPoint().y;
				carPressed.dropByPoint(new Point(x, y),cars, false);
				if(carPressed.getX() != savedX || carPressed.getY() != savedY)
					numMoves++;
				repaint();
			}
			if((cars[0]==carPressed) && (cars[0].getX() + cars[0].getWidth() == CarRect.getTileSize() * tileWidth)){
				gameRunning = false;
				GameMenu.nextLevel();
			}
			lastPressOffset = null;
			carPressed = null;

		}
	}
	
	/**
	 * Draws the CarRects on the GUI
	 * as well as grid lines
	 * @param g Graphics component to draw on
	 */
	@Override
	public void paint(Graphics g){
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		for(int i=0; i<tileWidth+1;i++)
			g.drawLine(CarRect.getTileSize()*i, 0, CarRect.getTileSize()*i, CarRect.getTileSize()*tileHeight);
		for(int i=0; i<tileHeight+1;i++)
			g.drawLine(0, CarRect.getTileSize()*i, CarRect.getTileSize()*tileWidth,CarRect.getTileSize()*i);
		MainFrame.setMoveCounter(numMoves);
		for (CarRect t: cars) {
			t.draw(g);
		}
	}

	/**
	 * While mouse is down, handles the drag 
	 * events to update GUI
	 * @param e MouseEvent for the position
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		if (carPressed != null){
			int x =  lastPressOffset.x + e.getPoint().x;
			int y =  lastPressOffset.y + e.getPoint().y;
			carPressed.moveByPoint(new Point(x, y),cars, false);
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	/**
	 * Sets the CarRects of this GUI
	 * after read in
	 * @param crs CarRects to use
	 */
	public void setCars(ArrayList<CarRect> crs) {
		cars = (CarRect[]) crs.toArray(new CarRect[0]);
	}

	/**
	 * Get the tile width
	 * @return Tile width in pixels
	 */
	public static int getTileWidth(){
		return tileWidth;
	}

	/**
	 * Sets the game running to false
	 */
	public static void stopGame(){
		gameRunning=false;
	}

	/**
	 * Get the tile height
	 * @return Height of tile in pixels
	 */
	public static int getTileHeight(){
		return tileHeight;
	}

	/**
	 * Get the CarRects of this GUI
	 * @return Array of the CarRects
	 */
	public CarRect[] getCars(){
		return this.cars;
	}

	/**
	 * Get the number of moves thus far
	 * @return Number of moves
	 */
	public static int getNumMoves(){
		return numMoves;
	}

	/**
	 * Reset the number of moves
	 */
	public static void resetNumMoves(){
		numMoves = 0;
	}

	/**
	 * Set the tile width
	 * @param width Width to set in pixels
	 */
	public static void setTileWidth(int width){
		tileWidth = width;
	}


	/**
	 * Set the tile height
	 * @param height Height to set in pixels
	 */
	public static void setTileHeight(int height){
		tileHeight = height;
	}
}

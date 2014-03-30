package launcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import logic.RushHourGame;
import logic.Triple;
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
	public void mouseEntered(MouseEvent e) {//arrow for hover?

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

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
				lastPressOffset = new Point(2, 2);
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
				int x =  t.x + dx * CarRect.getTileSize() + lastPressOffset.x;
				int y =  t.y + dy * CarRect.getTileSize() + lastPressOffset.y;
				System.out.println("to (x, y): (" + x + ", " + y + ")");
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

	private CarRect onCar(Point p){
		for (CarRect t: cars){
			if (t.contains(p))
				return t;
		}
		return null;
	}

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

	public void setCars(ArrayList<CarRect> crs) {
		cars = (CarRect[]) crs.toArray(new CarRect[0]);
	}

	public static int getTileWidth(){
		return tileWidth;
	}

	public static void stopGame(){
		gameRunning=false;
	}

	public static int getTileHeight(){
		return tileHeight;
	}

	public CarRect[] getCars(){
		return this.cars;
	}

	public static int getNumMoves(){
		return numMoves;
	}

	public static void resetNumMoves(){
		numMoves = 0;
	}

	public static void setTileWidth(int width){
		tileWidth = width;
	}

	public static void setTileHeight(int height){
		tileHeight = height;
	}
}

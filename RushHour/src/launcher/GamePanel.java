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
public class GamePanel extends JPanel implements MouseInputListener {

	private int numCars = 4;
	//private List<CarRect> cars = new ArrayList<CarRect>();
	private CarRect[] cars = new CarRect[0];
	private Point lastPressPoint;
	private RushHourGame game;//to communicate key events
								//should communicate on release only
	private CarRect carPressed;
	private static int tileWidth=10;
	private static int tileHeight=10;
	
	public GamePanel(int w, int h, RushHourGame g) {
		super();
		game = g;
		//CarRect.setTileSize(tileWidth,tileHeight);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.white);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {//arrow for hover?
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		CarRect t = onCar(e.getPoint());
		if (t != null){
			lastPressPoint = new Point(t.x - e.getPoint().x, t.y - e.getPoint().y);
			
			carPressed = t;
		}
		else {
			lastPressPoint = null;
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
		// TODO Auto-generated method stub
		if (carPressed != null){
			int x =  lastPressPoint.x + e.getPoint().x;
			int y =  lastPressPoint.y + e.getPoint().y;
			carPressed.dropByPoint(new Point(x, y),cars);
			repaint();
		}
		lastPressPoint = null;
		carPressed = null;
	}

	@Override
	public void paint(Graphics g){
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		for(int i=0; i<tileWidth+1;i++)
			g.drawLine(CarRect.getTileSize()*i, 0, CarRect.getTileSize()*i, CarRect.getTileSize()*tileHeight);
		for(int i=0; i<tileHeight+1;i++)
			g.drawLine(0, CarRect.getTileSize()*i, CarRect.getTileSize()*tileWidth,CarRect.getTileSize()*i);
		//System.out.println("" + cars.length);
		for (CarRect t: cars) {
			t.draw(g);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if (carPressed != null){
			int x =  lastPressPoint.x + e.getPoint().x;
			int y =  lastPressPoint.y + e.getPoint().y;
			carPressed.moveByPoint(new Point(x, y),cars);
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void setCars(ArrayList<CarRect> crs) {
		cars = (CarRect[]) crs.toArray(new CarRect[0]);
	}
	
	public static int getTileWidth(){
		return tileWidth;
	}
	
	public static int getTileHeight(){
		return tileHeight;
	}
	
	public CarRect[] getCars(){
		return this.cars;
	}

}

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

	private int numCars;
	//private List<CarRect> cars = new ArrayList<CarRect>();
	private CarRect[] cars;
	private Point lastPressPoint;
	private RushHourGame game;//to communicate key events
								//should communicate on release only
	private CarRect carPressed;

	public GamePanel(int w, int h, RushHourGame g) {
		super();
		game = g;
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
			carPressed.dropByPoint(new Point(x, y));
			repaint();
		}
		lastPressPoint = null;
		carPressed = null;
	}

	@Override
	public void paint(Graphics g){
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		
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
			carPressed.moveByPoint(new Point(x, y));
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

}

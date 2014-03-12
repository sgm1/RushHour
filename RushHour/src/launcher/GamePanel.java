//package launcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.*;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

public class GamePanel extends JPanel implements MouseInputListener {

	private int numCars;
	private List<CarRect> cars = new ArrayList<CarRect>();

	public GamePanel(int w, int h) {
		super();
		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.white);
		numCars = 4;
		CarRect car1 = new CarRect(0,0,3,1,'1');
		cars.add(car1);
		CarRect car2 = new CarRect(2,2,1,2,'2');
		cars.add(car2);
		CarRect car3 = new CarRect(3,1,3,1,'3');
		cars.add(car3);
		CarRect car4 = new CarRect(5,0,1,1,'4');
		cars.add(car4);
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

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paint(Graphics g){
		for(int i=0;i<numCars;i++){
			Color carColor = cars.get(i).getColor();
			g.drawRect(cars.get(i).x, cars.get(i).y, cars.get(i).width, cars.get(i).height);
			g.setColor(carColor);
			g.fillRect(cars.get(i).x, cars.get(i).y, cars.get(i).width, cars.get(i).height);
			g.setColor(new Color(255-carColor.getRed(),255-carColor.getGreen(),255-carColor.getBlue()));
			g.drawString("" + cars.get(i).getSymbol(),cars.get(i).x + cars.get(i).width/2 - 4, cars.get(i).y + cars.get(i).height/2 + 5);
			g.setColor(Color.black);
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}

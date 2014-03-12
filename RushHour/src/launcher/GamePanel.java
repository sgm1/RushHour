package launcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

public class GamePanel extends JPanel implements MouseInputListener {
	CarRect[] cars;

	public GamePanel(int w, int h) {
		super();
		this.setPreferredSize(new Dimension(w, h));
		this.setBackground(Color.white);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {// arrow for hover?
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
	public void paint(Graphics g) {
		g.setColor(Color.blue);
		for (CarRect temp: cars)
			temp.draw(g);

	}
	
	public void setCars(CarRect[] cs, int l){
		cars = new CarRect[l];
		for (int i = 0; i < l; i++){
			cars[i] = cs[i];
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

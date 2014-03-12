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
	
	private class myRect extends Rectangle{
		Random temp = new Random();
		public myRect(){
			super(10, 10, 50, 50);
		}
		
		public void draw(Graphics g){
			g.setColor(new Color(temp.nextInt(255), temp.nextInt(255), temp.nextInt(255)));
			g.fillRect(this.x, this.y, this.width, this.height);
		}
	}
	
	@Override
	public void paint(Graphics g){
		g.setColor(Color.blue);
		myRect temp = new myRect();
		temp.draw(g);
		
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

package launcher;

import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import launcher.GamePanel;
import logic.RushHourGame;

public class MainFrame extends JFrame implements Runnable{
	private static int winWidth = 602;
	private static int winHeight = 602;
	private ArrayList<CarRect> carlist = new ArrayList<CarRect>();
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new MainFrame());
	}

	public MainFrame(){
		super("Rush Hour");
		setLayout(new FlowLayout());
		CarRect.setTileSize(GamePanel.getTileWidth(),GamePanel.getTileHeight());
		CarRect car1 = new CarRect(0,0,3,1,0,'1');
		carlist.add(car1);
		CarRect car2 = new CarRect(2,2,1,2,1,'2');
		carlist.add(car2);
		CarRect car3 = new CarRect(3,1,3,1,0,'3');
		carlist.add(car3);
		CarRect car4 = new CarRect(5,0,1,1,2,'4');
		carlist.add(car4);
		RushHourGame daGame = new RushHourGame(602,carlist);
		add(daGame.getPanel());
		GamePanel temp = new GamePanel(winWidth, winHeight, daGame);			 
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}		
	@Override
	public void run() {
		pack();
		setVisible(true);
	}
	
	public static int getWinWidth(){
		return winWidth;
	}
	
	public static int getWinHeight(){
		return winHeight;
	}
}

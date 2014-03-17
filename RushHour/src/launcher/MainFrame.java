package launcher;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import launcher.GamePanel;
import logic.RushHourGame;

public class MainFrame extends JFrame implements Runnable{
	public static int width = 800; //frame width
	public static int height = 640; //frame height
	public static Dimension size;
	private static int winWidth = 602;
	private static int winHeight = 602;
	private static JTextArea moveCounter;
	private ArrayList<CarRect> carlist = new ArrayList<CarRect>();
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new MainFrame());
	}

	public MainFrame(){
		super("Rush Hour");
		size = new Dimension(width,height);
		setLayout(new FlowLayout());
		CarRect.setTileSize(GamePanel.getTileWidth(),GamePanel.getTileHeight());
		moveCounter = new JTextArea();
		moveCounter.setText("Moves: 0");
		this.setResizable(false);
		this.setSize(size);
		moveCounter.setEditable(false);
		CarRect car1 = new CarRect(0,0,3,1,0,'1');
		carlist.add(car1);
		CarRect car2 = new CarRect(2,2,1,2,1,'2');
		carlist.add(car2);
		CarRect car3 = new CarRect(3,1,3,1,0,'3');
		carlist.add(car3);
		CarRect car4 = new CarRect(5,0,1,1,2,'4');
		carlist.add(car4);
		RushHourGame daGame = new RushHourGame(carlist);
		BorderLayout gameLayout = new BorderLayout();
		add(daGame.getPanel(), gameLayout.SOUTH);
		add(moveCounter, gameLayout.NORTH);
		moveCounter.setPreferredSize(new Dimension(80,20));
		//TODO: fix bug when moves>=10 and window shifts
		GamePanel temp = new GamePanel(winWidth, winHeight, daGame);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}		
	@Override
	public void run() {
		pack();
		setVisible(true);
		this.setResizable(false);
	}
	
	public static int getWinWidth(){
		return winWidth;
	}
	
	public static int getWinHeight(){
		return winHeight;
	}
	
	public static void setMoveCounter(int numMoves){
		moveCounter.setText("Moves: " + numMoves);
		//moveCounter.setText("Moves: 1000");
		
	}
}

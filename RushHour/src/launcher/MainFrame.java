package launcher;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import launcher.GamePanel;
import logic.RushHourGame;

public class MainFrame extends JFrame implements Runnable{
	private static int winWidth = 602;
	private static int winHeight = 602;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new MainFrame());
	}

	public MainFrame(){
		super("Rush Hour");
		setLayout(new FlowLayout());
		RushHourGame daGame = new RushHourGame(602);
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

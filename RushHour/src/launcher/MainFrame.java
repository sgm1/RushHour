package launcher;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame implements Runnable{
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new MainFrame());
	}
	
	public MainFrame(){
		super("Rush Hour");
		setLayout(new FlowLayout());
		GamePanel temp = new GamePanel(300, 300);// here or run?
		add(temp);				 // we can either make a generic size and resize the tiles, or...
		setDefaultCloseOperation(EXIT_ON_CLOSE); // ...we can resize the JFrame for each level
	}						 // Option A is better from a user standpoint but Option B ensures
							 // that our tiles never get too small
	@Override
	public void run() {
		pack();
		setVisible(true);
		// TODO Auto-generated method stub
		
	}
}

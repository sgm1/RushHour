package launcher;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame implements Runnable{
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new MainFrame());
	}
	
	public MainFrame(){
		super("Rush Hour");
		setLayout(new FlowLayout());
		MyCanvas temp = new MyCanvas(300, 300);// here or run?
		add(temp);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void run() {
		pack();
		setVisible(true);
		// TODO Auto-generated method stub
		
	}
}

package launcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class GameMenu extends JMenuBar implements ActionListener{
	private static final long serialVersionUID = 1L;
	private static MainFrame myFrame;
	public JButton resetButt, solveButt, hintButt; //Reset Button

	public GameMenu(MainFrame m){
		myFrame = m;
		JMenu gameMenu = new JMenu("Game"), helpMenu = new JMenu("Help"); //create the two menus
		//create menu items for both menus
		JMenuItem resetMenuButton = new JMenuItem("Reset game", KeyEvent.VK_R), 
				exitButton = new JMenuItem("eXit", KeyEvent.VK_X),
				helpButton = new JMenuItem("Help", KeyEvent.VK_E),
				aboutButton = new JMenuItem("About", KeyEvent.VK_A);

		//Details for "Game" menu
		gameMenu.setMnemonic(KeyEvent.VK_G);

		exitButton.addActionListener(this);
		exitButton.setActionCommand("exit");

		resetMenuButton.addActionListener(this);
		resetMenuButton.setActionCommand("Reset");

		gameMenu.add(resetMenuButton);
		gameMenu.add(exitButton);

		//Details for "Help" menu
		helpMenu.setMnemonic(KeyEvent.VK_H);

		helpButton.addActionListener(this);
		helpButton.setActionCommand("help");

		aboutButton.addActionListener(this);
		aboutButton.setActionCommand("About");

		helpMenu.add(helpButton);
		helpMenu.add(aboutButton);

		//add menus to menu bar
		this.add(gameMenu);
		this.add(helpMenu);

		//Place reset button in frame
		resetButt = new JButton("Reset");
		resetButt.addActionListener(this);
		resetButt.setActionCommand("Reset");
		resetButt.setFocusPainted(false);

		solveButt = new JButton("Solve");
		solveButt.addActionListener(this);
		solveButt.setActionCommand("Solve");
		solveButt.setFocusPainted(false);

		hintButt = new JButton("Hint");
		hintButt.addActionListener(this);
		hintButt.setActionCommand("Hint");
		hintButt.setFocusPainted(false);

	}
	public static void resetLevel(){
		myFrame.newLevel(true);
	}

	public static void nextLevel(){
		JOptionPane.showMessageDialog(null,"Puzzle solved! Loading next level...");
		myFrame.newLevel(false);
	}

	public static void skipLevel(){
		JOptionPane.showMessageDialog(null, "Puzzle is not solvable! Loading next level...");
		myFrame.newLevel(false);
	}

	public void actionPerformed(ActionEvent e) {
		if("Reset".equals(e.getActionCommand())){ //Reset the game
			myFrame.newLevel(true);
		}
		if("Solve".equals(e.getActionCommand())){ //Solve the puzzle
			System.out.println("Solve button clicked!");
			MainFrame.startSolver();
		}
		if("Hint".equals(e.getActionCommand())){ //Solve the puzzle
			System.out.println("Hint button clicked!");
			MainFrame.startHint();
			//TODO: allow for the first move from the solver to displayed in a dialog window
		}
		else if("exit".equals(e.getActionCommand())){ //Close Game Window and stop running
			myFrame.quitGame();
		}
		else if("help".equals(e.getActionCommand())){ //Pop up help dialog
			JOptionPane.showMessageDialog(null,"Rules:\n\n- Left click on a tile to reveal the square.\n- Numbers on tiles represent the number of adjacent bombs.\n- Right click on a tile to mark it as a bomb.\n- Right click again to mark it as a possible bomb.\n- Right click a third time to clear markings.\n- The game is over once you click on a bomb.\n- To win, reveal all tiles that are not bombs.\n- There is a counter at the bottom representing the number of bombs left.\n- In order to be on the high score list, you must have one of the top ten fastest times!\n\n");
		}
		else if("About".equals(e.getActionCommand())){ //Pop up About Dialog
			JOptionPane.showMessageDialog(null,"By: Mike Albanese; Shanon Mathai\n\n                  CS342\n" );
		}
	}
}
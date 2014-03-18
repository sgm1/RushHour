package launcher;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.*;

public class GameMenu extends JMenuBar implements ActionListener{
	private static final long serialVersionUID = 1L;
	private MainFrame myFrame;
	JButton resetButt; //Reset Button

	public GameMenu(MainFrame m){
		myFrame = m;
		JMenu gameMenu = new JMenu("Game"), helpMenu = new JMenu("Help"); //create the two menus
		//create menu items for both menus
		JMenuItem resetMenuButton = new JMenuItem("Reset game", KeyEvent.VK_R), 
				topTenButton = new JMenuItem("Top Ten", KeyEvent.VK_T),
				exitButton = new JMenuItem("eXit", KeyEvent.VK_X),
				helpButton = new JMenuItem("Help", KeyEvent.VK_H),
				aboutButton = new JMenuItem("About", KeyEvent.VK_A),
				resetScoresButton = new JMenuItem("Reset Scores", KeyEvent.VK_S);

		//Details for "Game" menu
		gameMenu.setMnemonic(KeyEvent.VK_G);

		resetScoresButton.addActionListener(this);
		resetScoresButton.setActionCommand("Reset Scores");

		topTenButton.addActionListener(this);
		topTenButton.setActionCommand("Top Ten");

		exitButton.addActionListener(this);
		exitButton.setActionCommand("exit");

		resetMenuButton.addActionListener(this);
		resetMenuButton.setActionCommand("Reset");

		gameMenu.add(resetMenuButton);
		/*gameMenu.add(topTenButton);
		gameMenu.add(resetScoresButton);*/
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
	}

	public void actionPerformed(ActionEvent e) {
		if("Reset".equals(e.getActionCommand())){ //Reset the game
			myFrame.newLevel(true);
		}
		else if("exit".equals(e.getActionCommand())){ //Close Game Window and stop running
			MainFrame.quitGame();
		}
		else if("help".equals(e.getActionCommand())){ //Pop up help dialog
			JOptionPane.showMessageDialog(null,"Rules:\n\n- Left click on a tile to reveal the square.\n- Numbers on tiles represent the number of adjacent bombs.\n- Right click on a tile to mark it as a bomb.\n- Right click again to mark it as a possible bomb.\n- Right click a third time to clear markings.\n- The game is over once you click on a bomb.\n- To win, reveal all tiles that are not bombs.\n- There is a counter at the bottom representing the number of bombs left.\n- In order to be on the high score list, you must have one of the top ten fastest times!\n\n");
		}
		else if("About".equals(e.getActionCommand())){ //Pop up About Dialog
			JOptionPane.showMessageDialog(null,"By: Mike Albanese; Shanon Mathai\n\n                  CS342\n" );
		}
		/*else if("Reset Scores".equals(e.getActionCommand())){ //Clear high scores, in case you're jealous of that one guy who did slightly better than you and want his name off the list.  How dare he!
			Level.topScores = new String("           --Top Scores--\n\n");
			for(int i = 0; i < 10; i++){
				Level.scores[i] = 0;
				Level.names[i] = null;
			}

			//Delete the scores file
			try{
				Game.file.delete();
			}catch(Exception io){
				io.printStackTrace();
			}

			//Make sure it was deleted and create a new, empty file
			try{
				if (!Game.file.exists()) {
					Game.file.createNewFile();
				}
			}catch(IOException ioe){
				ioe.printStackTrace();
			}
		}*/

	}
}
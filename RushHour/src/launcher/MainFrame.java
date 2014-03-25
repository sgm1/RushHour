package launcher;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JTextArea;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import launcher.GamePanel;
import logic.RushHourGame;

public class MainFrame extends JFrame implements Runnable{
	public static int width = 708; //frame width
	public static int height = 622; //frame height
	public static Dimension size;
	private static int winWidth = 602;
	private static int winHeight = 602;

	private static JTextArea moveCounter;
	private static ArrayList<CarRect> carlist = new ArrayList<CarRect>();
	private static int numLevels = 12;
	private static int levelCount = 1;
	private static boolean closeGame = false;
	private static int carsMade;
	private static RushHourGame daGame;
	private static final char[] extraChars = {'\1', '\2', '\3', '\4', '\5', '\6', '\7', '\10', '\11', '\12', '\13', '\14', '\15'
		, '\16', '\17', '\20', '\21', '\22', '\23', '\24', '\25', '\26', '\27', '\30', '\31', '\32', '\33'
		, '\34', '\35', '\36', '\37', '\40', '\41', '\42', '\43', '\44', '\45', '\46', '\47', '\50', '\51'
		, '\52', '\53', '\54', '\55', '\56', '\57', '\60', '\61', '\62', '\63', '\64', '\65', '\66', '\67'
		, '\70', '\71', '\72', '\73', '\74', '\75', '\76', '\77', '\100', '\101', '\102', '\103', '\104', '\105'
		, '\106', '\107', '\110', '\111', '\112', '\113', '\114', '\115', '\116', '\117', '\120', '\121', '\122', '\123'
		, '\124', '\125', '\126', '\127', '\130', '\131', '\132', '\133', '\134', '\135', '\136', '\137', '\140', '\141'
		, '\142', '\143', '\144', '\145', '\146', '\147', '\150', '\151', '\152', '\153', '\154', '\155', '\156', '\157'
		, '\160', '\161', '\162', '\163', '\164', '\165', '\166', '\167', '\170', '\171', '\172', '\173', '\174', '\175'};
	//for puzzles with more than 61 pieces
	private static boolean gameWon = false;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new MainFrame());
	}

	public MainFrame(){
		super("Rush Hour");
		size = new Dimension(width,height);
		setLayout(null);
		GameMenu gm = new GameMenu(this);
		setJMenuBar(gm);
		if(levelCount<=numLevels)
			readInLevel();
		else{
			resetLevels();
			closeGame = true;//will be removed later on
		}
		if(closeGame){
			setVisible(false);
			dispose();
		}
		moveCounter = new JTextArea();
		moveCounter.setText("Moves: 0");
		moveCounter.setPreferredSize(new Dimension(80,20));
		this.setResizable(false);
		this.getContentPane().setPreferredSize(size);
		moveCounter.setEditable(false);
		daGame = new RushHourGame(carlist);
		add(daGame.getPanel());
		daGame.getPanel().setBounds(10,10,602,602);
		moveCounter.setBounds(620,100,80,20);
		add(moveCounter);
		add(gm.resetButt);
		gm.resetButt.setBounds(620,20,80,25);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}	

	public static boolean isInteger(String s) {
		try { 
			Integer.parseInt(s); 
		} catch(NumberFormatException e) { 
			return false; 
		}
		return true;
	}

	private void readInLevel() {//read in from file and initialize grid/cars
		BufferedReader br = null;
		int lineCount = 0;
		boolean skipLine = false;
		carsMade = 0;
		try {
			//TODO put back the other one after levels added
			InputStreamReader temp = new InputStreamReader(MainFrame.class.getResourceAsStream("proj3c.data"));
			//InputStreamReader temp = new InputStreamReader(MainFrame.class.getResourceAsStream("level" + levelCount + ".data"));
			String sCurrentLine;
			br = new BufferedReader(temp);
			if ((sCurrentLine = br.readLine()) != null) {//get grid dimensions
				String delims = "[ ]+";
				String[] tokens = sCurrentLine.split(delims);
				
				for(int i = 0; i<tokens.length; i++){
					if(tokens.length != 2 || !isInteger(tokens[i]) || Integer.parseInt(tokens[i])<1){
						System.out.println("ERROR: Level " + levelCount + " does not have valid grid dimensions!\nProgram terminated.");//close the game if grid is invalid
						quitGame();
						return;
					}
				}
				GamePanel.setTileWidth(Integer.parseInt(tokens[0]));
				GamePanel.setTileHeight(Integer.parseInt(tokens[1]));
				CarRect.setTileSize(GamePanel.getTileWidth(),GamePanel.getTileHeight());
				lineCount++;
			}

			while ((sCurrentLine = br.readLine()) != null) {//get car dimensions and add to car list; first car in list will be the goal piece ('Z')
				skipLine = false;//boolean to detect if we should add a car to the list or skip to the next line
				String delims = "[ ]+";
				String[] tokens = sCurrentLine.split(delims);
				for(int i = 0; i<4 ; i++){
					if(!isInteger(tokens[i]) || tokens.length!=5 || Integer.parseInt(tokens[i])<1){
						System.out.println("ERROR: Car on line " + lineCount + " of Level " + levelCount + " does not have valid dimensions!");
						skipLine = true;
					}
				}
				if(!tokens[4].equals("v") && !tokens[4].equals("h") && !tokens[4].equals("b")){
					System.out.println("ERROR: Car on line " + lineCount + " of Level " + levelCount + " does not have a valid movement direction!");
					skipLine = true;
				}
				if(skipLine);
				else{//add a new CarRect to carlist
					int dir;
					if(tokens[4].equals("h"))
						dir = 0;
					else if(tokens[4].equals("v"))
						dir = 1;
					else 
						dir = 2;
					CarRect newCar = new CarRect(Integer.parseInt(tokens[0])-1,Integer.parseInt(tokens[1])-1,Integer.parseInt(tokens[2]),Integer.parseInt(tokens[3]),dir,getCarText());
					for(CarRect t: carlist){//check for collisions with other rectangles already added to carlist
						if (newCar.intersects(t)){
							System.out.println("ERROR: Car on line " + lineCount + " of Level " + levelCount + " overlaps another car!");
							skipLine = true;
						}
					}
					for(Rectangle t: CarRect.getBorders()){//check for the rectangle going off the grid
						if (newCar.intersects(t)){
							System.out.println("ERROR: Car on line " + lineCount + " of Level " + levelCount + " is outside of the grid bounds!");
							skipLine = true;
						}
					}
					if(!skipLine)
						carlist.add(newCar);
					else
						System.out.println("Car on line " + lineCount + " discarded.\n");
				}
				lineCount++;
			}
			if(lineCount<2){
				System.out.println("ERROR: Level " + levelCount + " is either empty or does not contain enough information!");
				quitGame();
				return;
			}
	
		} catch (IOException e) {//exception handling
			e.printStackTrace();
		} finally {
			try {
				if (br != null){
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static char getCarText() {//determines what symbol will be displayed for each car using carsMade
		char result;
		if(carsMade == 0)
			result = 'Z';
		else if(carsMade<10)
			result = (char)(carsMade + 48);
		else if(carsMade<36)
			result = (char)(carsMade + 87);
		else if(carsMade<61)
			result = (char)(carsMade + 29);
		else 
			result = extraChars[carsMade-61 % extraChars.length];//allows for an infinite number of pieces w/ duplicate symbols for carsMade > 128
		carsMade++;
		return result;
	}

	@Override
	public void run() {
		pack();
		if(closeGame){
			
		}
		if(gameWon){
			//render last move, display appropriate message, and initialize the next level
			gameWon = false;
		}
		setVisible(true);
		this.setResizable(false);
	}

	public static int getWinWidth(){
		return winWidth;
	}

	public static int getWinHeight(){
		return winHeight;
	}

	public static void setMoveCounter(int numMoves){//updates the move counter in the GUI
		moveCounter.setText("Moves: " + numMoves);
	}

	public void newLevel(boolean reset){//read in the next level file and reset variables
		if(!reset){
			if(levelCount <= numLevels){//check if we've exhausted all our levels
				levelCount++;
				
			}
			else{
				resetLevels();
				return;
			}
		}
		this.remove(daGame.getPanel());
		carlist = new ArrayList<CarRect>();
		readInLevel();//TODO might be different later
		daGame = new RushHourGame(carlist);
		this.add(daGame.getPanel());
		daGame.getPanel().setBounds(10,10,602,602);
		GamePanel.resetNumMoves();
		// TODO
	}

	private static void resetLevels() {//after levels have been exhausted, start from the beginning
		// TODO Auto-generated method stub
	}

	public void quitGame(){
		setVisible(false);
		dispose();
	}
	
	public static void puzzleSolved(){
		gameWon = true;
	}
}

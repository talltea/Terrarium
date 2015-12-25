// Terrarium.java

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;

/*
 * NaCoWriMo: Project 1
 * Start: Nov 5, 2015
 * End: -----
 * LoC: ??

 * Terrarium presents the visual game board and
 * handles the GUI and animation. 
 * The TMap class handles the game logic, 
 * while the Critter class handles information
 * about the enhabitants in the game.
 */

public class Terrarium extends JComponent implements MouseListener{
	// size of the board in cells
	public static int WIDTH = 100;
	public static int HEIGHT = 100;

	// pixels for each side of the square cells
	public static int PIXEL_PER_CELL = 5;
	// white space under the times
	public static final int TIME_SPACING = 12;

	// When this is true, game is deterministic (somehow)
	protected boolean testMode = false;

	// Board data structures
	protected TMap map;

	// State of the game
	protected boolean gameOn;	// true if we are playing
	protected long startTime;	// used to measure elapsed time
	protected Random random;	// the random generator

	// Controls
	protected JLabel timeLabel;
	protected JButton startButton;
	protected JButton pauseButton;
	protected JButton newGameButton;
	protected javax.swing.Timer timer;
	protected JSlider speed;
	protected JCheckBox testButton;
	
	// milliseconds per tick
	public final int DELAY = 1000;
	private int timeCounter = 0;
	
	/*
	 * Creates a new Terrarium where each cell
	 * is drawn with the given number of pixels.
	 * Creates the timer object, but doesn't start it
	 */
	Terrarium() {
		super();
		gameOn = false;
		map = new TMap(WIDTH, HEIGHT);
		addMouseListener(this);

		// Set component size to allow given pixels for each block plus
		// a 1 pixel border around the whole thing.
		int pxlWidth = (WIDTH * PIXEL_PER_CELL) + 2;
		int pxlHeight = (HEIGHT * PIXEL_PER_CELL) + 2;
		setPreferredSize(new Dimension(pxlWidth, pxlHeight));
		requestFocusInWindow();

		// Create the Timer object and have it update the world
		ActionListener tickTock = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tickWorld();
			}
		};
		timer = new javax.swing.Timer(DELAY, tickTock);
	}

	/*
	 * Populates world randomly! 
	 */
	private double randPopulation = .2;

	public void populateWorldRandomly() {
		double p = randPopulation;
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				if(random.nextFloat() < p) {
					Critter crit = new Critter(randInt(0,Critter.nSpecies - 1));
					crit.setHealth(randInt(10,20));
					crit.setStrength(randInt(0,2));
					crit.setFertility(randInt(1,3));
					map.setGrid(x, y, crit);
				}
			}
		}
	}

	/*
	 * Populates world with a single critter! 
	 */
	public void populateLonelyWorld() {
		Critter crit = new Critter(randInt(0,Critter.nSpecies - 1));
		crit.setHealth(20);
		crit.setStrength(randInt(0,4));
		crit.setFertility(randInt(0,5));
		int x = randInt(0, WIDTH);
		int y = randInt(0, HEIGHT);
		map.setGrid(x, y, crit);
	}

	private int randInt(int min, int max) {
	    int randomNum = random.nextInt((max - min) + 1) + min;
	    return randomNum;
	}

	/*
	 * Updates the world and draws it every tick
	 */
	private void tickWorld() {
		drawTime();
		map.update();
		repaint();
	}

	/*
	 * Updatest the time label
	 */
	private void drawTime() {
		long delta = (System.currentTimeMillis() - startTime);
		timeLabel.setText(Double.toString(delta/1000.0) + " seconds");
	}

	/*
	 * Draws the current state of the map to the frame
	 */
	public void paintComponent(Graphics g) {
		
		// Draw a rect around the whole thing
		g.drawRect(0, 0, getWidth()-1, getHeight()-1);
				
		int mWidth = WIDTH * PIXEL_PER_CELL;
		int mHeight = HEIGHT * PIXEL_PER_CELL;
		map.getHeight();
		// Loop through and draw all the blocks
		for (int x = 0; x < WIDTH; x++) {			
			for (int y = 0; y < HEIGHT; y++) {
				Critter curCritter = map.getGrid(x, y);
				if (curCritter != null) {
					g.setColor(curCritter.getColor());
					g.fillOval(1 + x*PIXEL_PER_CELL, 1 + y*PIXEL_PER_CELL, PIXEL_PER_CELL, PIXEL_PER_CELL);
				} 
			}
		} 
	}


	private void drawTimeLabel(JPanel panel) {
		timeLabel = new JLabel(" ");
		panel.add(timeLabel);
		panel.add(Box.createVerticalStrut(TIME_SPACING));
	}

	private void drawStartButton(JPanel panel) {
		startButton = new JButton("Start");
		panel.add(startButton);
		startButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Start the game!");
				startGame();
			}
		});
	}

	private void drawPauseButton(JPanel panel) {
		pauseButton = new JButton("Pause");
		panel.add(pauseButton);
		pauseButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Pause the game!");
				pauseGame();
			}
		});
	}

	private void drawNewGameButton(JPanel panel) {
		newGameButton = new JButton("New Game");
		panel.add(newGameButton);
		newGameButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Generate new game!");
				newGame();
			}
		});
		newGameButton.setEnabled(true); 
	}

	private void drawSlider(JPanel panel) { 
		JPanel row = new JPanel();
		
		// SPEED slider
		panel.add(Box.createVerticalStrut(12));
		row.add(new JLabel("Speed:"));
		speed = new JSlider(0, 200, 75);	// min, max, current
		speed.setPreferredSize(new Dimension(100, 15));
		
		updateTimer();
		row.add(speed);
		
		panel.add(row);
		speed.addChangeListener( new ChangeListener() {
			// when the slider changes, sync the timer to its value
			public void stateChanged(ChangeEvent e) {
				updateTimer();
			}
		});

	}

	/*
	 * Creates the panel of UI controls -- controls wired
	 * up to call methods on the Terrarium.
	*/
	public JComponent createControlPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		drawTimeLabel(panel);

		drawStartButton(panel);
		drawPauseButton(panel);
		drawNewGameButton(panel);
		enableButtons();
		startButton.setEnabled(false);
		
		drawSlider(panel);
		
		testButton = new JCheckBox("Test sequence");
		panel.add(testButton);
		
		return panel;
	}

	/*
	 * Updates the timer to reflect the current setting of the 
	 * speed slider.
	 */
	public void updateTimer() {
		double value = ((double)speed.getValue())/speed.getMaximum();
		timer.setDelay((int)(DELAY - value*DELAY));
	}

	/*
	 Sets the internal state and starts the timer
	 so the game is happening.
	*/
	public void startGame() {
		gameOn = true;
		enableButtons();

		// play game
		timer.start();
	}

	/*
	 * Creates a new game.
	*/
	public void newGame() {
		gameOn = false;
		enableButtons();
		timer.stop();

		// cheap way to reset the board state
		map = new TMap(WIDTH, HEIGHT);
		// Set mode based on checkbox at start of game
		testMode = testButton.isSelected();
			
		if (testMode) random = new Random(0);	// same seq every time
		else random = new Random(); // diff seq each game
		
		populateWorldRandomly();
		//populateLonelyWorld();
		repaint();
		timeLabel.setText(" ");
		startTime = System.currentTimeMillis();
	}

	/*
	 * Pauses the game.
	*/
	public void pauseGame() {
		gameOn = false;
		enableButtons();
		timer.stop(); /* TODO: FIX */
	}

	/*
	 * Sets the enabling of the start/stop buttons
	 * based on the gameOn state.
	*/
	private void enableButtons() {
		startButton.setEnabled(!gameOn);
		pauseButton.setEnabled(gameOn);
	}

	/*
	 * Adds a quit button to the given control panel
	 */
	public void addQuit(JComponent controls) {
		controls.add(Box.createVerticalStrut(12));
		JButton quit = new JButton("Quit");
		controls.add(quit);
		quit.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

	/*
	 * Creates and returns a frame around the given Terrarium.
	 * The new frame is not visible.
	 */
	public static JFrame createFrame(Terrarium terrarium) {		
		JFrame frame = new JFrame("Terrarium");
		JComponent container = (JComponent)frame.getContentPane();
		container.setLayout(new BorderLayout());
		container.setBackground(Color.black);

		// Install the passed in Terrarium in the center.
		container.add(terrarium, BorderLayout.CENTER);
		
		// Create and install the panel of controls.
		JComponent controls = terrarium.createControlPanel();
		container.add(controls, BorderLayout.EAST);
		 
		// Add the quit button last so it's at the bottom
		terrarium.addQuit(controls);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		
		return frame;
	}

	private int Y_OFFSET = 45; 
	public void mouseClicked(MouseEvent e) {
		Point loc = e.getLocationOnScreen();
		int x = (int)loc.getX();
		int y = (int)loc.getY() - Y_OFFSET;
		int critX = x/PIXEL_PER_CELL;
		int critY = y/PIXEL_PER_CELL;
		Critter crit = map.getGrid(critX, critY);
		if (crit != null) {
			System.out.println(crit.getColor());
		} else {
			System.out.println("No critter");
		}
    	//System.out.println("Mouse pressed! " + x + " " + y + " = " + critX + " " + critY);

    }
    private Critter critHold;
    private int holdX;
    private int holdY;
    public void mousePressed(MouseEvent e){
    	if (gameOn) return;

    	//if game is paused, we can drag critters
    	Point loc = e.getLocationOnScreen();
		int x = (int)loc.getX();
		int y = (int)loc.getY() - Y_OFFSET;
		int critX = x/PIXEL_PER_CELL;
		int critY = y/PIXEL_PER_CELL;
		critHold = map.getGrid(critX, critY);
		holdX = critX;
		holdY = critY;
    }
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseReleased(MouseEvent e){
    	if (critHold == null) return;
    	Point loc = e.getLocationOnScreen();
		int x = (int)loc.getX();
		int y = (int)loc.getY() - Y_OFFSET;
		int critX = x/PIXEL_PER_CELL;
		int critY = y/PIXEL_PER_CELL;
		if (map.getGrid(critX, critY) == null) {
			map.setGrid(critX, critY, critHold);
			map.setGrid(holdX, holdY, null);
		}
		repaint();
    }

    private static void promptForConstants() {
    	// create a scanner so we can read the command-line input
	    Scanner scanner = new Scanner(System.in);

	    System.out.print("Would you like to input your own constants? (y/n) ");
	    while (true) {
		    String custom = scanner.next();
		    if (custom.equals("y")){
			    System.out.print("Width: ");
			    WIDTH = scanner.nextInt();
			    System.out.print("Height: ");
				HEIGHT = scanner.nextInt();
				System.out.print("Pixels per cell: ");
				PIXEL_PER_CELL = scanner.nextInt();
				System.out.print("Number of Species: ");
				Critter.nSpecies = scanner.nextInt();
				break;
			} else if (custom.equals("n")) {
				System.out.println("Using preset constants!");
				break;
			} else {
				System.out.print("Please either type 'y' or 'n': ");
			}
		}
    }

	/*
	 Creates a frame with a Terrarium.
	*/
	public static void main(String[] args) {

		// Set GUI Look And Feel Boilerplate.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }

		promptForConstants();
		
		Terrarium terrarium = new Terrarium();
		JFrame frame = Terrarium.createFrame(terrarium);
		frame.setVisible(true);
	}
}



























// Terrarium.java

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

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

public class Terrarium extends JComponent {
	// size of the board in cells
	public static final int WIDTH = 50;
	public static final int HEIGHT = 40;

	// pixels for each side of the square cells
	public static final int PIXEL_PER_CELL = 15;
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
	public void populateWorldRandomly() {
		double p = .3;
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				if(random.nextFloat() < p) {
					Critter crit = new Critter(randInt(0,Critter.nSpecies - 1));
					crit.setHealth(randInt(1,20));
					crit.setStrength(randInt(0,4));
					crit.setFertility(randInt(0,5));
					map.setGrid(x, y, crit);
				}
			}
		}
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
				} else {
					g.setColor(Color.BLACK);
				}
				g.fillRect(1 + x*PIXEL_PER_CELL, 1 + y*PIXEL_PER_CELL, PIXEL_PER_CELL, PIXEL_PER_CELL);
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

		// Install the passed in JTetris in the center.
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

	/*
	 Creates a frame with a Terrarium.
	*/
	public static void main(String[] args) {

		// Set GUI Look And Feel Boilerplate.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		Terrarium terrarium = new Terrarium();
		JFrame frame = Terrarium.createFrame(terrarium);
		frame.setVisible(true);
	}
}



























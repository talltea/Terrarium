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
	public static final int WIDTH = 20;
	public static final int HEIGHT = 20;

	// pixels for each side of the square cells
	public static final int PIXEL_PER_CELL = 20;
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
	protected JButton stopButton;
	protected javax.swing.Timer timer;
	protected JSlider speed;
	protected JCheckBox testButton;
	
	// milliseconds per tick
	public final int DELAY = 400;
	
	/**
	 * Creates a new Terrarium where each cell
	 * is drawn with the given number of pixels.
	 */
	Terrarium(int pixels) {
		super();
		gameOn = false;

		// Set component size to allow given pixels for each block plus
		// a 1 pixel border around the whole thing.
		int pxlWidth = (WIDTH * pixels) + 2;
		int pxlHeight = (HEIGHT * pixels) + 2;
		setPreferredSize(new Dimension(pxlWidth, pxlHeight));
		
		//map = new TMap(WIDTH, HEIGHT);
	}

	/*
	 * Creates the panel of UI controls -- controls wired
	 * up to call methods on the Terrarium.
	*/
	public JComponent createControlPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		
		// TIME 
		timeLabel = new JLabel("time ");
		panel.add(timeLabel);

		panel.add(Box.createVerticalStrut(TIME_SPACING));
		
		// START button
		startButton = new JButton("Start");
		panel.add(startButton);
		startButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Start the game!");
				startGame();
			}
		});
		
		// STOP button
		stopButton = new JButton("Stop");
		panel.add(stopButton);
		stopButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Stop the game!");
				stopGame();
			}
		});
		
		enableButtons();
		
		JPanel row = new JPanel();
		
		// SPEED slider
		panel.add(Box.createVerticalStrut(12));
		row.add(new JLabel("Speed:"));
		speed = new JSlider(0, 200, 75);	// min, max, current
		speed.setPreferredSize(new Dimension(100, 15));
		
		System.out.println("Tick Tock");
		// updateTimer();
		row.add(speed);
		
		panel.add(row);
		speed.addChangeListener( new ChangeListener() {
			// when the slider changes, sync the timer to its value
			public void stateChanged(ChangeEvent e) {
				System.out.println("Tick Tock");
				//updateTimer();
			}
		});
		
		testButton = new JCheckBox("Test sequence");
		panel.add(testButton);
		
		
		return panel;
	}

	/*
	 Sets the internal state and starts the timer
	 so the game is happening.
	*/
	public void startGame() {
		// cheap way to reset the board state
		map = new TMap(WIDTH, HEIGHT);
		
		// draw the new board state once
		// repaint();
		
		gameOn = true;
		
		// Set mode based on checkbox at start of game
		testMode = testButton.isSelected();
		
		if (testMode) random = new Random(0);	// same seq every time
		else random = new Random(); // diff seq each game
		
		enableButtons();
		timeLabel.setText(" ");

		// play game

		//timer.start();
		startTime = System.currentTimeMillis();
	}

	/*
	 * Stops the game.
	*/
	public void stopGame() {
		gameOn = false;
		enableButtons();
		//timer.stop();
	}

	/*
	 * Sets the enabling of the start/stop buttons
	 * based on the gameOn state.
	*/
	private void enableButtons() {
		startButton.setEnabled(!gameOn);
		stopButton.setEnabled(gameOn);
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
		
		Terrarium terrarium = new Terrarium(PIXEL_PER_CELL);
		JFrame frame = Terrarium.createFrame(terrarium);
		frame.setVisible(true);
	}
}



























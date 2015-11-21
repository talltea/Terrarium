// TMap.java

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

public class TMap {
	private int width;
	private int height;
	private Critter[][] map;

	private boolean DEBUG = true;


	/*
	 Creates an empty map of the given width and height
	 measured in cells.
	*/
	public TMap(int width, int height) {
		this.width = width;
		this.height = height;
		map = new Critter[width][height];

		for(int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				map[row][col] = null;
			}
		}
	}

	/*
	 * Returns the width of the map in cells.
	 */
	public int getWidth() {
		return width;
	}
	
	
	/*
	 * Returns the height of the map in cells.
	 */
	public int getHeight() {
		return height;
	}

	/*
	 * Returns a critter object if the given cell 
	 * is filled in the board. Empty cells return null.
	 * Blocks outside of the valid width/height area
	 * always return null.
	 */
	public Critter getGrid(int x, int y) {
		return map[x][y];
		//return null;
	}

	public void update() {
		/**
		TODO
		*/
	}

}
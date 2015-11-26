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
	 * Checks if the coordinates are in bounds of the 
	 * map. Returns true if they are out of bounds.
	 * Returns false for valid placements. 
	 */
	private boolean outOfBounds(int x, int y) {
		return (x < 0 || y < 0 || x > width-1 || y > height-1);
	}
	/*
	 * Returns a critter object if the given cell 
	 * is filled in the board. Empty cells return null.
	 * Blocks outside of the valid width/height area
	 * always return null.
	 */
	public Critter getGrid(int x, int y) {
		if(outOfBounds(x,y)) {
			return null;
		}
		return map[x][y];
	}

	/*
	 * Sets the location in the grid to contain the
	 * given critter. if location is out of bounds,
	 * does nothing.
	 */
	public void setGrid(int x, int y, Critter crit) {
		if (!outOfBounds(x,y)) { 
			map[x][y] = crit;
		}
	}

	private void fightNeighbor(int x, int y, int xE, int yE) {
		if (outOfBounds(x,y) || outOfBounds(xE,yE)) return;

		Critter crit = getGrid(x, y);
		Critter enemy = getGrid(xE, yE);
		if (enemy == null) return;
		int critStr = crit.getStrength();
		int critHlth = crit.getHealth();
		int enemyStr = enemy.getStrength();
		int enemyHlth = enemy.getHealth();
		if (critStr < enemyStr) {
			crit.setHealth(critHlth-enemyStr);
		} else if (critStr > enemyStr) {
			enemy.setHealth(enemyHlth-critStr);
		} else {
			crit.setHealth(critHlth-enemyStr);
			enemy.setHealth(enemyHlth-critStr);
		}
	}

	public void update() {
		for (int row = 0; row < getHeight(); row++) {
			for (int col = 0; col < getWidth(); col++) {
				Critter crit = getGrid(row, col);
				if (crit != null) {
					fightNeighbor(row, col, row + 1, col - 1);
					fightNeighbor(row, col, row + 1, col);
					fightNeighbor(row, col, row + 1, col + 1);
					fightNeighbor(row, col, row, col + 1);
					if (crit.getHealth() < 0) {
						setGrid(row, col, null); 
					}
				}
			}
		}

	}

}
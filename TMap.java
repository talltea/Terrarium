// TMap.java
import java.util.*;
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
	private Critter[][] mapUpdated;

	private boolean DEBUG = false;
	private Random random;


	/*
	 Creates an empty map of the given width and height
	 measured in cells.
	*/
	public TMap(int width, int height) {
		this.width = width;
		this.height = height;
		map = new Critter[height][width];
		mapUpdated = new Critter[height][width];

		if (DEBUG) random = new Random(0);	// same seq every time
		else random = new Random(); // diff seq each game

		for(int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				map[row][col] = null;
				mapUpdated[row][col] = null;
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
		return map[y][x];
	}

	/*
	 * Sets the location in the grid to contain the
	 * given critter. if location is out of bounds,
	 * does nothing.
	 */
	public void setGrid(int x, int y, Critter crit) {
		if (!outOfBounds(x,y)) { 
			map[y][x] = crit;
		}
	}

	/*
	 * Sets the location in the grid to contain the
	 * given critter. if location is out of bounds,
	 * does nothing.
	 */
	public void setGridUpdated(int x, int y, Critter crit) {
		if (!outOfBounds(x,y)) { 
			mapUpdated[y][x] = crit;
		}
	}

	private void fightCritters(Critter crit, Critter enemy) {
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

	private int fertilityMin = 3;

	private void generateLife(int xB, int yB) {
		int[] breedingPower = new int[Critter.nSpecies];
		for (int x = xB - 1; x < xB + 2; x++) {
			for (int y = yB - 1; y < yB + 2; y++) {
				if (!outOfBounds(x,y)) { 
					Critter crit = getGrid(x, y);
					if (crit != null) {
						breedingPower[crit.getSpecies()] += crit.getFertility();
					}
				}
			}
		}
		if (maxIntVal(breedingPower) > fertilityMin) {
			Critter baby = new Critter(maxIntIndex(breedingPower));
			baby.setHealth(randInt(1,20));
			baby.setStrength(randInt(0,4));
			baby.setFertility(randInt(0,5));
			setGrid(xB, yB, baby);
		} 
	}

	private int maxIntVal(int[] array) {
		int maxVal = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] > maxVal) {
				maxVal = array[i];
			}
		}
		return maxVal;
	}

	private int maxIntIndex(int[] array) {
		int maxVal = 0;
		int maxIndex = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] > maxVal) {
				maxVal = array[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	private int randInt(int min, int max) {
	    int randomNum = random.nextInt((max - min) + 1) + min;
	    return randomNum;
	}

	private void interactCritterWith(Critter crit, int xN, int yN) {
		if (outOfBounds(xN,yN)) return;
		Critter neighbor = getGrid(xN, yN);
		if (neighbor == null) return;

		if (crit.getSpecies() != neighbor.getSpecies()) {
			fightCritters(crit, neighbor);
		}
	}

	private void hungerHealth(Critter crit) {
		int health = crit.getHealth();
		crit.setHealth(health-1);
	}

	private void moveCritter(int x, int y) {
		int dX = randInt(-1, 1);
		int dY = randInt(-1, 1);
		if(outOfBounds(x+dX, y+dY)) return;

		if(getGrid(x+dX, y+dY) == null) {
			setGridUpdated(x+dX, y+dY, getGrid(x,y));
			setGridUpdated(x,y,null);
			if(DEBUG) System.out.println("Move: x = " +x+" + "+dX + " y = " + y+" + "+dY);
		} else {
			setGridUpdated(x, y, getGrid(x,y));
		}
	}

	public void update() {
		if(DEBUG) System.out.println("UPDATE");
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				Critter crit = getGrid(x, y);
				if (crit != null) {
					if(DEBUG) System.out.println("FOUND CRIT x = " + x + " y = " + y);
					interactCritterWith(crit, x + 1, y - 1);
					interactCritterWith(crit, x + 1, y);
					interactCritterWith(crit, x + 1, y + 1);
					interactCritterWith(crit, x, y + 1);
					if (crit.getHealth() < 1) {
						setGrid(x, y, null); 
						if (DEBUG) System.out.println("Critter Died " + y + " " + x);
					} else {
						hungerHealth(crit);
						moveCritter(x,y);
					}
				} else {
					generateLife(x, y);
				}
			}
		}
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				Critter crit = getGrid(x, y);
				if (crit != null) {
					moveCritter(x,y);
				}
			}
		}

		for(int i = 0; i < height; i++) {
    		map[i] = mapUpdated[i].clone();
    	}
	}

}
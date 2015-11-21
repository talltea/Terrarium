// Critter.java

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

import java.util.*;
import java.awt.*;

public class Critter {
    private int health;
    private Color color;
    private static int counter = 0;

    public Critter() {
		health = 0;
        counter++;
        if (counter%7 == 0) {
    		color = Color.GREEN;
        } else { 
            color = Color.RED;
        }
    }

    public void setColor(Color color) {
    	this.color = color;
    }

    public Color getColor() {
    	return color;
    }
}
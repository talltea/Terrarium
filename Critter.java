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
    private int strength;
    private Color color;

    public Critter() {
		health = 0;
        strength = 0;
        color = Color.RED;   
    }

    public void setHealth(int health) {
        this.health = health;
        if (health > 0) {
            color = new Color((health*10)%255,10,10);
        } else {
            color = Color.BLACK;
        }
    }

    public int getHealth() {
        return health;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getStrength() {
        return strength;
    }   

    public void setColor(Color color) {
    	this.color = color;
    }

    public Color getColor() {
    	return color;
    }
}
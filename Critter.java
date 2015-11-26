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
    private int fertility;

    private Color color;
    private int species;    // larger numbers = higher on food chain

    public static int nSpecies = 3;

    public Critter(int species) {
		health = 0;
        strength = 0;
        fertility = 0;
        this.species = species;
        if (species == 0) {
            color = Color.GREEN;   
        } else if (species == 1) {
            color = Color.RED;
        } else {
            color = Color.BLUE;
        }
    }

    public void setHealth(int health) {
        this.health = health;
        if (health > 0) {
            if (species == 0) {
                color = new Color((health*10)%255, 10, 10); 
            } else if (species == 1) {
                color = new Color(10, (health*10)%255, 10);
            } else {
                color = new Color(10, 10, (health*10)%255);
            }
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

    public int getSpecies() {
        return species;
    }   

    public void setFertility(int fertility) {
        this.fertility = fertility;
    }

    public int getFertility() {
        return fertility;
    } 

    public void setColor(Color color) {
    	this.color = color;
    }

    public Color getColor() {
    	return color;
    }
}
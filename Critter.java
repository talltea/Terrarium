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
import java.util.Random;

public class Critter {
    private int health;
    private int strength;
    private int fertility;
    private int hungerLoss;
    private int movement;

    private Color color;
    private int species;    // larger numbers = higher on food chain

    public static int nSpecies = 3;
    private static Color[] speciesColors;
    private static int[] speciesHungerLoss;
    private static int[] speciesMovement;
    private static int[] speciesMaxHealth;
    private static int[] speciesStrength;
    private static int[] speciesFertility;

    private static Random random;

    public static void initializeCritterSpecies() {
        random = new Random();
        speciesColors = new Color[nSpecies];
        speciesHungerLoss = new int[nSpecies];
        speciesMovement = new int[nSpecies];
        speciesMaxHealth = new int[nSpecies];
        speciesStrength = new int[nSpecies];
        speciesFertility = new int[nSpecies];
        for (int i = 0; i < speciesColors.length; i++) {
            float r = random.nextFloat();
            float g = random.nextFloat();
            float b = random.nextFloat();
            speciesColors[i] = new Color(r, g, b); 
            speciesHungerLoss[i] = randInt(1, 5);
            speciesMovement[i] = randInt(1, 5);
            speciesMaxHealth[i] = randInt(5, 25);
            speciesStrength[i] = randInt(1, 5);
            speciesFertility[i] = randInt(1, 5);
            System.out.println(speciesColors[i] + " " + speciesHungerLoss[i] + " " + speciesMovement[i] +" " + speciesMaxHealth[i] + " " + speciesStrength[i] + " " + speciesFertility[i]);
        }
    }

    private static int randInt(int min, int max) {
        int randomNum = random.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public Critter(int species) {
		health = 0;
        strength = 0;
        fertility = 0;
        this.species = species;
        color = speciesColors[species];
    }

    public static Critter randCritter(int species) {
        Critter randCrit = new Critter(species);
        randCrit.setHealth(randInt(1, speciesMaxHealth[species]));
        randCrit.setStrength( randInt(1, speciesStrength[species]) );
        randCrit.setFertility( randInt(1, speciesFertility[species]) );
        randCrit.setHungerLoss( randInt(1, speciesHungerLoss[species]) );
        randCrit.setMovement( randInt(1, speciesMovement[species]));
        return randCrit;
    }

    public void setMovement(int movement) {
        this.movement = movement;
    }

    public int getMovement() {
        return movement;
    }

    public void setHungerLoss(int hungerLoss) {
        this.hungerLoss = hungerLoss;
    }

    public int getHungerLoss() {
        return hungerLoss;
    }

    public void setHealth(int health) {
        this.health = health;
        if (health > 0) {
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), health*10);
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

    public void fightWith(Critter enemy) {
        int enemyStr = enemy.getStrength();
        int enemyHlth = enemy.getHealth();

        if (strength < enemyStr) {
            health -= enemyStr;
        } else if (strength > enemyStr) {
            enemy.setHealth(enemyHlth-strength);
        } else {
            health =- enemyStr;
            enemy.setHealth(enemyHlth-strength);
        }

        if (health < 0) {
            enemy.setMaxHealth();
        } 
        if (enemy.getHealth() < 0) {
            this.setMaxHealth();
        }
    }

    public boolean moveThisTurn() {
        if (movement==0) return false;
        return (1.0/movement) < random.nextFloat();
    }

    public void setMaxHealth() {
        this.setHealth(speciesMaxHealth[species]);
    }

    private void hungerUpdate() {
        setHealth(getHealth() - hungerLoss);
    }

    private void ageUpdate() {
        
    }

    public void updateCritter() {
        hungerUpdate();
        ageUpdate();
    }
}
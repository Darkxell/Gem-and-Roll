package com.darkxell.gemandroll.mechanics;

/**
 * Created by Darkxell on 04/12/2016.
 */

public class Player {

    /**
     * An array of gems. The player score is the length of the array. This is hardstored to memorize the gems sprites.
     */
    public Gem[] gems = new Gem[0];
    /**
     * The name of the player.
     */
    public String name = "Unnamed";
    /**
     * The ID of the AI being used. Is a real player if = 0.
     */
    private byte AItype = 0;

    /**
     * creates a new player object
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * creates a new AI player object
     */
    public Player(String name, byte ai) {
        this(name);
        this.AItype = ai;
    }

    /**
     * Gets the score of the player
     */
    public int getScore() {
        return this.gems.length;
    }

    /**Adds a gem to this player*/
    public void addGem(Gem g){
        Gem[] array = new Gem[this.gems.length + 1];
        System.arraycopy(this.gems, 0, array, 0, this.gems.length);
        array[array.length-1] = g;
        this.gems = array;
        Statistics.instance.increaseStat(Statistics.Stat.GEM_COUNT, 1);
    }

    /**
     * Returns true if this Player is an AI.
     */
    public boolean isAI() {
        return this.AItype != PlayerAI.UndefinedAI;
    }

}

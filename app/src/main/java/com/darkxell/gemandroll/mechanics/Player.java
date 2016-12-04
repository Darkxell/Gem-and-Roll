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


}

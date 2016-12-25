package com.darkxell.gemandroll.mechanics;

/**
 * Created by Darkxell on 04/12/2016.
 */

public abstract class PlayerAI {

    public final byte ID;

    public static final byte UndefinedAI = 0;
    public static final byte TurnValueAI = 1;

    public PlayerAI(byte ID){
        this.ID = ID;
    }

    /**
     * Returns wether this AI should play the turn or pass given the needed board informations.
     */
    public abstract boolean shouldPlay(int health, int score, Dice[] hand, Dice[] rolled);
}

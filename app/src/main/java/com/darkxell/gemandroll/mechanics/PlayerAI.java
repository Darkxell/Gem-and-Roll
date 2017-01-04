package com.darkxell.gemandroll.mechanics;

import com.darkxell.gemandroll.mechanics.ais.TurnValueAI;

/**
 * Created by Darkxell on 04/12/2016.
 */

public abstract class PlayerAI {

    public final byte ID;

    public static final byte UndefinedAI = 0;
    public static final byte TurnValueAI = 1;

    public PlayerAI(byte ID) {
        this.ID = ID;
    }

    /**
     * Returns wether this AI should play the turn or pass given the needed board informations.
     */
    public abstract boolean shouldPlay(int health, int score, Dice[] hand, Dice[] rolled);

    /**
     * Creates and return a new instance of the wanted AI.
     */
    public static PlayerAI getAI(byte ID) {
        switch (ID) {
            case UndefinedAI:
                return null;
            case TurnValueAI:
                return new TurnValueAI(TurnValueAI);
            default:
                return null;
        }
    }
}


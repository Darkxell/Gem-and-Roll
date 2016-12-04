package com.darkxell.gemandroll.mechanics;

import android.graphics.Color;

/**
 * Created by Darkxell on 04/12/2016.
 */

public class Dice {

    public static final byte UNROLLED = 0;
    public static final byte GEM = 1;
    public static final byte REROLL = 2;
    public static final byte HURT = 2;

    public Dice(byte[] faces, Color color) {
        this.display = color;
        this.faces = faces;
    }

    /**
     * The last rolled face.
     */
    byte face = UNROLLED;
    /**
     * The dice color.
     */
    Color display;

    /**
     * A list of all the rollable faces.
     */
    byte[] faces;

    /**
     * Roll this dice using the wanted random number generator. Will randomely roll if the generator is null. Also returns the result of the roll.
     */
    public byte roll(SeededRNG generator) {
        //TODO
        return UNROLLED;
    }

}

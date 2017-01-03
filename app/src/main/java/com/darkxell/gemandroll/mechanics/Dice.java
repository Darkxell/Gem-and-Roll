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

    public Dice(byte[] faces, int color) {
        this.display = color;
        this.faces = faces;
    }

    /**
     * The last rolled face.
     */
    private byte face = UNROLLED;
    /**
     * The dice color.
     */
    private int display;

    /**
     * A list of all the rollable faces.
     */
    private byte[] faces;

    /**
     * Roll this dice using the wanted random number generator. Will randomely roll if the generator is null. Also returns the result of the roll.
     */
    public byte roll(SeededRNG generator) {
        this.face = this.faces[generator.getRandomInt(0, this.faces.length - 1)];

        Statistics s = Statistics.instance;
        s.setStatValue(Statistics.Stat.DICES_ROLLED, s.getStatValue(Statistics.Stat.DICES_ROLLED) + 1);
        if (this.face == GEM) s.setStatValue(Statistics.Stat.GEM_COUNT, s.getStatValue(Statistics.Stat.GEM_COUNT) + 1);
        if (this.face == REROLL) s.setStatValue(Statistics.Stat.REROLL_COUNT, s.getStatValue(Statistics.Stat.REROLL_COUNT) + 1);
        if (this.face == HURT) s.setStatValue(Statistics.Stat.HURT_COUNT, s.getStatValue(Statistics.Stat.HURT_COUNT) + 1);

        return this.getFace();
    }

    public int getColor() {
        return this.display;
    }

    public byte getFace() {
        return this.face;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Dice) ? ((Dice) obj).getFace() == this.getFace() && ((Dice) obj).getColor() == this.getColor() : false;
    }

    /**
     * Returns the probability of rolling a certain face with this dice.
     */
    public float getFaceProbability(byte face) {
        int correcponding = 0;
        for (int i = 0; i < this.faces.length; ++i)
            if (this.faces[i] == face) ++correcponding;
        float r = correcponding / this.faces.length;
        return r;
    }

    /**
     * Creates and returns a new typical green dice dice object.
     */
    public static Dice getTypicalGreenDice() {
        return new Dice(new byte[]{GEM, GEM, GEM, HURT, REROLL, REROLL}, Color.GREEN);
    }

    /**
     * Creates and returns a new typical yellow dice dice object.
     */
    public static Dice getTypicalYellowDice() {
        return new Dice(new byte[]{GEM, GEM, HURT, HURT, REROLL, REROLL}, Color.YELLOW);
    }

    /**
     * Creates and returns a new typical red dice dice object.
     */
    public static Dice getTypicalRedDice() {
        return new Dice(new byte[]{GEM, HURT, HURT, HURT, REROLL, REROLL}, Color.RED);
    }

}

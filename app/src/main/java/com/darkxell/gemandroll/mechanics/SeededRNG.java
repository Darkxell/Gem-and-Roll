package com.darkxell.gemandroll.mechanics;

import java.util.Random;

/**
 * Created by Darkxell on 04/12/2016.
 * A SeededRNG object will generate random numbers based on a seed and the previously generated numbers. THis means that for the same seed, the first n outputs will be the same.
 */

public class SeededRNG {

    private Random rand;

    /**
     * Creates a new SeededRNG.
     */
    public SeededRNG(long seed) {
        this.rand = new Random(seed);
    }

    /**
     * Get a random number out of this SeededRNG. The returned number is an integer between min (inclusive) and maw (exclusive).
     */
    public int getRandomInt(int min, int max) {
        return min + this.rand.nextInt(max - min);
    }

}

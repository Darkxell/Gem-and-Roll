package com.darkxell.gemandroll.mechanics;

import java.util.Random;

/**
 * Created by Darkxell on 04/12/2016.
 * A SeededRNG object will generate random numbers based on a seed and the previously generated numbers. THis means that for the same seed, the first n outputs will be the same.
 */

public class SeededRNG {

    /**
     * The random java object used to do calculations
     */
    private Random rand;
    /**
     * THe seed of the internal random objkect.
     */
    private long seed;

    /**
     * Creates a new SeededRNG with the wanted seed.
     */
    public SeededRNG(long seed) {
        this.seed = seed;
        this.rand = new Random(seed);
    }

    /**
     * Creates a new SeededRNG with a random seed.
     */
    public SeededRNG() {
        this.seed = new Random().nextLong();
        this.rand = new Random(this.seed);
    }

    public long getSeed() {
        return this.seed;
    }

    /**
     * Get a random number out of this SeededRNG. The returned number is an integer between min (inclusive) and maw (exclusive).
     */
    public int getRandomInt(int min, int max) {
        return min + this.rand.nextInt(max - min);
    }

}

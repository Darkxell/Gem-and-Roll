package com.darkxell.gemandroll.mechanics;

import java.util.HashMap;

/**
 * Created by Cubi on 02/01/2017.
 */

public class Statistics {

    public static enum Stat {

        DEATHS_ROW("Deaths in a row"),
        DICES_ROLLED("Dices rolled"),
        GAME_COUNT("Games played"),
        GEM_COUNT("Gems collected"),
        HIGH_SCORE("High score"),
        HURT_COUNT("Traps activated"),
        REROLL_COUNT("Pickaxes found"),
        TOTAL_SCORE("Total score"),
        TOTAL_DEATHS("Total deaths");

        public final String name;

        private Stat(String name) {
            this.name = name;
        }
    }

    public static final Statistics instance = new Statistics();

    /**
     * Stores the value for each stat.
     */
    private HashMap<Stat, Integer> statistics;

    private Statistics() {
        this.statistics = new HashMap<Stat, Integer>();
        for (Stat stat : Stat.values()) this.statistics.put(stat, 0);
    }

    /**
     * Returns the value of the input Stat.
     */
    public int getStatValue(Stat stat) {
        if (this.statistics.containsKey(stat)) return this.statistics.get(stat);
        return 0;
    }

    /**
     * Changes the value of the input Stat. Triggers achievements in consequence.
     */
    public void setStatValue(Stat stat, int value) {
        this.statistics.put(stat, value);
        Achievement.triggerAchievements(stat);
    }

}

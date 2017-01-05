package com.darkxell.gemandroll.mechanics;

import com.darkxell.gemandroll.storage.Storage;

import java.util.HashMap;

/**
 * Created by Cubi on 02/01/2017.
 */

public class Statistics {

    public static enum Stat {

        GEM_COUNT("Gems collected"),
        REROLL_COUNT("Pickaxes found"),
        HURT_COUNT("Traps activated"),
        HIGH_SCORE("High score"),
        DICES_ROLLED("Dices rolled"),
        GAME_COUNT("Games completed"),
        DEATHS_ROW("Deaths in a row"),
        TOTAL_DEATHS("Total deaths"),
        TOTAL_SCORE("Total score"),
        REPLAYS_ENTERED("Replays entered");

        public final String name;

        private Stat(String name) {
            this.name = name;
        }
    }

    public static Statistics instance;

    /**
     * Stores the value for each stat.
     */
    private HashMap<Stat, Integer> statistics;

    public Statistics() {
        this.statistics = new HashMap<Stat, Integer>();
        for (Stat stat : Stat.values())
            this.statistics.put(stat, Integer.parseInt(Storage.i().getValue(stat.name)));
    }

    /**
     * Returns the value of the input Stat.
     */
    public int getStatValue(Stat stat) {
        if (this.statistics.containsKey(stat)) return this.statistics.get(stat);
        return 0;
    }

    /**
     * Increases the target stat by number.
     */
    public void increaseStat(Stat stat, int number) {
        this.setStatValue(stat, this.getStatValue(stat) + number);
    }

    /**
     * Changes the value of the input Stat. Triggers achievements in consequence.
     */
    public void setStatValue(Stat stat, int value) {
        this.statistics.put(stat, value);
        Storage.i().addVariable(stat.name, "" + value);
        Achievement.triggerAchievements(stat);
    }

}

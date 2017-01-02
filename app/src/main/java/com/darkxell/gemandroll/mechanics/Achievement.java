package com.darkxell.gemandroll.mechanics;

/**
 * Created by Cubi on 02/01/2017.
 */

public enum Achievement {

    BEST_SCORE("Hacker", Statistics.Stat.HIGH_SCORE, 25),
    ONE_DEATH("Too greedy", Statistics.Stat.TOTAL_DEATHS, 1),
    ONE_GAME("Expedition complete", Statistics.Stat.HIGH_SCORE, 13);

    /**
     * This achievement's name.
     */
    public final String name;
    /**
     * The stat this Achievement checks.
     */
    public final Statistics.Stat statToCheck;
    /**
     * The value statToCheck needs to reach for this Achievement to be acquired.
     */
    public final int valueToReach;
    /**
     * True if the user has acquired this Achievement.
     */
    private boolean isAcquired = false;

    private Achievement(String name, Statistics.Stat statToCheck, int valueToReach) {
        this.name = name;
        this.statToCheck = statToCheck;
        this.valueToReach = valueToReach;
    }

    /**
     * Checks if this Achievement should unlock and does so. Called when statToCheck is changed.
     */
    public boolean check() {
        boolean unlock = Statistics.instance.getStatValue(this.statToCheck) >= this.valueToReach;
        if (unlock) this.setAcquired(true, true);
        return unlock;
    }

    public boolean isAcquired() {
        return this.isAcquired;
    }

    /**
     * Should only be called for special achievements or when opening the game.
     * @param warnUser - True if the game should tell the user this achievement has been unlocked.
     */
    public void setAcquired(boolean acquired, boolean warnUser) {
        this.isAcquired = acquired;
        if (warnUser) {
            //TODO warn user
        }
    }

    /**
     * Called when updatedStat is changed. Checks if Achievements listening to updatedStat are acquired.
     */
    public static void triggerAchievements(Statistics.Stat updatedStat) {
        for (Achievement achievement : Achievement.values()) {
            if (!achievement.isAcquired() && achievement.statToCheck == updatedStat) achievement.check();
        }
    }

}

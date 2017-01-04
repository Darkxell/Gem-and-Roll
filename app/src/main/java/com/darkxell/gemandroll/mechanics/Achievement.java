package com.darkxell.gemandroll.mechanics;

import android.util.Log;

import com.darkxell.gemandroll.gamestates.statesutility.GameState;

/**
 * Created by Cubi on 02/01/2017.
 */

public enum Achievement {

    ONE_GAME("Expedition complete", "Win a game.", Statistics.Stat.HIGH_SCORE, 13),
    BEST_SCORE("Hacker", "Get a score of 25.", Statistics.Stat.HIGH_SCORE, 25),
    ONE_DEATH("Too greedy", "Get hurt three times in a turn.", Statistics.Stat.TOTAL_DEATHS, 1),
    SIXTYNINE_DEATH("Welp, it happened.", "Die. 69 times.", Statistics.Stat.TOTAL_DEATHS, 69),
    REPLAY_WATCHER("Replay watcher", "Enter the replays menu 10 times.", Statistics.Stat.REPLAYS_ENTERED, 10),
    REPLAY_WATCHER2("Very Big nerd.", "Enter the replays menu 1337 times... I don't even know why you even...", Statistics.Stat.REPLAYS_ENTERED, 1337),
    NECRODANCER("Necrodancer!", "Die 5 times in a row.", Statistics.Stat.DEATHS_ROW, 5),
    LUCKY3("Surprise!", "Get 3 Gems on the first throw.", null, 0);

    /**
     * This achievement's name and description.
     */
    public final String name, description;
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

    Achievement(String name, String description, Statistics.Stat statToCheck, int valueToReach) {
        this.name = name;
        this.description = description;
        this.statToCheck = statToCheck;
        this.valueToReach = valueToReach;

        this.isAcquired = this.statToCheck != null && Statistics.instance.getStatValue(this.statToCheck) >= this.valueToReach;
        Log.d(name, "" + isAcquired);
    }

    /**
     * Checks if this Achievement should unlock and does so. Called when statToCheck is changed.
     */
    public boolean check() {
        if (this.statToCheck == null) return false;
        boolean unlock = Statistics.instance.getStatValue(this.statToCheck) >= this.valueToReach;
        if (unlock) this.setAcquired(true, true);
        return unlock;
    }

    public boolean isAcquired() {
        return this.isAcquired;
    }

    /**
     * Should only be called for special achievements or when opening the game.
     *
     * @param warnUser - True if the game should tell the user this achievement has been unlocked.
     */
    public void setAcquired(boolean acquired, boolean warnUser) {
        this.isAcquired = acquired;
        if (warnUser) GameState.showAchievement(this);
    }

    /**
     * Called when updatedStat is changed. Checks if Achievements listening to updatedStat are acquired.
     */
    public static void triggerAchievements(Statistics.Stat updatedStat) {
        for (Achievement achievement : Achievement.values()) {
            if (!achievement.isAcquired() && achievement.statToCheck == updatedStat)
                achievement.check();
        }
    }

}

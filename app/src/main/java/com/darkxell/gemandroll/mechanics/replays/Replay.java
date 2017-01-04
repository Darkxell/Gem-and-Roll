package com.darkxell.gemandroll.mechanics.replays;

import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Darkxell on 25/12/2016.
 */

public class Replay {

    /**
     * The turns. One array index symbolize one player turn, and it's content is the number of rolls the player(or AI) has done in his turn.
     */
    public int[] turns;
    /**The name of the replay.*/
    public String name;
    /**
     * The player names.
     */
    public String[] playernames;
    /**
     * the seed used to create the game of this replay.
     */
    public long seed;

    /**
     * Adds a turn to this Replay.
     */
    public void addTurn() {
        int[] array = new int[this.turns.length + 1];
        System.arraycopy(this.turns, 0, array, 0, this.turns.length);
        array[this.turns.length] = 0;
        this.turns = array;
    }

    @Override
    public String toString() {
        String toreturn = this.name + "=names:";
        for (int i = 0; i < playernames.length; ++i) {
            if (i != 0) toreturn += ",";
            toreturn += playernames[i];
        }
        toreturn += ":turns:";
        for (int i = 0; i < turns.length; ++i) {
            if (i != 0) toreturn += ",";
            toreturn += turns[i];
        }
        toreturn += ":seed:" + this.seed + ":";
        return toreturn;
    }

    public String serialize() {
        return this.toString();
    }

    public static Replay unserialize(String data) {
        Replay toreturn = new Replay();

        try {
            toreturn.name = data.split("=")[0];

            data = data.substring((toreturn.name+"=names:").length());
            toreturn.playernames = data.substring(0, data.indexOf(":turns:")).split(",");
            toreturn.seed = Long.decode(data.substring(data.indexOf(":seed:") + ":seed:".length(), data.length() - ":".length()));

            String[] turnsData = data.substring(data.indexOf(":turns:") + ":turns:".length(), data.indexOf(":seed:")).split(",");
            toreturn.turns = new int[turnsData.length];
            for (int i = 0; i < toreturn.turns.length; ++i)
                toreturn.turns[i] = Integer.parseInt(turnsData[i]);

        } catch (Exception e) {
            Log.d("Replays", "Wrong replay format: " + data);
            return null;
        }

        return toreturn;
    }

    /**
     * Returns a short description of the replay.
     */
    public String getShortDesc() {
        String content = turns.length + ":";
        for (int i = 0; i < playernames.length; ++i)
            content += playernames[i] + ",";
        if (content.length() > 15) content = content.substring(0, 12) + "...";
        return content;
    }
}

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
    /**
     * The player names.
     */
    public String[] playernames;
    /**
     * the seed used to create the game of this replay.
     */
    public long seed;

    @Override
    public String toString() {
        String toreturn = "REPLAY=names:";
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
            data = data.substring("REPLAY=names:".length());
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
}

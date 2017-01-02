package com.darkxell.gemandroll.storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.darkxell.gemandroll.mechanics.replays.Replay;

import java.util.ArrayList;

/**
 * Created by Darkxell on 02/01/2017.
 * A class that holds the replays. This class data is synchronized with the android SQL lite database.
 */

public abstract class ReplaysHolder {

    /**
     * The replays in the ram.
     */
    private static ArrayList<Replay> replays;
    /**
     * Is true if the data hasn't been loaded yet.
     */
    private static boolean isLoaded = false;
    /**
     * The database used to hold the data.
     */
    private static ReplaysDatabase database;

    /**
     * Gets the replays. Note that the arraylist returned is shared with this class Data and should not be changed.
     */
    public static ArrayList<Replay> getReplays() throws Exception {
        if (!isLoaded)
            throw new Exception("Database is not loaded, could not get the replays.");
        return replays;
    }

    /**
     * Adds the wanted replay to the database.
     */
    public static void addReplay(Replay r) throws Exception {
        if (!isLoaded)
            throw new Exception("Database is not loaded, could not add replay.");
        
        SQLiteDatabase d = database.getWritableDatabase();
        d.execSQL("INSERT INTO replays values (" + r.serialize() + ");");
        d.close();
        replays.add(r);
    }

    /**
     * Loads the replays from the database to this class and overrides this class data.
     */
    public static void load(Context context) {
        database = new ReplaysDatabase(context, "replays", null, 1);

        SQLiteDatabase d = database.getReadableDatabase();
        ArrayList<Replay> data = new ArrayList<>();
        Cursor c = d.query("replays", new String[]{"replay" }, null, new String[]{}, null, null, null);
        while (c.moveToNext())
            data.add(Replay.unserialize(c.getString(c.getColumnIndex("replay"))));
        c.close();
        d.close();
        replays = data;
    }

}

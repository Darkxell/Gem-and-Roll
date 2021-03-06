package com.darkxell.gemandroll.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Darkxell on 02/01/2017.
 */

class ReplaysDatabase extends SQLiteOpenHelper {

    public ReplaysDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE replays(replay TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2)
            db.execSQL("CREATE TABLE replays(replay TEXT);");
    }
}

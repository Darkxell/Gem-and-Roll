package com.darkxell.gemandroll.storage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Darkxell on 02/01/2017.
 */

public class Storage {

    public static Storage instance;

    /**
     * Returns the instance of the Storage singleton
     */
    public static Storage i() {
        return instance;
    }

    public Storage(Context c) {
        this.pref = c.getSharedPreferences("pref", Context.MODE_PRIVATE);
    }

    private SharedPreferences pref;

    /**
     * Adds a variable with a value in storage
     */
    public void addVariable(String var, String value) {
        SharedPreferences.Editor e = pref.edit();
        e.putString(var, value);
        e.apply();
    }

    /**
     * Returns the value stored
     */
    public String getValue(String key) {
        return pref.getString(key, "0");
    }

}

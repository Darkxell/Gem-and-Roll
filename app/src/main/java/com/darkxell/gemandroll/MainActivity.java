package com.darkxell.gemandroll;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.darkxell.gemandroll.gamestates.TestState;
import com.darkxell.gemandroll.gamestates.statesutility.CustomView;
import com.darkxell.gemandroll.gamestates.statesutility.GameState;
import com.darkxell.gemandroll.gamestates.statesutility.Updater;

public class MainActivity extends Activity {

    /**
     * The current gamestate being used.
     */
    private GameState currentstate = new TestState(this);
    /**
     * Defines the ammount of updates per second of the app. FPS is as much as possible. (This is a small app for a school project, no one cares about battery usage.)
     */
    private static final int UPS = 30;
    private CustomView view;

    private Updater updater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.view = new CustomView(this);
        this.view.setState(currentstate);
        setContentView(view);
        createUpdaters();
    }

    /**
     * Create threads that calls the print and update methods of the current GameState if not null.
     */
    private void createUpdaters() {
        this.updater = new Updater(UPS) {
            @Override
            public void onUpdate() {
                try {
                    currentstate.update();
                } catch (Exception e) {
                    Log.e("Error", "Couldn't update the current state.");
                }
            }

            @Override
            public void onPrint() {
                try {
                    view.needRepaint(System.nanoTime());
                } catch (Exception e) {
                    Log.e("Error", "Couldn't print the state to the view.");
                }
            }
        };
    }

    /**
     * Sets the gamestate.
     */
    public void setState(GameState newstate) {
        currentstate = newstate;
        view.setState(newstate);
        newstate.setParent(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed(); // Close the activity
        this.currentstate.onBackPressed();
    }


}

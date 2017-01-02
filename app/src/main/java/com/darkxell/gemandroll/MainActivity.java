package com.darkxell.gemandroll;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.darkxell.gemandroll.gamestates.TestState;
import com.darkxell.gemandroll.gamestates.statesutility.CustomView;
import com.darkxell.gemandroll.gamestates.statesutility.GameState;
import com.darkxell.gemandroll.gamestates.MainMenuState;
import com.darkxell.gemandroll.gamestates.statesutility.Updater;
import com.darkxell.gemandroll.storage.ReplaysHolder;

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
    public InputMethodManager keyboard;

    private Updater updater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.view = new CustomView(this);
        this.view.setState(currentstate = new MainMenuState(this));
        setContentView(view);
        createUpdaters();
        ReplaysHolder.load(getApplicationContext());
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

    public void showKeyboard() {
        this.keyboard = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        this.keyboard.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void hideKeyboard() {
        this.keyboard.hideSoftInputFromWindow(this.view.getWindowToken(), 0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this.currentstate.onKeyTyped(keyCode, (char) event.getUnicodeChar());
        return super.onKeyDown(keyCode, event);
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

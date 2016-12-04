package com.darkxell.gemandroll.gamestates.statesutility;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.darkxell.gemandroll.MainActivity;

/**
 * A gamestate defines the behavior of the game at a given time (the time when this state is active) and allows a graphical output of it.
 */

public abstract class GameState {

    /**
     * The activity displaying this state.
     */
    protected MainActivity holder;

    /**
     * Prints the state on a buffer.
     */
    public abstract void print(Canvas buffer);

    /**
     * Updates this state to the next frame.
     */
    public abstract void update();

    /**
     * Called by the parent when the phone back button is pressed.
     */
    public void onBackPressed() {
    }

    /**
     * Called by the parent when the screen is touched.
     */
    public void onTouch(MotionEvent e) {
    }

    /**
     * Inform this state that it's parent is the wanted Activity.
     */
    public void setParent(MainActivity holder) {
        this.holder = holder;
    }
}

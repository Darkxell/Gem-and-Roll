package com.darkxell.gemandroll.gamestates.statesutility;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.darkxell.gemandroll.MainActivity;
import com.darkxell.gemandroll.R;

/**
 * A gamestate defines the behavior of the game at a given time (the time when this state is active) and allows a graphical output of it.
 */

public abstract class GameState {

    /**
     * The activity displaying this state.
     */
    protected MainActivity holder;
    private MenuButton[] buttons;

    public GameState(MainActivity holder) {
        this.holder = holder;
        this.buttons = new MenuButton[0];
    }

    /**
     * Adds a Button to this state.
     */
    protected void addButton(MenuButton button){
        MenuButton[] array = new MenuButton[this.buttons.length + 1];
        System.arraycopy(this.buttons, 0, array, 0, this.buttons.length);
        array[array.length - 1] = button;
        this.buttons = array;
    }

    /**
     * Prints the state on a buffer.
     */
    public abstract void print(Canvas buffer);

    /**
     * Prints this State's buttons.
     */
    public void printButtons(Canvas buffer) {
        for (MenuButton button : this.buttons) {
            button.draw(buffer);
        }
    }

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
        for (MenuButton button : this.buttons) {
            if (button.contains((int) e.getX(), (int) e.getY())) button.onClick();
        }
    }

    /**
     * Inform this state that it's parent is the wanted Activity.
     */
    public void setParent(MainActivity holder) {
        this.holder = holder;
    }
}

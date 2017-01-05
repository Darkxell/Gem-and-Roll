package com.darkxell.gemandroll.gamestates.statesutility;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.darkxell.gemandroll.MainActivity;
import com.darkxell.gemandroll.R;
import com.darkxell.gemandroll.audio.AudioBot;
import com.darkxell.gemandroll.mechanics.Achievement;

/**
 * A gamestate defines the behavior of the game at a given time (the time when this state is active) and allows a graphical output of it.
 */

public abstract class GameState {

    private static Achievement[] achievementsToShow = new Achievement[0];
    private static int achievementTimer = 0, achievementWidth = 0;
    private static MenuButton buttonAchievement = new MenuButton("", null, 0, 0) {
        @Override
        public void onClick() {
        }
    };

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
     * Constants for Achievement animation.
     */
    private static final int APPEAR = 20, STAY = 120;
    /**
     * Prints this State's UI (buttons & achievements).
     */
    public void printUI(Canvas buffer) {
        // Buttons
        for (MenuButton button : this.buttons) {
            button.draw(buffer);
        }

        // Achievement
        if (achievementsToShow.length > 0) {
            if (achievementWidth == 0) {
                // Init button
                buttonAchievement.bitmapOn = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_namebar_left);
                achievementWidth = buttonAchievement.width = buffer.getWidth() / 2;
            }
            // Place button
            if (achievementTimer >= APPEAR && achievementTimer < APPEAR + STAY) buttonAchievement.x = 0;
            else {
                if (achievementTimer < APPEAR) buttonAchievement.x = - (APPEAR - achievementTimer) * achievementWidth / APPEAR;
                else buttonAchievement.x = - (achievementTimer - APPEAR - STAY) * achievementWidth / APPEAR;
            }
            buttonAchievement.draw(buffer);
        }
    }

    /**
     * Updates this state to the next frame.
     */
    public abstract void update();

    /**
     * Updates this State's UI (buttons & achievements).
     */
    public void updateUI() {
        for (MenuButton button : this.buttons) {
            button.update();
        }

        if (achievementsToShow.length > 0) {
            if (achievementTimer == 0){
                buttonAchievement.text = "Success! " + achievementsToShow[0].name;
                AudioBot.i().playSound(R.raw.achievement);
            }
            ++achievementTimer;
            if (achievementTimer >= STAY + 2 * APPEAR) {
                // Skip to next achievement
                achievementTimer = 0;
                Achievement[] array = new Achievement[achievementsToShow.length - 1];
                System.arraycopy(achievementsToShow, 1, array, 0, achievementsToShow.length - 1);
                achievementsToShow = array;
            }
        }
    }

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
            if (button.delay == 0 && button.enabled && button.contains((int) e.getX(), (int) e.getY())){
                button.onClick();
                button.delay = 5;
            }
        }
    }

    /**
     * Inform this state that it's parent is the wanted Activity.
     */
    public void setParent(MainActivity holder) {
        this.holder = holder;
    }

    /**
     * Called by the parent when keyboard keys are pressed.
     * @param keyCode - The ID of the key.
     * @param unicodeChar - The char value of the key.
     */
    public void onKeyTyped(int keyCode, char unicodeChar) {
    }

    public static void showAchievement(Achievement achievement) {
        Achievement[] array = new Achievement[achievementsToShow.length + 1];
        System.arraycopy(achievementsToShow, 0, array, 0, achievementsToShow.length);
        array[array.length - 1] = achievement;
        achievementsToShow = array;
    }
}

package com.darkxell.gemandroll.gamestates.statesutility;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.darkxell.gemandroll.MainActivity;
import com.darkxell.gemandroll.mechanics.Dice;

/**
 * Created by Cubi on 04/01/2017.
 */

public class DiceAnimation {

    public int startX, startY, startSize;
    public int dX, dY, dSize;
    private int timer = 0;
    private int duration;
    public Dice dice;
    private boolean over;

    /**
     * Returns the bounds to display.
     */
    public Rect getCurrentBounds() {
        float m = this.currentMultiplier();
        return new Rect((int) (this.startX + this.dX * m),
                (int) (this.startY + this.dY * m),
                (int) (this.startSize + this.dSize * m),
                (int) (this.startSize + this.dSize * m));
    }

    private float currentMultiplier() {
        return this.isOver() ? 1 : this.timer * 1f / this.duration;
    }

    public void draw(Canvas buffer, MainActivity holder) {
        Rect bounds = this.getCurrentBounds();
        this.dice.draw(buffer, holder, bounds.left, bounds.top, bounds.right - bounds.left);
    }

    /**
     * Updates this Animation.
     */
    public void update() {
        ++this.timer;
    }

    /**
     * Defines this Animation's destination.
     */
    public void setDestination(int x, int y, int size) {
        this.dX = x - this.startX;
        this.dY = y - this.startY;
        this.dSize = size - this.startSize;
    }

    /**
     * Returns true if this Animation is finished.
     */
    public boolean isOver() {
        return this.timer >= this.duration;
    }
}

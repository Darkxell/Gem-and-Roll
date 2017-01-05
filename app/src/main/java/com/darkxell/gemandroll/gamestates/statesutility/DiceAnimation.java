package com.darkxell.gemandroll.gamestates.statesutility;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.darkxell.gemandroll.MainActivity;
import com.darkxell.gemandroll.mechanics.Dice;

/**
 * Created by Cubi on 04/01/2017.
 */

public class DiceAnimation {

    public int startX, startY, startSize;
    public int dX, dY, dSize;
    private int timer = 0;
    public int duration;
    public Dice dice;

    /**
     * Returns the bounds to display.
     */
    public Rect getCurrentBounds() {
        if (this.isOver()) return new Rect(startX + dX, startY + dY, startX + dX + startSize + dSize, startY + dY + startSize + dSize);
        int x = (int) (this.startX + this.dX * this.timer / this.duration), y = (int) (this.startY + this.dY * this.timer / this.duration);
        return new Rect(x, y,
                (int) (x + this.startSize + this.dSize * this.timer / this.duration),
                (int) (y + this.startSize + this.dSize * this.timer / this.duration));
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

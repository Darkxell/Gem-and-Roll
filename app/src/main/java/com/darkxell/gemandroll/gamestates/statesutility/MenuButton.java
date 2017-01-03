package com.darkxell.gemandroll.gamestates.statesutility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.darkxell.gemandroll.R;

/**
 * Created by Cubi on 01/01/2017.
 */

public abstract class MenuButton {

    /**
     * Default button with no click Event.
     */
    public static class Label extends MenuButton {

        public Label(String text, Bitmap bitmap) {
            super(text, bitmap);
        }

        @Override
        public void onClick() {
        }
    }

    public Paint paint;
    public String text;
    public int x, y;
    public int width, height;
    public Bitmap bitmap;
    public boolean visible;
    int delay = 0;

    /**
     * Position defaults to zero.
     */
    public MenuButton(String text, Bitmap bitmap) {
        this(text, bitmap, 0, 0);
    }

    public MenuButton(String text, Bitmap bitmap, int x, int y) {
        this.text = text;
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.width = -1;
        this.height = -1;
        this.visible = true;

        this.paint = new Paint();
        this.paint.setColor(Color.BLACK);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setTextSize(1);
    }

    /**
     * @return True if this Button contains the point of coordinates (x, y).
     */
    public boolean contains(int x, int y) {
        if (!this.visible) return false;
       return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }

    /**
     * Draws this button.
     */
    public void draw(Canvas buffer) {
        if (!this.visible) return;

        // Set default size
        if (this.width == -1) {
            if (this.height == -1) this.width = (int) (buffer.getWidth() / 4);
            else this.width = this.height / this.bitmap.getHeight() * this.bitmap.getWidth();
        }
        if (this.height == -1) this.height = this.width * this.bitmap.getHeight() / this.bitmap.getWidth();
        if (this.paint.getTextSize() == 1) this.paint.setTextSize(buffer.getHeight() / 20);

        // Draw bitmap
        buffer.drawBitmap(bitmap, null, new Rect(x, y, x + this.width, y + this.height), null);

        // Draw text
        buffer.drawText(this.text, x + (this.width / 2) - this.paint.measureText(this.text) / 2, y + (this.height / 2) + this.paint.getTextSize() / 4, this.paint);
    }

    public void update() {
        if (delay > 0) --delay;
    }

    /**
     * Called when this button is pressed.
     */
    public abstract void onClick();

}

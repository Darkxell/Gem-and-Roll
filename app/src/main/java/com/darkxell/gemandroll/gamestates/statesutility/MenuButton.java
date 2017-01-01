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

    private static Paint paint;

    public String text;
    public int x, y;
    public int width, height;
    public Bitmap bitmap;
    public boolean visible;

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
    }

    /**
     * @return True if this Button contains the point of coordinates (x, y).
     */
    public boolean contains(int x, int y) {
       return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }

    /**
     * Draws this button.
     */
    public void draw(Canvas buffer) {
        if (!this.visible) return;

        // Set default size
        if (this.width == -1 && this.height == -1) {
            this.width = (int) (buffer.getWidth() / 4);
            this.height = this.width * this.bitmap.getHeight() / this.bitmap.getWidth();
        }

        // Draw bitmap
        buffer.drawBitmap(bitmap, null, new Rect(x, y, x + this.width, y + this.height), null);

        // Draw text
        this.paint.setTextSize(buffer.getHeight() / 20);
        buffer.drawText(this.text, x + (this.width / 2) - this.paint.measureText(this.text) / 2, y + (this.height / 2) + this.paint.getTextSize() / 4, this.paint);
    }

    /**
     * Called when this button is pressed.
     */
    public abstract void onClick();

}

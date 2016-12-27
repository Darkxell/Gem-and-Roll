package com.darkxell.gemandroll.gamestates;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.darkxell.gemandroll.MainActivity;
import com.darkxell.gemandroll.R;
import com.darkxell.gemandroll.gamestates.statesutility.GameState;

/**
 * Created by Darkxell on 27/12/2016.
 */

public class OptionsState extends GameState {

    public OptionsState(MainActivity holder) {
        super(holder);
        this.paint = new Paint();
        this.paint.setColor(Color.BLACK);
        this.paint.setStyle(Paint.Style.FILL);
    }

    private Paint paint;
    private Bitmap background = BitmapFactory.decodeResource(holder.getResources(), R.drawable.woodbg);
    private Bitmap button = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_button);

    private int backButtonw, backButtonh;

    @Override
    public void print(Canvas buffer) {
        buffer.drawBitmap(background, null, new Rect(0, 0, buffer.getWidth(), buffer.getHeight()), null);

        //Display the back button
        int buttonheight, buttonwidth;
        this.paint.setTextSize(buffer.getHeight() / 20);
        buttonwidth = (int) (buffer.getWidth() / 4);
        buttonheight = buttonwidth * button.getHeight() / button.getWidth();
        this.backButtonh = buttonheight + 20;
        this.backButtonw = buttonwidth + 20;
        String text = "Back";
        int left = 20, top = 20;
        buffer.drawBitmap(button, null, new Rect(left, top, left + buttonwidth, top + buttonheight), null);
        buffer.drawText(text, left + (button.getWidth() / 2) - this.paint.measureText(text), top + (button.getHeight() / 2), this.paint);
    }

    @Override
    public void update() {

    }

    @Override
    public void onBackPressed() {
        super.holder.setState(new MainMenuState(super.holder));
    }

    @Override
    public void onTouch(MotionEvent e) {
        if (e.getY() < this.backButtonh && e.getX() < this.backButtonw)
            super.holder.setState(new MainMenuState(super.holder));
    }
}

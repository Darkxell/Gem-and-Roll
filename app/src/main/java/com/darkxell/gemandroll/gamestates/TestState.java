package com.darkxell.gemandroll.gamestates;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import com.darkxell.gemandroll.gamestates.statesutility.GameState;

/**
 * Created by Darkxell on 03/12/2016.
 */

public class TestState extends GameState {


    private int offset = 0;
    private int horoffset = 200;

    @Override
    public void print(Canvas buffer) {
        int radius;
        radius = 100;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        buffer.drawPaint(paint);
        paint.setColor(Color.parseColor("#CD5C5C"));
        buffer.drawCircle(horoffset, offset, radius, paint);
    }

    @Override
    public void update() {
        offset+=5;
    }

    @Override
    public void onTouch(MotionEvent e) {
        offset = 0;
        horoffset = (int) e.getX();

    }
}

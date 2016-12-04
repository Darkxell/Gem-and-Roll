package com.darkxell.gemandroll.gamestates.statesutility;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Darkxell on 04/12/2016.
 */

public class CustomView extends View {

    private GameState currentstate;
    private long time = 0L;
    private Canvas c;

    /**
     * Builds a new Customview.
     */
    public CustomView(Context context) {
        super(context);
       setOnTouchListener(new OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               currentstate.onTouch(event);
               return true;
           }
       });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        currentstate.print(canvas);
        c = canvas;
    }

    /**
     * Repaints this view.
     */
    public void needRepaint(long time) {
        this.time = time;
        postInvalidate();
    }

    /**
     * Sets the gamestate.
     */
    public void setState(GameState newstate) {
        currentstate = newstate;
    }

    @Override
    /**Sets this custom view to keep the screen on.*/
    public boolean getKeepScreenOn() {
        return true;
    }


}

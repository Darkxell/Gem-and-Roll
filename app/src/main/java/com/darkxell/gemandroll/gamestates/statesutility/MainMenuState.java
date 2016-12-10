package com.darkxell.gemandroll.gamestates.statesutility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.darkxell.gemandroll.MainActivity;
import com.darkxell.gemandroll.R;

/**
 * Created by Darkxell on 09/12/2016.
 */

public class MainMenuState extends GameState {

    public MainMenuState(MainActivity holder) {
        super(holder);
    }

    private int verticaloffset;
    private int counter;
    private Bitmap background = BitmapFactory.decodeResource(holder.getResources(), R.drawable.menubackground);
    private Bitmap title = BitmapFactory.decodeResource(holder.getResources(), R.drawable.title);

    @Override
    public void print(Canvas buffer) {
        int imageheight = buffer.getWidth() / background.getWidth() * background.getHeight();
        if (verticaloffset >= imageheight - buffer.getHeight())

            buffer.drawBitmap(background, null, new Rect(0, buffer.getHeight() - imageheight, buffer.getWidth(), buffer.getHeight()), null);
        else
            buffer.drawBitmap(background, null, new Rect(0, -verticaloffset, buffer.getWidth(), imageheight - verticaloffset), null);
        if(counter>90){
            buffer.drawBitmap(title,(buffer.getWidth()/2)-(title.getWidth()/2),(buffer.getHeight()/2)-(title.getHeight()/2)-Math.min(counter - 89,110),null);

        }

    }

    @Override
    public void update() {
        int nofs = verticaloffset + 1 + verticaloffset / 30;
        if (nofs < Integer.MAX_VALUE / 2)
            verticaloffset = nofs;//Prevent the offset to loop around INTEGER.MAX_VALUE
        ++counter;
    }
}

package com.darkxell.gemandroll.gamestates;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import com.darkxell.gemandroll.MainActivity;
import com.darkxell.gemandroll.R;
import com.darkxell.gemandroll.gamestates.OptionsState;
import com.darkxell.gemandroll.gamestates.statesutility.GameState;

/**
 * Created by Darkxell on 09/12/2016.
 */

public class MainMenuState extends GameState {

    public MainMenuState(MainActivity holder) {
        super(holder);
        this.paint = new Paint();
        this.paint.setColor(Color.BLACK);
        this.paint.setStyle(Paint.Style.FILL);
    }

    private Paint paint;
    private int verticaloffset;
    private int counter;
    private Bitmap background = BitmapFactory.decodeResource(holder.getResources(), R.drawable.menubackground);
    private Bitmap title = BitmapFactory.decodeResource(holder.getResources(), R.drawable.title);
    private Bitmap button = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_button);

    private int bufferwidth, bufferheight;

    @Override
    public void print(Canvas buffer) {
        this.bufferwidth = buffer.getWidth();
        this.bufferheight = buffer.getHeight();
        int imageheight = buffer.getWidth() / background.getWidth() * background.getHeight();
        if (verticaloffset >= imageheight - buffer.getHeight())
            buffer.drawBitmap(background, null, new Rect(0, buffer.getHeight() - imageheight, buffer.getWidth(), buffer.getHeight()), null);
        else
            buffer.drawBitmap(background, null, new Rect(0, -verticaloffset, buffer.getWidth(), imageheight - verticaloffset), null);
        if (counter > 145) {
            buffer.drawBitmap(title, (buffer.getWidth() / 2) - (title.getWidth() / 2), (buffer.getHeight() / 3) - (title.getHeight() / 2) - Math.min(counter - 145, 50), null);

            this.paint.setTextSize(buffer.getHeight() / 20);
            int buttonheight, buttonwidth;
            buttonwidth = (int) (buffer.getWidth() / 4);
            buttonheight = buttonwidth * button.getHeight() / button.getWidth();
            //Display the play button
            String text = "Play";
            int left = (buffer.getWidth() / 2) - (buttonwidth / 2), top = 2 * buffer.getHeight() / 3;
            buffer.drawBitmap(button, null, new Rect(left, top, left + buttonwidth, top + buttonheight), null);
            buffer.drawText(text, left + (button.getWidth() / 2) - this.paint.measureText(text), top + (button.getHeight() / 2), this.paint);

            //Display the options button
            text = "Options";
            left = (buffer.getWidth() / 4) - (buttonwidth / 2);
            top = 2 * buffer.getHeight() / 3 + 65;
            buffer.drawBitmap(button, null, new Rect(left, top, left + buttonwidth, top + buttonheight), null);
            buffer.drawText(text, left + (button.getWidth() / 2) - this.paint.measureText(text), top + (button.getHeight() / 2), this.paint);

            //Display the replays button
            text = "Replays";
            left = (3 * buffer.getWidth() / 4) - (buttonwidth / 2);
            buffer.drawBitmap(button, null, new Rect(left, top, left + buttonwidth, top + buttonheight), null);
            buffer.drawText(text, left + (button.getWidth() / 2) - this.paint.measureText(text), top + (button.getHeight() / 2), this.paint);

        }

    }

    @Override
    public void update() {
        int nofs = verticaloffset + 1 + verticaloffset / 30;
        if (nofs < Integer.MAX_VALUE / 2)
            verticaloffset = nofs;
        //Prevent the offset to loop around INTEGER.MAX_VALUE, causing a white flicker in the background.
        ++counter;
    }

    @Override
    public void onTouch(MotionEvent e) {
        if (e.getX() < bufferwidth / 3 && e.getY() > bufferheight / 2)
            super.holder.setState(new OptionsState(super.holder));
        else if (e.getX() > bufferwidth - (bufferwidth / 3) && e.getY() > bufferheight / 2)
            Log.d("Replays", "Feature not implemented yet, sorry...");
        else if (e.getY() > bufferheight / 2) Log.d("Play", "Play");

    }
}

package com.darkxell.gemandroll.gamestates.statesutility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.darkxell.gemandroll.MainActivity;
import com.darkxell.gemandroll.R;
import com.darkxell.gemandroll.gamestates.MainMenuState;

/**
 * Created by Darkxell on 04/01/2017.
 * A state designed to shows the list of replays.
 */

public class ReplaysState extends GameState {

    public ReplaysState(MainActivity holder) {
        super(holder);
        this.addButton(new MenuButton("Back", button, 20, 20) {
            @Override
            public void onClick() {
                onBackPressed();
            }
        });
    }

    private Bitmap background = BitmapFactory.decodeResource(holder.getResources(), R.drawable.woodbg);
    private Bitmap button = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_button);

    @Override
    public void print(Canvas buffer) {
        buffer.drawBitmap(background, null, new Rect(0, 0, buffer.getWidth(), buffer.getHeight()), null);
        this.printUI(buffer);
    }

    @Override
    public void update() {
        this.updateUI();
    }

    @Override
    public void onBackPressed() {
        super.holder.setState(new MainMenuState(super.holder));
    }
}

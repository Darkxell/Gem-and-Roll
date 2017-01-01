package com.darkxell.gemandroll.gamestates;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.darkxell.gemandroll.MainActivity;
import com.darkxell.gemandroll.R;
import com.darkxell.gemandroll.gamestates.statesutility.GameState;
import com.darkxell.gemandroll.gamestates.statesutility.MenuButton;

/**
 * Created by Cubi on 01/01/2017.
 */
public class PlayerSelectionState extends GameState {

    private int bufferWidth, bufferHeight;
    private Bitmap background = BitmapFactory.decodeResource(holder.getResources(), R.drawable.woodbg);
    private Bitmap button = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_button);

    public PlayerSelectionState(MainActivity holder) {
        super(holder);
        this.addButton(new MenuButton("Back", button, 20, 20) {
            @Override
            public void onClick() {
                onBackPressed();
            }
        });
    }

    @Override
    public void print(Canvas buffer) {
        this.bufferWidth = buffer.getWidth();
        this.bufferHeight = buffer.getHeight();
        int imageheight = buffer.getWidth() / background.getWidth() * background.getHeight();
        buffer.drawBitmap(background, null, new Rect(0, buffer.getHeight() - imageheight, buffer.getWidth(), buffer.getHeight()), null);

        this.printButtons(buffer);
    }

    @Override
    public void update() {
    }

    @Override
    public void onBackPressed() {
        super.holder.setState(new MainMenuState(super.holder));
    }
}

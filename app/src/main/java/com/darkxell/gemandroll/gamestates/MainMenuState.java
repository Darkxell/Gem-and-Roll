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
import com.darkxell.gemandroll.gamestates.statesutility.MenuButton;
import com.darkxell.gemandroll.mechanics.Player;
import com.darkxell.gemandroll.mechanics.PlayerAI;

/**
 * Created by Darkxell on 09/12/2016.
 */

public class MainMenuState extends GameState {

    private static final int PLAY = 0, OPTIONS = 1, REPLAYS = 2;

    public MainMenuState(MainActivity holder) {
        super(holder);

        this.addButton(this.buttonPlay = new MenuButton("Play", button, 0, 0) {
            @Override
            public void onClick() {
                onButtonClick(PLAY);
            }
        });
        this.addButton(this.buttonOptions = new MenuButton("Options", button, 0, 0) {
            @Override
            public void onClick() {
                onButtonClick(OPTIONS);
            }
        });
        this.addButton(this.buttonReplays = new MenuButton("Replays", button, 0, 0) {
            @Override
            public void onClick() {
                onButtonClick(REPLAYS);
            }
        });
    }

    private void onButtonClick(int buttonID) {
        if (buttonID == OPTIONS)
            super.holder.setState(new OptionsState(super.holder));
        else if (buttonID == REPLAYS)
            Log.d("Replays", "Feature not implemented yet, sorry...");
        else if (buttonID == PLAY)
            super.holder.setState(new PlayerSelectionState(super.holder));
            //super.holder.setState(new RecursiveGameState(super.holder, new Player[]{new Player("Player"), new Player("Testing AI", PlayerAI.TurnValueAI)}));
    }

    private int verticaloffset;
    private int counter;
    private Bitmap background = BitmapFactory.decodeResource(holder.getResources(), R.drawable.menubackground);
    private Bitmap title = BitmapFactory.decodeResource(holder.getResources(), R.drawable.title);
    private Bitmap button = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_button);
    private MenuButton buttonPlay, buttonOptions, buttonReplays;

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

            if (this.buttonPlay.x == 0) this.placeButtons(buffer);
            this.printButtons(buffer);
        }

    }

    /**
     * Places and resizes the buttons.
     */
    private void placeButtons(Canvas buffer) {
        int buttonheight, buttonwidth;
        buttonwidth = (int) (buffer.getWidth() / 4);
        buttonheight = buttonwidth * button.getHeight() / button.getWidth();

        // Place the play button
        this.buttonPlay.x = (buffer.getWidth() / 2) - (buttonwidth / 2);
        this.buttonPlay.y = 2 * buffer.getHeight() / 3;

        // Place the options button
        this.buttonOptions.x = (buffer.getWidth() / 4) - (buttonwidth / 2);
        this.buttonOptions.y = 2 * buffer.getHeight() / 3 + 65;

        // Place the replays button
        this.buttonReplays.x = (3 * buffer.getWidth() / 4) - (buttonwidth / 2);
        this.buttonReplays.y = this.buttonOptions.y;

        // Set buttons size
        this.buttonPlay.width = this.buttonOptions.width = this.buttonReplays.width = buttonwidth;
        this.buttonPlay.height = this.buttonOptions.height = this.buttonReplays.height = buttonheight;
    }

    @Override
    public void update() {
        int nofs = verticaloffset + 1 + verticaloffset / 30;
        if (nofs < Integer.MAX_VALUE / 2)
            verticaloffset = nofs;
        //Prevent the offset to loop around INTEGER.MAX_VALUE, causing a white flicker in the background.
        ++counter;
    }
}

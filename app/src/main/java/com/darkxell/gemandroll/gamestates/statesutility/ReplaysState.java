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

        this.addButton(this.button1 = new MenuButton("", namebar, 0, 0) {
            @Override
            public void onClick() {
                seeReplay(0);
            }
        });
        this.addButton(this.button2 = new MenuButton("", namebar, 0, 0) {
            @Override
            public void onClick() {
                seeReplay(1);
            }
        });
        this.addButton(this.button3 = new MenuButton("", namebar, 0, 0) {
            @Override
            public void onClick() {
                seeReplay(2);
            }
        });
        this.addButton(this.button4 = new MenuButton("", namebar, 0, 0) {
            @Override
            public void onClick() {
                seeReplay(3);
            }
        });
    }

    private int scrollOffset = 0;
    private boolean needReplace = true;

    private Bitmap namebar = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_namebar);
    private Bitmap background = BitmapFactory.decodeResource(holder.getResources(), R.drawable.woodbg);
    private Bitmap button = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_button);

    private MenuButton button1, button2, button3, button4;

    @Override
    public void print(Canvas buffer) {
        buffer.drawBitmap(background, null, new Rect(0, 0, buffer.getWidth(), buffer.getHeight()), null);
        if (this.needReplace) this.placeButtons(buffer);
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

    private void placeButtons(Canvas buffer){


        this.button1.width = this.button2.width = this.button3.width = this.button4.width = buffer.getWidth() * 2 / 5;
        this.button1.x = this.button2.x = this.button3.x = this.button4.x = buffer.getWidth() - this.button1.width;

        int pad = buffer.getHeight() / 12;
        int y = pad;

        y += pad * 2;
        this.button1.y = y;

        y += pad * 2;
        this.button2.y = y;

        y += pad * 2;
        this.button3.y = y;

        y += pad * 2;
        this.button4.y = y;
    }

    private void seeReplay(int offset){
        int tosee = offset + this.scrollOffset;
    }
}

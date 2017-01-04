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
import com.darkxell.gemandroll.gamestates.statesutility.MenuButton;
import com.darkxell.gemandroll.mechanics.Achievement;
import com.darkxell.gemandroll.mechanics.replays.Replay;
import com.darkxell.gemandroll.storage.ReplaysHolder;

import java.util.ArrayList;

/**
 * Created by Darkxell on 27/12/2016.
 */

public class AchievementsState extends GameState {

    public AchievementsState(MainActivity holder) {
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
        this.addButton(this.buttonminus = new MenuButton("", minus, 0, 20) {
            @Override
            public void onClick() {
                if (scrollOffset > 0) scrollOffset -= 4;
                updatebuttons();
            }
        });
        this.addButton(this.buttonplus = new MenuButton("", plus, 0, 20) {
            @Override
            public void onClick() {
                if (scrollOffset + 4 < Achievement.values().length - 1) scrollOffset += 4;
                updatebuttons();
            }
        });
        this.addButton(this.pagebutton = new MenuButton("Loading...",button, 0, 20) {
            @Override
            public void onClick() {
            }
        });
    }

    private boolean needReplace = true;

    private Bitmap namebar = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_namebar);
    private Bitmap nonamebar = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_namebar_ai);
    private Bitmap background = BitmapFactory.decodeResource(holder.getResources(), R.drawable.woodbg);
    private Bitmap button = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_button);
    private Bitmap plus = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_plus);
    private Bitmap minus = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_minus);

    private MenuButton button1, button2, button3, button4, buttonminus, buttonplus,pagebutton;

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

    private void placeButtons(Canvas buffer) {
        //the four buttons on the right
        this.button1.width = this.button2.width = this.button3.width = this.button4.width = buffer.getWidth() * 2 / 5;
        this.button1.x = this.button2.x = this.button3.x = this.button4.x = buffer.getWidth() - this.button1.width;
        int pad = buffer.getHeight() / 12;
        int y = pad;
        y += pad;
        this.button1.y = y;
        y += pad * 2;
        this.button2.y = y;
        y += pad * 2;
        this.button3.y = y;
        y += pad * 2;
        this.button4.y = y;
        //plus and minus buttons
        y += pad * 2;
        this.buttonminus.width = this.buttonminus.height = this.buttonplus.width = this.buttonplus.height = pad;
        int horalign =  buffer.getWidth() * 4 / 5;
        this.buttonminus.y = this.buttonplus.y = y;
        this.buttonminus.x = horalign - pad;
        this.buttonplus.x = horalign + pad;
        //Page button
        this.pagebutton.height = (int) (pad*1.5f);
        this.pagebutton.width = buffer.getWidth()/5;
        this.pagebutton.x =  buffer.getWidth() * 7 / 10;

        updatebuttons();
    }

    private void seeReplay(int offset) {
        int tosee = offset + this.scrollOffset;
    }

    private void updatebuttons() {
        Achievement[] vals = Achievement.values();
        if (scrollOffset < vals.length){
            button1.text = vals[scrollOffset].name;
            button1.bitmapOn = (vals[scrollOffset].isAcquired())?namebar:nonamebar;
        }else{
            button1.text = "...";
            button1.bitmapOn =namebar;
        }
        if (scrollOffset + 1 < vals.length){
            button2.text = vals[scrollOffset+1].name;
            button2.bitmapOn = (vals[scrollOffset+1].isAcquired())?namebar:nonamebar;
        }else{
            button2.text = "...";
            button2.bitmapOn =namebar;
        }
        if (scrollOffset + 2 < vals.length){
            button3.text = vals[scrollOffset+2].name;
            button3.bitmapOn = (vals[scrollOffset+2].isAcquired())?namebar:nonamebar;
        }else{
            button3.text = "...";
            button3.bitmapOn =namebar;
        }
        if (scrollOffset + 3 < vals.length){
            button4.text = vals[scrollOffset+3].name;
            button4.bitmapOn = (vals[scrollOffset+3].isAcquired())?namebar:nonamebar;
        }else{
            button4.text = "...";
            button4.bitmapOn =namebar;
        }
        pagebutton.text = "Page "+(scrollOffset/4+1)+"/"+((vals.length-1)/4+1);
    }
}

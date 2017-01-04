package com.darkxell.gemandroll.gamestates.statesutility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.darkxell.gemandroll.MainActivity;
import com.darkxell.gemandroll.R;
import com.darkxell.gemandroll.audio.AudioBot;
import com.darkxell.gemandroll.gamestates.MainMenuState;
import com.darkxell.gemandroll.gamestates.RecursiveGameState;
import com.darkxell.gemandroll.mechanics.replays.Replay;
import com.darkxell.gemandroll.storage.ReplaysHolder;

import java.util.ArrayList;

/**
 * Created by Darkxell on 04/01/2017.
 * A state designed to shows the list of replays.
 */

public class ReplaysState extends GameState {

    /**
     * The replays at the moment the state is created.
     */
    private ArrayList<Replay> replays;

    public ReplaysState(MainActivity holder) {
        super(holder);
        AudioBot.i().setBGM(R.raw.menus);
        try {
            this.replays = ReplaysHolder.getReplays();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                if (scrollOffset + 4 < replays.size() - 1) scrollOffset += 4;
                updatebuttons();
            }
        });
        this.addButton(this.pagebutton = new MenuButton("Loading...", button, 0, 20) {
            @Override
            public void onClick() {
            }
        });
    }

    private int scrollOffset = 0,counter=0;
    private boolean needReplace = true;

    private Bitmap namebar = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_namebar);
    private Bitmap background = BitmapFactory.decodeResource(holder.getResources(), R.drawable.woodbg);
    private Bitmap button = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_button);
    private Bitmap plus = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_plus);
    private Bitmap minus = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_minus);

    private MenuButton button1, button2, button3, button4, buttonminus, buttonplus, pagebutton;

    @Override
    public void print(Canvas buffer) {
        buffer.drawBitmap(background, null, new Rect(0, 0, buffer.getWidth(), buffer.getHeight()), null);
        if (this.needReplace) this.placeButtons(buffer);
        this.printUI(buffer);

    }

    @Override
    public void update() {
        this.updateUI();
    ++counter;
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
        int horalign = buffer.getWidth() * 4 / 5;
        this.buttonminus.y = this.buttonplus.y = y;
        this.buttonminus.x = horalign - pad;
        this.buttonplus.x = horalign + pad;
        //Page button
        this.pagebutton.height = (int) (pad * 1.5f);
        this.pagebutton.width = buffer.getWidth() / 5;
        this.pagebutton.x = buffer.getWidth() * 7 / 10;

        updatebuttons();
    }

    private void seeReplay(int offset) {
        int tosee = offset + this.scrollOffset;
        if (tosee < replays.size()&&counter>20)
            super.holder.setState(new RecursiveGameState(this.replays.get(tosee), super.holder));
    }

    private void updatebuttons() {
        if (scrollOffset < replays.size()) {
            button1.text = replays.get(scrollOffset).name;
        } else {
            button1.text = "...";
        }
        if (scrollOffset + 1 < replays.size()) {
            button2.text = replays.get(scrollOffset + 1).name;
        } else {
            button2.text = "...";
        }
        if (scrollOffset + 2 < replays.size()) {
            button3.text = replays.get(scrollOffset + 2).name;
        } else {
            button3.text = "...";
        }
        if (scrollOffset + 3 < replays.size()) {
            button4.text = replays.get(scrollOffset + 3).name;
        } else {
            button4.text = "...";
        }

        pagebutton.text = "Page " + (scrollOffset / 4 + 1) + "/" + ((replays.size() - 1) / 4 + 1);
    }
}

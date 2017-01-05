package com.darkxell.gemandroll.gamestates;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.Menu;

import com.darkxell.gemandroll.MainActivity;
import com.darkxell.gemandroll.R;
import com.darkxell.gemandroll.audio.AudioBot;
import com.darkxell.gemandroll.gamestates.statesutility.GameState;
import com.darkxell.gemandroll.gamestates.statesutility.MenuButton;
import com.darkxell.gemandroll.gamestates.statesutility.TextInputListener;
import com.darkxell.gemandroll.gamestates.statesutility.TextInputState;
import com.darkxell.gemandroll.mechanics.Player;
import com.darkxell.gemandroll.mechanics.replays.Replay;
import com.darkxell.gemandroll.storage.ReplaysHolder;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Cubi on 04/01/2017.
 */
public class EndGameState extends GameState implements TextInputListener {

    public EndGameState(MainActivity holder, Player[] players, Replay replay, boolean wasReplay) {
        super(holder);
        this.players = players;
        this.replay = replay;
        this.wasReplay = wasReplay;

        this.sortPlayers();
        this.createUI();
        AudioBot.i().setBGM(R.raw.menus);
    }

    /**
     * Sorts players according to their score.
     */
    private void sortPlayers() {
        ArrayList<Player> list = new ArrayList<Player>();
        for (Player p : this.players) {
            boolean placed = false;
            for (int i = 0; i < list.size(); ++i) {
                if (list.get(i).getScore() < p.getScore()) {
                    list.add(i, p);
                    placed = true;
                    break;
                }
            }
            if (!placed) list.add(p);
        }
        this.players = list.toArray(new Player[this.players.length]);
    }

    /**
     * Players of this Game.
     */
    private Player[] players;
    /**
     * The Game's replay.
     */
    private Replay replay;
    /**
     * Currently selected player.
     */
    private int selected = 0;
    /**
     * True if the Game was a replay.
     */
    private boolean wasReplay;

    // Bitmaps
    private Bitmap background = BitmapFactory.decodeResource(holder.getResources(), R.drawable.woodbg);
    private Bitmap button = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_button);
    private Bitmap button_off = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_button_deactivated);
    private Bitmap namebar = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_namebar_ai);
    private Bitmap namebar_selected = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_namebar);
    private Bitmap namebar_left = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_namebar_left);
    private Bitmap namebar_left_off = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_namebar_left_ai);
    private Paint paint = new Paint();

    // Player names
    private MenuButton buttonP1 = new MenuButton("", namebar) {
        @Override
        public void onClick() {
            select(0);
            AudioBot.i().playSound(R.raw.accept);
        }
    };
    private MenuButton buttonP2 = new MenuButton("", namebar) {
        @Override
        public void onClick() {
            select(1);
            AudioBot.i().playSound(R.raw.accept);
        }
    };
    private MenuButton buttonP3 = new MenuButton("", namebar) {
        @Override
        public void onClick() {
            select(2);
            AudioBot.i().playSound(R.raw.accept);
        }
    };
    private MenuButton buttonP4 = new MenuButton("", namebar) {
        @Override
        public void onClick() {
            select(3);
            AudioBot.i().playSound(R.raw.accept);
        }
    };

    private MenuButton buttonMenu = new MenuButton("Main Menu", button) {
        @Override
        public void onClick() {
            onBackPressed();
            AudioBot.i().playSound(R.raw.back);
        }
    };
    private MenuButton buttonReplay = new MenuButton("Replay: ", namebar_left) {
        @Override
        public void onClick() {
            editReplay();
            AudioBot.i().playSound(R.raw.accept);
        }
    };

    private MenuButton buttonSaveReplay = new MenuButton("Save replay", button) {
        @Override
        public void onClick() {
            saveReplay();
            AudioBot.i().playSound(R.raw.accept);
        }
    };

    private MenuButton buttonName = new MenuButton.Label("", namebar_left);

    /**
     * Canvas dimensions
     */
    private int width, height;
    private int horizontalSplit, verticalSplit;

    /**
     * Creates the Buttons.
     */
    private void createUI() {
        this.buttonP1.text = "Winner ! " + this.players[0].name;
        this.buttonP2.text = this.players[1].name;
        if (this.players.length >= 3) this.buttonP3.text = this.players[2].name;
        if (this.players.length >= 4) this.buttonP4.text = this.players[3].name;
        this.buttonP3.visible = this.players.length >= 3;
        this.buttonP4.visible = this.players.length >= 4;
        this.buttonReplay.text += this.players[0].name;
        this.replay.name = this.buttonReplay.text;

        this.buttonP1.bitmapOff = this.buttonP2.bitmapOff = this.buttonP3.bitmapOff = this.buttonP4.bitmapOff = namebar_selected;
        this.select(0);

        this.buttonSaveReplay.bitmapOff = button_off;
        this.buttonReplay.bitmapOff = namebar_left_off;

        this.addButton(this.buttonP1);
        this.addButton(this.buttonP2);
        this.addButton(this.buttonP3);
        this.addButton(this.buttonP4);
        this.addButton(this.buttonMenu);
        if (!this.wasReplay) {
            this.addButton(this.buttonReplay);
            this.addButton(this.buttonSaveReplay);
        }
        this.addButton(this.buttonName);
    }

    // size = draw size, width/height = distance dimensions
    private int startX = 10, startY, gemSize = 0, gemWidth, gemHeight, padX, padY;

    @Override
    public void print(Canvas buffer) {

        buffer.drawBitmap(background, null, new Rect(0, 0, buffer.getWidth(), buffer.getHeight()), null);

        if (this.buttonP1.x == 0) this.placeButtons(buffer);

        int x = this.startX, y = startY;
        for (int i = 0; i < this.players[this.selected].gems.length; ++i) {
            buffer.drawBitmap(this.players[this.selected].gems[i].sprite, null, new Rect(x, y, x + this.gemSize, y + this.gemSize), null);
            x += this.gemWidth + padX;
            if (i % 8 == 7) {
                x = this.startX;
                y += this.gemHeight + padY;
            }
        }

        this.printUI(buffer);
        String text = this.players[this.selected].deaths + " Deaths";
        buffer.drawText(text, this.buttonSaveReplay.x + this.buttonSaveReplay.width / 2 - this.paint.measureText(text) / 2, this.buttonSaveReplay.y - this.paint.getTextSize() * 1.2f, this.paint);

    }

    private void placeButtons(Canvas buffer) {
        this.width = buffer.getWidth();
        this.height = buffer.getHeight();

        this.horizontalSplit = this.width / 3;

        this.buttonMenu.x = this.buttonMenu.y = 10;

        this.buttonP1.width = this.width * 2 / 3;
        this.buttonP1.x = this.width - this.buttonP1.width;
        this.buttonP1.paint.setTextSize(this.height / 10);

        this.buttonP1.processDimensions(buffer);
        this.verticalSplit = this.buttonP1.height + this.height / 10;

        int buttonHeight = (this.height - this.verticalSplit) / Math.max(this.players.length, 3);
        int pad = buttonHeight / 4;
        int y = this.verticalSplit + pad / 2;

        this.buttonP2.y = y;
        y += pad + buttonHeight;
        this.buttonP3.y = y;
        y += pad + buttonHeight;
        this.buttonP4.y = y;

        this.buttonP2.height = this.buttonP3.height = this.buttonP4.height = buttonHeight;
        this.buttonP2.processDimensions(buffer);
        this.buttonP2.x = this.buttonP3.x = this.buttonP4.x = this.width - this.buttonP2.width;

        this.buttonReplay.width = this.horizontalSplit;
        this.buttonReplay.processDimensions(buffer);
        this.buttonReplay.x = 0;
        this.buttonReplay.y = this.height - 10 - this.buttonReplay.height;

        this.buttonSaveReplay.x = this.buttonReplay.width - 5;
        this.buttonSaveReplay.y = this.buttonReplay.y + this.buttonReplay.height / 3;

        this.buttonName.width = (int) (this.buttonP1.x * 1.1f);
        this.buttonName.y = this.verticalSplit / 2;

        this.buttonName.processDimensions(buffer);
        this.startY = this.buttonName.y + this.buttonName.height + 10;
        int availableWidth = this.width - this.buttonP2.width - 20, availableHeight = this.buttonReplay.y - (this.buttonName.y + this.buttonName.height);
        this.gemWidth = availableWidth / 9;
        this.gemHeight = availableHeight / 5;
        this.gemSize = Math.min(this.gemWidth, this.gemHeight);
        this.padX = this.gemWidth / 9;
        this.padY = this.gemHeight / 5;

        this.paint.setTextSize(this.height / 16);
        this.paint.setColor(Color.BLACK);
    }

    private void select(int i) {
        this.selected = i;
        this.buttonP1.enabled = this.selected != 0;
        this.buttonP2.enabled = this.selected != 1;
        this.buttonP3.enabled = this.selected != 2;
        this.buttonP4.enabled = this.selected != 3;
        this.buttonName.text = this.players[this.selected].name + "'s stats";
    }

    @Override
    public void onBackPressed() {
        super.holder.setState(new MainMenuState(super.holder));
    }

    @Override
    public void update() {
        this.updateUI();
    }

    private void editReplay() {
        super.holder.setState(new TextInputState(super.holder, this, this.buttonReplay.text));
    }

    private void saveReplay() {
        this.buttonReplay.enabled = false;
        this.buttonSaveReplay.enabled = false;
        try {
            ReplaysHolder.addReplay(this.replay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInput(String textInput) {
        this.buttonReplay.text = textInput;
        this.replay.name = textInput;
        super.holder.setState(this);
    }

    @Override
    public void cancelInput() {
        super.holder.setState(this);
    }
}

package com.darkxell.gemandroll.gamestates;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.darkxell.gemandroll.MainActivity;
import com.darkxell.gemandroll.R;
import com.darkxell.gemandroll.gamestates.statesutility.GameState;
import com.darkxell.gemandroll.mechanics.Dice;
import com.darkxell.gemandroll.mechanics.Player;
import com.darkxell.gemandroll.mechanics.SeededRNG;
import com.darkxell.gemandroll.mechanics.replays.Replay;

/**
 * Created by Darkxell on 20/12/2016.
 */

public class RecursiveGameState extends GameState {

    /**
     * Builds a new Playstate that can be used to actually play the game.
     */
    public RecursiveGameState(MainActivity holder, Player[] players) {
        super(holder);
        this.players = players;
        this.currentreplay = new Replay();
        this.generator = new SeededRNG();
        this.resetPouches();
        this.paint = new Paint();
        this.paint.setColor(Color.BLACK);
        this.paint.setStyle(Paint.Style.FILL);
    }

    /**
     * Creates a new Gamestate by recursion of the previous one parsed.
     */
    public RecursiveGameState(RecursiveGameState previous) {
        super(previous.holder);
        this.resetPouches();
        this.players = previous.players;
        this.stateiteration = previous.stateiteration + 1;
        this.paint = new Paint();
        this.paint.setColor(Color.BLACK);
        this.paint.setStyle(Paint.Style.FILL);
    }

    /**
     * Builds a new gamestate using a replay. This gamestate will play alone and display exactly the replay content.
     */
    public RecursiveGameState(Replay r, MainActivity holder) {
        super(holder);
        this.currentreplay = r;
        this.isReplay = true;
        this.generator = new SeededRNG(r.seed);
        this.resetPouches();
        this.paint = new Paint();
        this.paint.setColor(Color.BLACK);
        this.paint.setStyle(Paint.Style.FILL);
    }

    private Paint paint;

    private Player[] players;
    private int nowplaying;
    private SeededRNG generator;
    private Dice[] pouch, rolled, hand;
    int stateiteration = 0;
    private Replay currentreplay = null;
    private boolean isReplay = false;

    private Bitmap background = BitmapFactory.decodeResource(holder.getResources(), R.drawable.woodbg);
    private Bitmap button = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_button);
    private Bitmap heartfull = BitmapFactory.decodeResource(holder.getResources(), R.drawable.hearth_full);
    private Bitmap heartempty = BitmapFactory.decodeResource(holder.getResources(), R.drawable.hearth_empty);
    private Bitmap namebar = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_namebar);

    private Bitmap borderv = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_borderv);
    private Bitmap bordert = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_bordert);
    private Bitmap borderh = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_borderh);

    @Override
    public void print(Canvas buffer) {

        buffer.drawBitmap(background, null, new Rect(0, 0, buffer.getWidth(), buffer.getHeight()), null);

        //Draws the horizontal bar and T bar at the bottom.
        int barheight = 2 * buffer.getHeight() / 3;
        for (int i = 0; i < buffer.getWidth(); ) {
            buffer.drawBitmap(borderh, i, barheight, null);
            i += borderh.getWidth();
        }
        int verticle = (int) (buffer.getWidth() / 2.5f);
        buffer.drawBitmap(bordert, verticle, barheight, null);
        for (int i = barheight + 50; i < buffer.getHeight(); ) {
            buffer.drawBitmap(borderv, verticle + 21, i, null);
            i += borderv.getHeight();
        }

        //draws the hearts
        int health = 3;
        for (int i = 0; i < this.rolled.length; ++i)
            if (this.rolled[i].getFace() == Dice.HURT) --health;
        int heartheight = 10 * buffer.getHeight() / 12, spritesize = buffer.getHeight() / 6;
        for (int i = 0; i < 3; ++i) {
            int offset = i * (spritesize + 30);
            buffer.drawBitmap((health >= i) ? heartfull : heartempty, null, new Rect(verticle + 70 + offset, heartheight, verticle + 70 + spritesize + offset, heartheight + spritesize), null);
        }

        //Draws the player name
        String text = this.players[this.nowplaying].name;
        this.paint.setTextSize(buffer.getHeight() / 20);
        buffer.drawText(text, verticle + 80, ((barheight + 20) + heartheight) / 2, this.paint);


        //Draws the reroll and endturn buttons
        int buttonwidth = buffer.getWidth() - (verticle + 40 + (3 * (spritesize + 30)));
        int buttonheight = (button.getHeight() * buttonwidth) / button.getWidth();
        buffer.drawBitmap(button, null, new Rect(buffer.getWidth() - buttonwidth, barheight + 35, buffer.getWidth(), barheight + 35 + buttonheight), null);
        text = "Reroll";
        buffer.drawText(text, buffer.getWidth() - (buttonwidth / 2) - (this.paint.measureText(text) / 2), barheight + 45 + (buttonheight / 2), this.paint);
        int buttonpadding = buttonheight + 30;
        buffer.drawBitmap(button, null, new Rect(buffer.getWidth() - buttonwidth, barheight + 35 + buttonpadding, buffer.getWidth(), barheight + 35 + buttonheight + buttonpadding), null);
        text = "End Turn";
        buffer.drawText(text, buffer.getWidth() - (buttonwidth / 2) - (this.paint.measureText(text) / 2), barheight + 45 + (buttonheight / 2) + buttonpadding, this.paint);


        //Draws the list of players and their score on the top right
        int containerWidth = buffer.getWidth() / 4, contenerHeight = namebar.getHeight() * containerWidth / namebar.getWidth();
        int offset = 30;
        for (int i = this.nowplaying; i < players.length; ++i)
            if (offset + contenerHeight < barheight) {
                buffer.drawBitmap(namebar, null, new Rect(buffer.getWidth() - containerWidth, offset, buffer.getWidth(), offset + contenerHeight), null);
                text = this.players[i].getScore() + " : " + this.players[i].name;
                buffer.drawText(text, buffer.getWidth() - containerWidth + (containerWidth / 5), offset + (contenerHeight / 2), this.paint);
                offset += 30 + contenerHeight;
            }
        for (int i = 0; i < this.nowplaying; ++i)
            if (offset + contenerHeight < barheight) {
                buffer.drawBitmap(namebar, null, new Rect(buffer.getWidth() - containerWidth, offset, buffer.getWidth(), offset + contenerHeight), null);
                text = this.players[i].getScore() + " : " + this.players[i].name;
                buffer.drawText(text, buffer.getWidth() - containerWidth + (containerWidth / 5), offset + (contenerHeight / 2), this.paint);
                offset += 30 + contenerHeight;
            }

    }

    @Override
    public void update() {

    }

    /**
     * Gets a new array representing a full pouch
     */
    private static Dice[] getFullPouch() {
        return new Dice[]{Dice.getTypicalGreenDice(), Dice.getTypicalGreenDice(), Dice.getTypicalGreenDice(),
                Dice.getTypicalGreenDice(), Dice.getTypicalGreenDice(), Dice.getTypicalGreenDice(),
                Dice.getTypicalYellowDice(), Dice.getTypicalYellowDice(), Dice.getTypicalYellowDice(),
                Dice.getTypicalYellowDice(), Dice.getTypicalRedDice(), Dice.getTypicalRedDice(), Dice.getTypicalRedDice()};
    }

    /**
     * Reset the pouches of this object.
     */
    private void resetPouches() {
        this.pouch = getFullPouch();
        this.hand = new Dice[]{};
        this.rolled = new Dice[]{};
    }

    /**
     * Called when the player rerolls.
     */
    private void reroll() {

    }

    /**
     * Called when the player chooses not to reroll, or dies.
     */
    private void endTurn() {

    }

}

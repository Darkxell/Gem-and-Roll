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
import com.darkxell.gemandroll.gamestates.statesutility.MenuButton;
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
        this.generator = new SeededRNG();
        this.resetPouches();

        this.currentreplay = new Replay();
        this.currentreplay.seed = this.generator.getSeed();
        this.currentreplay.playernames = new String[this.players.length];
        for (int i = 0; i < this.currentreplay.playernames.length; ++i) this.currentreplay.playernames[i] = this.players[i].name;

        this.paint = new Paint();
        this.paint.setColor(Color.BLACK);
        this.paint.setStyle(Paint.Style.FILL);
        this.createUI();
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
        this.createUI();
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
        this.createUI();
    }

    private Paint paint;

    // Game logic
    /**
     * List of the players in this game.
     */
    private Player[] players;
    /**
     * Index of the player of the current turn.
     */
    private int nowplaying;
    /**
     * Random generator for dice rolls.
     */
    private SeededRNG generator;
    /**
     * Dice pouches. Pouch are dices left to use, hand are dices being used and/or rerolled, and rolled are dices rolled and not landed on reroll.
     */
    private Dice[] pouch, rolled, hand;
    /**
     * Number of times this State has been called. Equals the current turn.
     */
    int stateiteration = 0;
    /**
     * The Replay to build as this Game is being played. If isReplay is true, this Replay will instead be read and played.
     */
    private Replay currentreplay = null;
    /**
     * Determines if this Game is being played or if the app is reading a Replay.
     */
    private boolean isReplay = false;

    // Display bitmaps
    private Bitmap background = BitmapFactory.decodeResource(holder.getResources(), R.drawable.woodbg);
    private Bitmap button = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_button);
    private Bitmap heartfull = BitmapFactory.decodeResource(holder.getResources(), R.drawable.hearth_full);
    private Bitmap heartempty = BitmapFactory.decodeResource(holder.getResources(), R.drawable.hearth_empty);
    private Bitmap namebar = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_namebar);
    private Bitmap namebar_ai = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_namebar_ai);

    private Bitmap borderv = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_borderv);
    private Bitmap bordert = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_bordert);
    private Bitmap borderh = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_borderh);

    // Display buttons
    private MenuButton buttonEndTurn = new MenuButton("End turn", button) {
        @Override
        public void onClick() {
            endTurn();
        }
    };
    private MenuButton buttonReroll = new MenuButton("Reroll", button) {
        @Override
        public void onClick() {
            reroll();
        }
    };
    private MenuButton[] buttonsPlayers;
    private MenuButton buttonHeart1 = new MenuButton.Label("", heartfull), buttonHeart2 = new MenuButton.Label("", heartfull), buttonHeart3 = new MenuButton.Label("", heartfull);

    // Display logic
    private int horizontalSplit = 0, verticalSplit = 0;
    private int width = 0, height = 0;

    private void createUI() {
        this.buttonsPlayers = new MenuButton[this.players.length];
        for (int i = 0; i < this.players.length; ++i)
            this.buttonsPlayers[i] = new MenuButton.Label(this.players[i].name, this.players[i].isAI() ? namebar_ai : namebar);
    }

    @Override
    public void print(Canvas buffer) {

        buffer.drawBitmap(background, null, new Rect(0, 0, buffer.getWidth(), buffer.getHeight()), null);

        if (this.buttonEndTurn.x == 0) this.placeUI(buffer);

        // Draws the bars splitting the screen.
        int barthickness = buffer.getHeight() / 20;
        int barlength = borderh.getWidth() * borderh.getHeight() / barthickness;
        for (int x = 0; x < buffer.getWidth(); x += barlength)
            buffer.drawBitmap(borderh, null, new Rect(x, this.verticalSplit, x + barlength, this.verticalSplit + barthickness), null);

        barlength = borderv.getWidth() * borderv.getHeight() / barthickness;
        for (int y = this.verticalSplit + barthickness / 2; y < buffer.getHeight(); y += barlength)
            buffer.drawBitmap(borderv, null, new Rect(this.horizontalSplit, y, this.horizontalSplit + barthickness, y + barlength), null);
        buffer.drawBitmap(bordert, null, new Rect(this.horizontalSplit, this.verticalSplit, this.horizontalSplit + barthickness, this.verticalSplit + barthickness), null);

        //draws the hearts
        int health = 3;
        for (int i = 0; i < this.rolled.length; ++i)
            if (this.rolled[i].getFace() == Dice.HURT) --health;
        int heartheight = 10 * buffer.getHeight() / 12, spritesize = buffer.getHeight() / 6;
        for (int i = 0; i < 3; ++i) {
            int offset = i * (spritesize + 30);
            buffer.drawBitmap((health >= i) ? heartfull : heartempty, null, new Rect(this.horizontalSplit + 70 + offset, heartheight, this.horizontalSplit + 70 + spritesize + offset, heartheight + spritesize), null);
        }

        //Draws the player name
        String text = this.players[this.nowplaying].name;
        this.paint.setTextSize(buffer.getHeight() / 20);
        buffer.drawText(text, this.horizontalSplit + 80, ((this.verticalSplit + 20) + heartheight) / 2, this.paint);


        //Draws the reroll and endturn buttons
        int buttonwidth = buffer.getWidth() - (this.horizontalSplit + 40 + (3 * (spritesize + 30)));
        int buttonheight = (button.getHeight() * buttonwidth) / button.getWidth();
        buffer.drawBitmap(button, null, new Rect(buffer.getWidth() - buttonwidth, this.verticalSplit + 35, buffer.getWidth(), this.verticalSplit + 35 + buttonheight), null);
        text = "Reroll";
        buffer.drawText(text, buffer.getWidth() - (buttonwidth / 2) - (this.paint.measureText(text) / 2), this.verticalSplit + 45 + (buttonheight / 2), this.paint);
        int buttonpadding = buttonheight + 30;
        buffer.drawBitmap(button, null, new Rect(buffer.getWidth() - buttonwidth, this.verticalSplit + 35 + buttonpadding, buffer.getWidth(), this.verticalSplit + 35 + buttonheight + buttonpadding), null);
        text = "End Turn";
        buffer.drawText(text, buffer.getWidth() - (buttonwidth / 2) - (this.paint.measureText(text) / 2), this.verticalSplit + 45 + (buttonheight / 2) + buttonpadding, this.paint);


        //Draws the list of players and their score on the top right
        int containerWidth = buffer.getWidth() / 4, contenerHeight = namebar.getHeight() * containerWidth / namebar.getWidth();
        int offset = 30;
        for (int i = this.nowplaying; i < players.length; ++i)
            if (offset + contenerHeight < this.verticalSplit) {
                buffer.drawBitmap(namebar, null, new Rect(buffer.getWidth() - containerWidth, offset, buffer.getWidth(), offset + contenerHeight), null);
                text = this.players[i].getScore() + " : " + this.players[i].name;
                buffer.drawText(text, buffer.getWidth() - containerWidth + (containerWidth / 5), offset + (contenerHeight / 2), this.paint);
                offset += 30 + contenerHeight;
            }
        for (int i = 0; i < this.nowplaying; ++i)
            if (offset + contenerHeight < this.verticalSplit) {
                buffer.drawBitmap(namebar, null, new Rect(buffer.getWidth() - containerWidth, offset, buffer.getWidth(), offset + contenerHeight), null);
                text = this.players[i].getScore() + " : " + this.players[i].name;
                buffer.drawText(text, buffer.getWidth() - containerWidth + (containerWidth / 5), offset + (contenerHeight / 2), this.paint);
                offset += 30 + contenerHeight;
            }

        this.printUI(buffer);

    }

    /**
     * Places and resizes UI.
     */
    private void placeUI(Canvas buffer) {
        this.verticalSplit = 2 * buffer.getHeight() / 3;
        this.horizontalSplit = buffer.getWidth() * 2 / 5;
    }

    @Override
    public void update() {
        this.updateUI();
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
        super.holder.setState(new RecursiveGameState(this));
    }

}

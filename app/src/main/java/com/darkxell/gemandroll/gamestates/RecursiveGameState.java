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

    private static final byte SETUPUI = 0, START = 1, DRAW = 2, ROLL = 3, COLLECT = 4, DAMAGE = 5, REROLL = 6, AWAITING = 7, END = 8;

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
    /**
     * Determines the current substate.
     */
    private byte substate = SETUPUI;
    /**
     * A timer for animations.
     */
    private int stateTimer = 0;

    // Display bitmaps
    private Bitmap background = BitmapFactory.decodeResource(holder.getResources(), R.drawable.woodbg);
    private Bitmap button = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_button);
    private Bitmap heartfull = BitmapFactory.decodeResource(holder.getResources(), R.drawable.hearth_full);
    private Bitmap heartempty = BitmapFactory.decodeResource(holder.getResources(), R.drawable.hearth_empty);
    private Bitmap namebar = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_namebar);
    private Bitmap namebar_ai = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_namebar_ai);
    private Bitmap namebar_full = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_textinput);

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
    private MenuButton buttonCurrentPlayer;

    // Display logic
    private int horizontalSplit = 0, verticalSplit = 0;
    private int width = 0, height = 0;

    private void createUI() {
        this.buttonsPlayers = new MenuButton[this.players.length];
        for (int i = 0; i < this.players.length; ++i)
            this.addButton(this.buttonsPlayers[i] = new MenuButton.Label(this.players[i].name + " : " + this.players[i].getScore(), i == this.nowplaying ? namebar_ai : namebar));

        this.addButton(this.buttonReroll);
        this.addButton(this.buttonEndTurn);
        this.addButton(this.buttonHeart1);
        this.addButton(this.buttonHeart2);
        this.addButton(this.buttonHeart3);
        this.buttonCurrentPlayer = new MenuButton.Label(this.players[this.nowplaying].name + "'s Turn", namebar_full);
    }

    @Override
    public void print(Canvas buffer) {

        buffer.drawBitmap(background, null, new Rect(0, 0, buffer.getWidth(), buffer.getHeight()), null);

        if (this.buttonEndTurn.x == 0) this.placeUI(buffer);

        // Draws the bars splitting the screen.
        int barthickness = this.height / 20;
        int barlength = borderh.getWidth() * borderh.getHeight() / barthickness;
        for (int x = 0; x < this.width; x += barlength)
            buffer.drawBitmap(borderh, null, new Rect(x, this.verticalSplit, x + barlength, this.verticalSplit + barthickness), null);

        barlength = borderv.getWidth() * borderv.getHeight() / barthickness;
        for (int y = this.verticalSplit + barthickness / 2; y < this.height; y += barlength)
            buffer.drawBitmap(borderv, null, new Rect(this.horizontalSplit, y, this.horizontalSplit + barthickness, y + barlength), null);
        buffer.drawBitmap(bordert, null, new Rect(this.horizontalSplit, this.verticalSplit, this.horizontalSplit + barthickness, this.verticalSplit + barthickness), null);

        this.printUI(buffer);

        if (this.substate <= START) {
            this.paint.setAlpha(128);
            buffer.drawRect(new Rect(0, 0, this.width, this.height), this.paint);
            this.buttonCurrentPlayer.draw(buffer);
        }
    }

    /**
     * Places and resizes UI.
     */
    private void placeUI(Canvas buffer) {

        // Get Buffer dimensions
        this.width = buffer.getWidth();
        this.height = buffer.getHeight();

        this.verticalSplit = 2 * buffer.getHeight() / 3;
        this.horizontalSplit = buffer.getWidth() * 2 / 5;

        // Place the Player names UI
        int buttonWidth = this.width / 4;
        int pad = this.horizontalSplit / (this.players.length * 2 + 1);
        int pos = pad / 2;
        for (int i = 0; i < this.players.length; ++i) {
            this.buttonsPlayers[i].x = this.width - buttonWidth;
            this.buttonsPlayers[i].y = pos;
            this.buttonsPlayers[i].width = buttonWidth;
            pos += pad * 2;
        }

        // Place the Player turn UI
        int buttonHeight = this.height - this.verticalSplit * 5 / 6;
        buttonWidth = (this.width - this.horizontalSplit) / 6;
        int buttonSize = Math.min(buttonHeight, buttonWidth);
        pad = buttonWidth / 5;
        pos = this.horizontalSplit + pad * 3 / 2 + buttonWidth / 2 - buttonSize / 2;
        this.buttonHeart1.x = pos;
        pos += pad + buttonWidth;
        this.buttonHeart2.x = pos;
        pos += pad + buttonWidth;
        this.buttonHeart3.x = pos;
        pos += pad + buttonWidth;
        this.buttonReroll.x = this.buttonEndTurn.x = pos;
        this.buttonReroll.y = this.verticalSplit + (this.height - this.verticalSplit) / 8;
        this.buttonEndTurn.y = this.buttonReroll.y + (this.height - this.verticalSplit) / 2;
        this.buttonHeart1.y = this.buttonHeart2.y = this.buttonHeart3.y = this.verticalSplit + (this.height - this.verticalSplit) / 2 - buttonSize / 2;
        this.buttonHeart1.width = this.buttonHeart2.width = this.buttonHeart3.width = buttonSize;
        this.buttonReroll.width = this.buttonEndTurn.width = buttonSize * 2 + pad;

        // Other components
        this.buttonCurrentPlayer.x = this.width / 4;
        this.buttonCurrentPlayer.y = this.height;
        this.buttonCurrentPlayer.width = this.width / 2;
        this.buttonCurrentPlayer.paint.setTextSize(this.height / 10);

        this.setSubstate(START);
    }

    @Override
    public void update() {
        this.updateUI();

        if (this.substate != AWAITING) ++this.stateTimer;

        if (this.substate == START) {
            final int APPEAR = 20, STAY = 80;
            int pixels = this.height * 3 / 5 / APPEAR;
            if (this.stateTimer >= APPEAR && this.stateTimer < APPEAR + STAY)
                pixels = (this.height + this.buttonCurrentPlayer.height) / 5 / STAY;
            this.buttonCurrentPlayer.y -= pixels;
            if (this.buttonCurrentPlayer.y <= -this.buttonCurrentPlayer.height) this.setSubstate(DRAW);
        }
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

    /**
     * Changes the current substate and resets the state timer.
     */
    private void setSubstate(byte substate) {
        this.substate = substate;
        this.stateTimer = 0;
    }
}

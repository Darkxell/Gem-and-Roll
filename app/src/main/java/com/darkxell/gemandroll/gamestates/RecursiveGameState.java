package com.darkxell.gemandroll.gamestates;

import android.graphics.Canvas;

import com.darkxell.gemandroll.MainActivity;
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
     * <builds a new Playstate thjat can be used to actually play the game.
     */
    public RecursiveGameState(MainActivity holder) {
        super(holder);
    }

    /**
     * Creates a new Gamestate by recursion of the previous one parsed.
     */
    public RecursiveGameState(RecursiveGameState previous) {
        super(previous.holder);
    }

    /**
     * Builds a new gamestate using a replay. This gamestate will play alone and display exactly the replay content.
     */
    public RecursiveGameState(Replay r, MainActivity holder) {
        super(holder);
//TODO
    }


    private Player[] players;
    private SeededRNG generator;
    private Dice[] pouch, rolled, hand;
    int stateiteration;
    private Replay currentreplay = null;
    private boolean isReplay = false;


    @Override
    public void print(Canvas buffer) {

    }

    @Override
    public void update() {

    }
}

package com.darkxell.gemandroll.mechanics.ais;

import com.darkxell.gemandroll.mechanics.Dice;
import com.darkxell.gemandroll.mechanics.PlayerAI;

/**
 * Created by Darkxell on 20/12/2016.
 * A player AI based on making the most points per turn. Ends the game if it can.
 */

public class TurnValueAI extends PlayerAI {

    public TurnValueAI(byte ID) {
        super(PlayerAI.TurnValueAI);
    }

    @Override
    public boolean shouldPlay(int health, int score, Dice[] hand, Dice[] rolled) {
        //calculates the amount of points the AI already made this turn.
        int greeninpouch = 6, yellowinpouch = 4, redinpouch = 3;
        int banckedpoints = 0;
        for (int i = 0; i < rolled.length; ++i) {
            if (rolled[i].getFace() == Dice.GEM)
                ++banckedpoints;
            if (rolled[i].equals(Dice.getTypicalGreenDice())) --greeninpouch;
            if (rolled[i].equals(Dice.getTypicalYellowDice())) --yellowinpouch;
            if (rolled[i].equals(Dice.getTypicalRedDice())) --redinpouch;
        }
        int pouchsize = greeninpouch + yellowinpouch + redinpouch;
        //Quick easy decision making
        if (banckedpoints <= 0) return true;
        if (score + banckedpoints >= 13) return false;

        //Calculates the probability of being hurt for each dice.
        double dice1 = 0d, dice2 = 0d, dice3 = 0d;

        if (hand.length >= 1) dice1 = hand[0].getFaceProbability(Dice.HURT);
        else dice1 = greeninpouch * Dice.getTypicalGreenDice().getFaceProbability(Dice.HURT)
                + yellowinpouch * Dice.getTypicalYellowDice().getFaceProbability(Dice.HURT)
                + redinpouch * Dice.getTypicalRedDice().getFaceProbability(Dice.HURT);
        if (hand.length >= 2) dice1 = hand[1].getFaceProbability(Dice.HURT);
        else dice2 = greeninpouch * Dice.getTypicalGreenDice().getFaceProbability(Dice.HURT)
                + yellowinpouch * Dice.getTypicalYellowDice().getFaceProbability(Dice.HURT)
                + redinpouch * Dice.getTypicalRedDice().getFaceProbability(Dice.HURT);
        if (hand.length >= 3) dice1 = hand[2].getFaceProbability(Dice.HURT);
        else dice3 = greeninpouch * Dice.getTypicalGreenDice().getFaceProbability(Dice.HURT)
                + yellowinpouch * Dice.getTypicalYellowDice().getFaceProbability(Dice.HURT)
                + redinpouch * Dice.getTypicalRedDice().getFaceProbability(Dice.HURT);

        //Calculates the death probability
        double dieproba = 1d;
        if (health == 3) dieproba = dice1 * dice2 * dice3;
        if (health == 1) dieproba = (1 - dice1) * (1 - dice2) * (1 - dice3);
        if (health == 2)
            dieproba = dice1 * dice2 * dice3 + dice1 * dice2 * (1 - dice3) + dice1 * (1 - dice2) * dice3 + (1 - dice1) * dice2 * dice3;

        /**Calculates the average score the IA will make if rerolling next turn.*/
        if (hand.length >= 1) dice1 = hand[0].getFaceProbability(Dice.GEM);
        else dice1 = greeninpouch * Dice.getTypicalGreenDice().getFaceProbability(Dice.GEM)
                + yellowinpouch * Dice.getTypicalYellowDice().getFaceProbability(Dice.GEM)
                + redinpouch * Dice.getTypicalRedDice().getFaceProbability(Dice.GEM);
        if (hand.length >= 2) dice1 = hand[1].getFaceProbability(Dice.GEM);
        else dice2 = greeninpouch * Dice.getTypicalGreenDice().getFaceProbability(Dice.GEM)
                + yellowinpouch * Dice.getTypicalYellowDice().getFaceProbability(Dice.GEM)
                + redinpouch * Dice.getTypicalRedDice().getFaceProbability(Dice.GEM);
        if (hand.length >= 3) dice1 = hand[2].getFaceProbability(Dice.GEM);
        else dice3 = greeninpouch * Dice.getTypicalGreenDice().getFaceProbability(Dice.GEM)
                + yellowinpouch * Dice.getTypicalYellowDice().getFaceProbability(Dice.GEM)
                + redinpouch * Dice.getTypicalRedDice().getFaceProbability(Dice.GEM);


        double averagethisturn = dice1 + dice2 + dice3;


        /**Returns the maximum between the already wonscore if stopping right now and the average score on next turn by rerolling.*/
        return score + banckedpoints < score + (banckedpoints + averagethisturn) * (1 - dieproba);
    }
}

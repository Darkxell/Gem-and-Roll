package com.darkxell.gemandroll.mechanics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.darkxell.gemandroll.MainActivity;
import com.darkxell.gemandroll.R;

/**
 * Created by Darkxell on 04/12/2016.
 */

public class Dice {

    private static Bitmap dice;

    public static final byte UNROLLED = 0;
    public static final byte GEM = 1;
    public static final byte REROLL = 2;
    public static final byte HURT = 3;

    public Dice(byte[] faces, int color) {
        this.display = color;
        this.faces = faces;
    }

    /**
     * The last rolled face.
     */
    private byte face = UNROLLED;
    /**
     * The dice color.
     */
    private int display;

    /**
     * A list of all the rollable faces.
     */
    private byte[] faces;

    /**
     * The result of this Dice. Used to store the bitmap of the result (Gems & traps can have different visuals).
     */
    public DiceResult result = null;

    /**
     * Roll this dice using the wanted random number generator. Will randomely roll if the generator is null. Also returns the result of the roll.
     */
    public byte roll(SeededRNG generator, MainActivity holder) {
        this.face = this.faces[generator.getRandomInt(0, this.faces.length)];

        if (this.face == GEM) this.result = DiceResult.getRandomGem(holder);
        else if (this.face == REROLL) this.result = DiceResult.getRandomReroll(holder);
        else if (this.face == HURT) this.result = DiceResult.getRandomTrap(holder);

        Statistics s = Statistics.instance;
        s.setStatValue(Statistics.Stat.DICES_ROLLED, s.getStatValue(Statistics.Stat.DICES_ROLLED) + 1);
        if (this.face == GEM) s.setStatValue(Statistics.Stat.GEM_COUNT, s.getStatValue(Statistics.Stat.GEM_COUNT) + 1);
        if (this.face == REROLL) s.setStatValue(Statistics.Stat.REROLL_COUNT, s.getStatValue(Statistics.Stat.REROLL_COUNT) + 1);
        if (this.face == HURT) s.setStatValue(Statistics.Stat.HURT_COUNT, s.getStatValue(Statistics.Stat.HURT_COUNT) + 1);

        return this.getFace();
    }

    public int getColor() {
        return this.display;
    }

    public byte getFace() {
        return this.face;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Dice) ? ((Dice) obj).getFace() == this.getFace() && ((Dice) obj).getColor() == this.getColor() : false;
    }

    /**
     * Returns the probability of rolling a certain face with this dice.
     */
    public float getFaceProbability(byte face) {
        int correcponding = 0;
        for (int i = 0; i < this.faces.length; ++i)
            if (this.faces[i] == face) ++correcponding;
        float r = correcponding / this.faces.length;
        return r;
    }

    /**
     * Draws this Dice at the specified coordinates and with the specified size.
     */
    public void draw(Canvas buffer, MainActivity holder, int x, int y, int size) {
        if (dice == null) dice = BitmapFactory.decodeResource(holder.getResources(), R.drawable.dice1);
        Rect bounds = new Rect(x, y, x + size, y + size);
        buffer.drawBitmap(dice, null, bounds, null);
        Paint paint = new Paint();
        paint.setColor(this.getColor());
        paint.setAlpha(64);
        buffer.drawRect(bounds, paint);
        if (this.result != null) buffer.drawBitmap(this.result.sprite, null, new Rect(x + size / 8, y + size / 8, x + size * 7 / 8, y + size * 7 / 8), null);
    }

    /**
     * Creates and returns a new typical green dice dice object.
     */
    public static Dice getTypicalGreenDice() {
        return new Dice(new byte[]{GEM, GEM, GEM, HURT, REROLL, REROLL}, Color.GREEN);
    }

    /**
     * Creates and returns a new typical yellow dice dice object.
     */
    public static Dice getTypicalYellowDice() {
        return new Dice(new byte[]{GEM, GEM, HURT, HURT, REROLL, REROLL}, Color.YELLOW);
    }

    /**
     * Creates and returns a new typical red dice dice object.
     */
    public static Dice getTypicalRedDice() {
        return new Dice(new byte[]{GEM, HURT, HURT, HURT, REROLL, REROLL}, Color.RED);
    }

}

package com.darkxell.gemandroll.mechanics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.darkxell.gemandroll.MainActivity;
import com.darkxell.gemandroll.R;

/**
 * Created by Cubi on 04/01/2017.
 */

public class DiceResult {

    public Bitmap sprite;

    public DiceResult(Bitmap sprite) {
        this.sprite = sprite;
    }

    public static Gem getRandomGem(MainActivity holder) {
        return new Gem(BitmapFactory.decodeResource(holder.getResources(), Gem.bitmaps[new SeededRNG().getRandomInt(0, Gem.bitmaps.length)]));
    }

    public static Trap getRandomTrap(MainActivity holder) {
        return new Trap(BitmapFactory.decodeResource(holder.getResources(), Trap.bitmaps[new SeededRNG().getRandomInt(0, Trap.bitmaps.length)]));
    }

    public static DiceResult getRandomReroll(MainActivity holder) {
        return new DiceResult(BitmapFactory.decodeResource(holder.getResources(), R.drawable.reroll1));
    }

}

package com.darkxell.gemandroll.mechanics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.darkxell.gemandroll.MainActivity;
import com.darkxell.gemandroll.R;

/**
 * Created by Darkxell on 04/12/2016.
 */

public class Gem extends DiceResult {

    static final int[] bitmaps = {R.drawable.gem1, R.drawable.gem2, R.drawable.gem3, R.drawable.gem4, R.drawable.gem5, R.drawable.gem6, R.drawable.gem7, R.drawable.gem8, R.drawable.gem9, R.drawable.gem10};

    public Gem(Bitmap sprite) {
        super(sprite);
    }
}

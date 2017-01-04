package com.darkxell.gemandroll.mechanics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.darkxell.gemandroll.MainActivity;
import com.darkxell.gemandroll.R;

/**
 * Created by Cubi on 04/01/2017.
 */

public class Trap extends DiceResult {

    static final int[] bitmaps = {R.drawable.trap1, R.drawable.trap2, R.drawable.trap3};

    public Trap(Bitmap sprite) {
        super(sprite);
    }
}

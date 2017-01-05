package com.darkxell.gemandroll.gamestates;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.darkxell.gemandroll.MainActivity;
import com.darkxell.gemandroll.R;
import com.darkxell.gemandroll.gamestates.statesutility.GameState;
import com.darkxell.gemandroll.gamestates.statesutility.MenuButton;
import com.darkxell.gemandroll.mechanics.Achievement;
import com.darkxell.gemandroll.mechanics.Statistics;

/**
 * Created by Cubi on 05/01/2017.
 */
public class StatisticsState extends GameState {

    public StatisticsState(MainActivity holder) {
        super(holder);

        this.createUI();
    }

    private Bitmap background = BitmapFactory.decodeResource(holder.getResources(), R.drawable.woodbg);
    private Bitmap button = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_button);
    private Bitmap namebar = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_namebar);
    private Bitmap namebar_left = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_namebar_left);

    private boolean resetting = false;

    private MenuButton buttonReset = new MenuButton("Reset stats", button) {
        @Override
        public void onClick() {
            setResetting(true);
        }
    };
    private MenuButton buttonMenu = new MenuButton("Main Menu", button) {
        @Override
        public void onClick() {
            onBackPressed();
        }
    };
    private MenuButton buttonOK = new MenuButton("Yes, please !", button) {
        @Override
        public void onClick() {
            confirmDelete();
        }
    };

    private MenuButton buttonCancel = new MenuButton("NO, don't !", button) {
        @Override
        public void onClick() {
            setResetting(false);
        }
    };

    private MenuButton[] buttons;

    private void createUI() {
        this.buttons = new MenuButton[8];
        for (int i = 0; i < this.buttons.length; ++i)
            this.addButton(this.buttons[i] = new MenuButton.Label(Statistics.Stat.values()[i].name + " : " + Statistics.instance.getStatValue(Statistics.Stat.values()[i]), i < 4 ? namebar_left : namebar));
        this.addButton(this.buttonReset);
        this.addButton(this.buttonMenu);
        this.addButton(this.buttonOK);
        this.addButton(this.buttonCancel);

        this.buttonOK.visible = this.buttonCancel.visible = false;
    }

    @Override
    public void print(Canvas buffer) {

        buffer.drawBitmap(background, null, new Rect(0, 0, buffer.getWidth(), buffer.getHeight()), null);

        if (this.buttonReset.x == 0) this.placeUI(buffer);

        this.printUI(buffer);

        if (this.resetting) {
            Paint p = new Paint();
            p.setAlpha(128);
            buffer.drawRect(buffer.getClipBounds(), p);
            this.buttonOK.draw(buffer);
            this.buttonCancel.draw(buffer);
        }
    }

    private void placeUI(Canvas buffer) {
        int width = buffer.getWidth(), height = buffer.getHeight();
        int buttonHeight = height / 6;
        int pad = buttonHeight / 6;
        int y = pad;

        this.buttonMenu.y = this.buttonReset.y = y;
        y += buttonHeight + pad;
        this.buttons[0].y = this.buttons[4].y = y;
        y += buttonHeight + pad;
        this.buttons[1].y = this.buttons[5].y = this.buttonOK.y = y;
        y += buttonHeight + pad;
        this.buttons[2].y = this.buttons[6].y = this.buttonCancel.y = y;
        y += buttonHeight + pad;
        this.buttons[3].y = this.buttons[7].y = y;

        for (MenuButton b : this.buttons) b.height = buttonHeight;
        this.buttons[0].processDimensions(buffer);
        int buttonWidth = this.buttons[0].width;
        this.buttons[4].x = this.buttons[5].x = this.buttons[6].x = this.buttons[7].x = width - buttonWidth;

        this.buttonReset.processDimensions(buffer);
        this.buttonMenu.x = this.buttonMenu.y = this.buttonReset.y = 10;
        this.buttonReset.x = width - 10 - this.buttonReset.width;

        this.buttonOK.processDimensions(buffer);
        this.buttonOK.x = this.buttonCancel.x = width / 2 - this.buttonOK.width / 2;
    }

    @Override
    public void update() {
        this.updateUI();
    }

    private void setResetting(boolean resetting) {
        this.resetting = resetting;
        this.buttonMenu.enabled = this.buttonReset.enabled = !resetting;
        this.buttonOK.visible = this.buttonCancel.visible = resetting;
    }

    private void confirmDelete() {
        for (Statistics.Stat s : Statistics.Stat.values()) Statistics.instance.setStatValue(s, 0);
        for (Achievement a : Achievement.values()) a.setAcquired(false, false);
        this.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.holder.setState(new MainMenuState(super.holder));
    }
}

package com.darkxell.gemandroll.gamestates.statesutility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.KeyEvent;

import com.darkxell.gemandroll.MainActivity;
import com.darkxell.gemandroll.R;

/**
 * Created by Cubi on 01/01/2017.
 */

public class TextInputState extends GameState {

    private Bitmap background = BitmapFactory.decodeResource(holder.getResources(), R.drawable.woodbg);
    private Bitmap textinput = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_textinput);

    private int bufferWidth, bufferHeight;
    private String input;
    private byte capitalType;

    private TextInputListener listener;
    private MenuButton text;

    public TextInputState(MainActivity holder, TextInputListener listener, String defaultText) {
        super(holder);
        this.listener = listener;
        this.input = defaultText;
        this.addButton(this.text = new MenuButton(defaultText, textinput, 0, 0) {
            @Override
            public void onClick() {
                show();
            }
        });

        holder.showKeyboard();
    }

    private void show() {
        holder.showKeyboard();
    }

    public String getInput() {
        return this.input;
    }

    @Override
    public void onKeyTyped(int keyCode, char unicodeChar) {
        if (keyCode == KeyEvent.KEYCODE_DEL && this.input.length() >= 1) {
            this.input = this.input.substring(0, this.input.length() - 1);
            this.text.text = input;
        }
        else if (keyCode == KeyEvent.KEYCODE_ENTER) {
            this.listener.onInput(this.input);
            super.holder.hideKeyboard();
        }
        else if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.listener.cancelInput();
            super.holder.hideKeyboard();
        } else if (keyCode == KeyEvent.KEYCODE_CAPS_LOCK) this.capitalType = (byte) (this.capitalType == 2 ? 0 : this.capitalType + 1);
        else {
            try {
                this.input += this.capitalType >= 1 ? Character.toString(unicodeChar).toUpperCase() : unicodeChar;
                if (this.capitalType == 1) this.capitalType = 0;
            } catch (Exception e) {
            }
            this.text.text = input;
        }
    }

    @Override
    public void print(Canvas buffer) {
        this.bufferWidth = buffer.getWidth();
        this.bufferHeight = buffer.getHeight();

        int imageheight = buffer.getWidth() / background.getWidth() * background.getHeight();
        buffer.drawBitmap(background, null, new Rect(0, buffer.getHeight() - imageheight, buffer.getWidth(), buffer.getHeight()), null);

        if (this.text.x == 0) {
            this.text.width = this.bufferWidth * 3 / 4;
            this.text.x = this.bufferWidth / 8;
            this.text.y = this.bufferHeight / 6;
            this.text.paint.setTextSize(buffer.getHeight() / 10);
        }

        this.printUI(buffer);
    }

    @Override
    public void update() {
        this.updateUI();
    }
}

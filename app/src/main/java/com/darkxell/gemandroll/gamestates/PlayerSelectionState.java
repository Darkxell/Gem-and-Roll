package com.darkxell.gemandroll.gamestates;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.inputmethod.InputMethodManager;

import com.darkxell.gemandroll.MainActivity;
import com.darkxell.gemandroll.R;
import com.darkxell.gemandroll.gamestates.statesutility.GameState;
import com.darkxell.gemandroll.gamestates.statesutility.MenuButton;
import com.darkxell.gemandroll.gamestates.statesutility.TextInputListener;
import com.darkxell.gemandroll.gamestates.statesutility.TextInputState;
import com.darkxell.gemandroll.mechanics.Player;
import com.darkxell.gemandroll.mechanics.PlayerAI;
import com.darkxell.gemandroll.mechanics.RandomNameGenerator;

/**
 * Created by Cubi on 01/01/2017.
 */
public class PlayerSelectionState extends GameState implements TextInputListener {

    private static final int MAX_PLAYERS = 4;

    private Bitmap background = BitmapFactory.decodeResource(holder.getResources(), R.drawable.woodbg);
    private Bitmap button = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_button);
    private Bitmap buttonPlus = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_plus);
    private Bitmap buttonMinus = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_minus);
    private Bitmap box = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_bigbox2);
    private Bitmap boxUnchecked = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_box0);
    private Bitmap boxChecked = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_box1);
    private Bitmap namebar = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_namebar);
    private Bitmap namebarAI = BitmapFactory.decodeResource(holder.getResources(), R.drawable.ui_namebar_ai);

    private Paint paint;
    private MenuButton buttonBack, buttonStart;
    private MenuButton buttonPlayerPlus, buttonPlayerMinus, buttonNumberPlayers;
    private MenuButton buttonP1, buttonP2, buttonP3, buttonP4;
    private MenuButton buttonAI1, buttonAI2, buttonAI3, buttonAI4;

    private int bufferWidth, bufferHeight;
    private int playerCount = 2;
    private boolean[] isAI = new boolean[MAX_PLAYERS];
    private boolean needReplace = true;
    private String[] names;
    private int editingPlayerName = 0;

    public PlayerSelectionState(MainActivity holder) {
        super(holder);

        this.names = new String[MAX_PLAYERS];
        for (int i = 0; i < names.length; ++i) names[i] = RandomNameGenerator.getRandomName();

        this.createButtons();
        this.paint = new Paint();
        this.paint.setColor(Color.BLACK);
        this.paint.setStyle(Paint.Style.FILL);
    }

    /**
     * Creates this state's buttons. Called by constructor.
     */
    private void createButtons() {
        this.addButton(this.buttonBack = new MenuButton("Back", button, 20, 20) {
            @Override
            public void onClick() {
                onBackPressed();
            }
        });

        this.addButton(this.buttonStart = new MenuButton("Start Game !", button, 0, 0) {
            @Override
            public void onClick() {
                startGame();
            }
        });

        // Player Count management
        this.addButton(this.buttonPlayerPlus = new MenuButton("", buttonPlus, 0, 0) {
            @Override
            public void onClick() {
                if (playerCount < MAX_PLAYERS) {
                    ++playerCount;
                    buttonNumberPlayers.text = Integer.toString(playerCount);
                    needReplace = true;
                }
            }
        });
        this.addButton(this.buttonPlayerMinus = new MenuButton("", buttonMinus, 0, 0) {
            @Override
            public void onClick() {
                if (playerCount > 2) {
                    --playerCount;
                    buttonNumberPlayers.text = Integer.toString(playerCount);
                    needReplace = true;
                }
            }
        });
        this.addButton(this.buttonNumberPlayers = new MenuButton("2", box, 0, 0) {
            @Override
            public void onClick() {
            }
        });

        // Player names
        this.addButton(this.buttonP1 = new MenuButton(names[0], namebar, 0, 0) {
            @Override
            public void onClick() {
                editName(0);
            }
        });
        this.addButton(this.buttonP2 = new MenuButton(names[1], namebar, 0, 0) {
            @Override
            public void onClick() {
                editName(1);
            }
        });
        this.addButton(this.buttonP3 = new MenuButton(names[2], namebar, 0, 0) {
            @Override
            public void onClick() {
                editName(2);
            }
        });
        this.addButton(this.buttonP4 = new MenuButton(names[3], namebar, 0, 0) {
            @Override
            public void onClick() {
                editName(3);
            }
        });

        // AI checkboxes
        this.addButton(this.buttonAI1 = new MenuButton("", boxUnchecked, 0, 0) {
            @Override
            public void onClick() {
                isAI[0] = !isAI[0];
                this.bitmapOn = isAI[0] ? boxChecked : boxUnchecked;
                buttonP1.bitmapOn = isAI[0] ? namebarAI : namebar;
            }
        });
        this.addButton(this.buttonAI2 = new MenuButton("", boxUnchecked, 0, 0) {
            @Override
            public void onClick() {
                isAI[1] = !isAI[1];
                this.bitmapOn = isAI[1] ? boxChecked : boxUnchecked;
                buttonP2.bitmapOn = isAI[1] ? namebarAI : namebar;
            }
        });
        this.addButton(this.buttonAI3 = new MenuButton("", boxUnchecked, 0, 0) {
            @Override
            public void onClick() {
                isAI[2] = !isAI[2];
                this.bitmapOn = isAI[2] ? boxChecked : boxUnchecked;
                buttonP3.bitmapOn = isAI[2] ? namebarAI : namebar;
            }
        });
        this.addButton(this.buttonAI4 = new MenuButton("", boxUnchecked, 0, 0) {
            @Override
            public void onClick() {
                isAI[3] = !isAI[3];
                this.bitmapOn = isAI[3] ? boxChecked : boxUnchecked;
                buttonP4.bitmapOn = isAI[3] ? namebarAI : namebar;
            }
        });
    }

    /**
     * Called when the user clicks on a Player name.
     * @param player - The player number.
     */
    private void editName(int player) {
        this.editingPlayerName = player;
        super.holder.setState(new TextInputState(super.holder, this, this.names[player]));
    }

    /**
     * Called when user clicks on Start.
     */
    private void startGame() {
        Player[] players = new Player[this.playerCount];
        for (int i = 0; i < players.length; ++i) players[i] = new Player(this.names[i], (byte) (this.isAI[i] ? PlayerAI.TurnValueAI : PlayerAI.UndefinedAI));
        super.holder.setState(new RecursiveGameState(super.holder, players));
    }

    @Override
    public void print(Canvas buffer) {
        this.bufferWidth = buffer.getWidth();
        this.bufferHeight = buffer.getHeight();

        int imageheight = buffer.getWidth() / background.getWidth() * background.getHeight();
        buffer.drawBitmap(background, null, new Rect(0, buffer.getHeight() - imageheight, buffer.getWidth(), buffer.getHeight()), null);

        if (this.needReplace) this.placeButtons(buffer);

        int y = this.bufferHeight / 8 * 3;

        this.paint.setTextSize(buffer.getHeight() / 10);
        String text = "Miner count :";
        buffer.drawText(text, this.buttonNumberPlayers.x + (this.buttonNumberPlayers.width / 2) - this.paint.measureText(text) / 2, y, this.paint);

        y = this.buttonP1.y / 3;
        text = "Miner names";
        buffer.drawText(text, this.buttonP1.x + this.buttonP1.width / 2 - this.paint.measureText(text) / 2, y * 2, this.paint);

        this.paint.setTextSize(buffer.getHeight() / 12);
        text = "CPU";
        buffer.drawText(text, this.buttonAI1.x + this.buttonAI1.width / 2 - this.paint.measureText(text) / 2, y * 3, this.paint);

        this.printUI(buffer);
    }

    /**
     * Places and resizes the buttons.
     */
    private void placeButtons(Canvas buffer) {

        // Left side
        this.buttonPlayerPlus.width = this.buttonPlayerMinus.width = this.buttonPlayerPlus.bitmapOn.getWidth() * 3 / 2;
        this.buttonNumberPlayers.width = this.buttonPlayerPlus.width * 2;

        this.buttonNumberPlayers.x = this.bufferWidth / 5;
        this.buttonPlayerMinus.x = this.buttonNumberPlayers.x - this.buttonPlayerMinus.width * 3 / 2;
        this.buttonPlayerPlus.x = this.buttonNumberPlayers.x + this.buttonNumberPlayers.width + this.buttonPlayerPlus.width / 2;
        this.buttonStart.width = this.bufferWidth * 2 / 5;
        this.buttonStart.x = this.bufferWidth / 4 - this.buttonStart.width / 2;
        this.buttonStart.paint.setTextSize(buffer.getHeight() / 12);

        int y = this.bufferHeight / 8;
        int pad = y;
        y += pad * 3;
        this.buttonNumberPlayers.y = y - this.buttonNumberPlayers.width / 3;
        this.buttonPlayerMinus.y = this.buttonPlayerPlus.y = this.buttonNumberPlayers.y + this.buttonNumberPlayers.width / 2 - this.buttonPlayerMinus.width / 2;

        y += pad * 2;
        this.buttonStart.y = y;

        // Right side
        this.buttonP1.width = this.buttonP2.width = this.buttonP3.width = this.buttonP4.width = this.bufferWidth * 2 / 5;
        this.buttonP1.x = this.buttonP2.x = this.buttonP3.x = this.buttonP4.x = this.bufferWidth - this.buttonP1.width;

        this.buttonAI1.width = this.buttonAI2.width = this.buttonAI3.width = this.buttonAI4.width = this.buttonAI1.bitmapOn.getWidth();
        this.buttonAI1.x = this.buttonAI2.x = this.buttonAI3.x = this.buttonAI4.x = this.buttonP1.x - this.buttonAI1.width * 2;

        pad = y = this.bufferHeight / (this.playerCount + 2) / 2;
        int offsetAI = this.buttonP1.width * this.buttonP1.bitmapOn.getHeight() / this.buttonP1.bitmapOn.getWidth() / 2 - this.buttonAI1.width / 2;

        y += pad * 2;
        this.buttonP1.y = y;
        this.buttonAI1.y = y + offsetAI;

        y += pad * 2;
        this.buttonP2.y = y;
        this.buttonAI2.y = y + offsetAI;

        y += pad * 2;
        this.buttonP3.y = y;
        this.buttonAI3.y = y + offsetAI;

        y += pad * 2;
        this.buttonP4.y = y;
        this.buttonAI4.y = y + offsetAI;

        this.buttonP3.visible = this.buttonAI3.visible = this.playerCount >= 3;
        this.buttonP4.visible = this.buttonAI4.visible = this.playerCount >= 4;

        this.buttonP1.paint.setTextSize(buffer.getHeight() / 15);
        this.buttonP2.paint.setTextSize(buffer.getHeight() / 15);
        this.buttonP3.paint.setTextSize(buffer.getHeight() / 15);
        this.buttonP4.paint.setTextSize(buffer.getHeight() / 15);

        this.needReplace = false;
    }

    @Override
    public void update() {
        this.updateUI();
    }

    @Override
    public void onBackPressed() {
        super.holder.setState(new MainMenuState(super.holder));
    }

    @Override
    public void onInput(String textInput) {
        this.names[this.editingPlayerName] = textInput;
        if (this.editingPlayerName == 0) this.buttonP1.text = this.names[0];
        if (this.editingPlayerName == 1) this.buttonP2.text = this.names[1];
        if (this.editingPlayerName == 2) this.buttonP3.text = this.names[2];
        if (this.editingPlayerName == 3) this.buttonP4.text = this.names[3];

        super.holder.setState(this);
    }

    @Override
    public void cancelInput() {
        super.holder.setState(this);
    }
}

package com.darkxell.gemandroll.gamestates.statesutility;

/**
 * Created by Darkxell on 03/12/2016.
 */

public abstract class Updater {

    private static final boolean SHOWFPS = false;
    /**
     * Will allow 1 ms of sleep time between each print if set to true.
     */
    private static final boolean capfps = false;
    private FPSmeter meter = new FPSmeter();
    private Thread render;
    private Thread update;
    private long updatestarttime;

    public abstract void onUpdate();

    public abstract void onPrint();

    public Updater(final int ups) {
        // Rendering thread
        render = new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    onPrint();
                    if (SHOWFPS)
                        System.out.println("FPS : " + meter.calcFPS());
                    if (capfps)
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            System.err.println("WARNING : renderer thread could not sleep for unknown reasons.");
                        }
                }
            }
        });
        render.start();
        // Updater thread
        update = new Thread(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    updatestarttime = System.currentTimeMillis();
                    onUpdate();
                    while (System.currentTimeMillis() < updatestarttime + (1000 / ups)) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            System.err.println("WARNING : updater thread could not sleep for unknown reasons.");
                        }
                    }
                }
            }
        });
        update.start();

    }
}
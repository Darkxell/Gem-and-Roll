package com.darkxell.gemandroll.audio;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.darkxell.gemandroll.R;

/**
 * Created by Darkxell on 04/01/2017.
 */
public class AudioBot {

    public static AudioBot instance;

    public static AudioBot i() {
        return instance;
    }

    private Context context;

    public AudioBot(Context context) {
        this.context = context;
    }

    /**
     * The player used to play the background music.
     */
    private MediaPlayer bgm;

    /**
     * Sets the BGM to the wanted song.
     */
    public void setBGM(int song) {
        try {
            if (bgm != null) bgm.stop();
            bgm = MediaPlayer.create(context, song);
            bgm.setAudioStreamType(AudioManager.STREAM_MUSIC);
            bgm.setLooping(true);
            bgm.start();
        } catch (Exception e) {
        }

    }

    /**
     * Plays this sound in a new thread as soon as possible.
     */
    public void playSound(int song) {
        try {
            MediaPlayer mPlayer = MediaPlayer.create(context, song);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.start();
        } catch (Exception e) {
        }
    }
}

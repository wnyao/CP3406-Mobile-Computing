package com.example.kongwenyao.drumkit;

import android.content.Context;
import android.media.SoundPool;

/**
 * Created by kongwenyao on 12/31/17.
 */

public class SoundManager {

    private Context context;
    private SoundPool soundPool;

    public SoundManager(Context context) {
        this.context = context;

        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(10);  //number of stream that can play simultaneously
        soundPool = builder.build();
    }

    public int getSoundID(int resourcesID) {
        return (soundPool.load(context, resourcesID, 1));
    }

    public void play(int soundId) {
        soundPool.play(soundId, 1, 1, 1, 0, 1);
    }
}

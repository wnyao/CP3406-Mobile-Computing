package com.example.kongwenyao.educationalgame;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by kongwenyao on 1/5/18.
 */

public class MainPlayer {

    private Activity activity;
    private Drawable drawable;
    private AnimationDrawable animationDrawable;
    private PlayerState playerState;

    public MainPlayer(Activity activity, PlayerState defaultState) {
        this.activity = activity;
        this.playerState = defaultState;

        setDrawable(defaultState);
        setAnimationDrawable();
        startAnimation();
    }

    public void updatePlayer(PlayerState playerState) {
        this.playerState = playerState;
        setDrawable(playerState);
        setAnimationDrawable();
    }

    public void startAnimation() {
        if (!animationDrawable.isRunning()) {
            animationDrawable.start();
        }
    }

    public void stopAnimation() {
        if (animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }

    public void setAnimationDrawable() {
        animationDrawable = (AnimationDrawable) getDrawable();
    }

    public void setDrawable(PlayerState defaultState) {
        switch (defaultState) {
            case REST_RIGHT:
                drawable = activity.getDrawable(R.drawable.anim_player_restright);
                break;
            case REST_LEFT:
                drawable = activity.getDrawable(R.drawable.anim_player_restleft);
                break;
            case WALK_RIGHT:
                drawable = activity.getDrawable(R.drawable.anim_player_walkright);
                break;
            case WALK_LEFT:
                drawable = activity.getDrawable(R.drawable.anim_player_walkleft);
                break;
        }
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

}

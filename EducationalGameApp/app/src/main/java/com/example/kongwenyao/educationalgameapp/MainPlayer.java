package com.example.kongwenyao.educationalgameapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;

/**
 * Created by kongwenyao on 1/3/18.
 *
 * reference: https://www.ssaurel.com/blog/create-a-running-man-game-animation-on-android/
 */

public class MainPlayer implements GameObject {

    private AnimationDrawable animDrawableRest;
    private Bitmap bitmap;

    private boolean isRest;
    private Point playerPoint;

    public MainPlayer(Context context) {
        animDrawableRest = (AnimationDrawable) context.getDrawable(R.drawable.animation_player_rest);
    }

    @Override
    public void draw(Canvas canvas) {
        //Paint paint = new Paint();
        //paint.setColor(color);
        //canvas.drawRect(rect, paint);
        //canvas.drawBitmap();
    }

    @Override
    public void update() {
        if (isRest) {
           if (!animDrawableRest.isRunning()) {
                animDrawableRest.start();
           }
        } else {
            if (animDrawableRest.isRunning()) {
                animDrawableRest.stop();
            }
        }
    }

    public void updatePlayerPoint(Point playerPoint) {
        this.playerPoint = playerPoint;
    }

    public void setRest(boolean isRest) {
        this.isRest = isRest;
    }

}

package com.example.kongwenyao.educationalgameapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toolbar;

/**
 * Created by kongwenyao on 1/2/18.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private final String BACKGROUND_COLOR = "#ffdf5f";

    //MainPlayer
    private MainPlayer mainPlayer;
    private Point playerPoint;

    public GamePanel(Context context, AttributeSet attrs) {
        super(context, attrs);

        thread = new MainThread(getHolder(), this);
        getHolder().addCallback(this);
        setFocusable(true);

        //Main Player
        playerPoint = new Point();
        mainPlayer = new MainPlayer(context); //test
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (true) {
            try {
                thread.setRunning(false);
                thread.join();  //Wait for thread to die
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        playerPoint.set((int) event.getX(), (int) event.getY());
        return true;
    }

    public void update() {
        //mainPlayer.updatePlayerPoint(playerPoint);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawColor(Color.parseColor(BACKGROUND_COLOR));
        //mainPlayer.draw(canvas);

    }
}

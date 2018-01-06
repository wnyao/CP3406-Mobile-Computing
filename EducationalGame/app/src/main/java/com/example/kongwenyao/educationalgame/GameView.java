package com.example.kongwenyao.educationalgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by kongwenyao on 1/4/18.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String BACKGROUND_COLOR = "#ffdf5f";
    private static final int TEXT_SIZE = 200;
    private static Typeface typeface;
    private Paint paint;
    private int totalValue = 50;    //TODO: default total value; subject to change

    private GameThread gameThread;
    private DropObject dropObject1;
    private DropObject dropObject2;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);

        //Object instantiation
        gameThread = new GameThread(getHolder(), this);
        dropObject1 = new DropObject();
        dropObject2 = new DropObject(); //test

        //Paint
        typeface = Typeface.createFromAsset(context.getAssets(), "bungee.ttf");
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTypeface(typeface);
        paint.setTextSize(TEXT_SIZE);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameThread = new GameThread(getHolder(), this);
        gameThread.setRunning(true);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (true) {
            try {
                gameThread.setRunning(false);
                gameThread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update() {
        dropObject1.update();
        dropObject2.update();   //test
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawColor(Color.parseColor(BACKGROUND_COLOR));
        canvas.drawText(String.valueOf(totalValue), getWidth()/2 - TEXT_SIZE/2, 220, paint);

        dropObject1.draw(canvas);
        dropObject2.draw(canvas);   //test
    }

    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

}

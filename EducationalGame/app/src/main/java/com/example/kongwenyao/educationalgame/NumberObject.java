package com.example.kongwenyao.educationalgame;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.Random;

/**
 * Created by kongwenyao on 1/6/18.
 */

public class NumberObject implements GameObject{

    private static int maxRandNum = 10;
    private static int textSize = 100;
    private static int speed = 1500;
    private int posX, posY;
    private int width, height, displayValue;
    private boolean resetPos = false;

    private Paint paint;

    public NumberObject() {
        width = Resources.getSystem().getDisplayMetrics().widthPixels;
        height = Resources.getSystem().getDisplayMetrics().heightPixels;
        posX = generateRandPosX(width); //Random x value within screen width
        posY = generateRandNum(height);
        displayValue = generateRandNum(maxRandNum);

        //Paint Object
        paint = new Paint();
        paint.setTextSize(textSize);
    }

    @Override
    public void update() {
        posY = posY + speed/ GameThread.MAX_FPS;

        if (!resetPos) {
            if (posY > Resources.getSystem().getDisplayMetrics().heightPixels) {
                reset();
            }
        } else {    //TODO: when drop object touch main player
            reset();
            resetPos = false;  //Turn off reset
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText(String.valueOf(displayValue), posX, posY, paint);
    }

    public int generateRandNum(int max) {
        Random random = new Random();
        int num = random.nextInt(max);
        return num;
    }

    private int generateRandPosX(int width) {
        int x = generateRandNum(width);

        if (x > width - textSize) {
            posX = width - textSize;
        }

        return x;
    }

    public void resetPosition(boolean reset) {
        resetPos = reset;
    }

    private void reset() {
        displayValue = generateRandNum(maxRandNum);
        posX = generateRandPosX(width);
        posY = 0;
    }

    public PointF getObjectPos() {
        return  (new PointF(posX, posY));
    }

    public int getDisplayValue() {
        return displayValue;
    }
}

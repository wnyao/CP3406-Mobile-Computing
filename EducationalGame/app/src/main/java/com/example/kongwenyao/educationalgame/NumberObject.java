package com.example.kongwenyao.educationalgame;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.Random;

/**
 * Created by kongwenyao on 1/6/18.
 */

public class NumberObject implements GameObject{

    //Game parameter values
    private static int maxRandNum = 30;
    private static int textSize = 100;

    //Global Variables
    private int width, height, displayValue;
    private int droppingSpeed = 1800;
    private int posX, posY;
    private boolean resetPos = false;

    private Paint paint;

    public NumberObject() {
        width = Resources.getSystem().getDisplayMetrics().widthPixels;
        height = Resources.getSystem().getDisplayMetrics().heightPixels;
        posX = generateRandPosX(width); //Randomize x value within screen width
        posY = generateRandNum(height * 2);
        displayValue = generateRandNum(maxRandNum);

        //Paint Object
        paint = new Paint();
        paint.setTextSize(textSize);
    }

    @Override
    public void update() {
        posY = posY + droppingSpeed/ GameThread.MAX_FPS; //Increase y values

        if (!resetPos) {
            if (posY > Resources.getSystem().getDisplayMetrics().heightPixels) {
                resetCoordinate();
            }
        } else {
            resetCoordinate();
            resetPos = false;
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
            x = width - textSize;
        }

        return x;
    }

    public void resetPosition(boolean reset) {
        resetPos = reset;
    }

    private void resetCoordinate() {
        displayValue = generateRandNum(maxRandNum);
        posX = generateRandPosX(width);
        posY = 0;
    }

    public void increaseDroppingSpeed() {
        droppingSpeed += 1000;
    }

    public PointF getObjectPos() {
        return  (new PointF(posX, posY));
    }

    public int getDisplayValue() {
        return displayValue;
    }

    public void setDefault() {
        droppingSpeed = 1800;
    }
}

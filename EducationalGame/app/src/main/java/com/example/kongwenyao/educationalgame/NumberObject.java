package com.example.kongwenyao.educationalgame;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import java.util.Random;

/**
 * Created by kongwenyao on 1/6/18.
 */

public class NumberObject implements GameObject{

    //Game parameter values
    private static int textSize = 70;
    private static int maxRandNum = 30;

    //Global Variables
    private int width, height, displayValue;
    private int droppingSpeed = 500;
    private int posX, posY;
    private boolean resetPos = false;
    private Paint textPaint;
    private Paint strokePaint;

    public NumberObject() {
        width = Resources.getSystem().getDisplayMetrics().widthPixels;
        height = Resources.getSystem().getDisplayMetrics().heightPixels;
        posX = generateRandPosX(width); //Randomize x value within screen width
        posY = generateRandNum(height);
        displayValue = generateRandNum(maxRandNum);

        setPaints(); //Set all needed paint objects
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
        canvas.drawText(String.valueOf(displayValue), posX, posY, strokePaint);
        canvas.drawText(String.valueOf(displayValue), posX, posY, textPaint);
    }

    private void setPaints() {
        //Text paint object
        textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.parseColor("#350000"));

        //Stroke paint object
        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(Color.parseColor("#5d1919"));
        strokePaint.setTextSize(textPaint.getTextSize());
        strokePaint.setStrokeWidth(5);
    }

    public int generateRandNum(int max) {
        Random random = new Random();
        return (random.nextInt(max));
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
        posY = -generateRandNum(height/2);
    }

    public void increaseDroppingSpeed() {
        droppingSpeed += 80;
    }

    public PointF getObjectPos() {
        return  (new PointF(posX, posY));
    }

    public int getDisplayValue() {
        return displayValue;
    }

    public void setDefault() {
        droppingSpeed = 500;
    }
}

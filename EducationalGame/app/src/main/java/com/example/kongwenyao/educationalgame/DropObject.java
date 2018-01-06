package com.example.kongwenyao.educationalgame;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by kongwenyao on 1/6/18.
 */

public class DropObject implements GameObject{

    private static int maxRandNum = 10;
    private static int textSize = 100;

    private static int speed = 1500;
    private int posX, posY;
    private int width, height, displayNum;
    private boolean resetPos = false;

    private Paint paint;

    public DropObject() {
        width = Resources.getSystem().getDisplayMetrics().widthPixels;
        height = Resources.getSystem().getDisplayMetrics().heightPixels;
        posX = generateRandPosX(width); //Random x value within screen width
        posY = generateRandNum(height);
        displayNum = generateRandNum(maxRandNum);


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
        //string, x (stay the same), y (down), paint
        canvas.drawText(String.valueOf(displayNum), posX, posY, paint);
    }

    public int generateRandNum(int max) {
        Random random = new Random();
        int num = random.nextInt(max);
        return num;
    }

    public int generateRandPosX(int width) {
        int x = generateRandNum(width);

        if (x > width - textSize) {
            posX = width - textSize;
        }

        return x;
    }

    public void resetPosition(boolean reset) {
        resetPos = reset;
    }

    public void reset() {
        displayNum = generateRandNum(maxRandNum);
        posX = generateRandPosX(width);
        posY = 0;
    }
}

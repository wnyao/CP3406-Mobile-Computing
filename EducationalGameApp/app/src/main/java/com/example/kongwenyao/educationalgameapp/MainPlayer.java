package com.example.kongwenyao.educationalgameapp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by kongwenyao on 1/3/18.
 */

public class MainPlayer implements GameObject {

    private Rect rect;
    private int color;

    public MainPlayer(Rect rect, int color) {
        this.rect = rect;
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rect, paint);
    }

    @Override
    public void update() {
    }

    public void updatePlayerPoint(Point point) {
        rect.set(point.x - rect.width()/2, point.y - rect.height()/2, point.x + rect.width()/2, point.y + rect.width()/2);  //test
    }
}

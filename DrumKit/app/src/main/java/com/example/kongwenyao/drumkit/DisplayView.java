package com.example.kongwenyao.drumkit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * Created by kongwenyao on 12/31/17.
 */

public class DisplayView extends View {

    private Paint paint;
    private String displayMessage = "Drum Kit";

    public DisplayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(80);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        PointF center = getCenter();
        canvas.drawText(displayMessage, center.x, center.y, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);

        setBackgroundColor(Color.rgb(r, g, b));
        return super.onTouchEvent(event);
    }

    public void setDisplayMessage(String message) {
        displayMessage = message;
    }

    public PointF getCenter() {
        return (new PointF(getWidth() / 2f, getHeight() / 2f));
    }

    public Position getPosition(float x, float y) {
        PointF center = getCenter();

        if (x < center.x && y < center.y) {
            return Position.TOP_LEFT;
        } else if (x < center.x && y > center.y) {
            return Position.TOP_RIGHT;
        } else if (x > center.x && y > center.y) {
            return Position.BOTTOM_RIGHT;
        } else {
            return Position.BOTTOM_LEFT;
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}

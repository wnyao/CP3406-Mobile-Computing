package com.example.kongwenyao.educationalgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by kongwenyao on 1/4/18.
 */

public class GameView extends View {

    private final String BACKGROUND_COLOR = "#ffdf5f";
    private final int TEXT_SIZE = 200;
    private Typeface typeface;
    private Paint paint;
    private int totalValue = 50;    //TODO: default total value; subject to change

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        typeface = Typeface.createFromAsset(context.getAssets(), "bungee.ttf");
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTypeface(typeface);
        paint.setTextSize(TEXT_SIZE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.parseColor(BACKGROUND_COLOR));
        canvas.drawText(String.valueOf(totalValue), getWidth()/2 - TEXT_SIZE/2, 220, paint);
    }

    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

}

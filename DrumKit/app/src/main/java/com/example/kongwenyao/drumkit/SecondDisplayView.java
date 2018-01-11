package com.example.kongwenyao.drumkit;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by kongwenyao on 1/11/18.
 */

public class SecondDisplayView extends View {

    //private SoundManager soundManager;

    public SecondDisplayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //soundManager = new SoundManager(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}

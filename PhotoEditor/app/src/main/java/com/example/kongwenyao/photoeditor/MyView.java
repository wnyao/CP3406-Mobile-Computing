package com.example.kongwenyao.photoeditor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.ArrayList;

/**
 * Created by kongwenyao on 12/28/17.
 */

public class MyView extends android.support.v7.widget.AppCompatImageView {

    private ArrayList<PointF> points;
    private Paint paint;

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public void setPoints(ArrayList<PointF> points) {
        this.points = points;
    }

    public void setColor(int red, int green, int blue) {
        paint.setARGB(255, red, green, blue);
    }

    public void setStrokeWidth(float width) {
        paint.setStrokeWidth(width);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (points == null) return;

        for (int i = 0; i < points.size(); i++) {

            if (points.get(i) != null) {
                //draw on main point
                canvas.drawCircle(points.get(i).x, points.get(i).y, 5, paint);
            }

            if ((i + 1) != points.size()) {
                if (points.get(i + 1) != null && points.get(i) != null) {   //if both point i & i+1 not null
                    canvas.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y, paint);
                } else {
                    if (points.size() < (i + 2)) {  //if i + 2 not exceed array size
                        i = i + 2;
                        canvas.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y, paint);
                    }
                }
            }
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

}

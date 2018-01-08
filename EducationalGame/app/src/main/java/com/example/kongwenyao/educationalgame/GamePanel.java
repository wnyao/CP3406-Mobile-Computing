package com.example.kongwenyao.educationalgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

/**
 * Created by kongwenyao on 1/4/18.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private static final String BACKGROUND_COLOR = "#ffdf5f";
    private static final int TEXT_SIZE = 200;
    private Paint paint;
    private int totalValue = 50;    //TODO: default total value; subject to change

    private GameThread gameThread;
    private NumberObject numberObject1;
    private NumberObject numberObject2;
    private RecordManager recordManager;

    private Handler handler = new Handler(Looper.getMainLooper()) { //test
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Total msgObject = Total.valueOf(String.valueOf(msg.obj));

            if (msgObject == Total.MORE_THAN_TOTAL || msgObject == Total.IS_TOTAL) {
                recordManager.clearRecordValues();
                totalValue = numberObject1.generateRandNum(100);

                Toast.makeText(getContext(), msgObject.name(), Toast.LENGTH_SHORT).show();   //** test **
            }
        }
    };

    public GamePanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);

        //Object instantiation
        gameThread = new GameThread(getHolder(), this);
        numberObject1 = new NumberObject();
        numberObject2 = new NumberObject();
        recordManager = new RecordManager();

        //Paint
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "bungee.ttf");
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTypeface(typeface);
        paint.setTextSize(TEXT_SIZE);
        paint.setTextAlign(Paint.Align.CENTER);

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
        numberObject1.update();
        numberObject2.update();

        collisionDetection(GameActivity.playerPos, GameActivity.playerSize, numberObject1);
        collisionDetection(GameActivity.playerPos, GameActivity.playerSize, numberObject2);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawColor(Color.parseColor(BACKGROUND_COLOR)); //Background Color
        canvas.drawText(String.valueOf(totalValue), getWidth()/2, 220, paint); //Total Value

        numberObject1.draw(canvas);
        numberObject2.draw(canvas);

        //test
        /**
        PointF pointF = GameActivity.playerPos;
        PointF objectPos = numberObject1.getObjectPos();
        PointF gameViewSize = GameActivity.gameViewSize;
        PointF playerSize = GameActivity.playerSize;
        Paint paint = new Paint();
        paint.setTextSize(50);
        paint.setColor(Color.BLACK);
        canvas.drawText(String.valueOf("Player x: " + pointF.x), 10, 400, paint);  //player position x
        canvas.drawText(String.valueOf("Player y: " + pointF.y), 10, 500, paint);  //player position y
        canvas.drawText(String.valueOf("Obejct x: " + objectPos.x), 10, 600, paint);  //object position x
        canvas.drawText(String.valueOf("Obejct y: " + objectPos.y), 10, 700, paint);  //object position y
        canvas.drawText(String.valueOf("GameView w: " + gameViewSize.x), 10, 800, paint);  //Game View width
        canvas.drawText(String.valueOf("GameView h: " + gameViewSize.y), 10, 900, paint);  //Game View height
        canvas.drawText(String.valueOf("Player w: " + playerSize.x), 10, 1000, paint);  //Screen w
        canvas.drawText(String.valueOf("Player h: " + playerSize.y), 10, 1100, paint);  //Screen h
        */
    }

    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

    public void collisionDetection(PointF playerPos, PointF playerSize, NumberObject numberObject) {
        float playerLeft = playerPos.x;
        float playerRight = playerPos.x + playerSize.x;
        PointF objectPos = numberObject.getObjectPos();

        if (objectPos.y >= playerPos.y) {
            if (objectPos.x >= playerLeft && objectPos.x <= playerRight) {

                final Integer displayValue = numberObject.getDisplayValue(); //TODO: identify string and int

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        recordManager.addValue(displayValue);
                        Total result = recordManager.isTotal(totalValue);

                        Log.i("Value ", String.valueOf(recordManager.calculateTotal())); //***

                        //Send message to handler
                        Message message = handler.obtainMessage();
                        message.obj = result.name();
                        handler.sendMessage(message);
                    }
                }).start();

                numberObject.resetPosition(true);
            }
        }
    }
}

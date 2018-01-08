package com.example.kongwenyao.educationalgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

/**
 * Created by kongwenyao on 1/4/18.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    //Canvas Parameter
    private static final String CANVAS_COLOR = "#FFDF5F";
    private static final String BACKGROUND_COLOR1 = "#FFB95F";
    private static final int TARGET_VAL_TEXT_SIZE = 200;
    private static final int CURRENT_VAL_TEXT_SIZE = 90;

    //Objects
    private GameThread gameThread;
    private NumberObject numberObject1;
    private NumberObject numberObject2;
    private NumberObject numberObject3;
    private RecordManager recordManager;

    //Variable
    private Paint textPaint, textPaint1, graphicPaint;
    private int targetValue;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Total msgObject = Total.valueOf(String.valueOf(msg.obj));

            if (msgObject != Total.LESS_THAN_TOTAL) {
                String message;

                if (msgObject == Total.MORE_THAN_TOTAL) {
                    recordManager.decrementChances();
                    message = getResponseMsg(msgObject);
                    Toast.makeText(getContext(), message , Toast.LENGTH_LONG).show();

                    if (recordManager.getChances() == 0) {
                        resetGame();
                        //TODO: Share to twitter
                    }

                } else {
                    message = getResponseMsg(msgObject);
                    Toast.makeText(getContext(), message , Toast.LENGTH_LONG).show();
                }

                targetValue = numberObject1.generateRandNum(200);
                recordManager.clearRecordValues();
            }

        }
    };

    public GamePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setFocusable(true);

        //Object instantiation
        gameThread = new GameThread(getHolder(), this);
        numberObject1 = new NumberObject();
        numberObject2 = new NumberObject();
        numberObject3 = new NumberObject();
        recordManager = new RecordManager();

        //Set Paint Object
        setPaintObject();
        targetValue = numberObject1.generateRandNum(200);
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

    public void setPaintObject() {
        //Paint for target value
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "bungee.ttf");
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTypeface(typeface);
        textPaint.setTextSize(TARGET_VAL_TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.CENTER);

        //Paint for current value
        textPaint1 = new Paint();
        textPaint1.setColor(Color.BLACK);
        textPaint1.setTypeface(typeface);
        textPaint1.setTextAlign(Paint.Align.CENTER);
        textPaint1.setTextSize(CURRENT_VAL_TEXT_SIZE);

        //Paint for top left circle
        graphicPaint = new Paint();
        graphicPaint.setColor(Color.parseColor(BACKGROUND_COLOR1));
    }

    public void update() {
        numberObject1.update();
        numberObject2.update();
        numberObject3.update();
        collisionDetection(GameActivity.playerPos, GameActivity.playerSize, numberObject1);
        collisionDetection(GameActivity.playerPos, GameActivity.playerSize, numberObject2);
        collisionDetection(GameActivity.playerPos, GameActivity.playerSize, numberObject3);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        //GamePanel Graphic
        canvas.drawColor(Color.parseColor(CANVAS_COLOR)); //Background color
        canvas.drawRoundRect(new RectF(-100, -100, 250, 250), 700, 700, graphicPaint); //Top left circle
        canvas.drawText(String.valueOf(targetValue), getWidth()/2, 300, textPaint); //Target value
        canvas.drawText(String.valueOf(recordManager.calculateTotal()), 110, 140, textPaint1); //Current value

        //Dropping Object
        numberObject1.draw(canvas);
        numberObject2.draw(canvas);
        numberObject3.draw(canvas);
    }

    public void collisionDetection(PointF playerPos, PointF playerSize, NumberObject numberObject) {
        float playerLeft = playerPos.x;
        float playerRight = playerPos.x + playerSize.x;
        PointF objectPos = numberObject.getObjectPos();

        if (objectPos.y >= playerPos.y) {   //If number object in player height
            if (objectPos.x >= playerLeft && objectPos.x <= playerRight) { //if object y in player width

                final Integer displayValue = numberObject.getDisplayValue(); //TODO: identify string and int
                recordManagementTask(displayValue);
                numberObject.resetPosition(true);
            }
        }
    }

    public void recordManagementTask(final int displayValue) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                recordManager.addValue(displayValue);
                Total result = recordManager.isTotal(targetValue);  //Check total
                checkToIncreaseSpeed(2);  //Check whether to increase velocity of number objects

                //Pass result to handler
                Message message = handler.obtainMessage();
                message.obj = result.name();
                handler.sendMessage(message);
            }
        }).start();
    }

    public void checkToIncreaseSpeed(int scoreInterval) {
        int cuttingLine = 0;

        while (cuttingLine < recordManager.getScore()) {
            cuttingLine += scoreInterval;
        }

        if (recordManager.getScore() > cuttingLine) {
            numberObject1.increaseDroppingSpeed();
            numberObject2.increaseDroppingSpeed();
        }
    }

    public String getResponseMsg(Total result) {
        String message;

        if (result == Total.MORE_THAN_TOTAL) {
            if (recordManager.getChances() > 1) {
                message = "Too much! " + recordManager.getChances() + " chances left.";
            } else if (recordManager.getChances() == 1){
                message = "Too much! " + recordManager.getChances() + " chance left.";
            } else {
                message = "Game Over!";
            }
        } else {
            if (recordManager.getScore() > 1) {
                message = "Bravo! " + recordManager.getScore() + " correct answers.";
            } else {
                message = "Bravo! " + recordManager.getScore() + " correct answer.";
            }
        }
        return message;
    }

    public void resetGame() {
        recordManager.setDefault();
        numberObject1.setDefault();
        numberObject2.setDefault();
        numberObject3.setDefault();
    }
}

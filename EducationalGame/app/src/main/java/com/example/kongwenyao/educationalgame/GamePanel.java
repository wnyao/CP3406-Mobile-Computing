package com.example.kongwenyao.educationalgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private static final String CIRCLE_COLOR1 = "#FFB95F";
    private static final String CIRCLE_COLOR2 = "#E86A03";
    private static final int TARGET_VAL_TEXT_SIZE = 170;
    private static final int CURRENT_VAL_TEXT_SIZE = 90;

    //Canvas graphic
    private Bitmap treeBitmap;
    private float treeBitmapPosX, treeBitmapPosY;

    //Game Objects
    private GameThread gameThread;
    private NumberObject numberObject1;
    private NumberObject numberObject2;
    private NumberObject numberObject3;
    private NumberObject numberObject4;
    private NumberObject numberObject5;
    private GameRecord gameRecord;

    //Variables
    private Paint textPaint, textPaint1, graphicPaint, graphicPaint1, treeGraphicPaint;
    private int targetValue, scoreValue;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Total msgObject = Total.valueOf(String.valueOf(msg.obj));

            if (msgObject != Total.LESS_THAN_TOTAL) {
                String message;

                if (msgObject == Total.MORE_THAN_TOTAL) {
                    gameRecord.decrementChances();
                    message = getResponseMsg(msgObject);
                    Toast.makeText(getContext(), message , Toast.LENGTH_LONG).show();

                    if (gameRecord.getChances() == 0) { //If chances equals 0
                        scoreValue = gameRecord.getScore();
                        resetGame(); //Set game to default
                        startGameOverActivity();
                    }

                } else {
                    message = getResponseMsg(msgObject);
                    Toast.makeText(getContext(), message , Toast.LENGTH_LONG).show();
                }

                targetValue = numberObject1.generateRandNum(200);
                gameRecord.clearRecordValues();
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
        numberObject4 = new NumberObject();
        numberObject5 = new NumberObject();
        gameRecord = new GameRecord();

        //Set Paint Object
        setPaintObject();
        targetValue = numberObject1.generateRandNum(200);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameThread = new GameThread(getHolder(), this);
        gameThread.setRunning(true);
        gameThread.start();

        //Get tree bitmap
        treeBitmap = decodeDrawableToBitmap(R.drawable.image_tree);
        treeBitmapPosX = (-getBitmapWidth(R.drawable.image_tree) + 250) /2;
        treeBitmapPosY = getHeight() - getBitmapHeight(R.drawable.image_tree);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                gameThread.setRunning(false);
                gameThread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!gameThread.isAlive()) {
                retry = false;
            }
        }
    }

    private void setPaintObject() {
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
        graphicPaint.setColor(Color.parseColor(CIRCLE_COLOR1));

        //Paint for top left circle background
        graphicPaint1 = new Paint();
        graphicPaint1.setColor(Color.parseColor(CIRCLE_COLOR2));

        //Tree graphic paint
        treeGraphicPaint = new Paint();
        treeGraphicPaint.setAlpha(245);
    }

    public void update() {
        numberObject1.update();
        numberObject2.update();
        numberObject3.update();
        numberObject4.update();
        numberObject5.update();
        collisionDetection(GameActivity.playerPos, GameActivity.playerSize, numberObject1);
        collisionDetection(GameActivity.playerPos, GameActivity.playerSize, numberObject2);
        collisionDetection(GameActivity.playerPos, GameActivity.playerSize, numberObject3);
        collisionDetection(GameActivity.playerPos, GameActivity.playerSize, numberObject4);
        collisionDetection(GameActivity.playerPos, GameActivity.playerSize, numberObject5);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        //GamePanel Graphic
        canvas.drawColor(Color.parseColor(CANVAS_COLOR)); //Background color
        canvas.drawBitmap(treeBitmap, treeBitmapPosX, treeBitmapPosY, treeGraphicPaint); //Tree bitmap
        canvas.drawRoundRect(new RectF(-100, -80, 250, 270), 700, 700, graphicPaint1); //Top left circle background
        canvas.drawRoundRect(new RectF(-100, -100, 250, 250), 700, 700, graphicPaint); //Top left circle
        canvas.drawText(String.valueOf(targetValue), getWidth()/2, 300, textPaint); //Target value
        canvas.drawText(String.valueOf(gameRecord.calculateTotal()), 110, 140, textPaint1); //Current value

        //Number Objects
        numberObject1.draw(canvas);
        numberObject2.draw(canvas);
        numberObject3.draw(canvas);
        numberObject4.draw(canvas);
        numberObject5.draw(canvas);
    }

    private void collisionDetection(PointF playerPos, PointF playerSize, NumberObject numberObject) {
        float playerLeft = playerPos.x;
        float playerRight = playerPos.x + playerSize.x;
        PointF objectPos = numberObject.getObjectPos();

        if (objectPos.y >= playerPos.y) {   //If number object in player height
            if (objectPos.x >= playerLeft && objectPos.x <= playerRight) { //if object y in player width

                final Integer displayValue = numberObject.getDisplayValue();
                recordManagementTask(displayValue);
                numberObject.resetPosition(true);
            }
        }
    }

    private void recordManagementTask(final int displayValue) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                gameRecord.addValue(displayValue);
                Total result = gameRecord.isTotal(targetValue);  //Check total
                checkToIncreaseSpeed(2);  //Check whether to increase velocity of number objects

                //Pass result to handler
                Message message = handler.obtainMessage();
                message.obj = result.name();
                handler.sendMessage(message);
            }
        }).start();
    }

    private void checkToIncreaseSpeed(int scoreInterval) {
        int cuttingLine = 0;

        while (cuttingLine < gameRecord.getScore()) {
            cuttingLine += scoreInterval;
        }

        if (gameRecord.getScore() > cuttingLine) {
            numberObject1.increaseDroppingSpeed();
            numberObject2.increaseDroppingSpeed();
            numberObject3.increaseDroppingSpeed();
            numberObject4.increaseDroppingSpeed();
            numberObject5.increaseDroppingSpeed();
            Toast.makeText(getContext(), "Speed has increased!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getResponseMsg(Total result) {
        String message;

        if (result == Total.MORE_THAN_TOTAL) {
            if (gameRecord.getChances() > 1) {
                message = "Too much! " + gameRecord.getChances() + " chances left.";
            } else if (gameRecord.getChances() == 1){
                message = "Too much! " + gameRecord.getChances() + " chance left.";
            } else {
                message = "Game Over";
            }
        } else {
            if (gameRecord.getScore() > 1) {
                message = "Bravo! " + gameRecord.getScore() + " correct answers.";
            } else {
                message = "Bravo! " + gameRecord.getScore() + " correct answer.";
            }
        }
        return message;
    }

    private void resetGame() {
        gameRecord.setDefault();
        numberObject1.setDefault();
        numberObject2.setDefault();
        numberObject3.setDefault();
        numberObject4.setDefault();
        numberObject5.setDefault();
    }

    private void startGameOverActivity() {
        Intent intent = new Intent(getContext(), GameOverActivity.class);
        intent.putExtra("SCORE", scoreValue);
        getContext().startActivity(intent);
    }

    private Bitmap decodeDrawableToBitmap(int drawableId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), drawableId);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.6), (int) (bitmap.getHeight() * 0.6),true);
        return bitmap;
    }

    private int getBitmapHeight(int drawableID) {
        Bitmap bitmap = decodeDrawableToBitmap(drawableID);
        return bitmap.getHeight();
    }

    private int getBitmapWidth(int drawableID) {
        Bitmap bitmap = decodeDrawableToBitmap(drawableID);
        return bitmap.getWidth();
    }
}

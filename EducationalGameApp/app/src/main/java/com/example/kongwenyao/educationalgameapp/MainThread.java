package com.example.kongwenyao.educationalgameapp;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by kongwenyao on 1/2/18.
 */

public class MainThread extends Thread {
    public static final int MAX_FPS = 30;

    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    private Double averageFPS;
    private static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();

        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        long startTime;
        long timeTaken = 1000/ MAX_FPS; //Time taken per frame
        long waitTime;
        long targetTime = 1000/ MAX_FPS;
        long totalTime = 0;
        int frameCount = 0;

        while (running) {
            startTime = System.nanoTime();  //Current running JVM time source
            canvas = null;

            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            timeTaken = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeTaken;
            try {
                if (waitTime > 0) {
                    this.sleep(waitTime);   //Capping leftover frame rate
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;

            if (frameCount == 30) {
                averageFPS = Double.valueOf(1000/ ((totalTime/ frameCount)/ 1000000));

                //Reset values
                totalTime = 0;
                frameCount = 0;

                System.out.println("Average FPS: " + averageFPS);
            }
        }


    }
}

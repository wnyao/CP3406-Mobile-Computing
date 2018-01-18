package com.example.kongwenyao.spiritlevelapp;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private MainView mainView;
    private SensorManager sensorManager;
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainView = findViewById(R.id.Main_View);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager != null ? sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) : null;
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public float filterCoordinateHorizontally(float x) {
        Rect horizontalRect = mainView.getHorizontalRect();

        if (x > horizontalRect.right - mainView.getCircleRadius()) {
            x = horizontalRect.right - mainView.getCircleRadius();
        } else if (x < horizontalRect.left + mainView.getCircleRadius()){
            x = horizontalRect.left + mainView.getCircleRadius();
        }
        return x;
    }

    public float filterCoordinateVertically(float y) {
        Rect verticalRect = mainView.getVerticalRect();

        if (y > verticalRect.bottom - mainView.getCircleRadius()) {
            y = verticalRect.bottom - mainView.getCircleRadius();
        } else if (y < verticalRect.top + mainView.getCircleRadius()) {
            y = verticalRect.top + mainView.getCircleRadius();
        }
        return y;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x, y;

        if (event.values[0] > 0) {  //tilt device right
            x = mainView.getCx() + 2;
            x = filterCoordinateHorizontally(x);
            mainView.setCx(x);
        } else if (event.values[0] < 0) { //tilt device left
            x = mainView.getCx() - 2;
            x = filterCoordinateHorizontally(x);
            mainView.setCx(x);
        }

        if (event.values[1] < 0) {  //up
            y = mainView.getCy() + 2;
            y = filterCoordinateVertically(y);
            mainView.setCy(y);
        } else if (event.values[1] > 0) {
            y = mainView.getCy() - 2;
            y = filterCoordinateVertically(y);
            mainView.setCy(y);
        }

        mainView.invalidate(); //redraw canvas
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

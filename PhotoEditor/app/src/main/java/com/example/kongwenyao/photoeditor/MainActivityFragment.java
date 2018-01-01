package com.example.kongwenyao.photoeditor;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    static final int SELECT_IMAGE = 1;
    private MyView myView;
    private ArrayList pointFs;

    static int r = 255;
    static int g = 140;
    static int b = 70;
    static float strokeWidth = 20f;

    public MainActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_activity, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Instance Reference
        myView = getActivity().findViewById(R.id.my_view);
        Button selectButton = getActivity().findViewById(R.id.select_button);

        //Specify PointF
        pointFs = new ArrayList<PointF>();
        myView.setPoints(pointFs);

        //Set Event Listener
        selectButton.setOnClickListener(this);
        myView.setOnTouchListener(this);

    }

    @Override
    public void onClick(View v) {
        //Implicit Intent
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");  //MIME for image
        startActivityForResult(intent, MainActivityFragment.SELECT_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == MainActivityFragment.SELECT_IMAGE && resultCode == Activity.RESULT_OK) {

            Uri dataUri = intent.getData(); //get data URI

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), dataUri);   //get Bitmap through data URI
                myView.setImageBitmap(bitmap);
                pointFs.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:   //TODO: disable scroll in landscape when drawing
                PointF pointF = new PointF(event.getX(), event.getY());

                //Set Stroke
                myView.setColor(r, g, b);
                myView.setStrokeWidth(strokeWidth);

                //Store onTouch point
                pointFs.add(pointF);

                //Pass point to MyView.class
                myView.invalidate();    //Force redraw on custom view
                break;

            case MotionEvent.ACTION_UP:
                pointFs.add(null);  //using null as break point for not to execute canvas.drawLine() between previous end point and new point
                break;
        }

        //System.out.println(String.format("%f %f", event.getX(), event.getY())); //Test Result
        return true;
    }

    public void setColorRGB(int red, int green, int blue) {
        r = red;
        g = green;
        b = blue;
    }

}

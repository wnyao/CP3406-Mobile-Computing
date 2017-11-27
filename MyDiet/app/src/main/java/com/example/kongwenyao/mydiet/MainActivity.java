package com.example.kongwenyao.mydiet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Random rand = new Random();
        String[] messages = getResources().getStringArray(R.array.stringMessages);
        int[] images = {R.drawable.food1, R.drawable.food2, R.drawable.food3};

        setMessage(messages[rand.nextInt(3)]);
        setImage(images[rand.nextInt(3)]);

    }

    public void setMessage(String message) {
        TextView messageLabel = findViewById(R.id.txtMessage);
        messageLabel.setText(message);
    }

    public void setImage(int resId) {
        ImageView imageLabel = findViewById(R.id.insImage);
        imageLabel.setImageResource(resId);
    }

}

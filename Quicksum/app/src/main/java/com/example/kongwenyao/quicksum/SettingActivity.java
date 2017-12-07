package com.example.kongwenyao.quicksum;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void passColorCode(int color) {
        Intent intent = new Intent(this, MainActivity.class);
        String colorCode = Integer.toString(color);
        intent.putExtra("ColorChoice", colorCode); //pass message through intent
        startActivity(intent);
    }

    public void triggerColorPicker(View view) {
        final ColorPicker cp = new ColorPicker(SettingActivity.this, 255, 55, 50);

        cp.show(); //show color picker
        Button selectButton = cp.findViewById(R.id.okColorButton);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passColorCode(cp.getColor());
                cp.dismiss();
            }
        });
    }

}

package com.example.kongwenyao.guessinggameapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final String PRES_NAME = "MyPresFile";

    private TextView statusView;
    private int maximum = 10;
    private int minimum = 1;
    private int secretVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputFieldHandler();
        secretVal = generateSecretValue(minimum, maximum);

        try {
            Intent intent = getIntent();
            int[] settingValues = intent.getIntArrayExtra("SETTING_VALUE");   //get setting values from intent extras
            minimum = settingValues[0];
            maximum = settingValues[1];
            secretVal = generateSecretValue(minimum, maximum);
        } catch (Exception e) {
            SharedPreferences setting = getSharedPreferences(PRES_NAME, 0);
            minimum = setting.getInt("MIN_VAL", 1);
            maximum = setting.getInt("MAX_VAL",10);
            secretVal = setting.getInt("SEC_VAL", secretVal);
        }

        TextView min = findViewById(R.id.min);
        TextView max = findViewById(R.id.max);
        String mintext = "Min: " + minimum;
        String maxtext = "Max: " + maximum;
        min.setText(mintext);
        max.setText(maxtext);

    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences setting = getSharedPreferences(PRES_NAME, 0);
        SharedPreferences.Editor editor = setting.edit();
        editor.putInt("MIN_VAL", minimum);
        editor.putInt("MAX_VAL", maximum);
        editor.putInt("SEC_VAL", secretVal);
        editor.commit();
    }

    public void inputFieldHandler() {
        EditText textField = findViewById(R.id.textField);

        textField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {   //catch empty string conversion
                    int inputVal = Integer.parseInt(s.toString());
                    String result = inputVerification(inputVal);
                    statusView = findViewById(R.id.statusView);
                    statusView.setText(result);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });
    }

    public String inputVerification(int inputNum) {
        if (inputNum == secretVal) {
            return "You guess it! Hooray!!";
        } else if (inputNum < secretVal) {
            return  "Try bigger";
        } else {
            return  "Try smaller";
        }
    }

    public int generateSecretValue(int minVal, int maxVal) {
        Random rand = new Random();
        int value = minVal + rand.nextInt(maxVal - minVal);
        return value;
    }

    public void settingsButtonHandler(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        intent.putExtra("CURRENT_VALUE", new int[] {minimum, maximum});
        startActivity(intent);
    }

    public void setSecretValue(int secretValue) {
        secretVal = secretValue;
    }

    public void setMinimum(int minVal) {
        minimum = minVal;
    }

    public void setMaximum(int maxVal) {
        maximum = maxVal;
    }

}

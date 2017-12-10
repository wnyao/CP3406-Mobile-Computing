package com.example.kongwenyao.quicksum;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private ViewGroup gridLayout;
    private int[] buttonsIds;
    private double totalValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonsIds = getAllNumberButtons();
        setNumberButtonListener(buttonsIds);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar); //set custom toolbar

        try {
            Intent intent = getIntent();
            int colorCode = Integer.parseInt(intent.getStringExtra("ColorChoice"));
            setTextColor(colorCode);

        } catch (Exception e) { //catch when getIntent is null
            System.out.print("No Intent found!");
        }

    }

    public int[] getAllNumberButtons() {
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        int[] buttonsIds = new int[gridLayout.getChildCount()]; //specify new array

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            if (gridLayout.getChildAt(i) instanceof Button) {
                buttonsIds[i] = gridLayout.getChildAt(i).getId();
            }
        }
        return buttonsIds;
    }

    public void setNumberButtonListener(int[] buttonsIds) {
        Button numberButton;
        String string;

        for (int i = 0; i < buttonsIds.length; i++) {
            numberButton = findViewById(buttonsIds[i]);
            string = Integer.toString(i + 1);
            numberButton.setText(string);

            numberButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    calculateTotal(view);
                }
            });
        }
    }

    @SuppressLint("DefaultLocale")
    public void calculateTotal(View view) {
        Double value;
        String sTotalValue;
        Button numberButton = (Button) view;
        String buttonText = numberButton.getText().toString();

        Pattern pattern = Pattern.compile("1/");
        Matcher matcher = pattern.matcher(buttonText);

        if (matcher.find()) {
            char divisor = buttonText.charAt((buttonText.length() - 1));
            value = 1 / Double.parseDouble(Character.toString(divisor));

        } else {
            value = Double.parseDouble(buttonText);

        }
        totalValue += value;
        sTotalValue = String.format("%.2f", totalValue); //format to 2 decimals
        displayValue(sTotalValue);
    }

    public void displayValue(String value) {
        TextView mDisplayValue = findViewById(R.id.mDisplayValue);
        mDisplayValue.setText(value);
    }

    public void reset(View view) {
        TextView mDisplayValue = (TextView) view;

        totalValue = 0;
        mDisplayValue.setText("0");
    }

    public int[] getThreeButtonsId() {
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        int[] buttonsIds = new int[3];

        for (int i = 0; i < 3; i++) {
            if (gridLayout.getChildAt(i) instanceof Button) {
                buttonsIds[i] = gridLayout.getChildAt(i).getId();
            }
        }
        return buttonsIds;
    }


    public void setOtherButton(View view) {
        Button numberButton;
        String value;

        buttonsIds = getThreeButtonsId();

        for (int buttonId: buttonsIds) {
            numberButton = findViewById(buttonId);
            value = numberButton.getText().toString();

            switch (value) {
                case "1":
                    numberButton.setText("1/2");
                    break;
                case "2":
                    numberButton.setText("1/3");
                    break;
                default:
                    numberButton.setText("1/4");
                    break;
            }


            numberButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    calculateTotal(view);
                }
            });
        }
        setBackToButton(view);
    }

    public void setBackToButton(View view) {
        Button otherButton = (Button) view;
        otherButton.setText(R.string.backToNormal);

        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllNumberButtons();
                setNumberButtonListener(buttonsIds);

                Button btnButton = (Button) view;
                btnButton.setText(R.string.fraction);
                btnButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setOtherButton(view);
                    }
                });
            }
        });
    }

    public void setTextColor(int colorCode) {
        TextView mDisplayValue = findViewById(R.id.mDisplayValue);
        mDisplayValue.setTextColor(colorCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_setting) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent); //start SettingActivity
        }

        return super.onOptionsItemSelected(item);
    }

}

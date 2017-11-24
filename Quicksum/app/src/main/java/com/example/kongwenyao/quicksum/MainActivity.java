package com.example.kongwenyao.quicksum;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.GridLayout;
        import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mDisplayValue = (TextView) findViewById(R.id.mDisplayValue);
    private Button otherButton = (Button) findViewById(R.id.otherButton);
    private ViewGroup gridLayout = (GridLayout) findViewById(R.id.gridLayout);
    private int[] buttonsId;

    private Button button; //test

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getNumberButtons();
        setNumberButtonListener(buttonsId);

    }

    public void getNumberButtons() {
        buttonsId = new int[gridLayout.getChildCount()];

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            if (gridLayout.getChildAt(i) instanceof Button) {
                buttonsId[i] = gridLayout.getChildAt(i).getId();
            }
        }
    }

    public void setNumberButtonListener(int[] buttonsId) {
        for (int i = 0; i < buttonsId.length; i++) {
            button = findViewById(buttonsId[i]);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    calculateTotal(Double.parseDouble(button.getText().toString()));
                }
            });
        }

    }

    public void calculateTotal(double value) {
        mDisplayValue.setText((int) value);
    }



}

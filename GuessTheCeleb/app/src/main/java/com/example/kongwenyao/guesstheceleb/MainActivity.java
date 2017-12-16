package com.example.kongwenyao.guesstheceleb;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements SettingsFragment.OnSettingSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get Intent
        try {
            Intent intent = getIntent();
            String numOfOption = intent.getStringExtra(ActivityMainFragment.BUTTON_TEXT);
            ActivityMainFragment.numOfOptions = Integer.parseInt(numOfOption);
        } catch (Exception e) {
            ActivityMainFragment.numOfOptions = 6;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRadioButtonSelected(int buttonText) {
        ActivityMainFragment activityMainFragment = (ActivityMainFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);


        //If ActivityMainFragment is available, we're in two-pane layout...
        //Call a method in the ActivityMainFragment to update its content
        activityMainFragment.updateNumOfOptions(buttonText);

        try {
            activityMainFragment.setContent();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}

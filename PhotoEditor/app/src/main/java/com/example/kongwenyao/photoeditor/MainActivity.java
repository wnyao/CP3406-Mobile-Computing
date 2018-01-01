package com.example.kongwenyao.photoeditor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements SettingsFragment.OnColorChosenListener {

    private MainActivityFragment mainActivityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Variable Assignment
        mainActivityFragment = new MainActivityFragment();

        //Get Saved Data
        SharedPreferences sharedPreferences = getSharedPreferences(SettingsActivity.PRES_FILE, 0);
        MainActivityFragment.r = sharedPreferences.getInt("RED", 255);
        MainActivityFragment.g = sharedPreferences.getInt("GREEN", 140);
        MainActivityFragment.b = sharedPreferences.getInt("BLUE", 70);
        MainActivityFragment.strokeWidth = sharedPreferences.getFloat("STROKE_WIDTH", 20f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
    public void onColorChosen(int red, int green, int blue) {
        //pass color to MainActivityFragment from SettingsFragment
        mainActivityFragment.setColorRGB(red, green, blue);
    }

    @Override
    public void onSeekBarChange(int width) {
        //pass width from SettingsFragment to MainActivityFragment
        MainActivityFragment.strokeWidth = width;

        //TODO: reset canvas in landscape mode
    }
}

package com.example.kongwenyao.educationalgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private GridLayout leaderboardLayout;
    private LeaderboardDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_leaderboard);

        //Custom Toolbar
        Toolbar toolbar = findViewById(R.id.leaderboard_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //Variable Assignment
        leaderboardLayout = findViewById(R.id.leaderboard_layout);
        db = new LeaderboardDatabase(this);

        db.clearAllScoreRecords();  //
        db.addScoreRecord(new Score(1, "Stacy", 3)); //
        db.addScoreRecord(new Score(2, "Joe", 7));  //

        //Process
        displayScoreRecords(db.getScoreRecords());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.back_to_game) {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayScoreRecords(List<Score> records) {
        for (int i = -1; i < records.size(); i++) {
            //Set new TextView
            TextView textView1 = new TextView(this);
            TextView textView2 = new TextView(this);
            TextView textView3 = new TextView(this);
            textView1.setGravity(Gravity.CENTER_VERTICAL);
            textView2.setGravity(Gravity.CENTER);
            //textView3.setGravity(Gravity.CENTER_HORIZONTAL);
            textView3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView1.setTextAppearance(R.style.leaderboard_textStyle);
            textView2.setTextAppearance(R.style.leaderboard_textStyle);
            textView3.setTextAppearance(R.style.leaderboard_textStyle);
            textView1.setPadding(80, 50, 50, 50);
            textView2.setPadding(50, 50, 130, 50);
            textView3.setPadding(100, 50, 100, 50);
            textView1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            textView2.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            textView3.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            //Text
            String id, name, score;
            if (i == -1) {  //Set Title
                id = "ID";
                name = "PLAYER";
                score = "SCORE";
            } else {
                Score record = records.get(i);

                id = record.getId() + ". ";
                name = record.getPlayerName();
                score = String.valueOf(record.getScore());
            }

            //Set Text
            textView1.setText(id);
            textView2.setText(name);
            textView3.setText(score);

            //ViewGroup add view
            leaderboardLayout.addView(textView1);
            leaderboardLayout.addView(textView2);
            leaderboardLayout.addView(textView3);
        }


    }

}

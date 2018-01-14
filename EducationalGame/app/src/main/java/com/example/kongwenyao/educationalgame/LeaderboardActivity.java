package com.example.kongwenyao.educationalgame;

import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private GridLayout leaderboardLayout;
    private LeaderboardDatabase db;
    private TextView textView1, textView2, textView3;
    private MediaPlayerManager mediaPlayerManager;
    private boolean musicEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_leaderboard);

        //Custom Toolbar
        Toolbar toolbar = findViewById(R.id.leaderboard_toolbar);
        toolbar.setTitle(R.string.leaderboard);
        setSupportActionBar(toolbar);

        //Variable Assignment
        leaderboardLayout = findViewById(R.id.leaderboard_layout);
        db = new LeaderboardDatabase(this);

        //FOR TESTING ONLY
        //db.clearAllScoreRecords();
        //test();

        //Process
        displayScoreRecords(db.getScoreRecords());
        musicInit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences(SettingsActivity.PREFS_NAME, 0);
        musicEnabled = sharedPreferences.getBoolean(SettingsActivity.MUSIC_SETTINGS, true);
        musicInit();
    }

    private void musicInit() {
        if (musicEnabled) {
            mediaPlayerManager = new MediaPlayerManager(this);
            mediaPlayerManager.create(R.raw.music_wii, true);
            mediaPlayerManager.play();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (musicEnabled) {
            mediaPlayerManager.stop();
        }

        if (item.getItemId() == R.id.back_to_game) {
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayScoreRecords(List<Score> records) {
        List<Score> sortedRecords = sortScoreRecords(records); //Sort in descending order

        for (int i = -1; i < sortedRecords.size(); i++) {
            //Set textViews
            setTextViews();

            //Get text
            String id, name, score;
            if (i == -1) {  //Set Title
                textView1.setPadding(100, 100, 50, 50);
                textView2.setPadding(100, 80, 50, 50);
                textView3.setPadding(100, 80, 50, 50);
                id = "ID";
                name = "PLAYER";
                score = "SCORE";
            } else {
                Score record = sortedRecords.get(i);

                id = i + ". ";
                name = record.getPlayerName();
                score = String.valueOf(record.getScore());
            }

            //Set Text
            textView1.setText(id);
            textView2.setText(name);
            textView3.setText(score);

            //Gridlayout add view
            leaderboardLayout.addView(textView1);
            leaderboardLayout.addView(textView2);
            leaderboardLayout.addView(textView3);
        }
    }

    private List<Score> sortScoreRecords(List<Score> records) {

        List<Integer> scorelist = new ArrayList<>();
        List<Score> newRecords = new ArrayList<>();

        for (Score record: records) {
            scorelist.add(record.getScore());
        }

        Collections.sort(scorelist); //List of sorted scores
        Collections.reverse(scorelist); //List of reverse sorted scores

        int playerScore;
        for (int score: scorelist) {
            for (Score record: records) {
                playerScore = record.getScore();

                if (playerScore == score) {
                    newRecords.add(record);
                }
            }
        }

        return newRecords;
    }

    private void setTextViews() {
        textView1 = new TextView(this);
        textView2 = new TextView(this);
        textView3 = new TextView(this);
        textView1.setGravity(Gravity.CENTER_VERTICAL);
        textView2.setGravity(Gravity.CENTER);
        textView3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView1.setTextAppearance(R.style.Leaderboard_textStyle);
        textView2.setTextAppearance(R.style.Leaderboard_textStyle);
        textView3.setTextAppearance(R.style.Leaderboard_textStyle);
        textView1.setPadding(100, 50, 50, 50);
        textView2.setPadding(50, 50, 130, 50);
        textView3.setPadding(100, 50, 100, 50);
        textView1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        textView2.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        textView3.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    public void test() { //This method is for testing purposes
        Score score1 = new Score(1, "Stacey", 7);
        check(score1);

        Score score2 = new Score(2, "James", 3);
        check(score2);

    }

    public void check(Score score) { //This method is for testing purposes
        boolean exist = db.isRecordExisted(score.getPlayerName());

        if (exist) { //Check if record existed
            Score recordedScore = db.getScoreRecord(score.getPlayerName());
            if (recordedScore.getScore() != score.getScore()) {
                db.addScoreRecord(score);
            }
        } else {
            db.addScoreRecord(score);
        }
    }

}

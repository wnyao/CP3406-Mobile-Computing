package com.example.kongwenyao.educationalgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.andexert.library.RippleView;

public class GameOverActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEditText;
    private int score;

    private LeaderboardDatabase leaderboardDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        //Custom Toolbar
        Toolbar toolbar = findViewById(R.id.gameover_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //Variable assignment
        leaderboardDatabase = new LeaderboardDatabase(this);
        TextView scoreTextView = findViewById(R.id.score_TextView);
        nameEditText = findViewById(R.id.name_EditText);
        RippleView enterButton = findViewById(R.id.enter_Button);

        //Event listener
        enterButton.setOnClickListener(this);

        //init process
        score = getIntent().getIntExtra("SCORE", 0);
        scoreTextView.setText(String.valueOf(score));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.back_to_game:
                Intent intent = new Intent(this, GameActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.enter_Button:
                //Add record to database
                String playerName = nameEditText.getText().toString();
                playerName = leaderboardDatabase.capitalizeString(playerName);
                boolean existed = leaderboardDatabase.isRecordExisted(playerName);

                //Add score depending on specified conditions
                if (existed) { //Check if record existed
                    Score recordedScore = leaderboardDatabase.getScoreRecord(playerName);
                    if (recordedScore.getScore() != score) {
                        leaderboardDatabase.addScoreRecord(new Score(1, playerName, score));
                    }
                } else {
                    leaderboardDatabase.addScoreRecord(new Score(1, playerName, score));
                }

                //Launch Leaderboard Activity
                Intent intent = new Intent(this, LeaderboardActivity.class);
                startActivity(intent);
                break;
        }
    }
}

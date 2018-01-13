package com.example.kongwenyao.educationalgame;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;

import java.io.InputStream;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class GameOverActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String PREF_FILE = "SCORE_PREF_FILE";

    private LeaderboardDatabase leaderboardDatabase;
    private EditText nameEditText;
    private TextView scoreTextView;
    private int score;
    private ProgressDialog pDialog;

    //Twitter variable
    public static final int AUTHENTICATE = 1;
    private static RequestToken requestToken;
    private static Twitter twitter;
    private AccessToken accessToken;
    private String consumerKey;
    private String consumerSecretKey;
    private String callbackUrl;
    private String oAuthVerifier;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            new updateTwitterStatus().execute(accessToken);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Enabling strinct mode
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
        setContentView(R.layout.activity_game_over);

        //Custom Toolbar
        Toolbar toolbar = findViewById(R.id.gameover_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //Variable assignment
        leaderboardDatabase = new LeaderboardDatabase(this);
        scoreTextView = findViewById(R.id.score_TextView);
        nameEditText = findViewById(R.id.name_EditText);
        RippleView enterButton = findViewById(R.id.enter_Button);

        //Event listener
        enterButton.setOnClickListener(this);

        getScoreFromIntent(); //Get score from GameActivity
        twitterConfigs();
    }

    public void getScoreFromIntent() {
        score = getIntent().getIntExtra("SCORE", 0);
        scoreTextView.setText(String.valueOf(score));
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (score == 0) {   //If getIntent.getExtra return default score value
            SharedPreferences sharedPreferences = getSharedPreferences(GameOverActivity.PREF_FILE, 0);
            score = sharedPreferences.getInt("PREVIOUS_SCORE", 0);
            scoreTextView.setText(String.valueOf(score));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveScoreInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gameovermenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case R.id.back_to_game:
                intent = new Intent(this, GameActivity.class);
                startActivity(intent);
                break;
            case R.id.twitter:
                saveScoreInfo();
                loginInToTwitter();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTHENTICATE && resultCode == RESULT_OK) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //Get data from intent
                    String verifier = data.getStringExtra(oAuthVerifier);

                    try {
                        accessToken = twitter.getOAuthAccessToken(requestToken, verifier); //
                        mHandler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
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

    //Reading twitter essential configuration parameters
    private void twitterConfigs() {
        consumerKey = getString(R.string.twittter_consumer_key);
        consumerSecretKey = getString(R.string.twitter_consumer_secret);
        callbackUrl = getString(R.string.twitter_callback_url);
        oAuthVerifier = getString(R.string.twitter_oauth_verifier);
    }

    private void saveScoreInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences(GameOverActivity.PREF_FILE, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("PREVIOUS_SCORE", score);
        editor.apply();
    }

    public void loginInToTwitter() {
        final ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(consumerKey);
        builder.setOAuthConsumerSecret(consumerSecretKey);

        final twitter4j.conf.Configuration configuration = builder.build();
        final TwitterFactory twitterFactory = new TwitterFactory(configuration);
        twitter = twitterFactory.getInstance();

        try {
            requestToken = twitter.getOAuthRequestToken(callbackUrl); //Get request token

            //Launch Authentication Activity
            Intent intent = new Intent(this, AuthenticationActivity.class);
            intent.putExtra(AuthenticationActivity.EXTRA_URL, requestToken.getAuthenticationURL());
            startActivityForResult(intent, AUTHENTICATE);

        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    class updateTwitterStatus extends AsyncTask<AccessToken, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Set up progress dialog
            pDialog = new ProgressDialog(GameOverActivity.this);
            pDialog.setMessage("Posting to twitter...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(AccessToken... accessTokens) {
            AccessToken accessToken = accessTokens[0];

            try {
                twitter.setOAuthAccessToken(accessToken);

                //Message and image file
                String message = "I have achived a score of " + String.valueOf(score) + " in 'SumUp!'. Come and challenge me! :p";
                InputStream inputStream = getResources().openRawResource(+ R.drawable.app_image_media);

                //Set tweet
                StatusUpdate statusUpdate = new StatusUpdate(message);
                statusUpdate.setMedia("app_image_media.png", inputStream); //set status image
                twitter4j.Status status = twitter.updateStatus(statusUpdate); //tweet

                Log.d("Tweet status", status.toString());
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            Toast.makeText(GameOverActivity.this, "Tweet sent", Toast.LENGTH_LONG).show();
        }
    }

}

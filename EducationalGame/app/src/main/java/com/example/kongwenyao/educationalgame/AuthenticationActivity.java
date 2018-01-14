package com.example.kongwenyao.educationalgame;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AuthenticationActivity extends AppCompatActivity {

    public static final String AUTHENTICATION_URL = "EXTRA_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.authentication_toolbar);
        toolbar.setTitle("Twitter");
        setSupportActionBar(toolbar);

        //Get Intent
        String url = getIntent().getStringExtra(AUTHENTICATION_URL);
        if (url == null) {
            Log.e("twitter", "URL cannot be null");
            finish();
        }

        //Webview
        WebView webView = findViewById(R.id.webview);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(url);
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
                Intent intent = new Intent(this, GameOverActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    class MyWebViewClient extends WebViewClient {

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);

            if (url.contains(getResources().getString(R.string.twitter_callback_url))) {
                Uri uri = Uri.parse(url);
                String verifier = uri.getQueryParameter(getString(R.string.twitter_oauth_verifier));

                //Sending result back
                Intent resultIntent = new Intent();
                resultIntent.putExtra(getString(R.string.twitter_oauth_verifier), verifier);
                setResult(RESULT_OK, resultIntent);
                finish();
            }

        }
    }
}

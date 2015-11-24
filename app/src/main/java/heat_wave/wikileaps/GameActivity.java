package heat_wave.wikileaps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import heat_wave.wikileaps.utils.Animations;
import heat_wave.wikileaps.utils.Difficulty;
import heat_wave.wikileaps.utils.OnSwipeTouchListener;


public class GameActivity extends AppCompatActivity {

    public static final String DIFFICULTY = "difficulty";
    private Difficulty difficulty;
    private String path;
    private SharedPreferences sharedPreferences;
    private boolean gameFinished;
    private int secondsToOverlayHide = 5;
    private TextView topSlide;
    private TextView bottomSlide;
    private Handler timeHandler;
    private String startingPage;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar gameToolbar = (Toolbar) findViewById(R.id.game_toolbar);
        setSupportActionBar(gameToolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        topSlide = (TextView) findViewById(R.id.topSlide);
        bottomSlide = (TextView) findViewById(R.id.bottomSlide);

        Intent intent = getIntent();
        difficulty = (Difficulty) intent.getSerializableExtra(DIFFICULTY);
        sharedPreferences = this.getSharedPreferences("WIKI_LEAPS", Context.MODE_PRIVATE);
        gameFinished = false;
        path = "";

        webView = (WebView) findViewById(R.id.wiki);
        webView.setWebViewClient(new TrackingWebViewClient());
        webView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeBottom() {
                getSupportActionBar().show();
                secondsToOverlayHide = 5;
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }

                super.onTouch(v, event);
                return false;
            }
        });

        webView.loadUrl("https://en.m.wikipedia.org/wiki/Special:Random");
        bottomSlide.setText(difficulty.toString());
        showOverlay();
        timeHandler = new Handler();
        startTimeTracking();
    }

    Runnable intervalChecker = new Runnable() {
        @Override
        public void run() {

            if (secondsToOverlayHide > 0) {
                secondsToOverlayHide--;
                if (secondsToOverlayHide == 0) {
                    hideOverlay();
                }
            }
            timeHandler.postDelayed(intervalChecker, 1000);
        }
    };

    void startTimeTracking() {
        intervalChecker.run();
    }

    void stopTimeTracking() {
        timeHandler.removeCallbacks(intervalChecker);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_replay:
                path = "";
                webView.loadUrl(startingPage);
                return true;

            case R.id.action_help:
                showOverlay();
                secondsToOverlayHide = 5;
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class TrackingWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (path.contains(url) || (!url.contains("m.wikipedia.org/wiki/")) ||
                    (url.contains("Special") && !url.contains("Random"))) {
                Toast.makeText(getApplicationContext(), R.string.no_way_out, Toast.LENGTH_SHORT).show();
                return true;
            }
            view.loadUrl(url);
            url = parseUnicodeString(view.getUrl());

            if (path.length() == 0) {
                startingPage = url;
            }
            path = path + (path.length() > 0 ? " -> " : "") + (url.substring(url.lastIndexOf('/') + 1)).replace('_', ' ');
            if (!path.contains(" -> ")) {
                topSlide.setText(path);
            }
            if (!gameFinished && path.contains(difficulty.toString())) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(path, path);
                editor.apply();
                Toast.makeText(getApplicationContext(), R.string.congratulations, Toast.LENGTH_SHORT).show();
                gameFinished = true;
            }
            return true;
        }
    }

    public String parseUnicodeString(String source) {
        // parsing %HH%HH codes of Unicode symbols doesn't yield any good results
        // so the method is commented out for now

        /*Pattern pattern = Pattern.compile("%([0-9A-Z][0-9A-Z])%([0-9A-Z][0-9A-Z])");
        Matcher matcher = pattern.matcher(source);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String char1 = matcher.group(1);
            String char2 = matcher.group(2);
            String unEncoded = String.valueOf(Character.toChars(Integer.parseInt(char1 + char2, 16)));
            matcher.appendReplacement(sb, new String(unEncoded.getBytes(), Charset.forName("UTF-8")));
        }
        matcher.appendTail(sb);
        return sb.toString();*/
        return source;
    }

    public void showOverlay() {
        topSlide.bringToFront();
        topSlide.startAnimation(Animations.inFromLeftAnimation());
        topSlide.setVisibility(View.VISIBLE);

        bottomSlide.bringToFront();
        bottomSlide.startAnimation(Animations.inFromRightAnimation());
        bottomSlide.setVisibility(View.VISIBLE);
    }

    public void hideOverlay() {
        if (topSlide.getVisibility() != View.INVISIBLE) {
            topSlide.bringToFront();
            topSlide.startAnimation(Animations.outToLeftAnimation());
            topSlide.setVisibility(View.INVISIBLE);
        }

        if (bottomSlide.getVisibility() != View.INVISIBLE) {
            bottomSlide.bringToFront();
            bottomSlide.startAnimation(Animations.outToRightAnimation());
            bottomSlide.setVisibility(View.INVISIBLE);
        }

        getSupportActionBar().hide();
    }
}
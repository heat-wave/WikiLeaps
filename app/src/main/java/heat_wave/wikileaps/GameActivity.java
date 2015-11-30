package heat_wave.wikileaps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import heat_wave.wikileaps.utils.Animations;
import heat_wave.wikileaps.utils.Difficulty;
import heat_wave.wikileaps.utils.Helper;
import heat_wave.wikileaps.utils.OnSwipeTouchListener;


public class GameActivity extends AppCompatActivity {


    public static final String DIFFICULTY = "difficulty";
    public static final String TAG = "wiki_leaps";

    private Difficulty difficulty;
    private String path;
    private SharedPreferences sharedPreferences;
    private boolean gameFinished;
    private int secondsToOverlayHide = 6;
    private TextView topSlide;
    private TextView bottomSlide;
    private Handler timeHandler;
    private String startingPage;
    private WebView webView;
    private LinearLayout trans;
    private boolean firstUse;
    private boolean firstBackTap;
    private MenuItem leapCounter;
    private int leaps;
    private Menu menuReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        firstUse = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        trans = (LinearLayout)findViewById(R.id.pseudoblur);
        Toolbar gameToolbar = (Toolbar) findViewById(R.id.game_toolbar);
        setSupportActionBar(gameToolbar);
        try {
            getSupportActionBar().setTitle(R.string.app_name);
        } 
        catch (NullPointerException e) {
            Log.e(TAG, "Failed to interact with action bar: " +  e.getMessage());
        }

        Helper.init(this);

        topSlide = (TextView) findViewById(R.id.topSlide);
        bottomSlide = (TextView) findViewById(R.id.bottomSlide);

        Intent intent = getIntent();
        difficulty = (Difficulty) intent.getSerializableExtra(DIFFICULTY);
        sharedPreferences = this.getSharedPreferences("WIKI_LEAPS", Context.MODE_PRIVATE);
        gameFinished = false;
        path = "";
        leaps = 0;
        webView = (WebView) findViewById(R.id.wiki);
        webView.setWebViewClient(new TrackingWebViewClient());
        webView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeBottom() {
                getSupportActionBar().show();
                secondsToOverlayHide = 6;
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

        showOverlay();
        timeHandler = new Handler();
        startTimeTracking();
    }

    Runnable intervalChecker = new Runnable() {
        @Override
        public void run() {

            if (secondsToOverlayHide > 0) {
                secondsToOverlayHide--;
                if (secondsToOverlayHide == 1) {
                    firstBackTap = true;
                    hideOverlay();
                }
            }
            if (topSlide.getText().length() == 0 && !webView.getUrl().equals("https://en.m.wikipedia.org/wiki/Special:Random")) {
                bottomSlide.setText(parseUnicodeString(difficulty.toString()).replace('_', ' '));
                String url = parseUnicodeString(webView.getUrl());
                topSlide.setText(url.substring(url.lastIndexOf('/') + 1).replace('_', ' '));
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
    public void onBackPressed() {
        if (!firstBackTap) {
            super.onBackPressed();
        }
        else {
            Toast.makeText(this, "Press again to leave this game", Toast.LENGTH_SHORT).show();
            try {
                getSupportActionBar().show();
            }
            catch (NullPointerException e) {
            Log.e(TAG, "Failed to interact with action bar: " +  e.getMessage());
        }
            secondsToOverlayHide = 6;
            firstBackTap = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);
        this.menuReference = menu;
        leapCounter = menuReference.findItem(R.id.leaps);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_replay:
                //if (!path.contains(" -> "));
                webView.loadUrl(startingPage);
                String url = parseUnicodeString(webView.getUrl());
                path = url.substring(url.lastIndexOf('/') + 1).replace('_', ' ');
                firstUse = true;
                showOverlay();
                leaps = 0;
                leapCounter.setTitle(Integer.toString(leaps));
                return true;

            case R.id.action_help:
                showOverlay();
                secondsToOverlayHide = 6;
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class TrackingWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (path.contains(url) || (!url.contains("m.wikipedia.org/wiki/")) ||
                    (url.contains("Special") && !url.contains("Random")) ||
                    (url.contains("File:"))) {
                Toast.makeText(getApplicationContext(), R.string.no_way_out, Toast.LENGTH_SHORT).show();
                return true;
            }
            view.loadUrl(url);
            url = parseUnicodeString(view.getUrl());

            if (path.length() == 0) {
                startingPage = url;
                leaps = 0;
            }
            else {
                leaps++;
            }
            leapCounter.setTitle(Integer.toString(leaps));

            path = path + (path.length() > 0 ? " \u21d2 " : "") + (url.substring(url.lastIndexOf('/') + 1)).replace('_', ' ');
            if (!gameFinished && path.contains(difficulty.toString())) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(Helper.getRunsCount(), path);
                editor.apply();
                Toast.makeText(getApplicationContext(), R.string.congratulations, Toast.LENGTH_SHORT).show();
                gameFinished = true;
            }
            return true;
        }
    }

    public String parseUnicodeString(String source) {
        try {
            source = URLDecoder.decode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return source;
    }

    public void showOverlay() {
        topSlide.bringToFront();
        if (!firstUse)
            topSlide.startAnimation(Animations.inFromLeftAnimation());
        topSlide.setVisibility(View.VISIBLE);

        bottomSlide.bringToFront();
        if (!firstUse)
            bottomSlide.startAnimation(Animations.inFromRightAnimation());
        bottomSlide.setVisibility(View.VISIBLE);

        trans.startAnimation(Animations.fadeInAnimation());
        trans.setVisibility(View.VISIBLE);

        firstUse = false;
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
        if (trans.getVisibility() != View.INVISIBLE) {
            trans.startAnimation(Animations.fadeOutAnimation());
            trans.setVisibility(View.INVISIBLE);
        }
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to interact with action bar: " +  e.getMessage());
        }
    }
}
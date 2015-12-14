package heat_wave.wikileaps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

import heat_wave.wikileaps.utils.Animations;
import heat_wave.wikileaps.utils.Difficulty;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MenuActivity extends Activity {

    public static final String DIFFICULTY = "difficulty";
    public static final String TAG = "wiki_leaps";
    private ViewFlipper flipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        flipper = (ViewFlipper)findViewById(R.id.flipper);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(TAG, "Switched to landscape");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d(TAG, "Switched to portrait");
        }
    }

    public void startGame(View v) {
        String mode = ((Button)v).getText().toString();
        Intent intent = new Intent(this, GameActivity.class);
        Difficulty difficulty = null;
        switch (mode) {
            case "Easy":
                difficulty = Difficulty.EASY;
                break;
            case "Medium":
                difficulty = Difficulty.MEDIUM;
                break;
            case "Hard":
                difficulty = Difficulty.HARD;
                break;
        }
        intent.putExtra(DIFFICULTY, difficulty);
        startActivity(intent);
    }

    public void showHighscore(View v) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void flipForward(View v) {
        flipper.setInAnimation(Animations.inFromRightAnimation());
        flipper.setOutAnimation(Animations.outToLeftAnimation());
        flipper.showNext();
    }

    public void flipBackward(View v) {
        flipper.setInAnimation(Animations.inFromLeftAnimation());
        flipper.setOutAnimation(Animations.outToRightAnimation());
        flipper.showPrevious();
    }
}


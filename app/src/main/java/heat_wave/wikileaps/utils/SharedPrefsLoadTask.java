package heat_wave.wikileaps.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

/**
 * Created by heat_wave on 12/15/15.
 */
public class SharedPrefsLoadTask extends AsyncTask<Context, Void, SharedPreferences> {
    @Override
    protected SharedPreferences doInBackground(Context... params) {
        return params[0].getSharedPreferences("WIKI_LEAPS",
                Context.MODE_PRIVATE);
    }
}
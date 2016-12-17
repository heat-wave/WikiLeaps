package heat_wave.wikileaps.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;


/**
 * Created by heat_wave on 11/16/15.
 */
public class Helper {
    private static ArrayList<String> paths;
    private static SharedPreferences preferences;

    private static final String TAG = "wiki_leaps";
    private Helper() {
    }

    public static void initSharedPrefs(final Context newContext) {
        try {
            preferences = new SharedPrefsLoadTask().execute(newContext).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static void setContext(Context newContext) {
        if (preferences == null) {
            initSharedPrefs(newContext);
        }
        paths = new ArrayList<>();
        TreeMap<String, String> ordered = new TreeMap<>(Collections.reverseOrder());
        for (Map.Entry<String, ?> entry : preferences.getAll().entrySet()) {
            ordered.put(entry.getKey(), (String) entry.getValue());
        }
        paths.addAll(ordered.values());
    }

    @NonNull
    public static String getRunsCount() {
        return Integer.toString(preferences.getAll().size());
    }

    public static int getCount() {
        return paths.size();
    }

    public static String getHistoryAt(int position) {
        return paths.get(position);
    }

    public static String parseUnicodeString(String source) {
        try {
            source = URLDecoder.decode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return source;
    }

    public static String encodeUnicodeString(String source) {
        try {
            source = URLEncoder.encode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return source;
    }

    static SharedPreferences getPreferences() {
        return preferences;
    }

}

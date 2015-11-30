package heat_wave.wikileaps.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created by heat_wave on 11/16/15.
 */
public class Helper {
    private static ArrayList<String> paths;
    private static SharedPreferences preferences;

    private Helper() {}

    public static void init(Context newContext){
        preferences = newContext.getSharedPreferences("WIKI_LEAPS", Context.MODE_PRIVATE);
    }

    public static void setContext(Context newContext) {
        if (preferences == null) {
            init(newContext);
        }
        paths = new ArrayList<>();
        TreeMap<String, String> ordered = new TreeMap<>(Collections.reverseOrder());
        for (Map.Entry<String, ?> entry : preferences.getAll().entrySet()) {
            ordered.put(entry.getKey(), (String)entry.getValue());
        }
        paths.addAll(ordered.values());
    }

    public static String getRunsCount() {
        return Integer.toString(preferences.getAll().size());
    }

    public static int getCount() {
        return paths.size();
    }

    public static String getHistory(int position) {
        return paths.get(position);
    }

}

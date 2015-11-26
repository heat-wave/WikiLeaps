package heat_wave.wikileaps.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Map;


/**
 * Created by heat_wave on 11/16/15.
 */
public class Helper {
    private static Context context;
    private static ArrayList<String> paths;

    public Helper() {}

    public static void setContext(Context newContext) {
        context = newContext;
        SharedPreferences preferences = getSharedPreferences(context);
        paths = new ArrayList<>();
        for (Map.Entry<String, ?> entry : preferences.getAll().entrySet()) {
            paths.add((String)entry.getValue());
        }
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("WIKI_LEAPS", Context.MODE_PRIVATE);
    }

    public static int getCount() {
        return paths.size();
    }

    public static String getHistory(int position) {
        return paths.get(position);
    }

}

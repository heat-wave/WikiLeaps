package heat_wave.wikileaps.utils;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Created by heat_wave on 17.12.16.
 */

/*
    Actually not working due to WikiDistrict's page structure
 */
public class ShortestPathLoadTask extends AsyncTask<String, Void, String> {
    private Document doc;

    @Override
    protected String doInBackground(String... pages) {
        try {
            doc = Jsoup.connect("http://wikidistrict.com/p/" + Helper.encodeUnicodeString(pages[0])
                    + '/' + Helper.encodeUnicodeString(pages[1])).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        Element path = doc.select("div.path-result").first();
    }
}
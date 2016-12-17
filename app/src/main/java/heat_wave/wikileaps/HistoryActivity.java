package heat_wave.wikileaps;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import heat_wave.wikileaps.utils.Helper;
import heat_wave.wikileaps.utils.ui.HistoryRecyclerAdapter;
import heat_wave.wikileaps.utils.ui.HistorySelectedListener;
import heat_wave.wikileaps.utils.ui.RecyclerDividersDecorator;

public class HistoryActivity extends Activity implements HistorySelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Helper.setContext(this);
        setContentView(R.layout.activity_high_score);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecyclerDividersDecorator(Color.BLACK));
        HistoryRecyclerAdapter adapter = new HistoryRecyclerAdapter(this);
        adapter.setHistorySelectedListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onHighscoreSelected(String path) {
        Toast.makeText(this, path, Toast.LENGTH_LONG).show();
    }
}

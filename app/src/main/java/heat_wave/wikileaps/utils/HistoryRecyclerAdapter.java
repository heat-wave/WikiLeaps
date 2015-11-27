package heat_wave.wikileaps.utils;

/**
 * Created by heat_wave on 11/16/15.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import heat_wave.wikileaps.R;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.HighscoreViewHolder>
        implements View.OnClickListener {

    private final LayoutInflater layoutInflater;
    private HistorySelectedListener HistorySelectedListener;

    public HistoryRecyclerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setHistorySelectedListener(HistorySelectedListener listener) {
        HistorySelectedListener = listener;
    }

    @Override
    public HighscoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_highscore, parent, false);
        view.setOnClickListener(this);
        return new HighscoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HighscoreViewHolder holder, int position) {
        String path = Helper.getHistory(position);
        holder.highscoreNameView.setText(path.substring(0, path.indexOf('\u21d2')) +
            path.substring(path.lastIndexOf('\u21d2')));
        holder.itemView.setTag(R.id.tag_highscore, path);
    }

    @Override
    public int getItemCount() {
        return Helper.getCount();
    }

    @Override
    public void onClick(View v) {
        String highscore = (String) v.getTag(R.id.tag_highscore);
        if (HistorySelectedListener != null && highscore != null) {
            HistorySelectedListener.onHighscoreSelected(highscore);
        }
    }

    static class HighscoreViewHolder extends RecyclerView.ViewHolder {
        final TextView highscoreNameView;

        public HighscoreViewHolder(View itemView) {
            super(itemView);
            highscoreNameView = (TextView) itemView.findViewById(R.id.highscore_title);
        }
    }
}
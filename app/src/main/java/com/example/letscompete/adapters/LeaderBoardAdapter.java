package com.example.letscompete.adapters;

import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.letscompete.R;
import com.example.letscompete.UserLeaderBoardStats;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder> {
    private List<UserLeaderBoardStats> rankings;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView rank;
        private final TextView user;
        private final TextView stat;
        private final Image profilePic;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            rank = (TextView) view.findViewById(R.id.RankLeader);
            user = (TextView) view.findViewById(R.id.UserLeader);
            stat = (TextView) view.findViewById(R.id.numberLeader);
            profilePic = null;
        }

        public TextView getRank() {
            return rank;
        }

        public TextView getUser() {
            return user;
        }

        public TextView getStat() {
            return stat;
        }
    }

    public LeaderBoardAdapter(List<UserLeaderBoardStats> rankings) {
        this.rankings = new ArrayList<>();
        this.rankings.addAll(rankings);
        Log.i("calling", "ok");
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_leaderboard, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Log.i("position", position + "");
        viewHolder.getRank().setText("Ranking: " + rankings.get(position).getRank() + "");
        viewHolder.getUser().setText(rankings.get(position).getUsername());
        viewHolder.getStat().setText(rankings.get(position).getStat());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return rankings.size();
    }

}


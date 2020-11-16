package com.example.letscompete.adapters;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.letscompete.R;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder> {
    private String[] localDataSet;
    private String[] desc;
    private Image[] images;

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
            stat = (TextView) view.findViewById(R.id.statLeader);
            profilePic = null;
        }

        public TextView getTextView() {
            return rank;
        }

        public TextView getSub() {
            return user;
        }

    }

    public LeaderBoardAdapter(String[] dataSet, String[] des) {
        localDataSet = dataSet;
        desc = des;
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
        viewHolder.getTextView().setText(localDataSet[position]);
        viewHolder.getSub().setText(desc[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }

}


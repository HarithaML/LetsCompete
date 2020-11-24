package com.example.letscompete.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letscompete.R;
import com.example.letscompete.models.ModelChallenge;

import java.util.List;

public class AdapterChallengesLeaderboard extends RecyclerView.Adapter<AdapterChallengesLeaderboard.MyHolder> {

    List<ModelChallenge> modelChallengeList;

    public AdapterChallengesLeaderboard(List<ModelChallenge> modelChallengeList) {
        this.modelChallengeList = modelChallengeList;
    }



    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_leaderboard_challenges, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //get data
        String challengeTitle = modelChallengeList.get(position).getChallengeTitle();
        String challengeDescription = modelChallengeList.get(position).getChallengeDescription();
        String challengeType = modelChallengeList.get(position).getChallengeType();
        String startdate = modelChallengeList.get(position).getStartdate();
        //set data
        holder.mTitle.setText(challengeTitle);
        Log.i("help", "no input");

    }

    @Override
    public int getItemCount() {
        return modelChallengeList.size();
    }

    //view Holder class
    class MyHolder extends RecyclerView.ViewHolder{
        TextView mTitle, mDescription;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.textView5);

        }
    }
}

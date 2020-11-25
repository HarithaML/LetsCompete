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
import com.example.letscompete.UserLeaderBoardChallenges;
import com.example.letscompete.activities.DashBoardActivity;
import com.example.letscompete.activities.Setting_Activity;
import com.example.letscompete.models.ModelChallenge;

import java.util.List;

public class AdapterChallengesLeaderboard extends RecyclerView.Adapter<AdapterChallengesLeaderboard.MyHolder> {

    private Context mContext;
    List<UserLeaderBoardChallenges> modelChallengeList;

    public AdapterChallengesLeaderboard(List<UserLeaderBoardChallenges> modelChallengeList, Context context) {
        this.modelChallengeList = modelChallengeList;
        mContext = context;
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
        String challengeTitle = modelChallengeList.get(position).getChallengename();
        /*
        String challengeDescription = modelChallengeList.get(position).getChallengeDescription();
        String challengeType = modelChallengeList.get(position).getChallengeType();
        String startdate = modelChallengeList.get(position).getStartdate();
        */

        //set data
        holder.mTitle.setText(challengeTitle);
        Log.i("help", "no input");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContext instanceof DashBoardActivity) {
                    ((DashBoardActivity) mContext).onChallengeSelected(modelChallengeList.get(position).getChallengename());
                }
            }
        });
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

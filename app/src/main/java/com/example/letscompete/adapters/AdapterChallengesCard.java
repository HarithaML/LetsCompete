package com.example.letscompete.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.letscompete.R;
import com.example.letscompete.activities.activityBasedChallenge.ActivityBasedChallengeActivity;
import com.example.letscompete.activities.customBasedChallenge.CustomBasedChallengeActivity;
import com.example.letscompete.activities.scoreBasedChallenge.ScoreBasedChallengeActivity;
import com.example.letscompete.activities.timeBasedChallenge.TimeBasedChallengeActivity;
import com.example.letscompete.models.ModelChallenge;

import java.util.List;

public class AdapterChallengesCard extends RecyclerView.Adapter<AdapterChallengesCard.MyHolder> {
    Context context;
    List<ModelChallenge> challengeList;
    String username;

    public AdapterChallengesCard(Context context, List<ModelChallenge> challengeList,String username){
        this.context = context;
        this.challengeList = challengeList;
        this.username = username;
    }


    @NonNull
    @Override
    public AdapterChallengesCard.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_challenges,parent,false);
        return new AdapterChallengesCard.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterChallengesCard.MyHolder holder, int position) {
        //get data
        String userID = challengeList.get(position).getUserID();
        String challengeTitle = challengeList.get(position).getChallengeTitle();
        String challengeDescription = challengeList.get(position).getChallengeDescription();
        String challengeType = challengeList.get(position).getChallengeType();
//        String challengeType = challengeList.get(position).getChallengeType();
//        String challengeDuration = challengeList.get(position).getChallengeDuration();
//        String startdate = challengeList.get(position).getStartdate();
//        String imageName = challengeList.get(position).getImageName();
//        String imageURL = challengeList.get(position).imageURL;

        //set data
        holder.challengeTitle.setText(challengeTitle);
        holder.challengeDescription.setText(challengeDescription);
//        holder.challengeType.setText(challengeType);
//        holder.challengeDuration.setText(challengeDuration);
//        holder.challengeStartdate.setText(startdate);
//        holder.challengeImage.setText(imageName);


        //click challenge item on Home fragment
        holder.itemView.setOnClickListener(v -> {
            if(holder.challengeType.toString()=="Activity based") {
                Intent intent = new Intent(context, ActivityBasedChallengeActivity.class);
                intent.putExtra("challengeTitle", challengeTitle);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return challengeList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView challengeTitle, challengeDescription, challengeDuration, challengeImage, challengeType, challengeStartdate;
        LinearLayout challenge;
        public MyHolder(View itemView){
            super(itemView);
            challenge = itemView.findViewById(R.id.challengeLayout);
            challengeTitle = itemView.findViewById(R.id.card_challenge_title);
            challengeDescription = itemView.findViewById(R.id.card_challenge_description);
            challengeDuration = itemView.findViewById(R.id.challenge_duration);
            challengeType = itemView.findViewById(R.id.challenge_type);
            challengeStartdate = itemView.findViewById(R.id.challenge_startDate);
            challengeImage = itemView.findViewById(R.id.challenge_image);
            challenge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(challengeType.getText().toString()=="Activity based") {
                        Intent intent = new Intent(context, ActivityBasedChallengeActivity.class);
                        intent.putExtra("challengeTitle",challengeTitle.getText());
                        intent.putExtra("username",username);
                        context.startActivity(intent);
                    }else if(challengeType.getText().toString()=="Score based"){
                        Intent intent = new Intent(context, ScoreBasedChallengeActivity.class);
                        intent.putExtra("challengeTitle",challengeTitle.getText());
                        intent.putExtra("username",username);
                        context.startActivity(intent);
                    }else if(challengeType.getText().toString()=="Time based"){
                        Intent intent = new Intent(context, TimeBasedChallengeActivity.class);
                        intent.putExtra("challengeTitle",challengeTitle.getText());
                        intent.putExtra("username",username);
                        context.startActivity(intent);
                    }else{
                        Intent intent = new Intent(context, CustomBasedChallengeActivity.class);
                        intent.putExtra("challengeTitle",challengeTitle.getText());
                        intent.putExtra("username",username);
                        context.startActivity(intent);
                    }

                }
            });

        }
    }
}

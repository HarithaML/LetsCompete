package com.example.letscompete.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letscompete.activities.*;
import com.example.letscompete.models.ModelChallenge;
import com.example.letscompete.models.ModelUser;
import com.example.letscompete.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterChallenges extends RecyclerView.Adapter<AdapterChallenges.MyHolder> {

    Context context;
    List<ModelChallenge> challengeList;

    public AdapterChallenges(Context context, List<ModelChallenge> challengeList) {
        this.context = context;
        this.challengeList = challengeList;
    }



    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_challenges, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //get data
        String challengeTitle = challengeList.get(position).getChallengeTitle();
        String challengeDescription = challengeList.get(position).getChallengeDescription();
        String challengeType = challengeList.get(position).getChallengeType();
        String startdate = challengeList.get(position).getStartdate();
        //set data


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    //view Holder class
    class MyHolder extends RecyclerView.ViewHolder{
        TextView mTitle, mDescription;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}

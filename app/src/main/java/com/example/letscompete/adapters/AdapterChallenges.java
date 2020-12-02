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

import com.example.letscompete.R;
import com.example.letscompete.activities.JoinDetailActivity;
import com.example.letscompete.models.ModelChallenge;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterChallenges extends RecyclerView.Adapter<AdapterChallenges.MyHolder> {
    Context context;
    List<ModelChallenge> challengeList;
    ModelChallenge model;

    public AdapterChallenges(Context context, List<ModelChallenge> challengeList){
        this.context = context;
        this.challengeList = challengeList;
    }


    @NonNull
    @Override
    public AdapterChallenges.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.challenges_row,parent,false);
        return new AdapterChallenges.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterChallenges.MyHolder holder, int position) {
        //get data

        String ChalName = challengeList.get(position).getChallengeTitle();
        String ChalDuration = challengeList.get(position).getChallengeDuration();
        String imageURL = challengeList.get(position).imageURL;
        String challengeDescription = challengeList.get(position).getChallengeDescription();




        holder.ChalName.setText(ChalName);
        holder.ChalDuration.setText(ChalDuration);
        holder.ChalDesc.setText(challengeDescription);
        Picasso.get().load(imageURL).resize(0,376).into(holder.img1);


        //click challenge item on Home fragment

        holder.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AppCompatActivity activity =(AppCompatActivity)view.getContext();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content,new ChallengeDescriptionFragment(ChalName,ChalDuration,challengeDescription,imageURL)).addToBackStack(null).commit();
                Intent intent = new Intent(context, JoinDetailActivity.class);
                intent.putExtra("challengeTitle",ChalName);
                intent.putExtra("challengeDuration", ChalDuration);
                intent.putExtra("challengeDescription",challengeDescription);
                intent.putExtra("challengeImageURL",imageURL);
                context.startActivity(intent);
            }
        });


    }



    @Override
    public int getItemCount() {
        return challengeList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView img1;
        TextView ChalName,ChalDuration,ChalDesc;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            ChalName =itemView.findViewById(R.id.nametext);
            ChalDuration =itemView.findViewById(R.id.durationtext);
            ChalDesc =itemView.findViewById(R.id.desctext);
            img1 =(ImageView)itemView.findViewById(R.id.img1);
        }
    }
}



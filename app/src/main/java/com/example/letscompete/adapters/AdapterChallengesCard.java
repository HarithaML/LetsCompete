package com.example.letscompete.adapters;

import android.content.ContentValues;
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
import com.example.letscompete.models.ModelChallenge;

import java.util.List;

public class AdapterChallengesCard extends RecyclerView.Adapter<AdapterChallengesCard.MyHolder> {
    Context context;
    List<ModelChallenge> challengeList;

    public AdapterChallengesCard(Context context, List<ModelChallenge> challengeList){
        this.context = context;
        this.challengeList = challengeList;
    }


    @NonNull
    @Override
    public AdapterChallengesCard.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_challenges,parent,false);
        return new AdapterChallengesCard.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterChallengesCard.MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView participantImage;
        TextView userName, progress,role,rank,status;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
//            System.out.println(itemView.findViewById(R.id.avatar_tv));
            //init Views
            participantImage = itemView.findViewById(R.id.participant_image);
            userName = itemView.findViewById(R.id.participant_name);
            progress = itemView.findViewById(R.id.progress);
            role = itemView.findViewById(R.id.role);
            rank = itemView.findViewById(R.id.rank);
            status = itemView.findViewById(R.id.status);


        }
    }
}

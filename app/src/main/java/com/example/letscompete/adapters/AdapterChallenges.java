package com.example.letscompete.adapters;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.letscompete.Challengesfordisplay;
import com.example.letscompete.R;
import com.example.letscompete.activities.Challenge;
import com.example.letscompete.fragments.ChallengeslistFragment;
import com.example.letscompete.fragments.chaldescriptionFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;

import com.example.letscompete.R;
import com.example.letscompete.models.ModelChallenge;




public class AdapterChallenges extends FirebaseRecyclerAdapter<Challengesfordisplay,AdapterChallenges.myviewholder> {
    private AppCompatActivity activity;
    public AdapterChallenges(@NonNull FirebaseRecyclerOptions<Challengesfordisplay> options) {
        super(options);


    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull Challengesfordisplay model) {
        holder.ChalName.setText(model.gettitle());
        holder.ChalDuration.setText(model.getduration());
        holder.ChalDesc.setText(model.getdescription());
        //DataSnapshot ds =new DataSnapshot(); snapshot.getChildren()
        //Picasso.get().load(model.getimageurl()).into(holder.img1);
        Picasso.get().load(model.getimageurl()).into(holder.img1);
        // Glide.with(holder.img1.getContext()).load(model.getimageurl()).into(holder.img1);

        holder.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity =(AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content,new chaldescriptionFragment(model.gettitle(),model.getduration(),model.getdescription(),model.getimageurl())).addToBackStack(null).commit();
            }
        });


    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.challenges_row,parent,false);
        return new myviewholder(view);


    }

    public class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img1;
        TextView ChalName,ChalDuration,ChalDesc;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            ChalName =itemView.findViewById(R.id.nametext);
            ChalDuration =itemView.findViewById(R.id.durationtext);
            ChalDesc =itemView.findViewById(R.id.desctext);
            img1 =(ImageView)itemView.findViewById(R.id.img1);
        }
    }

}

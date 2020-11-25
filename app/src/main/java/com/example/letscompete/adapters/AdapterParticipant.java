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
import com.example.letscompete.activities.ChatActivity;
import com.example.letscompete.fragments.ProfileFragment;
import com.example.letscompete.models.ModelParticipant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterParticipant extends RecyclerView.Adapter<AdapterParticipant.MyHolder>{
    Context context;
    List<ModelParticipant> participantList;

    public AdapterParticipant(Context context, List<ModelParticipant> participantList) {
        this.context = context;
        this.participantList = participantList;
    }

    @NonNull
    @Override
    public AdapterParticipant.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout(row_user.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.row_participants, parent, false);

        return new AdapterParticipant.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterParticipant.MyHolder holder, int position) {
        //get data
        String userUID = participantList.get(position).getUserUID();
        String userImage = participantList.get(position).getUserImage();
        String userName = participantList.get(position).getUserName();
        String progress = participantList.get(position).getProgress();
        String role = participantList.get(position).getRole();
        String rank = participantList.get(position).getRank();
        String challengeTitle = participantList.get(position).getChallengeTitle();
        String status = participantList.get(position).getStatus();

        //set data
        holder.userName.setText(userName);
        holder.progress.setText(progress);
        holder.role.setText(role);
        holder.rank.setText(rank);
        holder.status.setText(status);
        try{
            Picasso.get().load(userImage)
                    .placeholder(R.drawable.ic_default_img_black)
                    .into(holder.participantImage);
        }catch (Exception e){

        }
        // handle item click
        holder.itemView.setOnClickListener(v -> {
//            /*Click user from useer list to start chatting/messaging
//             * Start Activity by putting UID of reciever
//             * we will use that UID to identify the user we are gonna chat*/
//            Intent intent = new Intent(context, ProfileFragment.class);
//            intent.putExtra("userUID",userUID);
//            context.startActivity(intent);

        });


    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }

    //view Holder class
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

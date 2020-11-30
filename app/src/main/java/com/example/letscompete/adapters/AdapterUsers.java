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
import com.example.letscompete.models.ModelUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder>{

    Context context;
    List<ModelUser> userList;

    public AdapterUsers(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout(row_user.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.row_users, parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //get data
        String hisUID = userList.get(position).getUid();
        String userImage = userList.get(position).getImage();
        String userName = userList.get(position).getName();
        String userEmail = userList.get(position).getEmail();

        //set data
        holder.mNameTv.setText(userName);
        holder.mEmailTv.setText(userEmail);
        try{
            Picasso.get().load(userImage)
                    .placeholder(R.drawable.ic_default_img_black)
                    .into(holder.mAvatarTv);
        }catch (Exception e){

        }
        // handle item click
        holder.itemView.setOnClickListener(v -> {
           /*Click user from useer list to start chatting/messaging
           * Start Activity by putting UID of reciever
           * we will use that UID to identify the user we are gonna chat*/
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("hisUID",hisUID);
            context.startActivity(intent);

        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    //view Holder class
    class MyHolder extends RecyclerView.ViewHolder{
        ImageView mAvatarTv;
        TextView mNameTv, mEmailTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
//            System.out.println(itemView.findViewById(R.id.avatar_tv));
            //init Views
            mAvatarTv = itemView.findViewById(R.id.avatar_tv);
            mNameTv = itemView.findViewById(R.id.name_tv);
            mEmailTv = itemView.findViewById(R.id.email_tv);

        }
    }
}

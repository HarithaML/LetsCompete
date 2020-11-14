package com.example.letscompete.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letscompete.models.ModelChat;
import com.example.letscompete.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder>{

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    Context context;
    List<ModelChat> chatList;
    String imageUrl;
    FirebaseUser fUser;

    public AdapterChat(Context context, List<ModelChat> chatList, String imageUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if(viewType == MSG_TYPE_RIGHT){
           View view = LayoutInflater.from(context).inflate(R.layout.row_chat_sender,parent,false);
            return new MyHolder(view);
       }else{
           View view = LayoutInflater.from(context).inflate(R.layout.row_chat_reciever,parent,false);
           return new MyHolder(view);
       }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String message = chatList.get(position).getMessage();
        String timeStamp = chatList.get(position).getTimestamp();

        //convert timestamp to dd/mm/yyyy hh:mm am/pm
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String dateTime = DateFormat.format("dd/MM/yyyy HH:mm:ss",cal).toString();

        //set data
        holder.messageTv.setText(message);
        holder.timeTv.setText(dateTime);
        try {
            Picasso.get().load(imageUrl).into(holder.profileTv);
        } catch (Exception e) {

        }

        //set seen/delivered status of message
        if (position == chatList.size()-1) {
            System.out.println(chatList.get(position).isSeen());
            if (chatList.get(position).isSeen()) {
                holder.isSeenTv.setText("Seen");
            } else {
                holder.isSeenTv.setText("delivered");
            }
        } else {
            holder.isSeenTv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatList.get(position).getSender().equals(fUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }

    //view Holder class
    class MyHolder extends RecyclerView.ViewHolder{

        ImageView profileTv;
        TextView messageTv,timeTv,isSeenTv;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //init Views

            profileTv = itemView.findViewById(R.id.profileTv);
            messageTv = itemView.findViewById(R.id.messageTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            isSeenTv = itemView.findViewById(R.id.isSeenTv);


        }
    }
}

package com.example.letscompete.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letscompete.R;
import com.example.letscompete.models.ModelImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterImage extends RecyclerView.Adapter<AdapterImage.HolderImage>{

    private Context context;
    private ArrayList<ModelImage> imageArrayList;


    public AdapterImage(Context context, ArrayList<ModelImage> imageArrayList) {
        this.context = context;
        this.imageArrayList = imageArrayList;
    }

    @NonNull
    @Override
    public HolderImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row_video.xml
        context= parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_image, parent, false);
        return new HolderImage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderImage holder, int position) {
        //get data
        ModelImage modelImage = imageArrayList.get(position);
        String challengeName = modelImage.getChallengeTitle();
        String imageUri = modelImage.getImageUrl();
        String userUID = modelImage.getUserUid();

        setVideoUrl(modelImage,holder);

    }

    private void setVideoUrl(ModelImage modelImage, HolderImage holder) {

        holder.textView.setText("User:"+" "+"Challenge:"+modelImage.getChallengeTitle());
        try{
            //if image is recieved then set
            Picasso.get().load(modelImage.getImageUrl()).resize(372, 200).into(holder.imageView);

        }catch(Exception e){
            //if there is any exception in getting image
            Picasso.get().load(R.drawable.ic_default_img_black).into(holder.imageView);
        }




    }

    @Override
    public int getItemCount() {
        return imageArrayList.size();
    }

    class HolderImage extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public HolderImage(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_profile_view);
            textView = itemView.findViewById(R.id.challengeTitle);


        }


    }


}

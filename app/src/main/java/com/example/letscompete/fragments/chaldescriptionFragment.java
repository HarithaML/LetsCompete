package com.example.letscompete.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import com.bumptech.glide.Glide;
import com.example.letscompete.R;
import com.example.letscompete.activities.DashBoardActivity;
import com.example.letscompete.activities.MainActivity;
import com.squareup.picasso.Picasso;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link chaldescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class chaldescriptionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String title, duration, description, imageurl;
    private AppCompatActivity activity;
    private final static String TAG_FRAGMENT = "TAG_FRAGMENT";

    public chaldescriptionFragment() {
        // Required empty public constructor
    }

    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment chaldescriptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public chaldescriptionFragment(String title, String duration, String description, String imageurl) {

        this.title = title;
        this.duration = duration;
        this.description = description;
        this.imageurl = imageurl;


    }

    public static chaldescriptionFragment newInstance(String param1, String param2) {
        chaldescriptionFragment fragment = new chaldescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_chaldescription, container, false);
        TextView name = view.findViewById(R.id.challengetitle);
        TextView ChalDuration = view.findViewById(R.id.duration);
        TextView ChalDescription = view.findViewById(R.id.description);
        ImageView imageholder = view.findViewById(R.id.imagegholder);


        name.setText(title);
        ChalDuration.setText(duration);
        ChalDescription.setText(description);

        Picasso.get().load(imageurl).into(imageholder);
        //Picasso.get().load(imageUri).into(imageholder);

        return view;

    }

}
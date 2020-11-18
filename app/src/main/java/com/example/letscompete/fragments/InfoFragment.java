package com.example.letscompete.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.letscompete.R;
import com.example.letscompete.models.ModelChallenge;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {
    ImageView mImage;
    TextView mTitle,mDescription,mType,mDuration,mStartDate;

    DatabaseReference databaseReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        mImage = view.findViewById(R.id.challenge_image);
        mTitle = view.findViewById(R.id.challenge_title);
        mDescription = view.findViewById(R.id.challenge_description);
        mDuration = view.findViewById(R.id.challenge_duration);
        mStartDate = view.findViewById(R.id.challenge_startDate);
        mType = view.findViewById(R.id.challenge_type);
        databaseReference = FirebaseDatabase.getInstance().getReference("/Challenge");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("Database Reached");
                for(DataSnapshot ds:  snapshot.getChildren()){
                    ModelChallenge modelChallenge = ds.getValue(ModelChallenge.class);
//                    if(modelChallenge.getChallengeTitle() != null){
                        System.out.println("yes");
                        if(modelChallenge.getChallengeTitle().equals("test")){
                            System.out.println(modelChallenge.getChallengeTitle());
                            mTitle.setText(modelChallenge.getChallengeTitle());
                            mDescription.setText(modelChallenge.getChallengeDescription());
                            mDuration.setText(modelChallenge.getChallengeDuration());
                            mStartDate.setText(modelChallenge.getStartdate());
                            mType.setText(modelChallenge.getChallengeType());
                            try{
                                Picasso.get().load(modelChallenge.getImageURL())
                                        .placeholder(R.drawable.ic_default_img_black)
                                        .into(mImage);
                            }catch (Exception e){
                                Picasso.get().load(R.drawable.ic_default_img_black)
                                        .into(mImage);
                            }

                        }
//                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void setInfo() {

    }
}
package com.example.letscompete.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.letscompete.R;
import com.example.letscompete.activities.DashBoardActivity;
import com.example.letscompete.activities.MainActivity;
import com.example.letscompete.activities.TimeChallengeActivity;
import com.example.letscompete.models.ModelChallenge;
import com.example.letscompete.models.ModelParticipant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

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
    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String challengeTitle;

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
        if (getArguments() != null && getArguments().containsKey("challengeTitle")) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            challengeTitle = getArguments().getString("challengeTitle");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        //get challengeTitle from TimeChallengeActivity.java
        TimeChallengeActivity timeChallengeActivity = (TimeChallengeActivity)getActivity();
        challengeTitle = timeChallengeActivity.getChallengeTitle();
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_info, null);
        //View view = inflater.inflate(R.layout.fragment_info, container, false);
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
                //System.out.println("Database Reached");
                for(DataSnapshot ds:  snapshot.getChildren()){
                    ModelChallenge modelChallenge = ds.getValue(ModelChallenge.class);
//                    if(modelChallenge.getChallengeTitle() != null){
                        //System.out.println("yes");
                        if(modelChallenge.getChallengeTitle().equals(challengeTitle)){
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


        //Button set
        //completed challenge button
        Button buttonComplete = view.findViewById(R.id.complete_challenge);
        buttonComplete.setOnClickListener(new ButtonListener());

        //delete challenge button
        Button buttonDelete = (Button) view.findViewById(R.id.delete_btn);
        buttonDelete.setOnClickListener(new ButtonListener());
//        buttonDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("you clicked         ");
//                //updateDelete();
//            }
//        });

        //leave challenge button
        Button buttonLeave = view.findViewById(R.id.leave_btn);
        buttonLeave.setOnClickListener(new ButtonListener());
//        buttonDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //updateLeave();
//            }
//        });


        return view;
    }

    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.complete_challenge:
                    updateComplete();
                    break;
                    case R.id.delete_btn:
                        updateDelete();
                        break;
                case R.id.leave_btn:
                    updateLeave();
                    break;
            }

        }
    }

    private void updateComplete() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Participants");
        ref.orderByChild("userUID").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    ModelParticipant modelParticipant = ds.getValue(ModelParticipant.class);
                    if(modelParticipant.getChallengeTitle().equals(challengeTitle)) {
                        String key = ds.getKey();
                        String newStatus = "Completed";
                        ref.child(key).child("status").setValue(newStatus);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //remove row from participants table
    // only Monitor can proceed this button
    private void updateDelete() {
        DatabaseReference refP = FirebaseDatabase.getInstance().getReference("Participants");
        Query queryP = refP.orderByChild("userUID").equalTo(user.getUid());
        queryP.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    ModelParticipant modelParticipant = ds.getValue(ModelParticipant.class);
                    if(modelParticipant.getChallengeTitle().equals(challengeTitle) && modelParticipant.getRole().equals("Moderator")) {
                        ds.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //remove row from participants table
    // only Participants can proceed this button
    private void updateLeave() {
        DatabaseReference refP = FirebaseDatabase.getInstance().getReference("Participants");
        Query queryP = refP.orderByChild("userUID").equalTo(user.getUid());
        queryP.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    ModelParticipant modelParticipant = ds.getValue(ModelParticipant.class);
                    if(modelParticipant.getChallengeTitle().equals(challengeTitle) && modelParticipant.getRole().equals("Participant")) {
                        System.out.println("you clicked         "+modelParticipant.challengeTitle);
                        ds.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setInfo() {

    }
}
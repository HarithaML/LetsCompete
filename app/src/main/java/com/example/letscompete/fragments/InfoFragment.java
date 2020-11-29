package com.example.letscompete.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.example.letscompete.activities.activityBasedChallenge.CompleteChallengeActivity;
import com.example.letscompete.R;
import com.example.letscompete.activities.activityBasedChallenge.ActivityBasedChallengeActivity;
import com.example.letscompete.activities.DashBoardActivity;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends Fragment {
    ImageView mImage;
    TextView mTitle,mDescription,mType,mDuration,mStartDate;
    Button mCompleteChallenge,mDeleteBtn,mLeaveChallenge;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;


    // TODO: Rename and change types of parameters
    private String challengeTitle;
    private String username;

    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param challengeTitle Parameter 1.
     * @param username Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(String challengeTitle,String username) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString("challengeTitle", challengeTitle);
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            challengeTitle = getArguments().getString("challengeTitle");
            username = getArguments().getString("username");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get challengeTitle from TimeChallengeActivity.java
        ActivityBasedChallengeActivity activityBasedChallengeActivity = (ActivityBasedChallengeActivity)getActivity();
        String challengeTitle = activityBasedChallengeActivity.getChallengeTitle();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        mImage = view.findViewById(R.id.challenge_image);
        mTitle = view.findViewById(R.id.challenge_title);
        mDescription = view.findViewById(R.id.challenge_description);
        mDuration = view.findViewById(R.id.challenge_duration);
        mStartDate = view.findViewById(R.id.challenge_startDate);
        mType = view.findViewById(R.id.challenge_type);
        mCompleteChallenge = view.findViewById(R.id.complete_challenge);
        mDeleteBtn = view.findViewById(R.id.delete_btn);
        mLeaveChallenge = view.findViewById(R.id.leave_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        changeCompleteButtonVisibility();
        checkAuth();
        mCompleteChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CompleteChallengeActivity.class);
                intent.putExtra("challengeTitle",mTitle.getText());
                intent.putExtra("username",username);
                startActivity(intent);

            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Challenge");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //System.out.println("Database Reached");
                for(DataSnapshot ds:  snapshot.getChildren()){
                    ModelChallenge modelChallenge = ds.getValue(ModelChallenge.class);
//                    if(modelChallenge.getChallengeTitle() != null){
                        System.out.println("yes");
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
        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                alert.setTitle("Delete entry");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        //remove challenge from table
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Challenge");
                        Query applesQuery = ref.orderByChild("challengeTitle").equalTo(challengeTitle);

                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                    startActivity(new Intent(getActivity(), DashBoardActivity.class));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });



        return view;
    }

    private void checkAuth() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/Participants");
        Query complete = ref.orderByChild("userUID").equalTo(firebaseUser.getUid());
        complete.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:  snapshot.getChildren()) {
                    ModelParticipant modelParticipant = ds.getValue(ModelParticipant.class);
                    if(modelParticipant.getChallengeTitle().equals(challengeTitle)) {
                        if (modelParticipant.getRole().equals("Moderator")) {
                            mLeaveChallenge.setVisibility(View.GONE);
                            mDeleteBtn.setVisibility(View.VISIBLE);
                        } else {
                            mLeaveChallenge.setVisibility(View.VISIBLE);
                            mDeleteBtn.setVisibility(View.GONE);
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void changeCompleteButtonVisibility() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/Participants");
        Query complete = ref.orderByChild("userUID").equalTo(firebaseUser.getUid());
        complete.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:  snapshot.getChildren()) {
                    ModelParticipant modelParticipant = ds.getValue(ModelParticipant.class);
                    if(modelParticipant.getChallengeTitle().equals(challengeTitle)) {
                        if (modelParticipant.getStatus().equals("Completed")) {
                            mCompleteChallenge.setVisibility(View.GONE);
                        } else {
                            mCompleteChallenge.setVisibility(View.VISIBLE);
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
package com.example.letscompete.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letscompete.R;
import com.example.letscompete.activities.activityBasedChallenge.ActivityBasedChallengeActivity;
import com.example.letscompete.adapters.AdapterParticipant;
import com.example.letscompete.models.ModelParticipant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ParticipantsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParticipantsFragment extends Fragment {
    RecyclerView recyclerView;
    AdapterParticipant adapterParticipant;
    List<ModelParticipant> participantList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String challengeTitle;

    public ParticipantsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param challengeTitle Parameter 1.
     * @return A new instance of fragment ParticipantsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParticipantsFragment newInstance(String challengeTitle) {
        ParticipantsFragment fragment = new ParticipantsFragment();
        Bundle args = new Bundle();
        args.putString("challengeTitle", challengeTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            challengeTitle = getArguments().getString("challengeTitle");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_participants, container, false);
        //init recyclerView
        recyclerView= view.findViewById(R.id.participants_recyclerView);

        //set its properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //init user list
        participantList = new ArrayList<>();
        getAllParticipants();

        return  view;
    }

    private void getAllParticipants() {
        //get challengeTitle from TimeChallengeActivity.java
        ActivityBasedChallengeActivity activityBasedChallengeActivity = (ActivityBasedChallengeActivity)getActivity();
        String challengeTitle = activityBasedChallengeActivity.getChallengeTitle();
        //get path of database named "Participants" containing users info
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Participants");
        //get all data from path
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                participantList.clear();
                for(DataSnapshot ds:  snapshot.getChildren()){
                    ModelParticipant modelParticipant = ds.getValue(ModelParticipant.class);
                    //get all searched users except currently signed in user
                    if(modelParticipant.getChallengeTitle().equals(challengeTitle)){
                        participantList.add(modelParticipant);
                    }
                    //adapter
                    adapterParticipant = new AdapterParticipant(getActivity(), participantList);
                    adapterParticipant.notifyDataSetChanged();
                    //set adapter to recycler view
                    recyclerView.setAdapter(adapterParticipant);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
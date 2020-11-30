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
import com.example.letscompete.adapters.AdapterChallenges;
import com.example.letscompete.models.ModelChallenge;
import com.example.letscompete.models.ModelParticipant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OngoingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChallengesListFragment extends Fragment {
    RecyclerView recyclerView;
    AdapterChallenges adapterChallenges;
    String titleKey;
    List<ModelChallenge> challengeList;
    List<String> joinedParticipantsList;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //firebase
    FirebaseAuth  firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //storage
    StorageReference storageReference;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChallengesListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OngoingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OngoingFragment newInstance(String param1, String param2) {
        OngoingFragment fragment = new OngoingFragment();
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
        View view = inflater.inflate(R.layout.fragment_challenges_list, container, false);
        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
        // init views
        recyclerView = view.findViewById(R.id.recview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        challengeList = new ArrayList<>();
        joinedParticipantsList = new ArrayList<>();
        getUnjoined();


        return view;
    }

    private void getUnjoined(){
        //get unjoined title list
        DatabaseReference participantsRef = FirebaseDatabase.getInstance().getReference().child("Participants");
        participantsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                challengeList.clear();
                joinedParticipantsList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    ModelParticipant modelParticipant = ds.getValue(ModelParticipant.class);
                    if (modelParticipant.getUserUID() != null) {
                        if (modelParticipant.getUserUID().equals(user.getUid())) {
                            titleKey = modelParticipant.getChallengeTitle();
                            joinedParticipantsList.add(titleKey);
                            System.out.println("you already join     "+titleKey);
                            //get challengeList-ModelChallenge  from unjoinedList-String
                            DatabaseReference chaRef = FirebaseDatabase.getInstance().getReference().child("Challenge");
                            chaRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    challengeList.clear();
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        ModelChallenge modelChallenge = dataSnapshot.getValue(ModelChallenge.class);
                                        if(!joinedParticipantsList.contains(modelChallenge.getChallengeTitle()))
                                        //if(modelChallenge.getChallengeTitle().equals(titleKey))
                                        {
                                            challengeList.add(modelChallenge);
                                            System.out.println("you can join     "+modelChallenge.getChallengeTitle());
                                        }

                                    }
                                    adapterChallenges = new AdapterChallenges(getActivity(), challengeList);
                                    recyclerView.setAdapter(adapterChallenges);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        //in case for users don't have challengs at all.
                        if(joinedParticipantsList.isEmpty()){
                            DatabaseReference chaRef = FirebaseDatabase.getInstance().getReference().child("Challenge");
                            chaRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    challengeList.clear();
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        ModelChallenge modelChallenge = dataSnapshot.getValue(ModelChallenge.class);
                                        challengeList.add(modelChallenge);
                                    }
                                    adapterChallenges = new AdapterChallenges(getActivity(), challengeList);
                                    recyclerView.setAdapter(adapterChallenges);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

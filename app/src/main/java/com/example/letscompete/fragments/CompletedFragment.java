package com.example.letscompete.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.letscompete.R;
import com.example.letscompete.adapters.AdapterChallenge;
import com.example.letscompete.models.ModelChallenge;
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
 * Use the {@link CompletedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompletedFragment extends Fragment {
    RecyclerView recyclerView;
    AdapterChallenge adapterChallenge;
    List<ModelChallenge> challengeList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //storage
    StorageReference storageReference;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CompletedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompletedFragement.
     */
    // TODO: Rename and change types and number of parameters
    public static CompletedFragment newInstance(String param1, String param2) {
        CompletedFragment fragment = new CompletedFragment();
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
        View view = inflater.inflate(R.layout.fragment_completed, container, false);
        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
        //init views
        recyclerView = view.findViewById(R.id.completed_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        challengeList = new ArrayList<>();
        getAllCompleted();
        return view;
    }

    private void getAllCompleted() {
        //retrieve challenge under current user
        //Query query = databaseReference.orderByChild("id").equalTo(user.getUid());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Participants");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                challengeList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
//                    ModelParticipant modelParticipant = ds.getValue(ModelParticipant.class);
//                    ModelChallenge modelChallenge = ds.getValue(ModelChallenge.class);
//                    //pass userid
//                    if(modelParticipant.getUserUID().equals(user.getUid()) && modelParticipant.getStatus().equals("Completed")){
//                    }
//                    adapterChallengesCard = new AdapterChallengesCard(getActivity(), challengeList);
//                    recyclerView.setAdapter(adapterChallengesCard);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
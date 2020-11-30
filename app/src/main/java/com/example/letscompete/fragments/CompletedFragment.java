package com.example.letscompete.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letscompete.R;
import com.example.letscompete.activities.MainActivity;
import com.example.letscompete.adapters.AdapterChallengesCard;
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
 * Use the {@link CompletedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompletedFragment extends Fragment {
    RecyclerView recyclerView;
    AdapterChallengesCard adapterChallengesCard;
    String titleKey;
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
        setHasOptionsMenu(true);
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
        challengeList.clear();
        getAllCompleted();
        return view;
    }

    private void getAllCompleted() {
        //retrieve challenge under current user
        //Query query = databaseReference.orderByChild("id").equalTo(user.getUid());
        DatabaseReference participantsRef = FirebaseDatabase.getInstance().getReference().child("Participants");
        participantsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                challengeList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    ModelParticipant modelParticipant = ds.getValue(ModelParticipant.class);
                    if (modelParticipant.getUserUID() != null) {
                        if (modelParticipant.getUserUID().equals(user.getUid()) && modelParticipant.getStatus().equals("Completed")) {
                            titleKey = modelParticipant.getChallengeTitle();
                            //get Chanllenge table
                            DatabaseReference chaRef = FirebaseDatabase.getInstance().getReference().child("Challenge");
                            chaRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        ModelChallenge modelChallenge = dataSnapshot.getValue(ModelChallenge.class);
                                        if(modelChallenge.getChallengeTitle()!=null){
                                            if(modelChallenge.getChallengeTitle().equals(modelParticipant.getChallengeTitle())){
                                                challengeList.add(modelChallenge);
                                            }
                                        }
                                    }
                                    adapterChallengesCard = new AdapterChallengesCard(getActivity(), challengeList,modelParticipant.getUserName());
                                    recyclerView.setAdapter(adapterChallengesCard);
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
    private void searchChallenges(String query) {
        //retrieve challenge under current user
        //Query query = databaseReference.orderByChild("id").equalTo(user.getUid());
        DatabaseReference participantsRef = FirebaseDatabase.getInstance().getReference().child("Participants");
        participantsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                challengeList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    ModelParticipant modelParticipant = ds.getValue(ModelParticipant.class);
                    if (modelParticipant.getUserUID() != null) {
                        if (modelParticipant.getUserUID().equals(user.getUid()) && modelParticipant.getStatus().equals("Completed")) {
                            titleKey = modelParticipant.getChallengeTitle();
                            //get Chanllenge table
                            DatabaseReference chaRef = FirebaseDatabase.getInstance().getReference().child("Challenge");
                            chaRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        ModelChallenge modelChallenge = dataSnapshot.getValue(ModelChallenge.class);
                                        if(modelChallenge.getChallengeTitle()!=null){
                                            if(modelChallenge.getChallengeTitle().equals(modelParticipant.getChallengeTitle())&&modelChallenge.getChallengeTitle().trim().toLowerCase().contains(query.toLowerCase())){
                                                challengeList.add(modelChallenge);
                                            }
                                        }
                                    }
                                    adapterChallengesCard = new AdapterChallengesCard(getActivity(), challengeList,modelParticipant.getUserName());
                                    recyclerView.setAdapter(adapterChallengesCard);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        //inflating menu
//        inflater.inflate(R.menu.menu_main,menu);
        menu.findItem(R.id.action_search_challenge).setVisible(false);
        //SearchView
        MenuItem item = menu.findItem(R.id.action_Search).setVisible(true);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //called when user presses search button from keyboard
                if(!TextUtils.isEmpty(query.trim())){
                    //search text contains text,
                    // search it

                    searchChallenges(query);
                }else{

                    getAllCompleted();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //called when user presses any single letter
                if(!TextUtils.isEmpty(newText.trim())){
                    //search text contains text, search it

                    searchChallenges(newText);
                }else{

                    getAllCompleted();
                }


                return false;
            }
        });
        super.onCreateOptionsMenu(menu,inflater);
    }

    /* handle menu item clicks*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //get item id
        int id= item.getItemId();
        if(id == R.id.action_logout){
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }
    private void checkUserStatus() {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            // user is signed in stay here
            //set email of l
            // ogged in user
//            mProfileTv.setText(user.getEmail());
        }else{
            // user not signed in go to main activity
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }
}
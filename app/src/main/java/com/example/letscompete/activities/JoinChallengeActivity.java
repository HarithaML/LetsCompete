package com.example.letscompete.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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

public class JoinChallengeActivity extends AppCompatActivity {
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
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //storage
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_challenge);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Join Challenge");
        actionBar.setDisplayHomeAsUpEnabled(true);
        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference();
        // init views
        recyclerView = findViewById(R.id.join_recyle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        challengeList = new ArrayList<>();
        joinedParticipantsList = new ArrayList<>();
        getUnjoined();

    }//end onCreate

    private void getUnjoined() {
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
                                    adapterChallenges = new AdapterChallenges(JoinChallengeActivity.this, challengeList);
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
                                    adapterChallenges = new AdapterChallenges(JoinChallengeActivity.this, challengeList);
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
    }//getUnjoined ends here
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(JoinChallengeActivity.this, DashBoardActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();
        Intent intent = new Intent(JoinChallengeActivity.this, DashBoardActivity.class);
        startActivity(intent);
        // optional depending on your needs
    }

}
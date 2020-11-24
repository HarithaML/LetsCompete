package com.example.letscompete.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toolbar;

import com.example.letscompete.R;
import com.example.letscompete.adapters.AdapterParticipant;
import com.example.letscompete.adapters.AdapterUsers;
import com.example.letscompete.fragments.ChallengeFragmentAdapter;
import com.example.letscompete.fragments.HomeFragment;
import com.example.letscompete.fragments.InfoFragment;
import com.example.letscompete.fragments.ParticipantsFragment;
import com.example.letscompete.models.ModelChallenge;
import com.example.letscompete.models.ModelParticipant;
import com.example.letscompete.models.ModelUser;
import com.example.letscompete.notifications.APIService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TimeChallengeActivity extends AppCompatActivity {
    ViewPager viewPager;

    FirebaseAuth firebaseAuth;
    APIService apiService;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String challengeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_challenge);
        //init views
        viewPager = findViewById(R.id.challenge_content);
        Intent intent = getIntent();
        challengeTitle = intent.getStringExtra("challengeTitle");
        System.out.println("you passed the challengeTitle to info/participants herrrrrrrrrrrrr     "+challengeTitle);
        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Challenge");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(challengeTitle);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //info  fragment transaction
        Bundle args = new Bundle();
        args.putString("challengeTitle", challengeTitle);
        InfoFragment fragment1 = new InfoFragment();
        fragment1.setArguments(args);
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.challenge_content,fragment1,"");
        ft1.commit();

        TabLayout tabLayout = findViewById(R.id.challenge_tablayout);
        final ChallengeFragmentAdapter adapter = new ChallengeFragmentAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }

    public String getChallengeTitle(){
        return challengeTitle;
    }


    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(TimeChallengeActivity.this, DashBoardActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }





}
package com.example.letscompete.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.letscompete.R;
import com.example.letscompete.adapters.ChallengeFragmentAdapter;
import com.example.letscompete.fragments.InfoFragment;
import com.google.android.material.tabs.TabLayout;

public class TimeChallengeActivity extends AppCompatActivity {
    ViewPager viewPager;
    String challengeTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_challenge);
         viewPager = findViewById(R.id.challenge_content);
        //challengeTitle
        Intent intent = getIntent();

        challengeTitle = intent.getStringExtra("challengeTitle");


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(challengeTitle);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //info  fragment transaction
        InfoFragment fragment1 = new InfoFragment();
        Bundle arguments = new Bundle();
        arguments.putString("challengeTitle",challengeTitle);
        fragment1.setArguments(arguments);
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.challenge_content,fragment1,"");
        ft1.commit();





        TabLayout tabLayout = findViewById(R.id.challenge_tablayout);
        final ChallengeFragmentAdapter adapter = new ChallengeFragmentAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount(),challengeTitle);

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